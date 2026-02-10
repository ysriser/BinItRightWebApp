package tech3.binitright.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScanServiceTest {

    private ScanService scanService;

    @BeforeEach
    void setUp() {
        scanService = new ScanService(new ObjectMapper());
        ReflectionTestUtils.setField(scanService, "confThreshold", 0.70);
        ReflectionTestUtils.setField(scanService, "marginThreshold", 0.05);
        ReflectionTestUtils.setField(scanService, "plasticConfThreshold", 0.80);
        ReflectionTestUtils.setField(scanService, "glassConfThreshold", 0.80);
        ReflectionTestUtils.setField(scanService, "tier2Provider", "mock");
    }

    @Test
    void handleScanWithoutTier2ReturnsTier1Final() {
        final MockMultipartFile image = new MockMultipartFile(
                "image",
                "sample.jpg",
                "image/jpeg",
                new byte[]{1, 2, 3}
        );

        final String tier1 = tier1Json("paper", 0.95, false, "plastic", 0.03);

        final Map<String, Object> response = scanService.handleScan(image, tier1, null, false);

        @SuppressWarnings("unchecked")
        final Map<String, Object> data = (Map<String, Object>) response.get("data");
        @SuppressWarnings("unchecked")
        final Map<String, Object> decision = (Map<String, Object>) data.get("decision");
        @SuppressWarnings("unchecked")
        final Map<String, Object> finalObj = (Map<String, Object>) data.get("final");

        assertFalse((Boolean) decision.get("used_tier2"));
        assertEquals("Paper", finalObj.get("category"));
        assertTrue((Boolean) finalObj.get("recyclable"));
        assertTrue(finalObj.containsKey("instruction"));
        assertTrue(finalObj.containsKey("instructions"));
    }

    @Test
    void handleScanForceCloudUsesMockTier2() {
        final MockMultipartFile image = new MockMultipartFile(
                "image",
                "sample.jpg",
                "image/jpeg",
                new byte[]{1, 2, 3}
        );

        final String tier1 = tier1Json("plastic", 0.91, false, "paper", 0.07);

        final Map<String, Object> response = scanService.handleScan(image, tier1, 123456L, true);

        @SuppressWarnings("unchecked")
        final Map<String, Object> data = (Map<String, Object>) response.get("data");
        @SuppressWarnings("unchecked")
        final Map<String, Object> decision = (Map<String, Object>) data.get("decision");
        @SuppressWarnings("unchecked")
        final Map<String, Object> finalObj = (Map<String, Object>) data.get("final");
        @SuppressWarnings("unchecked")
        final Map<String, Object> meta = (Map<String, Object>) data.get("meta");

        assertTrue((Boolean) decision.get("used_tier2"));
        assertEquals("mock", meta.get("tier2_provider_used"));
        assertEquals("Plastic container", finalObj.get("category"));
        assertTrue(finalObj.containsKey("instruction"));
        assertTrue(finalObj.containsKey("instructions"));
    }

    @Test
    void otherUncertainAlwaysTriggersTier2() {
        final MockMultipartFile image = new MockMultipartFile(
                "image",
                "sample.jpg",
                "image/jpeg",
                new byte[]{1, 2, 3}
        );

        final String tier1 = tier1Json("other_uncertain", 0.99, false, "metal", 0.01);

        final Map<String, Object> response = scanService.handleScan(image, tier1, null, false);

        @SuppressWarnings("unchecked")
        final Map<String, Object> data = (Map<String, Object>) response.get("data");
        @SuppressWarnings("unchecked")
        final Map<String, Object> decision = (Map<String, Object>) data.get("decision");
        @SuppressWarnings("unchecked")
        final List<String> reasons = (List<String>) decision.get("reason_codes");

        assertTrue((Boolean) decision.get("used_tier2"));
        assertTrue(reasons.contains("PRED_OTHER_UNCERTAIN"));
    }

    @Test
    void strictPlasticThresholdTriggersTier2() {
        final MockMultipartFile image = new MockMultipartFile(
                "image",
                "sample.jpg",
                "image/jpeg",
                new byte[]{1, 2, 3}
        );

        final String tier1 = tier1Json("plastic", 0.75, false, "paper", 0.10);
        final Map<String, Object> response = scanService.handleScan(image, tier1, null, false);

        @SuppressWarnings("unchecked")
        final Map<String, Object> data = (Map<String, Object>) response.get("data");
        @SuppressWarnings("unchecked")
        final Map<String, Object> decision = (Map<String, Object>) data.get("decision");
        @SuppressWarnings("unchecked")
        final List<String> reasons = (List<String>) decision.get("reason_codes");

        assertTrue((Boolean) decision.get("used_tier2"));
        assertTrue(reasons.contains("LOW_CONFIDENCE"));
    }

    @Test
    void lowMarginTriggersTier2EvenWhenConfidenceIsFine() {
        final MockMultipartFile image = new MockMultipartFile(
                "image",
                "sample.jpg",
                "image/jpeg",
                new byte[]{1, 2, 3}
        );

        final String tier1 = tier1Json("paper", 0.82, false, "plastic", 0.80);
        final Map<String, Object> response = scanService.handleScan(image, tier1, null, false);

        @SuppressWarnings("unchecked")
        final Map<String, Object> data = (Map<String, Object>) response.get("data");
        @SuppressWarnings("unchecked")
        final Map<String, Object> decision = (Map<String, Object>) data.get("decision");
        @SuppressWarnings("unchecked")
        final List<String> reasons = (List<String>) decision.get("reason_codes");

        assertTrue((Boolean) decision.get("used_tier2"));
        assertTrue(reasons.contains("LOW_MARGIN"));
    }

    @Test
    void invalidTier1JsonFallsBackToSafeTier1Default() {
        final MockMultipartFile image = new MockMultipartFile(
                "image",
                "sample.jpg",
                "image/jpeg",
                new byte[]{1, 2, 3}
        );

        final Map<String, Object> response = scanService.handleScan(image, "{not-json", null, false);

        @SuppressWarnings("unchecked")
        final Map<String, Object> data = (Map<String, Object>) response.get("data");
        @SuppressWarnings("unchecked")
        final Map<String, Object> tier1 = (Map<String, Object>) data.get("tier1");
        @SuppressWarnings("unchecked")
        final Map<String, Object> decision = (Map<String, Object>) data.get("decision");

        assertEquals("other_uncertain", tier1.get("category"));
        assertEquals(0.0, (Double) tier1.get("confidence"), 0.0001);
        assertTrue((Boolean) decision.get("used_tier2"));
    }

    @Test
    void openAiWithoutApiKeyFallsBackToMockWithErrorMetadata() {
        final MockMultipartFile image = new MockMultipartFile(
                "image",
                "sample.jpg",
                "image/jpeg",
                new byte[]{1, 2, 3}
        );

        ReflectionTestUtils.setField(scanService, "tier2Provider", "openai");
        final String tier1 = tier1Json("paper", 0.20, true, "plastic", 0.10);
        final Map<String, Object> response = scanService.handleScan(image, tier1, null, true);

        @SuppressWarnings("unchecked")
        final Map<String, Object> data = (Map<String, Object>) response.get("data");
        @SuppressWarnings("unchecked")
        final Map<String, Object> decision = (Map<String, Object>) data.get("decision");
        @SuppressWarnings("unchecked")
        final Map<String, Object> meta = (Map<String, Object>) data.get("meta");
        @SuppressWarnings("unchecked")
        final List<String> reasons = (List<String>) decision.get("reason_codes");
        @SuppressWarnings("unchecked")
        final Map<String, Object> error = (Map<String, Object>) meta.get("tier2_error");

        assertTrue((Boolean) decision.get("used_tier2"));
        assertTrue(reasons.contains("TIER2_FALLBACK_MOCK"));
        assertEquals("openai", meta.get("tier2_provider_attempted"));
        assertEquals("mock", meta.get("tier2_provider_used"));
        assertEquals("missing_api_key", error.get("code"));
    }

    private static String tier1Json(
            final String category,
            final double confidence,
            final boolean escalate,
            final String secondLabel,
            final double secondProb
    ) {
        return "{"
                + "\"category\":\"" + category + "\","
                + "\"confidence\":" + confidence + ","
                + "\"escalate\":" + escalate + ","
                + "\"top3\":["
                + "{\"label\":\"" + category + "\",\"p\":" + confidence + "},"
                + "{\"label\":\"" + secondLabel + "\",\"p\":" + secondProb + "},"
                + "{\"label\":\"paper\",\"p\":0.0}"
                + "]"
                + "}";
    }
}


