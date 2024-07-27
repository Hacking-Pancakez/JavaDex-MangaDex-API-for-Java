package dev.kurumidisciples.javadex.internal.auth;

import dev.kurumidisciples.javadex.api.core.authentication.Token;

public interface Authenticator {
    String getAuthorizationHeader();
    void authenticate() throws Exception;
    void refresh() throws Exception;
    Token getToken();
    String getClientId();
    String getClientSecret();
}
