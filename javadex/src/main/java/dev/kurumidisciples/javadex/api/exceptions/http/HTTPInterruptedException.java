package dev.kurumidisciples.javadex.api.exceptions.http;

import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;

/**
 * Represents an exception that is thrown when an HTTP request is interrupted.
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public class HTTPInterruptedException extends HTTPRequestException{

    /**
     * <p>Constructor for HTTPInterruptedException.</p>
     *
     * @param message a {@link java.lang.String} object
     */
    public HTTPInterruptedException(String message) {
        super(message);
    }

    /**
     * <p>Constructor for HTTPInterruptedException.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param cause a {@link java.lang.Throwable} object
     */
    public HTTPInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
