package tech3.binitright.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ForecastServiceTest {
    private ForecastService service;
    private FakeRestTemplate fakeRestTemplate;


    static class FakeRestTemplate extends RestTemplate {
        String lastUrl;
        Object response;

        @Override
        @SuppressWarnings("unchecked")
        public <T> T getForObject(
                String url,
                Class<T> responseType,
                Object... uriVariables
        ) {
            this.lastUrl = url;
            return (T) response;
        }
    }



    @BeforeEach
    void setUp() {
        fakeRestTemplate = new FakeRestTemplate();
        service = new ForecastService(fakeRestTemplate);

        // set @Value field
        ReflectionTestUtils.setField(service, "pythonBaseUrl", "http://localhost:5000");
    }

    @Test
    void getForecastData_callsForecastEndpoint_andReturnsMap() {
        // given
        Map<String, Object> fakeMap = new HashMap<>();
        fakeMap.put("temp", 29);
        fakeMap.put("condition", "Rain");

        fakeRestTemplate.response = fakeMap;

        // when
        Map<String, Object> result = service.getForecastData();

        // then
        assertNotNull(result);
        assertEquals(29, result.get("temp"));
        assertEquals("Rain", result.get("condition"));

        assertEquals("http://localhost:5000/forecast", fakeRestTemplate.lastUrl);
    }
}


