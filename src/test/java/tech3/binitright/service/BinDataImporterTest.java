package tech3.binitright.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import tech3.binitright.model.DropOffLocation;
import tech3.binitright.model.DropOffLocation.Status;
import tech3.binitright.repository.DropOffLocationRepository;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BinDataImporterTest {

    private DropOffLocationRepository repo;   // mock manually (no @Mock needed)
    private BinDataImporter importer;

    @BeforeEach
    void setUp() {
        repo = mock(DropOffLocationRepository.class);

        // IMPORTANT: Use a REAL RestTemplate (we are not mocking it)
        importer = new BinDataImporter(repo, new RestTemplate());

        // Set @Value field so substring(0,10) won't crash if importFromApi is ever invoked
        ReflectionTestUtils.setField(importer, "dataGovApiKey", "TEST_API_KEY_1234567890");
    }

    @Test
    void parseAndSaveBins_whenNewBin_savesNewEntity() {
        when(repo.findById("INC1")).thenReturn(Optional.empty());

        ReflectionTestUtils.invokeMethod(
                importer,
                "parseAndSaveBins",
                geoJsonWithIncCrc("INC1"),
                "BlueBin"
        );

        ArgumentCaptor<DropOffLocation> cap = ArgumentCaptor.forClass(DropOffLocation.class);
        verify(repo).save(cap.capture());

        DropOffLocation saved = cap.getValue();
        assertEquals("INC1", saved.getId());
        assertEquals("BlueBin", saved.getBinType());
        assertEquals(Status.ACTIVE, saved.getStatus());

        // NOTE: your code uses new BigDecimal(double), so exact decimal may vary.
        // We'll check approx via doubleValue.
        assertEquals(1.290270, saved.getLatitude().doubleValue(), 0.000001);
        assertEquals(103.851959, saved.getLongitude().doubleValue(), 0.000001);
    }

    @Test
    void parseAndSaveBins_whenExistingBin_updatesExistingAndSavesExisting() {
        DropOffLocation existing = new DropOffLocation();
        existing.setId("INC1");
        existing.setLatitude(BigDecimal.ZERO);
        existing.setLongitude(BigDecimal.ZERO);
        existing.setDescription("old-desc");
        existing.setStatus(Status.ACTIVE);

        when(repo.findById("INC1")).thenReturn(Optional.of(existing));

        ReflectionTestUtils.invokeMethod(
                importer,
                "parseAndSaveBins",
                geoJsonWithIncCrc("INC1"),
                "BlueBin"
        );

        // It should save the existing object (db)
        verify(repo).save(existing);

        assertEquals(Status.ACTIVE, existing.getStatus());
        assertEquals(1.290270, existing.getLatitude().doubleValue(), 0.000001);
        assertEquals(103.851959, existing.getLongitude().doubleValue(), 0.000001);

        // description is taken from HTML meta DESCRIPTION
        assertEquals("hello", existing.getDescription());
    }

    @Test
    void parseAndSaveBins_whenIncCrcMissing_skipsSave() {
        String geoJsonMissingInc = """
            {
              "type":"FeatureCollection",
              "features":[
                {
                  "type":"Feature",
                  "geometry":{"type":"Point","coordinates":[103.851959,1.290270]},
                  "properties":{
                    "Name":"Test Bin",
                    "Description":"<table><tr><th>DESCRIPTION</th><td>hello</td></tr></table>"
                  }
                }
              ]
            }
            """;

        ReflectionTestUtils.invokeMethod(importer, "parseAndSaveBins", geoJsonMissingInc, "BlueBin");

        verify(repo, never()).save(any());
    }

    @Test
    void parseAndSaveBins_whenEwaste_parsesEwasteFieldsCorrectly() {
        when(repo.findById("INC9")).thenReturn(Optional.empty());

        String ewasteGeoJson = """
            {
              "type":"FeatureCollection",
              "features":[
                {
                  "type":"Feature",
                  "geometry":{"type":"Point","coordinates":[103.800000,1.300000]},
                  "properties":{
                    "INC_CRC":"INC9",
                    "ADDRESSBLOCKHOUSENUMBER":"10",
                    "ADDRESSSTREETNAME":"Test Street",
                    "ADDRESSPOSTALCODE":"123456",
                    "DESCRIPTION":"Near lobby"
                  }
                }
              ]
            }
            """;

        ReflectionTestUtils.invokeMethod(importer, "parseAndSaveBins", ewasteGeoJson, "EWaste");

        ArgumentCaptor<DropOffLocation> cap = ArgumentCaptor.forClass(DropOffLocation.class);
        verify(repo).save(cap.capture());

        DropOffLocation saved = cap.getValue();
        assertEquals("INC9", saved.getId());
        assertEquals("EWaste", saved.getBinType());
        assertEquals("E-Waste Bin", saved.getName());
        assertEquals("10 Test Street", saved.getAddress());
        assertEquals("123456", saved.getPostalCode());
        assertEquals("Near lobby", saved.getDescription());
        assertEquals(Status.ACTIVE, saved.getStatus());
    }

    // ---------------- helper ----------------
    private String geoJsonWithIncCrc(String incCrc) {
        return """
            {
              "type":"FeatureCollection",
              "features":[
                {
                  "type":"Feature",
                  "geometry":{"type":"Point","coordinates":[103.851959,1.290270]},
                  "properties":{
                    "INC_CRC":"%s",
                    "Name":"Test Bin",
                    "Description":"<table><tr><th>DESCRIPTION</th><td>hello</td></tr></table>"
                  }
                }
              ]
            }
            """.formatted(incCrc);
    }
}