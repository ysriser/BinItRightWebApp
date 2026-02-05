package tech3.binitright.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import tech3.binitright.model.DropOffLocation;
import tech3.binitright.model.DropOffLocation.Status;
import tech3.binitright.repository.DropOffLocationRepository;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Profile({"test", "prod", "default"})
@Service
public class BinDataImporter {
    private final DropOffLocationRepository repo;
    private final ObjectMapper mapper;
    private final RestTemplate restTemplate;
    @Value("${data.gov.api.key}")
    private String dataGovApiKey;

    private static final String BLUE_BIN_API =
            "https://api-open.data.gov.sg/v1/public/api/datasets/d_4dde14826642f49eefff48b7832b90db/poll-download";

    private static final String EWASTE_API =
            "https://api-open.data.gov.sg/v1/public/api/datasets/d_db40d004afeb5a7f0f555fdcc34934cc/poll-download";

    private static final String LAMP_API =
            "https://api-open.data.gov.sg/v1/public/api/datasets/d_6226f69998ed0cb62151af37706508cd/poll-download";

    public BinDataImporter(DropOffLocationRepository repo, RestTemplate restTemplate) {
        this.repo = repo;
        this.restTemplate = restTemplate;
        this.mapper = new ObjectMapper();
    }

    public void importData() {
        // Import Lighting bins
        importFromApi(LAMP_API, "Lighting");
        pause(12000); // 12-second breath for the API

        // Import EWaste bins
        importFromApi(EWASTE_API, "EWaste");
        pause(12000);

        // Import BlueBin bins
        importFromApi(BLUE_BIN_API, "BlueBin");
    }

    private void importFromApi(String pollUrl, String binType) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-API-KEY", dataGovApiKey);

            System.out.println("Using API key header: " + dataGovApiKey.substring(0, 10));
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<JsonNode> response =
                    restTemplate.exchange(
                            pollUrl,
                            HttpMethod.GET,
                            entity,
                            JsonNode.class
                    );

            JsonNode pollResp = response.getBody();
            String s3Url = pollResp.path("data").path("url").asText();

            if (s3Url == null || s3Url.isEmpty() || s3Url.equals("null")) {
                throw new RuntimeException("Error: 'data.url' not found for " + binType);
            }

            URI signed = new URI(s3Url);
            String geoJson = restTemplate.getForObject(signed, String.class);

            parseAndSaveBins(geoJson, binType);
            System.out.println(">>> Successfully Imported: " + binType);

        } catch (HttpClientErrorException.TooManyRequests e) {
            System.err.println("!!! Rate Limit Hit (429) for " + binType + ". Waiting 30s to retry...");
            pause(30000);
            importFromApi(pollUrl, binType); // Recursive retry once
        } catch (Exception e) {
            System.err.println("!!! Failed to import " + binType + ": " + e.getMessage());
        }
    }

    @Transactional
    private void parseAndSaveBins(String geoJson, String binType) {
        try {
            JsonNode root = mapper.readTree(geoJson);
            JsonNode features = root.get("features");

            int count = 0;
            for (JsonNode feature : features) {
                JsonNode geom = feature.get("geometry");
                JsonNode props = feature.get("properties");

                double lng = geom.get("coordinates").get(0).asDouble();
                double lat = geom.get("coordinates").get(1).asDouble();

                String incCrc;
                if (props.has("INC_CRC")) {
                    incCrc = props.get("INC_CRC").asText();
                } else {
                    String html = props.path("Description").asText("");
                    Map<String, String> meta = parseHtmlTable(html);
                    incCrc = meta.get("INC_CRC");
                }

                if (incCrc == null || incCrc.isEmpty()) continue;

                DropOffLocation bin = binType.equals("EWaste")
                        ? parseEWasteBin(props, lat, lng, binType)
                        : parseHtmlBasedBin(props, lat, lng, binType);

                bin.setId(incCrc);

                Optional<DropOffLocation> existing = repo.findById(incCrc);
                if (existing.isPresent()) {
                    DropOffLocation db = existing.get();
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
        } catch (Exception e) {
            System.err.println("!!! Error parsing " + binType + ": " + e.getMessage());
        }
    }

    private DropOffLocation parseEWasteBin(JsonNode props, double lat, double lng, String binType) {
        String block = props.path("ADDRESSBLOCKHOUSENUMBER").asText("");
        String street = props.path("ADDRESSSTREETNAME").asText("");
        String postal = props.path("ADDRESSPOSTALCODE").asText("");
        String desc = props.path("DESCRIPTION").asText("");

        DropOffLocation bin = new DropOffLocation();
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

    private DropOffLocation parseHtmlBasedBin(JsonNode props, double lat, double lng, String binType) {
        String html = props.path("Description").asText("");
        Map<String, String> meta = parseHtmlTable(html);

        String block = meta.getOrDefault("ADDRESSBLOCKHOUSENUMBER", "");
        String street = meta.getOrDefault("ADDRESSSTREETNAME", "");
        String building = meta.getOrDefault("ADDRESSBUILDINGNAME", "");

        String fullAddress = (!block.isEmpty() && !street.isEmpty()) ? block + " " + street :
                (!building.isEmpty() ? building : street);

        DropOffLocation bin = new DropOffLocation();
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

    private Map<String, String> parseHtmlTable(String html) {
        Map<String, String> map = new HashMap<>();
        try {
            Document doc = Jsoup.parse(html);
            Elements rows = doc.select("tr");
            for (Element row : rows) {
                Elements th = row.select("th");
                Elements td = row.select("td");
                if (!th.isEmpty() && !td.isEmpty()) map.put(th.text(), td.text());
            }
        } catch (Exception e) { e.printStackTrace(); }
        return map;
    }

    private void pause(int ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}