package dev.kurumidisciples.javadex.api.exceptions.http;

import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;

/**
 * Represents an exception that is thrown when an HTTP request is interrupted.
 */
public class HTTPInterruptedException extends HTTPRequestException{

    public HTTPInterruptedException(String message) {
        super(message);
    }

    public HTTPInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
