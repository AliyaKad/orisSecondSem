package ru.itis.inf304.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyService {

    private final String apiUrl = "https://api.exchangerate-api.com/v4/latest/{base}";

    private final RestTemplate restTemplate;

    public CurrencyService() {
        this.restTemplate = new RestTemplate();
    }


    public Map<String, Double> getLatestRates(String baseCurrency) {
        try {
            String url = apiUrl.replace("{base}", baseCurrency);
            ResponseEntity<CurrencyResponse> response = restTemplate.getForEntity(url, CurrencyResponse.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getRates();
            } else {
                throw new RuntimeException("Failed to fetch currency rates");
            }
        } catch (RestClientException e) {
            throw new RuntimeException("Error while fetching currency rates: " + e.getMessage());
        }
    }


    private static class CurrencyResponse {
        private Map<String, Double> rates;

        public Map<String, Double> getRates() {
            return rates;
        }

        public void setRates(Map<String, Double> rates) {
            this.rates = rates;
        }
    }
}