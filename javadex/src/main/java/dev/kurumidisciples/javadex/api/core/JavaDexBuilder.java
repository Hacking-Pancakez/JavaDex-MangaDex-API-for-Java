package dev.kurumidisciples.javadex.api.core;

import java.time.Duration;

import javax.security.auth.login.LoginException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.kurumidisciples.javadex.internal.auth.Authenticator;
import dev.kurumidisciples.javadex.internal.auth.ClientCredentialsAuthenticator;

/**
 * Builder class for creating JavaDex instances with specified configurations.
 */
public class JavaDexBuilder {

    private static final Logger logger = LogManager.getLogger(JavaDexBuilder.class);

    private String clientId;
    private String clientSecret;
    private String username;
    private String password;
    private Duration refreshRate = Duration.ofMinutes(15); // Default value

    public static JavaDex createGuest() {
        return new JavaDex();
    }

    public static JavaDexBuilder createPersonal(String clientId) {
        return new JavaDexBuilder().setClientId(clientId);
    }

    public static JavaDexBuilder createPersonal() {
        return new JavaDexBuilder();
    }

    protected JavaDexBuilder() {
    }

    public JavaDexBuilder setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public JavaDexBuilder setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public JavaDexBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public JavaDexBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public Duration getRefreshRate() {
        return refreshRate;
    }

    public JavaDexBuilder setRefreshRate(Duration refreshRate) {
        logger.warn("Setting the refresh rate is not recommended unless necessary and may cause issues when using authenticated endpoints.");
        this.refreshRate = refreshRate;
        return this;
    }

    public JavaDex build() throws LoginException {
        try {
            logger.debug("Building JavaDex instance with client ID: {}, username: {}", clientId, username);
            Authenticator authenticator = new ClientCredentialsAuthenticator(clientId, clientSecret, username, password);
            authenticator.authenticate();
            return new JavaDex(authenticator, refreshRate);
        } catch (Exception e) {
            logger.error("Error while building JavaDex instance", e);
            throw new LoginException(e.getMessage());
        }
    }
}
