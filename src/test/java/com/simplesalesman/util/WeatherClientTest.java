package com.simplesalesman.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WeatherClientTest {

    private RestTemplate restTemplate;
    private WeatherClient weatherClient;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        weatherClient = new WeatherClient(restTemplate);
    }

    @Test
    @DisplayName("fetchWeather liefert gültigen Wetter-String für gegebene Stadt")
    void fetchWeather_returnsWeatherData() {
        // Arrange
        String city = "Linz";
        String mockResponse = "Linz: ☀️ +25°C";  // repräsentative Beispielantwort
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(mockResponse);

        // Act
        String result = weatherClient.fetchWeather(city);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains(city), "Antwort sollte den Stadtnamen enthalten");
    }

    @Test
    @DisplayName("fetchWeather gibt Fehler zurück bei Ausnahme")
    void fetchWeather_handlesException() {
        // Arrange
        String city = "UnknownCity";
        when(restTemplate.getForObject(anyString(), eq(String.class)))
            .thenThrow(new RuntimeException("API Fehler"));

        // Act
        String result = weatherClient.fetchWeather(city);

        // Assert
        assertTrue(result.contains("Fehler beim Abrufen der Wetterdaten"));
        assertTrue(result.contains("API Fehler"));
    }
}
