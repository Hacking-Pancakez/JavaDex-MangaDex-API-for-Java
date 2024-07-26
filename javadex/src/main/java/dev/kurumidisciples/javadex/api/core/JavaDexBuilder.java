package dev.kurumidisciples.javadex.api.core;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import dev.kurumidisciples.javadex.internal.http.HTTPRequest;

/**
 * Builder class for creating JavaDex instances with specified configurations.
 *
 * @author Hacking Pancakez
 */
public class JavaDexBuilder {

    private static final Logger logger = LogManager.getLogger(JavaDexBuilder.class);

    private String clientId;
    private String clientSecret;
    private String username;
    private String password;
    private Duration refreshRate = Duration.ofMinutes(15); // Default value

    /**
     * Creates a new JavaDex instance with the default configurations.
     * <p>Guest access is used, <b>any methods that require authentication will not work and throw an exception.</b></p>
     *
     * @return a {@link dev.kurumidisciples.javadex.api.core.JavaDex} object
     */
    public static JavaDex createGuest(){
        return new JavaDex();
    }
    /**
     * Creates a new JavaDexBuilder instance with the default configurations.
     *
     * @param clientId a {@link String} object
     * @return a {@link JavaDexBuilder} object
     */
    public static JavaDexBuilder createPersonal(String clientId){
        return new JavaDexBuilder().setClientId(clientId);
    }

    /**
     * Creates a new JavaDexBuilder instance with the default configurations.
     *
     * @return a {@link JavaDexBuilder} object
     */
    public static JavaDexBuilder createPersonal(){
        return new JavaDexBuilder();
    }

    /**
     * Contronstructor for JavaDexBuilder.
     */
    protected JavaDexBuilder() {
    }

    /**
     * Sets the client ID.
     *
     * @param clientId The client ID.
     * @return The current instance of JavaDexBuilder.
     */
    public JavaDexBuilder setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    /**
     * Sets the client secret.
     *
     * @param clientSecret The client secret.
     * @return The current instance of JavaDexBuilder.
     */
    public JavaDexBuilder setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    /**
     * Sets the username.
     *
     * @param username The username.
     * @return The current instance of JavaDexBuilder.
     */
    public JavaDexBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * Sets the password.
     *
     * @param password The password.
     * @return The current instance of JavaDexBuilder.
     */
    public JavaDexBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * <p>Getter for the field <code>refreshRate</code>.</p>
     *
     * @return a {@link java.time.Duration} object
     */
    public Duration getRefreshRate() {
        return refreshRate;
    }

    /**
     * Sets the refresh rate. It is recommended to not set this value unless necessary.
     *
     * @param refreshRate The refresh rate.
     * @return The current instance of JavaDexBuilder.
     */
    public JavaDexBuilder setRefreshRate(Duration refreshRate) {
        logger.warn("Setting the refresh rate is not recommended unless necessary and may cause issues when using authenticated endpoints.");
        this.refreshRate = refreshRate;
        return this;
    }

    /**
     * Builds a new JavaDex instance with the specified configurations.
     *
     * @return A new JavaDex instance.
     * @throws javax.security.auth.login.LoginException if any.
     */
    public JavaDex build() throws LoginException {
        try{
            logger.debug("Building JavaDex instance with client ID: {}, username: {}", clientId, username);
            String[] tokens = authenticate();
            return new JavaDex(tokens, refreshRate, clientId, clientSecret);
        } catch (Exception e){
            logger.error("Error while building JavaDex instance", e);
            throw new LoginException(e.getMessage());
        }
        
    }

    /**
     * Authenticates with the MangaDex API and retrieves authentication tokens.
     *
     * @return An array containing the session and refresh tokens.
     * @throws IOException If an I/O error occurs during authentication.
     */
    private String[] authenticate() throws IOException, InterruptedException, HTTPRequestException {
        logger.debug("Authenticating with MangaDex API using username: {}", username);
        String loginUrl = "https://auth.mangadex.org/realms/mangadex/protocol/openid-connect/token";
        Map<String, String> formData = new HashMap<>();
        formData.put("client_id", clientId);
        formData.put("client_secret", clientSecret);
        formData.put("grant_type", "password");
        formData.put("username", username);
        formData.put("password", password);

        String jsonResponse = HTTPRequest.postForm(loginUrl, formData);
        Gson gson = new Gson();
        LoginResponse loginResponse = gson.fromJson(jsonResponse, LoginResponse.class);
        logger.debug("Authentication successful. Access token: {}", loginResponse.access_token);
        return new String[]{loginResponse.access_token, loginResponse.refresh_token};
    }

    /**
     * Inner class representing the response from the MangaDex API during authentication.
     */
    private class LoginResponse {
        private String access_token;
        private String refresh_token;
    }
}
