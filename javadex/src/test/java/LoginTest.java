import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.security.auth.login.LoginException;

import org.junit.jupiter.api.Test;

import dev.kurumidisciples.javadex.api.core.JavaDex;
import dev.kurumidisciples.javadex.api.core.JavaDexBuilder;
import dev.kurumidisciples.javadex.api.entities.ScanlationGroup;
import io.github.cdimascio.dotenv.Dotenv;

public class LoginTest {
    
    private Dotenv dotenv = Dotenv.load();

    @Test
    public void testLoginPersonal() throws LoginException {
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
        assertNotNull(((ScanlationGroup) javaDex.retrieveFollowingGroups().complete().get(0)).getName());
    }

    @Test
    public void testRefreshPersonal() throws LoginException{
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
        assertNotNull(javaDex.getAuthenticator().getToken().getAccessToken());
        String oldToken = javaDex.getAuthenticator().getToken().getAccessToken();
        assertNotNull(((ScanlationGroup) javaDex.retrieveFollowingGroups().complete().get(0)).getName());
        javaDex.getAuthenticator().refresh();
        String newToken = javaDex.getAuthenticator().getToken().getAccessToken();
        assertNotEquals(oldToken, newToken);
    }
}
