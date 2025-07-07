package com.simplesalesman.exam.externalapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * HTTP utility class for retrieving jokes from JokeAPI.
 * 
 * ALTERNATIVE VERSION: Uses Spring Boot's auto-configured beans
 * - RestTemplate: From com.simplesalesman.config.RestTemplateConfig
 * - ObjectMapper: Spring Boot auto-configuration
 * 
 *
 * @author Yaroslav Volokhodko
 * @version exam
 * @since 0.0.9
 */
@Component
public class JokeClient {

    private static final Logger logger = LoggerFactory.getLogger(JokeClient.class);
    private static final String JOKE_URL = "https://sv443.net/jokeapi/v2/joke/Dark";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // Spring injiziert automatisch die konfigurierten Beans
    public JokeClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        logger.info("JokeClient initialized with auto-configured beans");
    }

    public String fetchRandomJoke() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("User-Agent", "SimpleSalesman/1.0");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(JOKE_URL, HttpMethod.GET, entity, String.class);
            logger.debug("Joke API response: {}", response.getBody());

            return parseJokeResponse(response.getBody());
        } catch (Exception e) {
            logger.error("Failed to fetch joke: {}", e.getMessage());
            return "Joke service unavailable";
        }
    }

    private String parseJokeResponse(String jsonResponse) {
        try {
            JsonNode json = objectMapper.readTree(jsonResponse);
            
            if (json.has("error") && json.get("error").asBoolean()) {
                logger.warn("JokeAPI returned error: {}", json.get("message").asText());
                return "No joke available";
            }

            String type = json.get("type").asText();
            
            if ("single".equals(type)) {
                return json.get("joke").asText();
            } else if ("twopart".equals(type)) {
                String setup = json.get("setup").asText();
                String delivery = json.get("delivery").asText();
                return setup + "\n\n" + delivery;
            }
            
            return "Unknown joke format";
        } catch (Exception e) {
            logger.error("Failed to parse joke response: {}", e.getMessage());
            return "Failed to parse joke";
        }
    }
}