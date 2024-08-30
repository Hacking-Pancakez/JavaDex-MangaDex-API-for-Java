package dev.kurumidisciples.javadex.api.exceptions.http;

import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;

/**
 * This exception is thrown when an HTTP request is made and the requested resource is not found.
 * This typically corresponds to HTTP 404 status codes.
 *
 * @since 0.1.5.0.BETA.2
 * @see {@link HTTPRequestException}
 * @author Hacking Pancakez
 */
public class HTTPNotFoundException extends HTTPRequestException{
    
    public HTTPNotFoundException(String message) {
        super(message);
    }

    public HTTPNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
