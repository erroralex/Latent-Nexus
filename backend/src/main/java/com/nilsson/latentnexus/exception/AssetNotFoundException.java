package com.nilsson.latentnexus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception indicating that an asset was not found.
 * <p>
 * This exception is typically thrown when an attempt to retrieve an asset by its ID
 * or other unique identifiers fails to locate a matching record in the system.
 * </p>
 * <p>
 * Annotated with `@ResponseStatus(HttpStatus.NOT_FOUND)`, this exception automatically
 * translates to an HTTP 404 Not Found status when thrown from a Spring MVC
 * controller, providing a clear and standardized error response to API clients.
 * </p>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AssetNotFoundException extends RuntimeException {
    public AssetNotFoundException(String message) {
        super(message);
    }
}
