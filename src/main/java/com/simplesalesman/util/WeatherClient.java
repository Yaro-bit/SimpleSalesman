package com.simplesalesman.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * HTTP utility for fetching weather information from external services.
 *
 * This client encapsulates calls to the public wttr.in weather service.
 * Supports queries by: - Region or city name - GPS coordinates (lat/lon)
 *
 * Format: - wttr.in/{query}?format=3 → returns plain-text weather summary
 *
 * Example Results: - "Linz: ☀️ +24°C" - "48.3,14.3: 🌧 +19°C"
 *
 * Use Cases: - Backend service call from WeatherService to retrieve formatted
 * weather - Transparent fallback when city not provided (coordinates supported
 * directly)
 *
 * Dependencies: - Requires working RestTemplate (injected via constructor)
 *
 * Error Handling: - Graceful fallback message returned on exception
 *
 * API Source: - https://wttr.in (no key required)
 * 
 * Author: SimpleSalesman Team
 * 
 * @version 0.0.6
 * @since 0.0.3
 */

@Component
public class WeatherClient {

	private static final Logger logger = LoggerFactory.getLogger(WeatherClient.class);
	private static final String BASE_URL = "https://wttr.in/%s?format=3"; // Kostenlose Wetterdaten ohne API-Key

	private final RestTemplate restTemplate;

	public WeatherClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	/**
	 * Ruft Wetterinformationen für eine Stadt/Region ab (z. B. "Linz", "Graz").
	 */
	public String fetchWeather(String city) {
		String url = String.format(BASE_URL, city);
		try {
			logger.info("Wetterdaten werden abgerufen für Stadt '{}'", city);
			return restTemplate.getForObject(url, String.class);
		} catch (Exception e) {
			logger.error("Fehler beim Abrufen der Wetterdaten für '{}': {}", city, e.getMessage());
			return "Fehler beim Abrufen der Wetterdaten für " + city;
		}
	}

	/**
	 * Ruft Wetterinformationen für GPS-Koordinaten ab (z. B. vom Browser).
	 */
	public String fetchWeatherByLatLon(double lat, double lon) {
		String coordinates = String.format("%.4f,%.4f", lat, lon);
		String url = String.format(BASE_URL, coordinates);
		try {
			logger.info("Wetterdaten werden abgerufen für Koordinaten: {}", coordinates);
			return restTemplate.getForObject(url, String.class);
		} catch (Exception e) {
			logger.error("Fehler beim Abrufen der Wetterdaten für Koordinaten {}: {}", coordinates, e.getMessage());
			return "Fehler beim Abrufen der Wetterdaten für Koordinaten " + coordinates;
		}
	}
}
