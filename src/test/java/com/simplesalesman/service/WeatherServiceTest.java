package com.simplesalesman.service;

import com.simplesalesman.util.WeatherClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class WeatherServiceTest {

    private WeatherClient weatherClient;
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        // Mock für WeatherClient
        weatherClient = mock(WeatherClient.class);
        weatherService = new WeatherService(weatherClient);
    }

    @Test
    @DisplayName("getWeatherForRegion gibt Wetter-String für Linz zurück")
    void getWeatherForRegion_returnsWeatherStringForLinz() {
        // Arrange
        String regionName = "Linz";
        String weatherInfo = "Bewölkt, 19°C";
        when(weatherClient.fetchWeather(regionName)).thenReturn(weatherInfo);

        // Act
        String result = weatherService.getWeatherForRegion(regionName);

        // Assert
        assertEquals(weatherInfo, result);
        verify(weatherClient, times(1)).fetchWeather(regionName);
    }

    @Test
    @DisplayName("getWeatherForRegion gibt null zurück, wenn Client null liefert")
    void getWeatherForRegion_returnsNull_whenClientReturnsNull() {
        String regionName = "Unbekannt";
        when(weatherClient.fetchWeather(regionName)).thenReturn(null);

        String result = weatherService.getWeatherForRegion(regionName);

        assertNull(result);
        verify(weatherClient, times(1)).fetchWeather(regionName);
    }
}
