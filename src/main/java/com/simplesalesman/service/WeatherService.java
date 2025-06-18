package com.simplesalesman.service;

import com.simplesalesman.util.WeatherClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service class for retrieving weather data in the SimpleSalesman application.
 *
 * This service provides abstraction over weather-fetching logic: - Delegates
 * direct API calls to the WeatherClient utility - Resolves city names from GPS
 * coordinates via reverse geocoding (OpenStreetMap Nominatim)
 *
 * Use Cases: - Enables frontend to fetch weather via coordinates or city name -
 * Central place for future caching, error recovery, or fallback strategies
 *
 * Functional Overview: - getWeatherForRegion(String) → calls wttr.in with a
 * region string - getWeatherForCoordinates(lat, lon) → reverse-geocodes
 * coordinates, then queries wttr.in
 *
 * Dependencies: - WeatherClient for HTTP access to wttr.in - RestTemplate for
 * OpenStreetMap reverse geocoding
 * 
 * API Source: - https://wttr.in - https://nominatim.openstreetmap.org
 * 
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.3
 */
@Service
public class WeatherService {

	private final WeatherClient weatherClient;
	private final RestTemplate restTemplate; // Optional, falls du es brauchst

	public WeatherService(WeatherClient weatherClient, RestTemplate restTemplate) {
		this.weatherClient = weatherClient;
		this.restTemplate = restTemplate;
	}

	public String getWeatherForRegion(String regionName) {
		return weatherClient.fetchWeather(regionName);
	}

	public String getWeatherForCoordinates(double lat, double lon) {
		String city = resolveCityFromCoordinates(lat, lon);
		if (city == null) {
			return String.format("Stadt für Koordinaten %.4f, %.4f konnte nicht ermittelt werden.", lat, lon);
		}
		return weatherClient.fetchWeather(city);
	}

	private String resolveCityFromCoordinates(double lat, double lon) {
		try {
			String url = String.format(
					"https://nominatim.openstreetmap.org/reverse?lat=%.4f&lon=%.4f&format=json&zoom=10&addressdetails=1",
					lat, lon);

			var response = restTemplate.getForObject(url, java.util.Map.class);
			var address = (java.util.Map<?, ?>) response.get("address");

			if (address == null)
				return null;

			// Fallback-Kette: city → town → village → county
			for (String key : new String[] { "city", "town", "village", "county" }) {
				Object name = address.get(key);
				if (name instanceof String s && !s.isBlank())
					return s;
			}

			return null;

		} catch (Exception e) {
			return null;
		}
	}
}
