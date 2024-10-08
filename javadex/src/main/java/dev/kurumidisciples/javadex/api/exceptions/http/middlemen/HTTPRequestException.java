package dev.kurumidisciples.javadex.api.exceptions.http.middlemen;

import okhttp3.Response;

/**
 * Represents an exception that is thrown when an HTTP request fails.
 * This is a middleman exception that is used to catch all HTTP request exceptions.
 * This exception is not meant to be thrown directly.
 *
 * @since 0.1.1
 * @see This might change in the future. As of now, this just a test to see if this is a good idea. :)
 * @see RuntimeException
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public abstract class HTTPRequestException extends RuntimeException {
    
    /**
     * Constructs a new HTTPRequestException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
     */
    public HTTPRequestException(String message) {
        super(message);
    }

    /**
     * <p>Constructor for HTTPRequestException.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param cause a {@link java.lang.Throwable} object
     */
    public HTTPRequestException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * SHOULD BE OVERRIDDEN BY SUBCLASSES
     *
     * @return a {@link okhttp3.Response} object
     */
    public Response getResponse() {
        // Provide implementation for the getResponse() method
        return null;
    }
}
