package tech3.binitright.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import tech3.binitright.service.ScanService;

@RestController
@RequestMapping("/api/v1")
public class ScanRestController {

    private final ScanService scanService;

    public ScanRestController(final ScanService scanService) {
        this.scanService = scanService;
    }

    @PostMapping(value = "/scan", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> scan(
            @RequestPart("image") final MultipartFile image,
            @RequestPart(value = "tier1", required = false) final String tier1Json,
            @RequestPart(value = "timestamp", required = false) final Long timestamp,
            @RequestPart(value = "forceUcloud", required = false) final String forceCloudRaw
    ) {
        final boolean forceCloud = "true".equalsIgnoreCase(forceCloudRaw);
        final Map<String, Object> response = scanService.handleScan(image, tier1Json, timestamp, forceCloud);
        return ResponseEntity.ok(response);
    }
}
