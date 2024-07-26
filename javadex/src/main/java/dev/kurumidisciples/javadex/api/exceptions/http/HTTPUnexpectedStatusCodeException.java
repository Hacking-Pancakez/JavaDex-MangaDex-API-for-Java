package dev.kurumidisciples.javadex.api.exceptions.http;

import org.jetbrains.annotations.Nullable;

import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import okhttp3.Response;

/**
 * Is typically thrown when no other exception meets the requirements for the status code.
 * @See {@link HTTPRequestException}
 */
public class HTTPUnexpectedStatusCodeException extends HTTPRequestException{
    
    private final Response response;

    /**
     * Constructs a new HTTPUnexpectedStatusCodeException with the specified detail message.
     * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
     * @param response the Response object associated with the exception (which is saved for later retrieval by the getResponse() method).
     */
    public HTTPUnexpectedStatusCodeException(@Nullable String message, @Nullable Response response) {
        super(message);
        this.response = response;
    }
    /**
     * Returns the Response object associated with this exception.
     * @return the response
     */
    @Override
    public Response getResponse() {
        return response;
    }
}
