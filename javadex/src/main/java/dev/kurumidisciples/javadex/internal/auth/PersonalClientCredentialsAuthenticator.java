package dev.kurumidisciples.javadex.internal.auth;

import java.io.IOException;

import javax.security.auth.login.LoginException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.TokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;

import dev.kurumidisciples.javadex.api.core.authentication.Token;

public class PersonalClientCredentialsAuthenticator implements Authenticator {

    private static final Logger logger = LogManager.getLogger(PersonalClientCredentialsAuthenticator.class);

    private final String clientId;
    private final String clientSecret;
    private final String username;
    private final String password;
    private Token token;

    // OAuth2-related configurations
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final String TOKEN_SERVER_URL = "https://auth.mangadex.org/realms/mangadex/protocol/openid-connect/token";

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

            // Use TokenRequest for OAuth2 password flow
            TokenRequest tokenRequest = new TokenRequest(
                    HTTP_TRANSPORT,
                    JSON_FACTORY,
                    new GenericUrl(TOKEN_SERVER_URL),
                    "password"
            );

            // Set client ID, client secret, username, and password
            tokenRequest.setClientAuthentication(new ClientParametersAuthentication(clientId, clientSecret));
            tokenRequest.put("username", username);
            tokenRequest.put("password", password);

            // Execute the request and retrieve the token response
            TokenResponse tokenResponse = tokenRequest.execute();

            logger.debug("Authentication successful. Access token: {}", tokenResponse.getAccessToken());
            token = new Token(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());
        } catch (IOException e) {
            logger.fatal("Error during authentication", e);
            throw new LoginException("Authentication failed: " + e.getMessage());
        }
    }

    @Override
    public synchronized void refresh() throws LoginException {
        try{

        logger.debug("Refreshing access token using refresh token: {}", getToken().getRefreshToken());

        TokenRequest refreshRequest = new TokenRequest(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                new GenericUrl(TOKEN_SERVER_URL),
                "refresh_token"
        );

        refreshRequest.setClientAuthentication(new ClientParametersAuthentication(clientId, clientSecret));
        refreshRequest.put("refresh_token", getToken().getRefreshToken());

        TokenResponse tokenResponse = refreshRequest.execute();

        logger.debug("Access token refreshed successfully.");
        getToken().setAccessToken(tokenResponse.getAccessToken());
        getToken().setRefreshToken(tokenResponse.getRefreshToken());
        }
        catch (IOException e) {
            logger.fatal("Error during token refresh", e);
            throw new LoginException("Token refresh failed: " + e.getMessage());
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
}
