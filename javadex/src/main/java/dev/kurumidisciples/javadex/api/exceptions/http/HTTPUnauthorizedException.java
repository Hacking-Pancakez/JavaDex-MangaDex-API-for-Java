package dev.kurumidisciples.javadex.api.exceptions.http;

import org.jetbrains.annotations.Nullable;

import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import okhttp3.Response;

/**
 * This exception is thrown when an HTTP request is made to a protected resource
 * that requires authentication, and either no authentication was provided or
 * the provided authentication was invalid.
 *
 * This typically corresponds to an HTTP 401 Unauthorized status code.
 *
 * @see {@link dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException}
 * @since 0.1.1
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public class HTTPUnauthorizedException extends HTTPRequestException{

    private final Response response;

    /**
     * Constructs a new UnauthorizedException with the specified detail message and response.
     *
     * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
     * @param response the Response object associated with the exception (which is saved for later retrieval by the getResponse() method).
     */
    public HTTPUnauthorizedException(@Nullable String message, @Nullable Response response) {
        super(message);
        this.response = response;
    }

    /**
     * Constructs a new UnauthorizedException with the specified detail message, response, and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
     * @param response the Response object associated with the exception (which is saved for later retrieval by the getResponse() method).
     * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public HTTPUnauthorizedException(@Nullable String message, @Nullable Response response, @Nullable Throwable cause) {
        super(message, cause);
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
    @Nullable
    public Response getResponse() {
        return response;
    }

}
