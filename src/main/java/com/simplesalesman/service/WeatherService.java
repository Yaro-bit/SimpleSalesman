package com.simplesalesman.service;

import com.simplesalesman.util.WeatherClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
/**
 * Service component for handling weather requests using GPS coordinates.
 *
 * This service delegates all weather-fetching logic to the WeatherClient.
 * It encapsulates only coordinate validation and logging.
 *
 * Design:
 * - Uses coordinates as the only input (lat/lon)
 * - Returns a single formatted weather string, e.g., "Vienna: ☀️ +24°C"
 * - Combines weather and city name into one result
 *
 * Usage:
 * - Called by WeatherController to serve frontend or API consumers
 *
 * Dependencies:
 * - WeatherClient: handles both weather fetching and optional city resolution
 *
 * @version 0.0.9
 * @since 0.0.9
 */

@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private final WeatherClient weatherClient;

    public WeatherService(WeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    public String getWeatherForCoordinates(double lat, double lon) {
        logger.info("Fetching weather for coordinates: {}, {}", lat, lon);
        return weatherClient.fetchWeatherByLatLon(lat, lon);
    }
}
