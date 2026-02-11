package techthree.binitright.controller;

import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import techthree.binitright.interfacemethods.ScanInterface;

@RestController
@RequestMapping("/api/v1")
public class ScanRestController {

    private final ScanInterface scanService;

    public ScanRestController(final ScanInterface scanService) {

        this.scanService = scanService;
    }

    @PostMapping(value = "/scan", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> scan(
            @RequestPart("image") final MultipartFile image,
            @RequestPart(value = "tier1", required = false) final String tier1Json,
            @RequestPart(value = "timestamp", required = false) final Long timestamp,
            @RequestPart(value = "force_cloud", required = false) final String forceCloudRaw
    ) {
        final boolean forceCloud = "true".equalsIgnoreCase(forceCloudRaw);
        final Map<String, Object> response = scanService.handleScan(image, tier1Json, timestamp, forceCloud);
        return ResponseEntity.ok(response);
    }
}
