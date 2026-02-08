package tech3.binitright.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public final class ForecastService {
    private final RestTemplate restTemplate;

    @Value("${python.service.base-url}")
    private String pythonBaseUrl;

    public ForecastService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getForecastData() {
        // Appends the specific path to the base URL (localhost OR host.docker.internal)
        final String url = pythonBaseUrl + "/forecast";
        return restTemplate.getForObject(url, Map.class);
    }
}
