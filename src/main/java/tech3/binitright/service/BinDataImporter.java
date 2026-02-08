package tech3.binitright.service;

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import tech3.binitright.model.DropOffLocation;
import tech3.binitright.model.DropOffLocation.Status;
import tech3.binitright.repository.DropOffLocationRepository;

@Profile({"test", "prod", "default"})
@Service
public final class BinDataImporter {
    private final DropOffLocationRepository repo;
    private final ObjectMapper mapper;
    private final RestTemplate restTemplate;
    @Value("${data.gov.api.key}")
    private String dataGovApiKey;

    private static final String BLUEUBINUAPI =
            "https://api-open.data.gov.sg/v1/public/api/datasets/d_4dde14826642f49eefff48b7832b90db/poll-download";

    private static final String EWASTEUAPI =
            "https://api-open.data.gov.sg/v1/public/api/datasets/dUdb40d004afeb5a7f0f555fdcc34934cc/poll-download";

    private static final String LAMPUAPI =
            "https://api-open.data.gov.sg/v1/public/api/datasets/d_6226f69998ed0cb62151af37706508cd/poll-download";

    public BinDataImporter(final DropOffLocationRepository repo, final RestTemplate restTemplate) {
        this.repo = repo;
        this.restTemplate = restTemplate;
        this.mapper = new ObjectMapper();
    }

    public void importData() {
        // Import Lighting bins
        importFromApi(LAMPUAPI, "Lighting");
        pause(12000); // 12-second breath for the API

        // Import EWaste bins
        importFromApi(EWASTEUAPI, "EWaste");
        pause(12000);

        // Import BlueBin bins
        importFromApi(BLUEUBINUAPI, "BlueBin");
    }

    private void importFromApi(final String pollUrl, final String binType) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.set("X-API-KEY", dataGovApiKey);

            System.out.println("Using API key header: " + dataGovApiKey.substring(0, 10));
            final HttpEntity<Void> entity = new HttpEntity<>(headers);

            final ResponseEntity<JsonNode> response =
                    restTemplate.exchange(
                            pollUrl,
                            HttpMethod.GET,
                            entity,
                            JsonNode.class
                    );

            final JsonNode pollResp = response.getBody();
            final String s3Url = pollResp.path("data").path("url").asText();

            if (s3Url == null || s3Url.isEmpty() || s3Url.equals("null")) {
                throw new RuntimeException("Error: 'data.url' not found for " + binType);
            }

            final URI signed = new URI(s3Url);
            final String geoJson = restTemplate.getForObject(signed, String.class);

            parseAndSaveBins(geoJson, binType);
            System.out.println(">>> Successfully Imported: " + binType);

        } catch (final HttpClientErrorException.TooManyRequests e) {
            System.err.println("!!! Rate Limit Hit (429) for " + binType + ". Waiting 30s to retry...");
            pause(30000);
            importFromApi(pollUrl, binType); // Recursive retry once
        } catch (final Exception e) {
            System.err.println("!!! Failed to import " + binType + ": " + e.getMessage());
        }
    }

    @Transactional
    private void parseAndSaveBins(final String geoJson, final String binType) {
        try {
            final JsonNode root = mapper.readTree(geoJson);
            final JsonNode features = root.get("features");

            int count = 0;
            for (final JsonNode feature : features) {
                final JsonNode geom = feature.get("geometry");
                final JsonNode props = feature.get("properties");

                final double lng = geom.get("coordinates").get(0).asDouble();
                final double lat = geom.get("coordinates").get(1).asDouble();

                String incCrc;
                if (props.has("INCUCRC")) {
                    incCrc = props.get("INCUCRC").asText();
                } else {
                    final String html = props.path("Description").asText("");
                    final Map<String, String> meta = parseHtmlTable(html);
                    incCrc = meta.get("INCUCRC");
                }

                if (incCrc == null || incCrc.isEmpty()) {
					continue;
				}

                final DropOffLocation bin = binType.equals("EWaste")
                        ? parseEWasteBin(props, lat, lng, binType)
                        : parseHtmlBasedBin(props, lat, lng, binType);

                bin.setId(incCrc);

                final Optional<DropOffLocation> existing = repo.findById(incCrc);
                if (existing.isPresent()) {
                    final DropOffLocation db = existing.get();
                    db.setLatitude(bin.getLatitude());
                    db.setLongitude(bin.getLongitude());
                    db.setDescription(bin.getDescription());
                    db.setStatus(Status.ACTIVE);
                    repo.save(db);
                } else {
                    repo.save(bin);
                    count++;
                }
            }
            System.out.println(">>> Saved " + count + " " + binType + " bins");
        } catch (final Exception e) {
            System.err.println("!!! Error parsing " + binType + ": " + e.getMessage());
        }
    }

    private DropOffLocation parseEWasteBin(final JsonNode props, final double lat, final double lng, final String binType) {
        final String block = props.path("ADDRESSBLOCKHOUSENUMBER").asText("");
        final String street = props.path("ADDRESSSTREETNAME").asText("");
        final String postal = props.path("ADDRESSPOSTALCODE").asText("");
        final String desc = props.path("DESCRIPTION").asText("");

        final DropOffLocation bin = new DropOffLocation();
        bin.setName("E-Waste Bin");
        bin.setAddress((block + " " + street).trim());
        bin.setPostalCode(postal);
        bin.setDescription(desc);
        bin.setBinType(binType);
        bin.setLatitude(new BigDecimal(lat));
        bin.setLongitude(new BigDecimal(lng));
        bin.setStatus(Status.ACTIVE);
        return bin;
    }

    private DropOffLocation parseHtmlBasedBin(final JsonNode props, final double lat, final double lng, final String binType) {
        final String html = props.path("Description").asText("");
        final Map<String, String> meta = parseHtmlTable(html);

        final String block = meta.getOrDefault("ADDRESSBLOCKHOUSENUMBER", "");
        final String street = meta.getOrDefault("ADDRESSSTREETNAME", "");
        final String building = meta.getOrDefault("ADDRESSBUILDINGNAME", "");

        final String fullAddress = (!block.isEmpty() && !street.isEmpty()) ? block + " " + street :
                (!building.isEmpty() ? building : street);

        final DropOffLocation bin = new DropOffLocation();
        bin.setName(props.has("Name") ? props.get("Name").asText() : binType);
        bin.setAddress(fullAddress);
        bin.setPostalCode(meta.getOrDefault("ADDRESSPOSTALCODE", ""));
        bin.setDescription(meta.getOrDefault("DESCRIPTION", ""));
        bin.setBinType(binType);
        bin.setLatitude(new BigDecimal(lat));
        bin.setLongitude(new BigDecimal(lng));
        bin.setStatus(Status.ACTIVE);
        return bin;
    }

    private Map<String, String> parseHtmlTable(final String html) {
        final Map<String, String> map = new HashMap<>();
        try {
            final Document doc = Jsoup.parse(html);
            final Elements rows = doc.select("tr");
            for (final Element row : rows) {
                final Elements th = row.select("th");
                final Elements td = row.select("td");
                if (!th.isEmpty() && !td.isEmpty()) {
					map.put(th.text(), td.text());
				}
            }
        } catch (final Exception e) { e.printStackTrace(); }
        return map;
    }

    private void pause(final int ms) {
        try { Thread.sleep(ms); }
        catch (final InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}