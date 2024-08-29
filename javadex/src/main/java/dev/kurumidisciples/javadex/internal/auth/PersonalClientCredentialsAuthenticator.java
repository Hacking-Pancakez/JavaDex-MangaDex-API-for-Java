package dev.kurumidisciples.javadex.internal.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionException;

import javax.naming.AuthenticationException;
import javax.security.auth.login.LoginException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import dev.kurumidisciples.javadex.api.core.authentication.Token;
import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import dev.kurumidisciples.javadex.internal.http.HTTPRequest;

public class PersonalClientCredentialsAuthenticator implements Authenticator {

    private static final Logger logger = LogManager.getLogger(PersonalClientCredentialsAuthenticator.class);

    private final String clientId;
    private final String clientSecret;
    private final String username;
    private final String password;
    private Token token;

    public PersonalClientCredentialsAuthenticator(String clientId, String clientSecret, String username, String password) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.username = username;
        this.password = password;
    }

    @Override
    public void authenticate() throws LoginException {
        try {
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
            token = new Token(loginResponse.access_token, loginResponse.refresh_token);
        } catch (HTTPRequestException e) {
            logger.fatal("Error during authentication", e);
            throw new LoginException("Authentication failed: " + e.getMessage());
        }
    }

    @Override
    public synchronized void refresh() throws AuthenticationException {
        logger.debug("Refreshing access token using refresh token: {}", getToken().getRefreshToken());
    String refreshUrl = "https://auth.mangadex.org/realms/mangadex/protocol/openid-connect/token";
    Map<String, String> formData = Map.of(
        "grant_type", "refresh_token",
        "refresh_token", getToken().getRefreshToken(),
        "client_id", getClientId(),
        "client_secret", getClientSecret()
    );

    try {
        String jsonResponse = HTTPRequest.postForm(refreshUrl, formData);
        RefreshResponse refreshResponse = new Gson().fromJson(jsonResponse, RefreshResponse.class);
        getToken().setAccessToken(refreshResponse.access_token);
        getToken().setRefreshToken(refreshResponse.refresh_token);
        logger.debug("Access token refreshed successfully. New access token: {}", getToken().getAccessToken());
    } catch (HTTPRequestException e) {
        logger.fatal("Access token was not able to be refreshed. Check your credentials and/or Mangadex API status.", e);
        throw new CompletionException(e);
    }
    }

    @Override
    public String getAuthorizationHeader() {
        return "Bearer " + token.getAccessToken();
    }

    @Override
    public synchronized Token getToken() {
        return token;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    private static class LoginResponse {
        private String access_token;
        private String refresh_token;
    }

    private class RefreshResponse {
        private String access_token;
        private String refresh_token;
    }
}
