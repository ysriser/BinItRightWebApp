package tech3.binitright.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ForecastService {
    private final RestTemplate restTemplate;

    @Value("${python.service.url}")
    private String pythonServiceUrl;

    public ForecastService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getForecastData() {
        String url = pythonServiceUrl + "/forecast";
        return restTemplate.getForObject(url, Map.class);
    }

    public String getPrediction() {
        String url = pythonServiceUrl + "/predict";
        return restTemplate.getForObject(url, String.class);
    }
}
