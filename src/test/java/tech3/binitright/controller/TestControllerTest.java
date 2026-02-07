package tech3.binitright.controller;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import tech3.binitright.service.ScanService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScanRestController.class)
class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScanService scanService;

    @Test
    void scanReturnsV01EnvelopeAndFinalFiveFields() throws Exception {
        Map<String, Object> finalObj = Map.of(
                "category", "Plastic container",
                "recyclable", true,
                "confidence", 0.92,
                "instruction", "Rinse and recycle in blue bin.",
                "instructions", List.of(
                        "Empty contents.",
                        "Rinse the container.",
                        "Recycle in blue bin."
                )
        );

        Map<String, Object> response = Map.of(
                "status", "success",
                "request_id", "req-1",
                "data", Map.of(
                        "tier1", Map.of("category", "plastic", "confidence", 0.92, "escalate", false),
                        "decision", Map.of("used_tier2", false, "reason_codes", List.of()),
                        "final", finalObj,
                        "meta", Map.of("schema_version", "0.1")
                )
        );

        when(scanService.handleScan(any(), any(), any(), eq(false))).thenReturn(response);

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "sample.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[]{1, 2, 3}
        );

        mockMvc.perform(multipart("/api/v1/scan")
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.final.category").value("Plastic container"))
                .andExpect(jsonPath("$.data.final.recyclable").value(true))
                .andExpect(jsonPath("$.data.final.confidence").value(0.92))
                .andExpect(jsonPath("$.data.final.instruction").exists())
                .andExpect(jsonPath("$.data.final.instructions[0]").exists());
    }

    @Test
    void scanParsesForceCloudAsTrue() throws Exception {
        when(scanService.handleScan(any(), any(), any(), eq(true))).thenReturn(
                Map.of("status", "success", "request_id", "req-2", "data", Map.of())
        );

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "sample.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[]{1, 2, 3}
        );
        MockMultipartFile forceCloud = new MockMultipartFile(
                "force_cloud",
                "",
                MediaType.TEXT_PLAIN_VALUE,
                "true".getBytes()
        );

        mockMvc.perform(multipart("/api/v1/scan")
                        .file(image)
                        .file(forceCloud)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk());

        verify(scanService).handleScan(any(), any(), any(), eq(true));
    }
}
