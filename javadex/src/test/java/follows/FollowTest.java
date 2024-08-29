package follows;

import io.github.cdimascio.dotenv.Dotenv;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dev.kurumidisciples.javadex.api.core.JavaDex;
import dev.kurumidisciples.javadex.api.core.JavaDexBuilder;
import dev.kurumidisciples.javadex.api.entities.MDList;
import dev.kurumidisciples.javadex.api.entities.Manga;

public class FollowTest {

    private static Dotenv dotenv = Dotenv.load();

    @Test
    public void testFollowingManga() throws Exception {
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

        assertNotNull(javaDex.retrieveFollowingManga().complete());
        javaDex.close();
    }

    @Test
    public void testFollowingGroups() throws Exception {
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

        assertNotNull(javaDex.retrieveFollowingGroups().complete());
        javaDex.close();
    }

    @Test
    public void testFollowingLists() throws Exception {
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

        MDList list = (MDList) javaDex.retrieveFollowingLists().complete().get(0);

        System.out.println((list.getMangas().get(1).retrieve().getTags()));

        assertNotNull(javaDex.retrieveFollowingLists().complete());
        javaDex.close();
    }

    @Test
    public void testIfCanFollowManga() throws Exception{
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

        Manga manga = javaDex.search("Komi-san wa Komyushou Desu").setLimit(1).complete().get(0);

        assertTrue(javaDex.followManga(manga).get());
        javaDex.close();
    }

    @Test
    public void testIsFollowingManga() throws Exception{
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

        Manga manga = javaDex.search("Komi-san wa Komyushou Desu").setLimit(1).complete().get(0);

        assertTrue(javaDex.isFollowingManga(manga).get());
        javaDex.close();
    }
}
