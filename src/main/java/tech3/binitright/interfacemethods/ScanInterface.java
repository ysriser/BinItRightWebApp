package tech3.binitright.interfacemethods;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ScanInterface {
    Map<String, Object> handleScan(MultipartFile image, String tier1Json, Long timestamp, boolean forceCloud);
}
