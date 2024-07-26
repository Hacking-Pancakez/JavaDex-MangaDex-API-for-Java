package dev.kurumidisciples.javadex.internal.http;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.kurumidisciples.javadex.api.exceptions.NetworkErrorException;
import dev.kurumidisciples.javadex.api.exceptions.http.HTTPInterruptedException;
import dev.kurumidisciples.javadex.api.exceptions.http.HTTPTimeoutException;
import dev.kurumidisciples.javadex.api.exceptions.http.HTTPUnexpectedStatusCodeException;
import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import dev.kurumidisciples.javadex.internal.utils.ErrorResponseChecker;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Utility class for making HTTP requests.
 * @apiNote This class is an internal utility class and should not be used directly.
 * @since 0.0.1
 */
@SuppressWarnings("unused")
public class HTTPRequest {

    private static final Logger logger = LogManager.getLogger(HTTPRequest.class);

    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private static final int MAX_REQUESTS_PER_MINUTE = 100; // Adjust this value as needed
    private static final Semaphore rateLimiter = new Semaphore(MAX_REQUESTS_PER_MINUTE);
    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);


    /**
     * Sends a POST request to the specified URL with the specified JSON payload.
     * @param url the URL to send the request to
     * @param json the JSON payload to send with the request
     * @return the response body as a string
     * @throws HTTPRequestException if the request fails
     */
    public static String post(String url, String json) throws HTTPRequestException{
        return postWithBearer(url, json, Optional.empty());
    }

    public static String post(String url, Optional<String> bearer) throws HTTPRequestException {
        return postWithBearer(url, "", bearer);
    }

    /**
     * Sends a POST request to the specified URL with the specified JSON payload and bearer token.
     * @param url the URL to send the request to
     * @param json the JSON payload to send with the request
     * @param bearer the bearer token to use for authorization
     * @return the response body as a string
     * @throws HTTPRequestException if the request fails
     */
    public static String post(String url, String json, Optional<String> bearer) throws HTTPRequestException {
        return postWithBearer(url, json, bearer);
    }

    /**
     * Helper method to send a POST request with optional bearer token.
     * @param url the URL to send the request to
     * @param json the JSON payload to send with the request
     * @param bearer the bearer token to use for authorization
     * @return the response body as a string
     * @throws HTTPRequestException if the request fails
     */
    private static String postWithBearer(String url, String json, Optional<String> bearer) throws HTTPRequestException {
        try {
            rateLimiter.acquire();
            logger.debug("Sending POST request to URL: {} with JSON payload", url);
            RequestBody body = RequestBody.create(json, JSON);
            Request.Builder requestBuilder = new Request.Builder()
                    .url(url)
                    .post(body);
            bearer.ifPresent(b -> requestBuilder.addHeader("Authorization", "Bearer " + b));
            Request request = requestBuilder.build();

            return executeRequest(request, url);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HTTPInterruptedException(e.getMessage(), e);
        } finally {
            executor.schedule((Runnable) rateLimiter::release, 1, TimeUnit.MINUTES);
        }
    }

    private static String deleteWithBearer(String url, Optional<String> bearer) throws HTTPRequestException {
        try {
            rateLimiter.acquire();
            logger.debug("Sending DELETE request to URL: {}", url);
            Request.Builder requestBuilder = new Request.Builder()
                    .url(url)
                    .delete();
            bearer.ifPresent(b -> requestBuilder.addHeader("Authorization", "Bearer " + b));
            Request request = requestBuilder.build();

            return executeRequest(request, url);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HTTPInterruptedException(e.getMessage(), e);
        } finally {
            executor.schedule((Runnable) rateLimiter::release, 1, TimeUnit.MINUTES);
        }
    }

    /**
     * Sends a POST request to the specified URL with the specified JSON payload and bearer token, returning the raw response.
     * @param url the URL to send the request to
     * @param json the JSON payload to send with the request
     * @param bearer the bearer token to use for authorization
     * @return the raw response object
     * @throws InterruptedException if the request is interrupted
     */
    public static Response postResponse(String url, String json, Optional<String> bearer) throws HTTPRequestException {
        try {
            rateLimiter.acquire();
            logger.debug("Sending POST request to URL: {} with JSON payload and authorization", url);
            RequestBody body = RequestBody.create(json, JSON);
            Request.Builder requestBuilder = new Request.Builder()
                    .url(url)
                    .post(body);
            bearer.ifPresent(b -> requestBuilder.addHeader("Authorization", "Bearer " + b));
            Request request = requestBuilder.build();

            return executeRequestForResponse(request, url);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HTTPInterruptedException(e.getMessage(), e);
        } finally {
            executor.schedule((Runnable) rateLimiter::release, 1, TimeUnit.MINUTES);
        }
    }

    /**
     * Sends a GET request to the specified URL with the specified JSON payload.
     * @param url the URL to send the request to
     * @param json the JSON payload to send with the request
     * @return the response body as a string
     * @throws HTTPRequestException if the request fails
     */
    public static String get(String url, String json) throws HTTPRequestException {
        return getWithBearer(url, Optional.of(json), Optional.empty());
    }

    /**
     * Sends a GET request to the specified URL.
     * @param url the URL to send the request to
     * @return the response body as a string
     * @throws HTTPRequestException if the request fails
     */
    public static String get(String url) throws HTTPRequestException {
        return getWithBearer(url, Optional.empty(), Optional.empty());
    }

    /**
     * Sends a GET request to the specified URL with the specified bearer token.
     * @param url the URL to send the request to
     * @param bearer the bearer token to use for authorization
     * @return the response body as a string
     * @throws HTTPRequestException if the request fails
     */
    public static String get(String url, Optional<String> bearer) throws HTTPRequestException {
        return getWithBearer(url, Optional.empty(), bearer);
    }

    /**
     * Helper method to send a GET request with optional JSON payload and bearer token.
     * @param url the URL to send the request to
     * @param json the optional JSON payload to send with the request
     * @param bearer the bearer token to use for authorization
     * @return the response body as a string
     * @throws InterruptedException if the request is interrupted
     */
    private static String getWithBearer(String url, Optional<String> json, Optional<String> bearer) throws HTTPRequestException {
        try {
            rateLimiter.acquire();
            logger.debug("Sending GET request to URL: {} with optional payload and authorization", url);
            Request.Builder requestBuilder = new Request.Builder()
                    .url(url)
                    .get();
            json.ifPresent(j -> requestBuilder.method("GET", RequestBody.create(j, JSON)));
            bearer.ifPresent(b -> requestBuilder.addHeader("Authorization", "Bearer " + b));
            Request request = requestBuilder.build();

            return executeRequest(request, url);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HTTPInterruptedException(e.getMessage(), e);
        } finally {
            executor.schedule((Runnable) rateLimiter::release, 1, TimeUnit.MINUTES);
        }
    }

    /**
     * Sends a GET request to the specified URL with the specified bearer token, returning the raw response.
     * @param url the URL to send the request to
     * @param bearer the bearer token to use for authorization
     * @return the raw response object
     * @throws HTTPRequestException if the request fails
     */
    public static Response getResponse(String url, Optional<String> bearer) throws HTTPRequestException {
        try {
            rateLimiter.acquire();
            logger.debug("Sending GET request to URL: {} with authorization", url);
            Request.Builder requestBuilder = new Request.Builder()
                    .url(url)
                    .get();
            bearer.ifPresent(b -> requestBuilder.addHeader("Authorization", "Bearer " + b));
            Request request = requestBuilder.build();

            return executeRequestForResponse(request, url);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HTTPInterruptedException(e.getMessage(), e);
        } finally {
            executor.schedule((Runnable) rateLimiter::release, 1, TimeUnit.MINUTES);
        }
    }

    /**
     * Sends a POST request to the specified URL with the specified form data. Is mostly used for access token requests.
     * @param url the URL to send the request to
     * @param formData the form data to send with the request
     * @return the response body as a string
     * @throws HTTPRequestException if the request fails
     */
    public static String postForm(String url, Map<String, String> formData) throws HTTPRequestException {
        try {
            rateLimiter.acquire();
            logger.debug("Sending POST request to URL: {} with form data", url);
            FormBody.Builder formBuilder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : formData.entrySet()) {
                formBuilder.add(entry.getKey(), entry.getValue());
            }

            RequestBody body = formBuilder.build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            return executeRequest(request, url);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HTTPInterruptedException(e.getMessage(), e);
        } finally {
            executor.schedule((Runnable) rateLimiter::release, 1, TimeUnit.MINUTES);
        }
    }

    /**
     * Executes the given request and returns the response body as a string.
     * @param request the HTTP request to execute
     * @param url the URL to which the request is sent (for logging purposes)
     * @return the response body as a string
     * @throws HTTPRequestException if the request fails
     */
    private static String executeRequest(Request request, String url) throws HTTPRequestException {
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.debug("Request to URL: {} failed with code: {}", url, response.code());
                throw ErrorResponseChecker.retrieveCorrectHTTPException(response);
            }
            logger.debug("Request to URL: {} succeeded", url);
            return response.body().string();
        } catch (SocketTimeoutException e) {
            logger.debug("Request to URL: {} timed out", url, e);
            throw new HTTPTimeoutException("Request timed out", e);
        } catch (IOException e) {
            if (ErrorResponseChecker.isNetworkError(e)){
                logger.debug("Network error occurred", e);
                throw new NetworkErrorException(e.getMessage(), e);
            } else {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    /**
     * Executes the given request and returns the raw response object.
     * @param request the HTTP request to execute
     * @param url the URL to which the request is sent (for logging purposes)
     * @return the raw response object
     * @throws HTTPRequestException if the request fails
     */
    private static Response executeRequestForResponse(Request request, String url) throws HTTPRequestException {
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.debug("Request to URL: {} failed with code: {}", url, response.code());
                throw ErrorResponseChecker.retrieveCorrectHTTPException(response);
            }
            logger.debug("Request to URL: {} succeeded", url);
            return response;
        } catch (SocketTimeoutException e) {
            logger.debug("Request to URL: {} timed out", url, e);
            throw new HTTPTimeoutException("Request timed out", e);
        } catch (IOException e) {
            if (ErrorResponseChecker.isNetworkError(e)){
                logger.debug("Network error occurred", e);
                throw new NetworkErrorException(e.getMessage(), e);
            } else {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }
}
