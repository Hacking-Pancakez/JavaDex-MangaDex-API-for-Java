package dev.kurumidisciples.javadex.internal.http;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.kurumidisciples.javadex.api.exceptions.NetworkErrorException;
import dev.kurumidisciples.javadex.api.exceptions.http.HTTPInterruptedException;
import dev.kurumidisciples.javadex.api.exceptions.http.HTTPTimeoutException;
import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import dev.kurumidisciples.javadex.internal.config.HTTPConfig;
import dev.kurumidisciples.javadex.internal.utils.ErrorResponseChecker;
import dev.kurumidisciples.javadex.internal.http.ratelimit.RateLimiter;
import dev.kurumidisciples.javadex.internal.http.handlers.ResponseHandler;
import dev.kurumidisciples.javadex.internal.http.ratelimit.EndpointLimits;
import okhttp3.Request;
import okhttp3.Response;

public class HTTPRequest {

    private static final Logger logger = LogManager.getLogger(HTTPRequest.class);
    private static final Pattern UUID_PATTERN = Pattern.compile("[0-9a-fA-F-]{36}");
    private static final String BASE_URL = "https://api.mangadex.org";

    public static String post(String url, String json) throws HTTPRequestException {
        return postWithBearer(url, json, Optional.empty());
    }

    public static String post(String url, Optional<String> bearer) throws HTTPRequestException {
        return postWithBearer(url, "", bearer);
    }

    public static String post(String url, String data, Optional<String> bearer) throws HTTPRequestException {
        return postWithBearer(url, data, bearer);
    }

    private static String postWithBearer(String url, String data, Optional<String> bearer) throws HTTPRequestException {
        EndpointLimits limit = getEndpointLimit(url, EndpointLimits.Method.POST);
        try {
            RateLimiter.acquire(limit);
            Request request = RequestBuilder.buildPostRequest(url, data, bearer);
            return executeRequest(request, url);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HTTPInterruptedException(e.getMessage(), e);
        } finally {
            RateLimiter.release(limit);
        }
    }

    public static String delete(String url, Optional<String> bearer) throws HTTPRequestException {
        EndpointLimits limit = getEndpointLimit(url, EndpointLimits.Method.DELETE);
        try {
            RateLimiter.acquire(limit);
            Request request = RequestBuilder.buildDeleteRequest(url, bearer);
            return executeRequest(request, url);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HTTPInterruptedException(e.getMessage(), e);
        } finally {
            RateLimiter.release(limit);
        }
    }

    public static String get(String url, String json) throws HTTPRequestException {
        return getWithBearer(url, Optional.of(json), Optional.empty());
    }

    public static String get(String url) throws HTTPRequestException {
        return getWithBearer(url, Optional.empty(), Optional.empty());
    }

    public static String get(String url, Optional<String> bearer) throws HTTPRequestException {
        return getWithBearer(url, Optional.empty(), bearer);
    }

    private static String getWithBearer(String url, Optional<String> json, Optional<String> bearer) throws HTTPRequestException {
        EndpointLimits limit = getEndpointLimit(url, EndpointLimits.Method.GET);
        try {
            RateLimiter.acquire(limit);
            Request request = RequestBuilder.buildGetRequest(url, bearer);
            return executeRequest(request, url);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HTTPInterruptedException(e.getMessage(), e);
        } finally {
            RateLimiter.release(limit);
        }
    }

    public static Response postResponse(String url, String json, Optional<String> bearer) throws HTTPRequestException {
        return postResponseWithBearer(url, json, bearer);
    }

    private static Response postResponseWithBearer(String url, String data, Optional<String> bearer) throws HTTPRequestException {
        EndpointLimits limit = getEndpointLimit(url, EndpointLimits.Method.POST);
        try {
            RateLimiter.acquire(limit);
            Request request = RequestBuilder.buildPostRequest(url, data, bearer);
            return executeRequestForResponse(request, url);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HTTPInterruptedException(e.getMessage(), e);
        } finally {
            RateLimiter.release(limit);
        }
    }

    public static Response getResponse(String url, Optional<String> bearer) throws HTTPRequestException {
        EndpointLimits limit = getEndpointLimit(url, EndpointLimits.Method.GET);
        try {
            RateLimiter.acquire(limit);
            Request request = RequestBuilder.buildGetRequest(url, bearer);
            return executeRequestForResponse(request, url);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HTTPInterruptedException(e.getMessage(), e);
        } finally {
            RateLimiter.release(limit);
        }
    }

    public static String postForm(String url, Map<String, String> formData) throws HTTPRequestException {
        return postFormWithBearer(url, formData, Optional.empty());
    }

    public static String postForm(String url, Map<String, String> formData, Optional<String> bearer) throws HTTPRequestException {
        return postFormWithBearer(url, formData, bearer);
    }

    private static String postFormWithBearer(String url, Map<String, String> formData, Optional<String> bearer) throws HTTPRequestException {
        EndpointLimits limit = getEndpointLimit(url, EndpointLimits.Method.POST);
        try {
            RateLimiter.acquire(limit);
            Request request = RequestBuilder.buildPostFormRequest(url, formData, bearer);
            return executeRequest(request, url);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HTTPInterruptedException(e.getMessage(), e);
        } finally {
            RateLimiter.release(limit);
        }
    }

    public static Response postFormResponse(String url, Map<String, String> formData) throws HTTPRequestException {
        return postFormResponseWithBearer(url, formData, Optional.empty());
    }

    public static Response postFormResponse(String url, Map<String, String> formData, Optional<String> bearer) throws HTTPRequestException {
        return postFormResponseWithBearer(url, formData, bearer);
    }

    private static Response postFormResponseWithBearer(String url, Map<String, String> formData, Optional<String> bearer) throws HTTPRequestException {
        EndpointLimits limit = getEndpointLimit(url, EndpointLimits.Method.POST);
        try {
            RateLimiter.acquire(limit);
            Request request = RequestBuilder.buildPostFormRequest(url, formData, bearer);
            return executeRequestForResponse(request, url);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HTTPInterruptedException(e.getMessage(), e);
        } finally {
            RateLimiter.release(limit);
        }
    }

    private static String executeRequest(Request request, String url) throws HTTPRequestException {
        try (Response response = HTTPConfig.HTTP_CLIENT.newCall(request).execute()) {
            return ResponseHandler.handleResponse(response, url);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static Response executeRequestForResponse(Request request, String url) throws HTTPRequestException {
        try (Response response = HTTPConfig.HTTP_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw ErrorResponseChecker.retrieveCorrectHTTPException(response);
            }
            return response;
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

    private static EndpointLimits getEndpointLimit(String url, EndpointLimits.Method method) {
        // Remove base URL and UUID from URL to match against endpoint patterns
        String sanitizedUrl = removeBaseUrlAndUUIDs(url);

        // Replace UUID placeholders with actual pattern
        for (EndpointLimits limit : EndpointLimits.values()) {
            String endpointPattern = limit.getEndpoint().replace("{id}", UUID_PATTERN.pattern());
            if (sanitizedUrl.matches(endpointPattern) && limit.getMethod() == method) {
                return limit;
            }
        }

        // Default rate limit if no specific limit is found (5 requests per second)
        return EndpointLimits.DEFAULT;
    }

    private static String removeBaseUrlAndUUIDs(String url) {
        // Remove base URL
        String sanitizedUrl = url.replace(BASE_URL, "");

        // Replace UUIDs with {id}
        Matcher matcher = UUID_PATTERN.matcher(sanitizedUrl);
        return matcher.replaceAll("{id}");
    }
}
