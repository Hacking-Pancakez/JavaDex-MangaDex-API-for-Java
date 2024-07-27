package dev.kurumidisciples.javadex.internal.http.handlers;

import java.io.IOException;
import java.net.SocketTimeoutException;

import dev.kurumidisciples.javadex.api.exceptions.NetworkErrorException;
import dev.kurumidisciples.javadex.api.exceptions.http.HTTPTimeoutException;
import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import dev.kurumidisciples.javadex.internal.utils.ErrorResponseChecker;
import okhttp3.Response;

public class ResponseHandler {

    public static String handleResponse(Response response, String url) throws HTTPRequestException {
        try {
            if (!response.isSuccessful()) {
                throw ErrorResponseChecker.retrieveCorrectHTTPException(response);
            }
            return response.body().string();
        } catch (SocketTimeoutException e) {
            throw new HTTPTimeoutException("Request timed out", e);
        } catch (IOException e) {
            if (ErrorResponseChecker.isNetworkError(e)) {
                throw new NetworkErrorException(e.getMessage(), e);
            } else {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }
}
