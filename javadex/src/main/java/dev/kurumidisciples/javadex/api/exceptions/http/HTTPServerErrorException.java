package dev.kurumidisciples.javadex.api.exceptions.http;

import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import okhttp3.Response;

/**
 * This exception is thrown when an HTTP request is made to a protected resource
 * that requires authentication, and either no authentication was provided or
 * the provided authentication was invalid.
 * @see {@link HTTPRequestException}
 */
public class HTTPServerErrorException extends HTTPRequestException{
        
        private final Response response;
    
        public HTTPServerErrorException(String message, Response response) {
            super(message);
            this.response = response;
        }
    
        public Response getResponse() {
            return response;
        }
}
