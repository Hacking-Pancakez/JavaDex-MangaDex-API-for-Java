package dev.kurumidisciples.javadex.api.exceptions.http;

import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import okhttp3.Response;

/**
 * This exception is thrown when an HTTP request is made and a client error occurs.
 * This typically corresponds to HTTP 4xx status codes.
 *
 * @since 0.1.1
 * @see {@link dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException}
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public class HTTPClientErrorException extends HTTPRequestException{
    
    private final Response response;

    /**
     * Constructs a new HTTPClientErrorException with the specified detail message and response.
     *
     * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
     * @param response the Response object associated with the exception (which is saved for later retrieval by the getResponse() method).
     */
    public HTTPClientErrorException(String message, Response response) {
        super(message);
        this.response = response;
    }

    /**
     * Returns the Response object associated with this exception.
     *
     * This method can be used to get more information about the HTTP request
     * that caused this exception, such as the request method, URL, headers, and body.
     *
     * @return the Response object associated with this exception.
     */
    public Response getResponse() {
        return response;
    }
}
