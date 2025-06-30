package com.simplesalesman.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.simplesalesman.service.WeatherService;
/**
 * REST Controller for weather retrieval using GPS coordinates.
 *
 * This controller accepts latitude and longitude as query parameters and returns
 * a formatted weather string (e.g., "Vienna: ☀️ +24°C"). The response always uses
 * coordinates and optionally resolves the city name using reverse geocoding.
 *
 * Supported endpoint:
 * - GET /api/v1/weather?lat={latitude}&lon={longitude}
 *
 * Example:
 * - GET /api/v1/weather?lat=48.2082&lon=16.3738 → "Vienna: ☀️ +24°C"
 *
 * Input validation:
 * - Latitude must be in range [-90.0, 90.0]
 * - Longitude must be in range [-180.0, 180.0]
 * - Coordinates must be valid decimal numbers
 *
 * Error Handling:
 * - 400 Bad Request for invalid coordinates
 * - 503 Service Unavailable if weather service fails
 *
 * @version 0.0.9
 * @since 0.0.9
 */
@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
        logger.info("WeatherController initialized");
    }

    @GetMapping
    public ResponseEntity<String> getWeatherByCoordinates(@RequestParam String lat, @RequestParam String lon) {
        logger.info("GET /weather called for lat={}, lon={}", lat, lon);

        try {
            double parsedLat = Double.parseDouble(lat.replace(",", "."));
            double parsedLon = Double.parseDouble(lon.replace(",", "."));

            if (parsedLat < -90.0 || parsedLat > 90.0 || parsedLon < -180.0 || parsedLon > 180.0) {
                logger.warn("Invalid coordinates: lat={}, lon={}", parsedLat, parsedLon);
                return ResponseEntity.badRequest().body("Coordinates are out of valid range.");
            }

            String weatherInfo = weatherService.getWeatherForCoordinates(parsedLat, parsedLon);
            return ResponseEntity.ok(weatherInfo);

        } catch (NumberFormatException e) {
            logger.warn("Invalid coordinate format: lat='{}', lon='{}'", lat, lon);
            return ResponseEntity.badRequest().body("Coordinates must be decimal numbers.");
        } catch (Exception e) {
            logger.error("Error retrieving weather for lat={}, lon={}: {}", lat, lon, e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Weather service unavailable.");
        }
    }
}
