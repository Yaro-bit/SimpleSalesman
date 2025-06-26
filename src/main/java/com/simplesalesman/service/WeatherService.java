package com.simplesalesman.service;

import com.simplesalesman.util.WeatherClient;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * Service class for retrieving weather data in the SimpleSalesman application.
 *
 * This service provides abstraction over weather-fetching logic:
 * - Delegates direct API calls to the WeatherClient utility
 * - Resolves city/village names from GPS coordinates via reverse geocoding (OpenStreetMap Nominatim)
 * - Ensures location names are always displayed instead of raw coordinates
 *
 * Use Cases:
 * - Enables frontend to fetch weather via coordinates or city name
 * - Central place for future caching, error recovery, or fallback strategies
 * - Displays village/hamlet names for better user experience
 *
 * Functional Overview:
 * - getWeatherForRegion(String) → calls wttr.in with a region string
 * - getWeatherForCoordinates(lat, lon) → reverse-geocodes coordinates, then queries wttr.in
 *
 * Dependencies:
 * - WeatherClient for HTTP access to wttr.in
 * - RestTemplate for OpenStreetMap reverse geocoding
 *
 * API Sources:
 * - https://wttr.in
 * - https://nominatim.openstreetmap.org
 *
 * @author SimpleSalesman Team
 * @version 0.0.8
 * @since 0.0.3
 */
@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    
    private final WeatherClient weatherClient;
    private final RestTemplate restTemplate;

    public WeatherService(WeatherClient weatherClient, RestTemplate restTemplate) {
        this.weatherClient = weatherClient;
        this.restTemplate = restTemplate;
    }

    public String getWeatherForRegion(String regionName) {
        return weatherClient.fetchWeather(regionName);
    }

    /**
     * Gets weather for coordinates and ALWAYS returns location name instead of coordinates.
     */
    public String getWeatherForCoordinates(double lat, double lon) {
        // Step 1: Resolve location name from coordinates
        String locationName = resolveCityFromCoordinates(lat, lon);
        
        if (locationName == null) {
            logger.warn("Could not resolve location for coordinates {}, {}", lat, lon);
            // Fallback: use coordinates but return error message
            return String.format("Location for coordinates %.4f, %.4f could not be determined.", lat, lon);
        }
        
        logger.info("Resolved location: '{}' from coordinates [{}, {}]", locationName, lat, lon);
        
        // Step 2: Get weather using the resolved location name
        String weatherResult = weatherClient.fetchWeather(locationName);
        
        // Step 3: Ensure the response starts with the location name
        return ensureLocationInResponse(weatherResult, locationName);
    }

    /**
     * Ensures the weather response starts with the actual location name.
     */
    private String ensureLocationInResponse(String weatherResult, String locationName) {
        if (weatherResult == null) {
            return locationName + ": Weather data unavailable";
        }
        
        // If the response already starts with our location name, return as-is
        if (weatherResult.toLowerCase().startsWith(locationName.toLowerCase())) {
            return weatherResult;
        }
        
        // If response contains different location or coordinates, replace with our location
        if (weatherResult.contains(":")) {
            // Replace everything before the first colon with our location name
            String weatherPart = weatherResult.substring(weatherResult.indexOf(":") + 1).trim();
            return locationName + ": " + weatherPart;
        }
        
        // If no colon, prepend our location name
        return locationName + ": " + weatherResult;
    }


    private String resolveCityFromCoordinates(double lat, double lon) {
        try {
            String url = String.format(
                "https://nominatim.openstreetmap.org/reverse?lat=%.6f&lon=%.6f&format=json&zoom=10&addressdetails=1",
                lat, lon);
            
            // Add User-Agent header (required by Nominatim)
            HttpHeaders headers = new HttpHeaders();
            headers.add("User-Agent", "SimpleSalesman/1.0");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            Map<?, ?> responseBody = response.getBody();
            
            if (responseBody == null) {
                return null;
            }
            
            @SuppressWarnings("unchecked")
            Map<String, Object> address = (Map<String, Object>) responseBody.get("address");
            
            if (address == null) {
                return null;
            }

            // Enhanced fallback chain: village → hamlet → town → city → municipality → county
            for (String key : new String[] { "village", "hamlet", "town", "city", "municipality", "county" }) {
                Object name = address.get(key);
                if (name instanceof String locationName && !locationName.isBlank()) {
                    logger.info("Found location using '{}': {}", key, locationName);
                    return locationName.trim();
                }
            }

            return null;

        } catch (Exception e) {
            logger.error("Error resolving location from coordinates: {}", e.getMessage());
            return null;
        }
    }
}