package tech3.binitright.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
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

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Profile({"test", "prod", "default"})
@Service
public class BinDataImporter {
    private final DropOffLocationRepository repo;
    private final ObjectMapper mapper;
    private final RestTemplate restTemplate;

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

        importFromApi(
                BLUE_BIN_API,
                "BlueBin"
        );

        importFromApi(
                EWASTE_API,
                "EWaste"
        );

        importFromApi(
                LAMP_API,
                "Lamp"
        );
    }

    private void importFromApi(String pollUrl, String binType) {
        try {
            JsonNode pollResp = restTemplate.getForObject(pollUrl, JsonNode.class);

            String s3Url = pollResp.path("data").path("url").asText();

            if (s3Url == null || s3Url.isEmpty() || s3Url.equals("null")) {
                throw new RuntimeException("Error: 'data.url' not found in poll-download JSON");
            }

 //           System.out.println("S3 URL fetched for " + binType + ": " + s3Url);

            URI signed = new URI(s3Url);
            String geoJson = restTemplate.getForObject(signed, String.class);


            parseAndSaveBins(geoJson, binType);

            System.out.println("Imported: " + binType);

        } catch (Exception e) {
            System.out.println("ERROR importing: " + binType);
            e.printStackTrace();
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

                DropOffLocation bin;

                if (binType.equals("BlueBin")) {
                    bin = parseHtmlBasedBin(props, lat, lng, binType);
                }
                else if (binType.equals("EWaste")) {
                    bin = parseEWasteBin(props, lat, lng, binType);
                }
                else {
                    bin = parseHtmlBasedBin(props, lat, lng, binType);
                }

                repo.save(bin);
                count++;
            }
            
            System.out.println("Saved " + count + " " + binType + " bins");  

        } catch (Exception e) {
            System.err.println("ERROR parsing " + binType + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to parse bins: " + e.getMessage(), e);  
        }
    }

    private DropOffLocation parseEWasteBin(JsonNode props, double lat, double lng, String binType) {

        String block = props.path("ADDRESSBLOCKHOUSENUMBER").asText("");
        String street = props.path("ADDRESSSTREETNAME").asText("");
        String postal = props.path("ADDRESSPOSTALCODE").asText("");
        String desc = props.path("DESCRIPTION").asText("");

        String fullAddress = (block + " " + street).trim();

        DropOffLocation bin = new DropOffLocation();
        bin.setName("E-Waste Bin");
        bin.setAddress(fullAddress);
        bin.setPostalCode(postal);
        bin.setDescription(desc);
        bin.setBinType(binType);
        bin.setLatitude(new BigDecimal(lat));
        bin.setLongitude(new BigDecimal(lng));
        bin.setStatus(Status.ACTIVE);

        return bin;
    }

    // Extract clean data from BlueBin HTML
    private DropOffLocation parseHtmlBasedBin(JsonNode props, double lat, double lng, String binType) {

        String html = props.get("Description").asText("");
        Map<String, String> meta = parseHtmlTable(html);

        String block = meta.getOrDefault("ADDRESSBLOCKHOUSENUMBER", "");
        String street = meta.getOrDefault("ADDRESSSTREETNAME", "");
        String postal = meta.getOrDefault("ADDRESSPOSTALCODE", "");
        String desc = meta.getOrDefault("DESCRIPTION", "");
        String building = meta.getOrDefault("ADDRESSBUILDINGNAME", "");

        String fullAddress;
        if (!block.isEmpty() && !street.isEmpty()) {
            fullAddress = block + " " + street;
        } else if (!building.isEmpty()) {
            fullAddress = building;
        } else {
            fullAddress = street;
        }

        DropOffLocation bin = new DropOffLocation();
        bin.setName(props.has("Name") ? props.get("Name").asText() : binType);
        bin.setAddress(fullAddress);
        bin.setPostalCode(postal);
        bin.setDescription(desc);
        bin.setBinType(binType);
        bin.setLatitude(new BigDecimal(lat));
        bin.setLongitude(new BigDecimal(lng));
        bin.setStatus(Status.ACTIVE);

        return bin;
    }

    // Parse <th> / <td> table pairs using Jsoup
    private Map<String, String> parseHtmlTable(String html) {
        Map<String, String> map = new HashMap<>();

        try {
            Document doc = Jsoup.parse(html);
            Elements rows = doc.select("tr");

            for (Element row : rows) {
                Elements th = row.select("th");
                Elements td = row.select("td");
                if (!th.isEmpty() && !td.isEmpty()) {
                    map.put(th.text(), td.text());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    private String extractName(JsonNode props) {
        if (props.has("NAME")) return props.get("NAME").asText();
        if (props.has("Name")) return props.get("Name").asText();
        return "Unknown";
    }

    private String extractAddress(JsonNode props) {
        if (props.has("ADDRESSSTREETNAME")) return props.get("ADDRESSSTREETNAME").asText();
        return "";
    }

    private String extractPostal(JsonNode props) {
        if (props.has("ADDRESSPOSTALCODE")) return props.get("ADDRESSPOSTALCODE").asText();
        return "";
    }

    private String extractDescription(JsonNode props) {
        if (props.has("DESCRIPTION")) return props.get("DESCRIPTION").asText();
        if (props.has("Description")) return props.get("Description").asText();
        return "";
    }

}
