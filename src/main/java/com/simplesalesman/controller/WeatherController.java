package com.simplesalesman.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.simplesalesman.service.WeatherService;

/**
 * REST Controller for weather-related queries in the SimpleSalesman application.
 *
 * This controller provides an endpoint to retrieve current weather data
 * for a specified region using an external API (e.g. wttr.in).
 *
 * Use Case:
 * - Enables sales representatives to check local weather before planning door-to-door routes.
 *
 * API Endpoint:
 * - GET /api/v1/weather?region={name}
 *
 * Security:
 * - Typically public, but can be secured via global config if needed.
 *
 * Dependencies:
 * - Delegates the actual HTTP request to WeatherService → WeatherClient (RestTemplate-based).
 *
 * Example:
 * - /api/v1/weather?region=Linz → returns "Linz: ☀️ +24°C"
 *
 * @author SimpleSalesman Team
 * @version 0.0.5
 * @since 0.0.3
 */
@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    private final WeatherService weatherService;

    /**
     * Constructor for WeatherController.
     *
     * @param weatherService Service component for weather data retrieval
     */
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
        logger.info("WeatherController initialized");
    }

    /**
     * Retrieves weather information for a given region.
     *
     * @param region the name of the target region (e.g., "Vienna", "Graz")
     * @return HTTP 200 OK with formatted weather info string
     */
    @GetMapping
    public ResponseEntity<String> getWeather(@RequestParam String region) {
        logger.info("GET /weather called for region '{}'", region);

        String weatherInfo = weatherService.getWeatherForRegion(region);

        logger.debug("Weather info for region '{}': {}", region, weatherInfo);
        return ResponseEntity.ok(weatherInfo);
    }
}
