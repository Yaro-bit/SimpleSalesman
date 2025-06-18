package com.simplesalesman.exception;

import java.time.LocalDateTime;
/**
 * Global exception handler for the SimpleSalesman application.
 *
 * Captures and transforms exceptions thrown during REST controller execution
 * into structured and meaningful HTTP responses. Provides both logging and
 * user-friendly error messages to clients.
 *
 * Supported exception types:
 * - RuntimeException → HTTP 400 with detailed message
 * - Exception        → HTTP 500 with generic error text
 *
 * Each response follows the {@link ErrorResponse} format and includes
 * timestamp, status, error type, and a message.
 *
 * Extendable for application-specific exceptions (e.g. NotFound, Validation).
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.6
 */
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;

    public ErrorResponse(int status, String error, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
    }

    // Getter & Setter

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
