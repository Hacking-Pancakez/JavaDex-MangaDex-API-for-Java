package dev.kurumidisciples.javadex.internal.config;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class HTTPConfig {
    public static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final int MAX_REQUESTS_PER_MINUTE = 100;
}
