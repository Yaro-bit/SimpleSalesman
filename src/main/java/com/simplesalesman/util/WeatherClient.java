package com.simplesalesman.util;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherClient {

    private static final String BASE_URL = "https://wttr.in/%s?format=3"; // Kostenlose Wetterdaten ohne API-Key

    private final RestTemplate restTemplate;

    public WeatherClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String fetchWeather(String city) {
        String url = String.format(BASE_URL, city);
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            return "Fehler beim Abrufen der Wetterdaten: " + e.getMessage();
        }
    }
}
