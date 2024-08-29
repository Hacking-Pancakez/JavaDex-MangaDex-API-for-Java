package dev.kurumidisciples.javadex.api.core;

import java.time.Duration;

import javax.security.auth.login.LoginException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.kurumidisciples.javadex.internal.auth.Authenticator;
import dev.kurumidisciples.javadex.internal.auth.PersonalClientCredentialsAuthenticator;

/**
 * Builder class for creating JavaDex instances with specified configurations.
 */
public abstract class JavaDexBuilder {

    private static final Logger logger = LogManager.getLogger(JavaDexBuilder.class);
    
    private Duration refreshRate = Duration.ofMinutes(15); // Default value

    public static JavaDex createGuest() {
        return new JavaDex();
    }

    public static ClientPersonalJavaDexBuilder createPersonal(String clientId) {
        return new ClientPersonalJavaDexBuilder().setClientId(clientId);
    }

    public static ClientPersonalJavaDexBuilder createPersonal() {
        return new ClientPersonalJavaDexBuilder();
    }

    

    public Duration getRefreshRate() {
        return refreshRate;
    }

    public JavaDexBuilder setRefreshRate(Duration refreshRate) {
        logger.warn("Setting the refresh rate is not recommended unless necessary and may cause issues when using authenticated endpoints.");
        this.refreshRate = refreshRate;
        return this;
    }

    public abstract JavaDex build() throws LoginException;
}
