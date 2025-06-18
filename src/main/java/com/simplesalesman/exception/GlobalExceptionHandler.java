package com.simplesalesman.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
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
 * @since 0.0.5
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        logger.warn("Handled RuntimeException: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage() != null ? ex.getMessage() : "Ungültige Anfrage"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        logger.error("Unhandled exception caught", ex);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ein interner Fehler ist aufgetreten."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
