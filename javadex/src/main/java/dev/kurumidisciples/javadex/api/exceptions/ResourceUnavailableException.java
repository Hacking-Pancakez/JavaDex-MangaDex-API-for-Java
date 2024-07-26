package dev.kurumidisciples.javadex.api.exceptions;


/**
 * This exception is thrown when a resource is unavailable.
 */
public class ResourceUnavailableException extends RuntimeException {

    public ResourceUnavailableException() {
        super();
    }

    public ResourceUnavailableException(String message) {
        super(message);
    }

    public ResourceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceUnavailableException(Throwable cause) {
        super(cause);
    }
}