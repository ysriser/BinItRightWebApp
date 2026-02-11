package techthree.binitright.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import techthree.binitright.interfacemethods.ScanInterface;

@Service
public class ScanService implements ScanInterface {

    private final ObjectMapper objectMapper;

    @Value("${scan.threshold.conf:0.70}")
    private double confThreshold;

    @Value("${scan.threshold.margin:0.05}")
    private double marginThreshold;

    @Value("${scan.threshold.conf.plastic:0.80}")
    private double plasticConfThreshold;

    @Value("${scan.threshold.conf.glass:0.80}")
    private double glassConfThreshold;

    @Value("${scan.tier2.provider:mock}")
    private String tier2Provider;

    @Value("${scan.tier2.openai.url:https://api.openai.com/v1/responses}")
    private String openAiUrl;

    @Value("${scan.tier2.openai.model:gpt-5-mini}")
    private String openAiModel;

    @Value("${scan.tier2.openai.timeout-ms:5000}")
    private int openAiTimeoutMs;

    @Value("${scan.tier2.openai.max-retries:0}")
    private int openAiMaxRetries;

    @Value("${scan.tier2.openai.reasoning-effort:minimal}")
    private String openAiReasoningEffort;

    @Value("${scan.tier2.openai.verbosity:low}")
    private String openAiVerbosity;

    private static final String OPENAI_REQUEST_FAILED = "OpenAI request failed";

    private static final String CONTENT_KEY = "content";

    private static final String TYPE_STRING = "string";

    private static final String MAX_LENGTH = "maxLength";

    private static final String MIN_LENGTH = "minLength";

    private static final String UNKNOWN = "unknown";

    private static final String ERROR_MESSAGE = "message";

    private static final String KEY_INSTRUCTIONS = "instructions";

    private static final String KEY_INSTRUCTION = "instruction";

    private static final String KEY_RECYCLABLE = "recyclable";

    public ScanService(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Map<String, Object> handleScan(
            final MultipartFile image,
            final String tier1Json,
            final Long timestamp,
            final boolean forceCloud
    ) {
        final long startMs = System.currentTimeMillis();
        final String requestId = UUID.randomUUID().toString();

        final Map<String, Object> tier1 = parseTier1OrFallback(tier1Json);
        final String category = String.valueOf(tier1.get("category"));
        final double confidence = toDouble(tier1.get("confidence"));
        final boolean tier1Escalate = Boolean.TRUE.equals(tier1.get("escalate"));
        final double margin = computeMargin(tier1);

        final double categoryThreshold = getCategoryThreshold(category);
        final boolean lowConfidence = confidence < categoryThreshold;
        final boolean lowMargin = margin < marginThreshold;
        final boolean otherUncertain = "other_uncertain".equalsIgnoreCase(category);

        final List<String> reasonCodes = new ArrayList<>();
        if (forceCloud) {
            reasonCodes.add("FORCE_CLOUD");
        }
        if (lowConfidence) {
            reasonCodes.add("LOW_CONFIDENCE");
        }
        if (lowMargin) {
            reasonCodes.add("LOW_MARGIN");
        }
        if (otherUncertain) {
            reasonCodes.add("PRED_OTHER_UNCERTAIN");
        }
        if (tier1Escalate) {
            reasonCodes.add("TIER1_ESCALATE");
        }

        final boolean usedTier2 = forceCloud || tier1Escalate || otherUncertain || lowConfidence || lowMargin;

        final Map<String, Object> finalResult;
        final Map<String, Object> meta = new HashMap<>();
        meta.put("schema_version", "0.1");
        meta.put("force_cloud", forceCloud);
        if (timestamp != null) {
            meta.put("request_timestamp", timestamp);
        }

        if (usedTier2) {
            final String providerAttempted = normalizeProvider(tier2Provider);
            meta.put("tier2_provider_attempted", providerAttempted);

            if ("openai".equals(providerAttempted)) {
                Map<String, Object> openAiFinal = null;
                OpenAiCallException openAiError = null;

                for (int attempt = 0; attempt <= openAiMaxRetries; attempt++) {
                    try {
                        openAiFinal = callOpenAiForFinal(image, tier1);
                        break;
                    } catch (OpenAiCallException ex) {
                        openAiError = ex;
                    }
                }

                if (openAiFinal != null) {
                    finalResult = openAiFinal;
                    meta.put("tier2_provider_used", "openai");
                } else {
                    finalResult = buildMockTier2Final(tier1);
                    reasonCodes.add("TIER2_FALLBACK_MOCK");
                    meta.put("tier2_provider_used", "mock");
                    if (openAiError != null) {
                        meta.put("tier2_error", openAiError.getError());
                    }
                }
            } else {
                finalResult = buildMockTier2Final(tier1);
                meta.put("tier2_provider_used", "mock");
            }
        } else {
            finalResult = buildTier1Final(tier1);
            meta.put("tier2_provider_attempted", "mock");
            meta.put("tier2_provider_used", "mock");
        }

        final Map<String, Object> decision = new HashMap<>();
        decision.put("used_tier2", usedTier2);
        decision.put("reason_codes", reasonCodes);

        final Map<String, Object> thresholds = new HashMap<>();
        thresholds.put("conf_threshold", confThreshold);
        thresholds.put("margin_threshold", marginThreshold);
        decision.put("thresholds", thresholds);

        final Map<String, Object> latency = new HashMap<>();
        latency.put("total", System.currentTimeMillis() - startMs);
        meta.put("latency_ms", latency);

        final Map<String, Object> data = new HashMap<>();
        data.put("tier1", tier1);
        data.put("decision", decision);
        data.put("final", finalResult);
        data.put("meta", meta);

        final Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("request_id", requestId);
        response.put("data", data);
        return response;
    }

    private Map<String, Object> parseTier1OrFallback(final String tier1Json) {
        if (tier1Json == null || tier1Json.isBlank()) {
            return createFallbackTier1();
        }

        try {
            final JsonNode root = objectMapper.readTree(tier1Json);
            final Map<String, Object> parsed = new HashMap<>();
            parsed.put("category", root.path("category").asText("other_uncertain"));
            parsed.put("confidence", root.path("confidence").asDouble(0.0));
            parsed.put("escalate", root.path("escalate").asBoolean(true));

            final List<Map<String, Object>> top3 = new ArrayList<>();
            for (JsonNode item : root.path("top3")) {
                final Map<String, Object> one = new HashMap<>();
                one.put("label", item.path("label").asText("other_uncertain"));
                one.put("p", item.path("p").asDouble(0.0));
                top3.add(one);
            }
            parsed.put("top3", top3);
            return parsed;
        } catch (Exception ex) {
            return createFallbackTier1();
        }
    }

    private Map<String, Object> createFallbackTier1() {
        final Map<String, Object> tier1 = new HashMap<>();
        tier1.put("category", "other_uncertain");
        tier1.put("confidence", 0.0);
        tier1.put("escalate", true);
        tier1.put("top3", new ArrayList<>());
        return tier1;
    }

    private double computeMargin(final Map<String, Object> tier1) {
        final Object rawTop3 = tier1.get("top3");
        if (rawTop3 instanceof List<?> top3List && top3List.size() >= 2) {
            final double p1 = extractProbability(top3List.get(0));
            final double p2 = extractProbability(top3List.get(1));
            return p1 - p2;
        }
        return toDouble(tier1.get("confidence"));
    }

    private double extractProbability(final Object topItem) {
        if (topItem instanceof Map<?, ?> map) {
            return toDouble(map.get("p"));
        }
        return 0.0;
    }

    private double toDouble(final Object raw) {
        if (raw instanceof Number number) {
            return number.doubleValue();
        }
        if (raw == null) {
            return 0.0;
        }
        try {
            return Double.parseDouble(String.valueOf(raw));
        } catch (Exception ex) {
            return 0.0;
        }
    }

    private double getCategoryThreshold(final String category) {
        if ("plastic".equalsIgnoreCase(category)) {
            return plasticConfThreshold;
        }
        if ("glass".equalsIgnoreCase(category)) {
            return glassConfThreshold;
        }
        return confThreshold;
    }

    private Map<String, Object> buildTier1Final(final Map<String, Object> tier1) {
        final String category = String.valueOf(tier1.get("category")).toLowerCase(Locale.ROOT);
        final double confidence = toDouble(tier1.get("confidence"));

        if ("paper".equals(category)) {
            return buildFinal("Paper", true, confidence, List.of(
                    "Keep paper clean and dry before recycling.",
                    "Remove food-stained layers if possible.",
                    "Place in the blue recycling bin."
            ));
        }
        if ("plastic".equals(category)) {
            return buildFinal("Plastic", true, confidence, List.of(
                    "Empty all contents from the container.",
                    "Rinse to remove residue.",
                    "Place clean plastic in the blue recycling bin."
            ));
        }
        if ("metal".equals(category)) {
            return buildFinal("Metal", true, confidence, List.of(
                    "Empty the can or metal container.",
                    "Rinse quickly if needed.",
                    "Place in the blue recycling bin."
            ));
        }
        if ("glass".equals(category)) {
            return buildFinal("Glass", true, confidence, List.of(
                    "Empty all contents from the glass item.",
                    "Rinse to remove food or drink residue.",
                    "Place unbroken glass in the blue recycling bin."
            ));
        }
        if ("e-waste".equals(category)) {
            return buildFinal("E-waste - Electronic Item", false, confidence, List.of(
                    "Do not place electronics in the blue recycling bin.",
                    "Remove detachable batteries if safe.",
                    "Bring to an e-waste collection point."
            ));
        }
        if ("textile".equals(category)) {
            return buildFinal("Textile - Fabric Item", false, confidence, List.of(
                    "Keep textile items dry before disposal.",
                    "Separate reusable clothing if possible.",
                    "Bring to a textile collection point or donation channel."
            ));
        }

        return buildFinal("Not sure", false, confidence, List.of(
                "Dispose as general waste to avoid recycling contamination.",
                "If item has battery or electronics, use an e-waste collection point.",
                "Only place clean paper/plastic/metal/glass into the blue recycling bin."
        ));
    }

    private Map<String, Object> buildMockTier2Final(final Map<String, Object> tier1) {
        final String category = String.valueOf(tier1.get("category")).toLowerCase(Locale.ROOT);
        final double confidence = toDouble(tier1.get("confidence"));

        if ("paper".equals(category)) {
            return buildFinal("Paper packaging", true, confidence, List.of(
                    "Keep paper clean and dry before recycling.",
                    "Tear off wet or oily sections.",
                    "Recycle the clean part in the blue recycling bin."
            ));
        }
        if ("plastic".equals(category)) {
            return buildFinal("Plastic container", true, confidence, List.of(
                    "Empty all contents before disposal.",
                    "Rinse the container to remove residue.",
                    "Place the clean container in the blue recycling bin."
            ));
        }
        if ("metal".equals(category)) {
            return buildFinal("Metal container", true, confidence, List.of(
                    "Empty all contents from the container.",
                    "Rinse lightly if needed.",
                    "Place in the blue recycling bin."
            ));
        }
        if ("glass".equals(category)) {
            return buildFinal("Glass container", true, confidence, List.of(
                    "Empty all contents from the glass item.",
                    "Rinse to remove residue.",
                    "Place unbroken glass in the blue recycling bin."
            ));
        }
        if ("e-waste".equals(category)) {
            return buildFinal("E-waste - Electronic Item", false, confidence, List.of(
                    "Do not place electronics in the blue recycling bin.",
                    "Remove batteries if removable and safe.",
                    "Bring the item to an e-waste collection point."
            ));
        }
        if ("textile".equals(category)) {
            return buildFinal("Textile - Fabric Item", false, confidence, List.of(
                    "Keep textile items dry before disposal.",
                    "Separate reusable clothing from damaged pieces.",
                    "Send to textile collection or donation channels."
            ));
        }

        return buildFinal("Not sure", false, confidence, List.of(
                "Dispose as general waste to avoid contaminating recycling streams.",
                "If the item may contain electronics or battery, use e-waste collection points.",
                "Only place clean and known recyclable materials into the blue recycling bin."
        ));
    }

    private Map<String, Object> buildFinal(
            final String category,
            final boolean recyclable,
            final double confidence,
            final List<String> instructions
    ) {
        final List<String> safeInstructions = new ArrayList<>(instructions);
        if (safeInstructions.isEmpty()) {
            safeInstructions.add("Dispose according to local recycling guidelines.");
            safeInstructions.add("If unsure, dispose as general waste.");
        }
        return buildFinal(category, recyclable, confidence, safeInstructions.get(0), safeInstructions);
    }

    private Map<String, Object> buildFinal(
            final String category,
            final boolean recyclable,
            final double confidence,
            final String instruction,
            final List<String> instructions
    ) {
        final List<String> safeInstructions = new ArrayList<>(instructions);
        if (safeInstructions.isEmpty()) {
            safeInstructions.add("Dispose according to local recycling guidelines.");
            safeInstructions.add("If unsure, dispose as general waste.");
        }

        final String safeInstruction =
                instruction == null || instruction.isBlank() ? safeInstructions.get(0) : instruction;

        final Map<String, Object> result = new HashMap<>();
        result.put("category", category);
        result.put(KEY_RECYCLABLE, recyclable);
        result.put("confidence", clampConfidence(confidence));
        result.put(KEY_INSTRUCTION, safeInstruction);
        result.put(KEY_INSTRUCTIONS, safeInstructions);
        return result;
    }

    private double clampConfidence(final double confidence) {
        if (confidence < 0.0) {
            return 0.0;
        }
        if (confidence > 1.0) {
            return 1.0;
        }
        return confidence;
    }

    private String normalizeProvider(final String provider) {
        if (provider == null || provider.isBlank()) {
            return "mock";
        }
        final String lowered = provider.trim().toLowerCase(Locale.ROOT);
        if ("openai".equals(lowered)) {
            return "openai";
        }
        return "mock";
    }

    private Map<String, Object> callOpenAiForFinal(
            final MultipartFile image,
            final Map<String, Object> tier1
    ) {
        final String apiKey = resolveOpenAiKey();
        if (apiKey == null || apiKey.isBlank()) {
            final Map<String, Object> error = new HashMap<>();
            error.put("type", "auth");
            error.put("code", "missing_api_key");
            error.put(ERROR_MESSAGE, "OPENAI_API_KEY or LLM_API_KEY is not set");
            throw new OpenAiCallException(error);
        }

        try {
            final byte[] bytes = image.getBytes();
            final String contentType = image.getContentType() == null
                    ? MediaType.IMAGE_JPEG_VALUE
                    : image.getContentType();
            final String dataUrl = "data:" + contentType + ";base64,"
                    + Base64.getEncoder().encodeToString(bytes);

            final Map<String, Object> payload = buildOpenAiPayload(dataUrl, tier1);
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            final HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
            final ResponseEntity<String> response = buildOpenAiRestTemplate().exchange(
                    openAiUrl,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            final String body = response.getBody();
            if (body == null || body.isBlank()) {
                throw unknownOpenAiError("Empty OpenAI response body");
            }

            return parseOpenAiFinal(body);
        } catch (HttpStatusCodeException ex) {
            throw new OpenAiCallException(parseOpenAiError(ex));
        } catch (ResourceAccessException ex) {
            final Map<String, Object> error = new HashMap<>();
            error.put("type", "timeout");
            error.put("code", "timeout");
            error.put(ERROR_MESSAGE, "OpenAI request timeout or network error");
            throw new OpenAiCallException(error);
        } catch (IOException ex) {
            throw unknownOpenAiError("Failed to read image bytes");
        } catch (OpenAiCallException ex) {
            throw ex;
        } catch (Exception ex) {
            throw unknownOpenAiError(ex.getMessage() == null ? UNKNOWN : ex.getMessage());
        }
    }

    private RestTemplate buildOpenAiRestTemplate() {
        final SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(openAiTimeoutMs);
        factory.setReadTimeout(openAiTimeoutMs);
        return new RestTemplate(factory);
    }

    private Map<String, Object> buildOpenAiPayload(
            final String dataUrl,
            final Map<String, Object> tier1
    ) {
        final Map<String, Object> schema = new HashMap<>();
        schema.put("type", "object");
        schema.put("additionalProperties", false);
        schema.put("required", List.of("category", KEY_RECYCLABLE, "confidence", KEY_INSTRUCTION, KEY_INSTRUCTIONS));

        final Map<String, Object> properties = new HashMap<>();
        properties.put("category", Map.of("type", TYPE_STRING, MIN_LENGTH, 1, MAX_LENGTH, 80));
        properties.put(KEY_RECYCLABLE, Map.of("type", "boolean"));
        properties.put("confidence", Map.of("type", "number", "minimum", 0, "maximum", 1));
        properties.put(KEY_INSTRUCTION, Map.of("type", TYPE_STRING, MIN_LENGTH, 1, MAX_LENGTH, 140));
        properties.put(KEY_INSTRUCTIONS , Map.of(
                "type", "array",
                "minItems", 2,
                "maxItems", 8,
                "items", Map.of("type", TYPE_STRING, MIN_LENGTH, 1, MAX_LENGTH, 180)
        ));
        schema.put("properties", properties);

        final Map<String, Object> textFormat = new HashMap<>();
        textFormat.put("type", "json_schema");
        textFormat.put("name", "ScanFinal");
        textFormat.put("strict", true);
        textFormat.put("schema", schema);

        final Map<String, Object> text = new HashMap<>();
        text.put("format", textFormat);

        final String model = openAiModel == null || openAiModel.isBlank() ? "gpt-4o-mini" : openAiModel;
        if (model.startsWith("gpt-5")) {
            text.put("verbosity", openAiVerbosity == null || openAiVerbosity.isBlank() ? "low" : openAiVerbosity);
        }

        final String tier1Category = String.valueOf(tier1.get("category")).trim().toLowerCase(Locale.ROOT);
        final double tier1Confidence = toDouble(tier1.get("confidence"));
        final boolean tier1HighlyUncertain = "other_uncertain".equals(tier1Category)
                || "uncertain".equals(tier1Category)
                || UNKNOWN.equals(tier1Category)
                || tier1Confidence < confThreshold;

        final String tier1Hint;
        if (tier1HighlyUncertain) {
            tier1Hint = "Tier1 is uncertain. Do not mirror Tier1 label. Use image evidence as primary source.";
        } else {
            tier1Hint = "Tier1 hint (may still be wrong): category=" + tier1.get("category")
                    + ", confidence=" + tier1.get("confidence")
                    + ", top3=" + tier1.get("top3");
        }
        final String systemPrompt = "You are a Tier-2 recycling disposal expert for a Singapore waste-sorting app. "
                + "Tier-1 already made a first guess. Output ONLY a JSON object that strictly matches the schema. "
                + "No markdown, no extra keys, no extra text. Keep output concise so the API can return quickly.\n\n"
                + "Rules:\n"
                + "1) Do NOT ask user questions. Do NOT output quiz or follow-up questions.\n"
                + "2) category:\n"
                + "   - If item is e-waste (electronics, battery, cable, charger, small device), category MUST start with 'E-waste - '.\n"
                + "   - If item is textile/fabric/clothing, category MUST start with 'Textile - '.\n"
                + "   - For any clearly visible main object, category MUST be a concrete short noun phrase (for example: 'Ceramic mug', 'Plastic takeaway box', 'A Heytea cup with lid').\n"
                + "   - Do NOT output uncertain just because the object is outside Tier-1 labels.\n"
                + "   - Use category='Not sure' ONLY when the image is truly unreadable (severe blur/out-of-focus/fully occluded) or no clear single main item exists.\n"
                + "3) recyclable:\n"
                + "   - true ONLY for normal blue-bin flow (clean, dry, mostly single-material recyclable).\n"
                + "   - false for e-waste, textile, contaminated paper, heavily food-stained items, unknown items.\n"
                + "4) instructions:\n"
                + "   - Provide disposal-only steps (2-5), imperative style.\n"
                + "   - For composite items, explain each part clearly (for example: empty/rinse first, then where each part goes).\n"
                + "   - If special drop-off is needed, say generic 'bring to an e-waste recycling point'.\n"
                + "   - Do not include store names or exact addresses.\n"
                + "5) confidence:\n"
                + "   - 0.85-0.99 when very clear.\n"
                + "   - 0.55-0.80 when somewhat clear.\n"
                + "   - If category='Not sure', confidence MUST be <=0.54.\n"
                + "6) If the photo is clearly unusable, you may include ONE short rescan hint, but still provide safe disposal guidance in instructions.\n"
                + "If they're pranks (like just taking pictures of people or airplanes), you can be appropriately humorous, but don't be offensive.\n"
                ;

        final List<Map<String, Object>> input = new ArrayList<>();
        input.add(Map.of(
                "role", "system",
                CONTENT_KEY, List.of(Map.of("type", "input_text", "text", systemPrompt))
        ));
        input.add(Map.of(
                "role", "user",
                CONTENT_KEY, List.of(
                        Map.of("type", "input_text", "text", tier1Hint),
                        Map.of("type", "input_image", "image_url", dataUrl)
                )
        ));

        final Map<String, Object> payload = new HashMap<>();
        payload.put("model", model);
        payload.put("input", input);
        payload.put("text", text);

        if (model.startsWith("gpt-5")) {
            payload.put("reasoning", Map.of(
                    "effort", openAiReasoningEffort == null || openAiReasoningEffort.isBlank()
                            ? "minimal"
                            : openAiReasoningEffort
            ));
        }

        return payload;
    }

    private Map<String, Object> parseOpenAiFinal(final String body) throws IOException {
        final JsonNode root = objectMapper.readTree(body);
        String outputJson = root.path("output_text").asText(null);

        if (outputJson == null || outputJson.isBlank()) {
            for (JsonNode outputNode : root.path("output")) {
                for (JsonNode contentNode : outputNode.path("content")) {
                    if (contentNode.has("text")) {
                        outputJson = contentNode.path("text").asText("");
                        if (!outputJson.isBlank()) {
                            break;
                        }
                    }
                }
                if (outputJson != null && !outputJson.isBlank()) {
                    break;
                }
            }
        }

        if (outputJson == null || outputJson.isBlank()) {
            throw unknownOpenAiError("OpenAI response does not contain output_text");
        }

        final JsonNode finalNode = objectMapper.readTree(outputJson);
        String category = finalNode.path("category").asText("").trim();
        if ("other_uncertain".equalsIgnoreCase(category)
                || "uncertain".equalsIgnoreCase(category)
                || "not sure".equalsIgnoreCase(category)
                || UNKNOWN.equalsIgnoreCase(category)) {
            category = "Not sure";
        }
        final String instruction = finalNode.path(KEY_INSTRUCTION).asText("").trim();
        final boolean recyclable = finalNode.path(KEY_RECYCLABLE).asBoolean(false);
        final double confidence = clampConfidence(finalNode.path("confidence").asDouble(0.0));

        final List<String> instructions = new ArrayList<>();
        for (JsonNode step : finalNode.path(KEY_INSTRUCTIONS)) {
            final String text = step.asText("").trim();
            if (!text.isBlank()) {
                instructions.add(text);
            }
        }

        if (category.isBlank() || instruction.isBlank() || instructions.isEmpty()) {
            throw unknownOpenAiError("OpenAI JSON is missing required fields");
        }
        if (instructions.size() == 1) {
            instructions.add("Follow local disposal guidance if the item is mixed material.");
        }

        return buildFinal(category, recyclable, confidence, instruction, instructions);
    }

    private OpenAiCallException unknownOpenAiError(final String message) {
        final Map<String, Object> error = new HashMap<>();
        error.put("type", UNKNOWN);
        error.put("code", UNKNOWN);
        error.put(ERROR_MESSAGE, message);
        return new OpenAiCallException(error);
    }

    private Map<String, Object> parseOpenAiError(final HttpStatusCodeException ex) {
        final Map<String, Object> error = new HashMap<>();
        error.put("http_status", String.valueOf(ex.getStatusCode().value()));

        final String body = ex.getResponseBodyAsString(StandardCharsets.UTF_8);
        if (body == null || body.isBlank()) {
            error.put("type", UNKNOWN);
            error.put("code", UNKNOWN);
            error.put(ERROR_MESSAGE, OPENAI_REQUEST_FAILED);
            return error;
        }

        try {
            final JsonNode root = objectMapper.readTree(body);
            final JsonNode err = root.path("error");
            error.put("type", err.path("type").asText(UNKNOWN));
            error.put("code", err.path("code").asText(UNKNOWN));
            error.put(ERROR_MESSAGE, err.path(ERROR_MESSAGE).asText(OPENAI_REQUEST_FAILED));
        } catch (Exception parseEx) {
            error.put("type", UNKNOWN);
            error.put("code", UNKNOWN);
            error.put(ERROR_MESSAGE, OPENAI_REQUEST_FAILED);
        }

        return error;
    }

    private String resolveOpenAiKey() {
        final String primary = System.getenv("OPENAI_API_KEY");
        if (primary != null && !primary.isBlank()) {
            return primary;
        }
        final String fallback = System.getenv("LLM_API_KEY");
        if (fallback != null && !fallback.isBlank()) {
            return fallback;
        }
        return null;
    }

    private static final class OpenAiCallException extends RuntimeException {
        private final transient Map<String, Object> error;

        private OpenAiCallException(final Map<String, Object> error) {
            this.error = error;
        }

        private Map<String, Object> getError() {
            return error;
        }
    }
}



