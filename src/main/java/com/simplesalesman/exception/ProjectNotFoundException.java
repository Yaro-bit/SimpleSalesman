package com.simplesalesman.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a project with the specified ID is not found in the database.
 *
 * This exception is typically thrown by the service layer when a requested
 * project does not exist. It results in an HTTP 404 Not Found response
 * when used within a Spring REST controller.
 *
 * Example usage:
 * <pre>
 *     throw new ProjectNotFoundException("Project with ID 42 not found.");
 * </pre>
 *
 * Handled automatically by Spring Boot via {@link ResponseStatus}.
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.5
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProjectNotFoundException extends RuntimeException {

    /**
     * Constructs a new ProjectNotFoundException with the specified detail message.
     *
     * @param message a descriptive message explaining why the project was not found
     */
    public ProjectNotFoundException(String message) {
        super(message);
    }
}
