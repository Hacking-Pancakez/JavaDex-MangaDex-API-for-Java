package dev.kurumidisciples.javadex.api.exceptions.http;

import java.io.IOException;

@Deprecated
public class HTTPException extends IOException {
    private final int responseCode;

    public HTTPException(String message, int responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }
}

