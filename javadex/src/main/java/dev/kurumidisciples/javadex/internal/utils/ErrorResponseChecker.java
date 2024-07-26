package dev.kurumidisciples.javadex.internal.utils;

import java.io.IOException;
import java.net.SocketTimeoutException;

import dev.kurumidisciples.javadex.api.exceptions.NetworkErrorException;
import dev.kurumidisciples.javadex.api.exceptions.http.HTTPClientErrorException;
import dev.kurumidisciples.javadex.api.exceptions.http.HTTPServerErrorException;
import dev.kurumidisciples.javadex.api.exceptions.http.HTTPTimeoutException;
import dev.kurumidisciples.javadex.api.exceptions.http.HTTPUnauthorizedException;
import dev.kurumidisciples.javadex.api.exceptions.http.HTTPUnexpectedStatusCodeException;
import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import okhttp3.Response;

/**
 * Checks if the response is an error response.
 */
public class ErrorResponseChecker {
    
    /**
     * Retrieves the correct exception based on the response code.
     *
     * @param response the response to check.
     * @return the exception corresponding to the response code.
     * @since 0.1.1
     */
    public static HTTPRequestException retrieveCorrectHTTPException(Response response) {
        int code = response.code();
        if (code == 401) {
            return new HTTPUnauthorizedException("Unauthorized request", response);
        } else if (code == 408) {
            return new HTTPTimeoutException("Request timed out", response);
        } else if (code >= 400 && code < 500) {
            return new HTTPClientErrorException("Client error: " + code, response);
        } else if (code >= 500) {
            return new HTTPServerErrorException("Server error: " + code, response);
        } else {
            return new HTTPUnexpectedStatusCodeException("Unexpected status code: " + code, response);
        }
    }

    public static Exception retrieveCorrectIOException(IOException e) {
        if (isNetworkError(e)) {
            return new NetworkErrorException("Network error", e);
        } else if (e instanceof SocketTimeoutException) {
            return new HTTPTimeoutException("Request timed out", e);
        } else {
            return new RuntimeException("Unexpected IOException", e);
        }
    }

    /**
     * Checks if the response is a network error.
     * @param e
     * @return true if the response is a network error, false otherwise.
     */
    public static boolean isNetworkError(IOException e){
        if (e instanceof java.net.SocketTimeoutException || 
        e instanceof java.net.NoRouteToHostException || 
        e instanceof java.net.UnknownHostException || 
        e instanceof java.net.ConnectException || e instanceof okhttp3.internal.http2.ConnectionShutdownException || e instanceof java.net.SocketException) {
            return true;
        }
        return false;
    }
    
    /**
     * Checks if the response is an unauthorized response.
     * @param response
     * @return
     */
    @Deprecated
    public static boolean isUnauthorizedResponse(Response response) {
        return response.code() >= 400;
    }
}
