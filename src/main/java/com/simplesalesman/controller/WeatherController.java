package com.simplesalesman.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.simplesalesman.service.WeatherService;

/**
 * REST Controller for weather-related queries in the SimpleSalesman application.
 *
 * This controller provides endpoints to retrieve current weather data based on
 * either region names or GPS coordinates using external APIs. Enhanced to always
 * display location names (cities/villages) instead of raw coordinates.
 *
 * Use Cases:
 * - Enables sales representatives to check local weather before door-to-door visits
 * - Allows mobile browsers to fetch weather using built-in geolocation
 * - Displays village and hamlet names for better user experience
 * - Provides reliable weather data with location context
 *
 * API Endpoints:
 * - GET /api/v1/weather?region={name} → fetches weather by city, town, or village
 * - GET /api/v1/weather?lat=...&lon=... → fetches weather by GPS coordinates (with reverse geocoding to location names)
 *
 * Security:
 * - Public by default; can be protected via Spring Security configuration
 *
 * Dependencies:
 * - Delegates logic to WeatherService → WeatherClient → external APIs (wttr.in, nominatim)
 *
 * Examples:
 * - /api/v1/weather?region=Linz → returns "Linz: ☀️ +24°C"
 * - /api/v1/weather?region=Adlwang → returns "Adlwang: 🌧 +19°C"
 * - /api/v1/weather?lat=48.27&lon=14.229 → reverse-geocodes to "Adlwang", returns "Adlwang: ☀️ +21°C"
 *
 * Response Format:
 * - Always returns plain text in format: "LocationName: WeatherIcon Temperature"
 * - Location names are resolved from coordinates using OpenStreetMap Nominatim
 * - Supports cities, towns, villages, and hamlets
 *
 * @author SimpleSalesman Team
 * @version 0.0.8
 * @since 0.0.4
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
	@GetMapping(params = "region")
	public ResponseEntity<String> getWeatherByRegion(@RequestParam String region) {
		logger.info("GET /weather called for region '{}'", region);

		if (region == null || region.trim().isEmpty()) {
			logger.warn("Empty or missing region parameter");
			return ResponseEntity.badRequest().body("Region parameter is required.");
		}

		try {
			String weatherInfo = weatherService.getWeatherForRegion(region);
			logger.debug("Weather info for region '{}': {}", region, weatherInfo);
			return ResponseEntity.ok(weatherInfo);
		} catch (Exception ex) {
			logger.error("Error fetching weather for region '{}': {}", region, ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Weather service failed.");
		}
	}

	/**
	 * Retrieves weather information for given GPS coordinates (latitude &
	 * longitude).
	 *
	 * @param lat Latitude (e.g., 48.3)
	 * @param lon Longitude (e.g., 14.3)
	 * @return HTTP 200 OK with formatted weather info string
	 */
	@GetMapping(params = { "lat", "lon" })
	public ResponseEntity<String> getWeatherByCoordinates(@RequestParam double lat, @RequestParam double lon) {
		logger.info("GET /weather called for coordinates lat={}, lon={}", lat, lon);

		try {
			String weatherInfo = weatherService.getWeatherForCoordinates(lat, lon);
			logger.debug("Weather info for coordinates [{}, {}]: {}", lat, lon, weatherInfo);
			return ResponseEntity.ok(weatherInfo);
		} catch (Exception ex) {
			logger.error("Error fetching weather for coordinates [{}, {}]: {}", lat, lon, ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Weather service failed.");
		}
	}
}
