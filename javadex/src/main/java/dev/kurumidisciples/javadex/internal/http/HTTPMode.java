package dev.kurumidisciples.javadex.internal.http;

public enum HTTPMode {
    FORM("application/x-www-form-urlencoded"),
    JSON("application/json");

    private final String contentType;

    HTTPMode(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }
}
