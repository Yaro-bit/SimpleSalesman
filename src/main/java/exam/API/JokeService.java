package exam.API;

import exam.API.JokeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service component for handling joke requests from JokeAPI.
 *
 * This service delegates all joke-fetching logic to the JokeClient.
 * It provides a clean interface for retrieving random dark jokes.
 *
 * Design:
 * - Fetches random jokes from the Dark category
 * - Returns formatted joke strings ready for display
 * - Handles both single-line and setup/delivery jokes
 *
 * Usage:
 * - Called by JokeController to serve frontend or API consumers
 *
 * Dependencies:
 * - JokeClient: handles HTTP communication with JokeAPI and JSON parsing
 * 
 * @author Yaroslav Volokhodko
 * @version exam
 * @since 0.0.9
 */
@Service
public class JokeService {

    private static final Logger logger = LoggerFactory.getLogger(JokeService.class);
    private final JokeClient jokeClient;

    public JokeService(JokeClient jokeClient) {
        this.jokeClient = jokeClient;
    }

    public String getRandomJoke() {
        logger.info("Fetching random joke from JokeAPI");
        return jokeClient.fetchRandomJoke();
    }
}