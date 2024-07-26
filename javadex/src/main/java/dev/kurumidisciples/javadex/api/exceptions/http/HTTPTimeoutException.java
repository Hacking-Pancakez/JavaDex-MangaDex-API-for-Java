package dev.kurumidisciples.javadex.api.exceptions.http;

import org.jetbrains.annotations.Nullable;

import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import okhttp3.Response;

/**
 * Is usaully thrown when the HTTP request times out.
 *
 * @see {@link dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException}
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public class HTTPTimeoutException extends HTTPRequestException{

    private final Response response;

    /**
     * Constructs a new HTTPTimeoutException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
     * @param response the Response object associated with the exception (which is saved for later retrieval by the getResponse() method).
     */
    public HTTPTimeoutException(@Nullable String message, @Nullable Response response) {
        super(message);
        this.response = response;
    }

    /**
     * Constructs a new HTTPTimeoutException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
     */
    public HTTPTimeoutException(@Nullable String message) {
        super(message);
        this.response = null;
    }

    /**
     * Constructs a new HTTPTimeoutException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
     * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public HTTPTimeoutException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
        this.response = null;
    }

    /**
     * Returns the Response object associated with this exception.
     *
     * @return a {@link okhttp3.Response} object
     */
    @Nullable
    public Response getResponse() {
        return response;
    }
}
