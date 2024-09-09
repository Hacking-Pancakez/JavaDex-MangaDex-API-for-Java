package javadex;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import javax.security.auth.login.LoginException;

import org.junit.jupiter.api.Test;

import dev.kurumidisciples.javadex.api.core.JavaDex;
import dev.kurumidisciples.javadex.api.core.JavaDexBuilder;
import io.github.cdimascio.dotenv.Dotenv;

public class LoginTest {
    private Dotenv dotenv = Dotenv.load();

    @Test
    public void testLoginPersonal() throws LoginException, IOException {
        String clientId = dotenv.get("MANGADEX_CLIENT_ID");
        String clientSecret = dotenv.get("MANGADEX_CLIENT_SECRET");
        String username = dotenv.get("MANGADEX_USERNAME");
        String password = dotenv.get("MANGADEX_PASSWORD");

        JavaDex javaDex = JavaDexBuilder.createPersonal(clientId)
            .setClientSecret(clientSecret)
            .setUsername(username)
            .setPassword(password)
            .build();

        assertNotNull(javaDex);
        assertNotNull(javaDex.getAccessToken());
    }
}
