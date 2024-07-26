package dev.kurumidisciples.javadex.api.exceptions.http;

import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import okhttp3.Response;

/**
 * This exception is thrown when an HTTP request is made to a protected resource
 * that requires authentication, and either no authentication was provided or
 * the provided authentication was invalid.
 *
 * @see {@link dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException}
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public class HTTPServerErrorException extends HTTPRequestException{
        
        private final Response response;
    
        /**
         * <p>Constructor for HTTPServerErrorException.</p>
         *
         * @param message a {@link java.lang.String} object
         * @param response a {@link okhttp3.Response} object
         */
        public HTTPServerErrorException(String message, Response response) {
            super(message);
            this.response = response;
        }
    
        /**
         * <p>Getter for the field <code>response</code>.</p>
         *
         * @return a {@link okhttp3.Response} object
         */
        public Response getResponse() {
            return response;
        }
}
