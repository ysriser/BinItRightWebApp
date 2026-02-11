package techthree.binitright.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import techthree.binitright.interfacemethods.ScanInterface;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ScanRestControllerTest {
    static class FakeScanService implements ScanInterface {

        MultipartFile receivedImage;
        String receivedTier1;
        Long receivedTimestamp;
        boolean receivedForceCloud;

        @Override
        public Map<String, Object> handleScan(
                MultipartFile image,
                String tier1Json,
                Long timestamp,
                boolean forceCloud
        ) {
            this.receivedImage = image;
            this.receivedTier1 = tier1Json;
            this.receivedTimestamp = timestamp;
            this.receivedForceCloud = forceCloud;

            Map<String, Object> response = new HashMap<>();
            response.put("result", "plastic");
            response.put("confidence", 0.95);
            return response;
        }
    }

    @Test
    void scanReturnsResponseFromService() {
        // arrange
        FakeScanService fakeService = new FakeScanService();
        ScanRestController controller = new ScanRestController(fakeService);

        MultipartFile image = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "dummy-image-content".getBytes()
        );

        String tier1Json = "{\"key\":\"value\"}";
        Long timestamp = 123456789L;
        String forceCloud = "true";

        // act
        ResponseEntity<Map<String, Object>> response =
                controller.scan(image, tier1Json, timestamp, forceCloud);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("plastic", response.getBody().get("result"));
        assertEquals(0.95, response.getBody().get("confidence"));

        // verify service interaction (without Mockito)
        assertEquals(image, fakeService.receivedImage);
        assertEquals(tier1Json, fakeService.receivedTier1);
        assertEquals(timestamp, fakeService.receivedTimestamp);
        assertTrue(fakeService.receivedForceCloud);
    }

    @Test
    void scanForceCloudDefaultsToFalse() {
        FakeScanService fakeService = new FakeScanService();
        ScanRestController controller = new ScanRestController(fakeService);

        MultipartFile image = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "dummy".getBytes()
        );

        ResponseEntity<Map<String, Object>> response =
                controller.scan(image, null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(fakeService.receivedForceCloud);
    }
}
