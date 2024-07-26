package dev.kurumidisciples.javadex.api.core.authentication;

/**
 * Represents an authentication token with access and refresh capabilities.
 * This class provides synchronized methods to safely get and set the access and refresh tokens,
 * ensuring thread safety in multi-threaded environments.
 */
public class Token {

    private static Token instance; // TODO look into multiple concurrent javadex objects and how this will work

    private String accessToken;
    private String refreshToken;

    /**
     * Constructs a new Token instance with specified access and refresh tokens.
     *
     * @param accessToken the access token string
     * @param refreshToken the refresh token string
     */
    public Token(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        instance = this;
    }

    /**
     * Retrieves the access token.
     *
     * @return the access token string
     */
    public synchronized String getAccessToken() {
        return accessToken;
    }

    /**
     * Sets the access token.
     *
     * @param accessToken the new access token string to set
     */
    public synchronized void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public synchronized static Token getInstance() {
        return instance;
    }

    /**
     * Retrieves the refresh token.
     *
     * @return the refresh token string
     */
    public synchronized String getRefreshToken() {
        return refreshToken;
    }

    /**
     * Sets the refresh token.
     *
     * @param refreshToken the new refresh token string to set
     */
    public synchronized void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}