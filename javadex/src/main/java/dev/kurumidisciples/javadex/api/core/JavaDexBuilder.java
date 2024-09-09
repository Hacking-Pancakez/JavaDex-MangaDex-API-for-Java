package dev.kurumidisciples.javadex.api.core;

import java.time.Duration;

import javax.security.auth.login.LoginException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    public abstract JavaDex build() throws LoginException;
}
