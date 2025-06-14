package com.simplesalesman.service;

import com.simplesalesman.util.WeatherClient;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private final WeatherClient weatherClient;

    public WeatherService(WeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    public String getWeatherForRegion(String regionName) {
        return weatherClient.fetchWeather(regionName);
    }
}
