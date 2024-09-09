package dev.kurumidisciples.javadex.internal.auth;

import javax.security.auth.login.LoginException;

import dev.kurumidisciples.javadex.api.core.authentication.Token;

public interface Authenticator {
    String getAuthorizationHeader();
    void authenticate() throws LoginException;
    void refresh() throws LoginException;
    Token getToken();
    String getClientId();
    String getClientSecret();
}
