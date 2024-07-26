package dev.kurumidisciples.javadex.api.exceptions;

/**
 * This exception is thrown when a network error occurs. Encapsulates all exceptions that can occur during network operations.
 */
public class NetworkErrorException extends RuntimeException {

    public NetworkErrorException() {
        super();
    }

    public NetworkErrorException(String message) {
        super(message);
    }

    public NetworkErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkErrorException(Throwable cause) {
        super(cause);
    }
}