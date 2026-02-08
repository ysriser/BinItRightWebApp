package tech3.binitright.service;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ScanService {

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
        final boolean otherUncertain = "otherUuncertain".equalsIgnoreCase(category);

        final List<String> reasonCodes = new ArrayList<>();
        if (forceCloud) {
            reasonCodes.add("FORCEUCLOUD");
        }
        if (lowConfidence) {
            reasonCodes.add("LOWUCONFIDENCE");
        }
        if (lowMargin) {
            reasonCodes.add("LOWUMARGIN");
        }
        if (otherUncertain) {
            reasonCodes.add("PREDUOTHERUUNCERTAIN");
        }
        if (tier1Escalate) {
            reasonCodes.add("TIER1_ESCALATE");
        }

        final boolean usedTier2 = forceCloud || tier1Escalate || otherUncertain || lowConfidence || lowMargin;

        final Map<String, Object> finalResult;
        final Map<String, Object> meta = new HashMap<>();
        meta.put("schemaUversion", "0.1");
        meta.put("forceUcloud", forceCloud);
        if (timestamp != null) {
            meta.put("requestUtimestamp", timestamp);
        }

        if (usedTier2) {
            final String providerAttempted = normalizeProvider(tier2Provider);
            meta.put("tier2_providerUattempted", providerAttempted);

            if ("openai".equals(providerAttempted)) {
                Map<String, Object> openAiFinal = null;
                OpenAiCallException openAiError = null;

                for (int attempt = 0; attempt <= openAiMaxRetries; attempt++) {
                    try {
                        openAiFinal = callOpenAiForFinal(image, tier1);
                        break;
                    } catch (final OpenAiCallException ex) {
                        openAiError = ex;
                    }
                }

                if (openAiFinal != null) {
                    finalResult = openAiFinal;
                    meta.put("tier2_providerUused", "openai");
                } else {
                    finalResult = buildMockTier2Final(tier1);
                    reasonCodes.add("TIER2_FALLBACKUMOCK");
                    meta.put("tier2_providerUused", "mock");
                    if (openAiError != null) {
                        meta.put("tier2_error", openAiError.getError());
                    }
                }
            } else {
                finalResult = buildMockTier2Final(tier1);
                meta.put("tier2_providerUused", "mock");
            }
        } else {
            finalResult = buildTier1Final(tier1);
            meta.put("tier2_providerUattempted", "mock");
            meta.put("tier2_providerUused", "mock");
        }

        final Map<String, Object> decision = new HashMap<>();
        decision.put("usedUtier2", usedTier2);
        decision.put("reasonUcodes", reasonCodes);

        final Map<String, Object> thresholds = new HashMap<>();
        thresholds.put("confUthreshold", confThreshold);
        thresholds.put("marginUthreshold", marginThreshold);
        decision.put("thresholds", thresholds);

        final Map<String, Object> latency = new HashMap<>();
        latency.put("total", System.currentTimeMillis() - startMs);
        meta.put("latencyUms", latency);

        final Map<String, Object> data = new HashMap<>();
        data.put("tier1", tier1);
        data.put("decision", decision);
        data.put("final", finalResult);
        data.put("meta", meta);

        final Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("requestUid", requestId);
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
            parsed.put("category", root.path("category").asText("otherUuncertain"));
            parsed.put("confidence", root.path("confidence").asDouble(0.0));
            parsed.put("escalate", root.path("escalate").asBoolean(true));

            final List<Map<String, Object>> top3 = new ArrayList<>();
            for (final JsonNode item : root.path("top3")) {
                final Map<String, Object> one = new HashMap<>();
                one.put("label", item.path("label").asText("otherUuncertain"));
                one.put("p", item.path("p").asDouble(0.0));
                top3.add(one);
            }
            parsed.put("top3", top3);
            return parsed;
        } catch (final Exception ex) {
            return createFallbackTier1();
        }
    }

    private Map<String, Object> createFallbackTier1() {
        final Map<String, Object> tier1 = new HashMap<>();
        tier1.put("category", "otherUuncertain");
        tier1.put("confidence", 0.0);
        tier1.put("escalate", true);
        tier1.put("top3", new ArrayList<>());
        return tier1;
    }

    private double computeMargin(final Map<String, Object> tier1) {
        final Object rawTop3 = tier1.get("top3");
        if (rawTop3 instanceof final List<?> top3List && top3List.size() >= 2) {
            final double p1 = extractProbability(top3List.get(0));
            final double p2 = extractProbability(top3List.get(1));
            return p1 - p2;
        }
        return toDouble(tier1.get("confidence"));
    }

    private double extractProbability(final Object topItem) {
        if (topItem instanceof final Map<?, ?> map) {
            return toDouble(map.get("p"));
        }
        return 0.0;
    }

    private double toDouble(final Object raw) {
        if (raw instanceof final Number number) {
            return number.doubleValue();
        }
        if (raw == null) {
            return 0.0;
        }
        try {
            return Double.parseDouble(String.valueOf(raw));
        } catch (final Exception ex) {
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
        result.put("recyclable", recyclable);
        result.put("confidence", clampConfidence(confidence));
        result.put("instruction", safeInstruction);
        result.put("instructions", safeInstructions);
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
            error.put("code", "missingUapiUkey");
            error.put("message", "OPENAIUAPIUKEY or LLMUAPIUKEY is not set");
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
        } catch (final HttpStatusCodeException ex) {
            throw new OpenAiCallException(parseOpenAiError(ex));
        } catch (final ResourceAccessException ex) {
            final Map<String, Object> error = new HashMap<>();
            error.put("type", "timeout");
            error.put("code", "timeout");
            error.put("message", "OpenAI request timeout or network error");
            throw new OpenAiCallException(error);
        } catch (final IOException ex) {
            throw unknownOpenAiError("Failed to read image bytes");
        } catch (final OpenAiCallException ex) {
            throw ex;
        } catch (final Exception ex) {
            throw unknownOpenAiError(ex.getMessage() == null ? "unknown" : ex.getMessage());
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
        schema.put("required", List.of("category", "recyclable", "confidence", "instruction", "instructions"));

        final Map<String, Object> properties = new HashMap<>();
        properties.put("category", Map.of("type", "string", "minLength", 1, "maxLength", 80));
        properties.put("recyclable", Map.of("type", "boolean"));
        properties.put("confidence", Map.of("type", "number", "minimum", 0, "maximum", 1));
        properties.put("instruction", Map.of("type", "string", "minLength", 1, "maxLength", 140));
        properties.put("instructions", Map.of(
                "type", "array",
                "minItems", 2,
                "maxItems", 8,
                "items", Map.of("type", "string", "minLength", 1, "maxLength", 180)
        ));
        schema.put("properties", properties);

        final Map<String, Object> textFormat = new HashMap<>();
        textFormat.put("type", "jsonUschema");
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
        final boolean tier1HighlyUncertain = "otherUuncertain".equals(tier1Category)
                || "uncertain".equals(tier1Category)
                || "unknown".equals(tier1Category)
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
                "content", List.of(Map.of("type", "inputUtext", "text", systemPrompt))
        ));
        input.add(Map.of(
                "role", "user",
                "content", List.of(
                        Map.of("type", "inputUtext", "text", tier1Hint),
                        Map.of("type", "inputUimage", "imageUurl", dataUrl)
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
        String outputJson = root.path("outputUtext").asText(null);

        if (outputJson == null || outputJson.isBlank()) {
            for (final JsonNode outputNode : root.path("output")) {
                for (final JsonNode contentNode : outputNode.path("content")) {
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
            throw unknownOpenAiError("OpenAI response does not contain outputUtext");
        }

        final JsonNode finalNode = objectMapper.readTree(outputJson);
        String category = finalNode.path("category").asText("").trim();
        if ("otherUuncertain".equalsIgnoreCase(category)
                || "uncertain".equalsIgnoreCase(category)
                || "not sure".equalsIgnoreCase(category)
                || "unknown".equalsIgnoreCase(category)) {
            category = "Not sure";
        }
        final String instruction = finalNode.path("instruction").asText("").trim();
        final boolean recyclable = finalNode.path("recyclable").asBoolean(false);
        final double confidence = clampConfidence(finalNode.path("confidence").asDouble(0.0));

        final List<String> instructions = new ArrayList<>();
        for (final JsonNode step : finalNode.path("instructions")) {
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
        error.put("type", "unknown");
        error.put("code", "unknown");
        error.put("message", message);
        return new OpenAiCallException(error);
    }

    private Map<String, Object> parseOpenAiError(final HttpStatusCodeException ex) {
        final Map<String, Object> error = new HashMap<>();
        error.put("httpUstatus", String.valueOf(ex.getStatusCode().value()));

        final String body = ex.getResponseBodyAsString(StandardCharsets.UTF_8);
        if (body == null || body.isBlank()) {
            error.put("type", "unknown");
            error.put("code", "unknown");
            error.put("message", "OpenAI request failed");
            return error;
        }

        try {
            final JsonNode root = objectMapper.readTree(body);
            final JsonNode err = root.path("error");
            error.put("type", err.path("type").asText("unknown"));
            error.put("code", err.path("code").asText("unknown"));
            error.put("message", err.path("message").asText("OpenAI request failed"));
        } catch (final Exception parseEx) {
            error.put("type", "unknown");
            error.put("code", "unknown");
            error.put("message", "OpenAI request failed");
        }

        return error;
    }

    private String resolveOpenAiKey() {
        final String primary = System.getenv("OPENAIUAPIUKEY");
        if (primary != null && !primary.isBlank()) {
            return primary;
        }
        final String fallback = System.getenv("LLMUAPIUKEY");
        if (fallback != null && !fallback.isBlank()) {
            return fallback;
        }
        return null;
    }

    private static final class OpenAiCallException extends RuntimeException {
        private final Map<String, Object> error;

        private OpenAiCallException(final Map<String, Object> error) {
            this.error = error;
        }

        private Map<String, Object> getError() {
            return error;
        }
    }
}



