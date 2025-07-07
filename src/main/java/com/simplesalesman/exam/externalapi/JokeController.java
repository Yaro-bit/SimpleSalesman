package com.simplesalesman.exam.externalapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.simplesalesman.exam.externalapi.JokeService;

/**
 * REST Controller for joke retrieval from JokeAPI.
 *
 * This controller provides endpoints to fetch random dark jokes from the JokeAPI.
 * The response contains formatted joke strings that can handle both single-line
 * and setup/delivery joke formats.
 *
 * Supported endpoints:
 * - GET /api/v1/jokes → returns a random dark joke
 *
 * Example responses:
 * - Single joke: "Why did the programmer quit his job? Because he didn't get arrays."
 * - Two-part joke: "Setup text\n\nDelivery text"
 *
 * Error Handling:
 * - 503 Service Unavailable if joke service fails
 * - Returns error message in response body for debugging
 *
 * @author Yaroslav Volokhodko
 * @version exam
 * @since 0.0.9
 */
@RestController
@RequestMapping("/api/v1/jokes")
@CrossOrigin(origins = "*") // Enable CORS for frontend
public class JokeController {

    private static final Logger logger = LoggerFactory.getLogger(JokeController.class);
    private final JokeService jokeService;

    public JokeController(JokeService jokeService) {
        this.jokeService = jokeService;
        logger.info("JokeController initialized");
    }

    @GetMapping
    public ResponseEntity<String> getRandomJoke() {
        logger.info("GET /jokes called for random joke");

        try {
            String joke = jokeService.getRandomJoke();
            
            if ("Joke service unavailable".equals(joke) || "No joke available".equals(joke)) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(joke);
            }
            
            return ResponseEntity.ok(joke);

        } catch (Exception e) {
            logger.error("Error retrieving joke: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Joke service unavailable");
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Joke service is running");
    }
}