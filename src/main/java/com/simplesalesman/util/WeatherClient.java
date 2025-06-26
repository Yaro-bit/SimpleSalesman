package com.simplesalesman.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
/**
 * HTTP utility for fetching weather information from external services.
 *
 * This client encapsulates calls to the public wttr.in weather service.
 * Enhanced with proper URL encoding and headers for reliable API communication.
 * 
 * Supports queries by:
 * - Region or city name (including villages and hamlets)
 * - GPS coordinates (lat/lon)
 *
 * Format:
 * - wttr.in/{query}?format=3 → returns plain-text weather summary
 *
 * Example Results:
 * - "Linz: ☀️ +24°C"
 * - "Adlwang: 🌧 +19°C"
 * - "48.3,14.3: ☁️ +21°C" (fallback when location not recognized)
 *
 * Use Cases:
 * - Backend service call from WeatherService to retrieve formatted weather
 * - Transparent fallback when city not provided (coordinates supported directly)
 * - Reliable weather data retrieval with proper error handling
 *
 * Dependencies:
 * - Requires working RestTemplate (injected via constructor)
 *
 * Error Handling:
 * - Graceful fallback message returned on exception
 * - Proper User-Agent headers for API compliance
 * - URL encoding for location names with special characters
 *
 * API Source:
 * - https://wttr.in (no key required)
 *
 * @author SimpleSalesman Team
 * @version 0.0.8
 * @since 0.0.3
 */

@Component
public class WeatherClient {

    private static final Logger logger = LoggerFactory.getLogger(WeatherClient.class);
    private static final String BASE_URL = "https://wttr.in/%s?format=3";

    private final RestTemplate restTemplate;

    public WeatherClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Fetches weather for a city/village name.
     */
    public String fetchWeather(String location) {
        try {
            // URL encode the location for better reliability
            String encodedLocation = URLEncoder.encode(location.trim(), StandardCharsets.UTF_8);
            String url = String.format(BASE_URL, encodedLocation);
            
            // Add User-Agent header
            HttpHeaders headers = new HttpHeaders();
            headers.add("User-Agent", "SimpleSalesman/1.0");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            logger.info("Fetching weather for location: '{}'", location);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            
            String result = response.getBody();
            logger.debug("Weather response: {}", result);
            
            return result;
            
        } catch (Exception e) {
            logger.error("Error fetching weather for '{}': {}", location, e.getMessage());
            return "Weather service unavailable for " + location;
        }
    }

    /**
     * Fetches weather for GPS coordinates (fallback method).
     */
    public String fetchWeatherByLatLon(double lat, double lon) {
        String coordinates = String.format("%.4f,%.4f", lat, lon);
        return fetchWeather(coordinates);
    }
}
