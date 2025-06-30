package com.simplesalesman.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * HTTP utility class for retrieving weather data using coordinates.
 *
 * This client handles:
 * - Querying wttr.in with encoded coordinates (e.g., "48.2,16.3")
 * - Reverse geocoding via OpenStreetMap Nominatim (for optional city name)
 * - Combining both to return formatted result like "Vienna: ☀️ +24°C"
 *
 * External API Calls:
 * - https://wttr.in/{query}?format=3 → returns plain-text weather
 * - https://nominatim.openstreetmap.org/reverse → resolves city name
 *
 * Functional Behavior:
 * - Always sends coordinates to wttr.in
 * - Tries to resolve city for a prettier label
 * - Falls back to coordinate prefix if city cannot be resolved
 *
 * API Format Examples:
 * - wttr.in/48.3,14.3 → "48.3,14.3: 🌧 +18°C"
 * - With reverse geocoding: "Linz: 🌧 +18°C"
 *
 * Error Handling:
 * - Graceful fallback: returns raw weather string or "Weather service unavailable"
 * - Invalid coordinates or missing data do not crash the system
 *
 * Dependencies:
 * - RestTemplate: for HTTP requests
 * - Jackson ObjectMapper: for JSON parsing of reverse geocoding result
 *
 * @version 0.0.9
 * @since 0.0.3
 */
@Component
public class WeatherClient {

    private static final Logger logger = LoggerFactory.getLogger(WeatherClient.class);
    private static final String WEATHER_URL = "https://wttr.in/%s?format=3";
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/reverse?lat=%s&lon=%s&format=json";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public WeatherClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String fetchWeatherByLatLon(double lat, double lon) {
        String coordString = String.format("%.4f,%.4f", lat, lon);
        String weather = fetchWeather(coordString); // e.g. "48.2082,16.3738: ☀️ +24°C"

        String city = resolveCity(lat, lon);
        if (city != null && !city.isBlank()) {
            return replacePrefixWithCity(weather, city);
        }
        return weather;
    }

    private String fetchWeather(String query) {
        try {
            String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url = String.format(WEATHER_URL, encoded);

            HttpHeaders headers = new HttpHeaders();
            headers.add("User-Agent", "SimpleSalesman/1.0");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            logger.debug("Weather response for '{}': {}", query, response.getBody());

            return response.getBody();
        } catch (Exception e) {
            logger.error("Failed to fetch weather for '{}': {}", query, e.getMessage());
            return "Weather service unavailable";
        }
    }

    private String resolveCity(double lat, double lon) {
        try {
            String url = String.format(NOMINATIM_URL, lat, lon);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

            JsonNode json = objectMapper.readTree(response.getBody());
            if (json.has("address")) {
                JsonNode addr = json.get("address");
                if (addr.has("city")) return addr.get("city").asText();
                if (addr.has("town")) return addr.get("town").asText();
                if (addr.has("village")) return addr.get("village").asText();
                if (addr.has("hamlet")) return addr.get("hamlet").asText();
            }
        } catch (Exception e) {
            logger.warn("Failed to resolve city from lat={}, lon={}: {}", lat, lon, e.getMessage());
        }
        return null;
    }

    private String replacePrefixWithCity(String weather, String city) {
        if (weather == null || city == null) return weather;
        int colonIndex = weather.indexOf(":");
        if (colonIndex > 0) {
            return city + weather.substring(colonIndex);
        }
        return city + ": " + weather;
    }
}
