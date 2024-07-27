package dev.kurumidisciples.javadex.internal.http;

import java.util.Map;
import java.util.Optional;

import dev.kurumidisciples.javadex.internal.config.HTTPConfig;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RequestBuilder {

    public static Request buildPostRequest(String url, String json, Optional<String> bearer) {
        RequestBody body = RequestBody.create(json, HTTPConfig.JSON);
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);
        bearer.ifPresent(b -> builder.addHeader("Authorization", "Bearer " + b));
        return builder.build();
    }

    public static Request buildGetRequest(String url, Optional<String> bearer) {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .get();
        bearer.ifPresent(b -> builder.addHeader("Authorization", "Bearer " + b));
        return builder.build();
    }

    public static Request buildDeleteRequest(String url, Optional<String> bearer) {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .delete();
        bearer.ifPresent(b -> builder.addHeader("Authorization", "Bearer " + b));
        return builder.build();
    }

    public static Request buildPutRequest(String url, String json, Optional<String> bearer) {
        RequestBody body = RequestBody.create(json, HTTPConfig.JSON);
        Request.Builder builder = new Request.Builder()
                .url(url)
                .put(body);
        bearer.ifPresent(b -> builder.addHeader("Authorization", "Bearer " + b));
        return builder.build();
    }

    public static Request buildPostFormRequest(String url, Map<String, String> form, Optional<String> bearer) {
        FormBody.Builder builder = new FormBody.Builder();
        form.forEach(builder::add);
        FormBody body = builder.build();
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(body);
        bearer.ifPresent(b -> requestBuilder.addHeader("Authorization", "Bearer " + b));
        return requestBuilder.build();
    }
}
