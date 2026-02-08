package tech3.binitright.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ForecastService {
    private final RestTemplate restTemplate;

    @Value("${python.service.base-url}")
    private String pythonBaseUrl;

    public ForecastService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getForecastData() {
        // Appends the specific path to the base URL (localhost OR host.docker.internal)
        String url = pythonBaseUrl + "/forecast";
        return restTemplate.getForObject(url, Map.class);
    }
}
