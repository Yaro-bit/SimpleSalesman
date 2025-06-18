package com.simplesalesman.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Base class for all 404 Not Found exceptions in the SimpleSalesman application.
 *
 * This abstract class simplifies creation of specific not-found exceptions (e.g. for Project, Note, Address)
 * and ensures that all such exceptions automatically return HTTP 404 in REST APIs.
 *
 * Usage:
 * Extend this class and call super(message) in the subclass constructor.
 *
 * Example subclass:
 * <pre>
 *     public class ProjectNotFoundException extends AbstractNotFoundException {
 *         public ProjectNotFoundException(String message) {
 *             super(message);
 *         }
 *     }
 * </pre>
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.6
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public abstract class AbstractNotFoundException extends RuntimeException {

    /**
     * Constructs a new AbstractNotFoundException with the specified message.
     *
     * @param message descriptive error message for the missing entity
     */
    protected AbstractNotFoundException(String message) {
        super(message);
    }
}
