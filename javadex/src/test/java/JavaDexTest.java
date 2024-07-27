import java.io.IOException;
import java.time.Duration;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.kurumidisciples.javadex.api.core.JavaDex;
import dev.kurumidisciples.javadex.api.core.JavaDexBuilder;
import dev.kurumidisciples.javadex.api.core.authentication.Token;
import dev.kurumidisciples.javadex.api.entities.Chapter;
import dev.kurumidisciples.javadex.api.entities.User;
import dev.kurumidisciples.javadex.api.entities.content.Manga;
import dev.kurumidisciples.javadex.api.entities.intermediate.middlemen.EntityMiddleman;
import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import dev.kurumidisciples.javadex.internal.actions.retrieve.ChapterAction;
import dev.kurumidisciples.javadex.internal.actions.retrieve.FollowsAction;
import dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction;
import io.github.cdimascio.dotenv.Dotenv;

@ExtendWith(MockitoExtension.class)
public class JavaDexTest {

    private static final Logger logger = LogManager.getLogger(JavaDexTest.class);
    private static final Dotenv dotenv = Dotenv.configure().filename(".env").load();

    @Test
    public void testSetRefreshRate() {
        JavaDexBuilder builder = JavaDexBuilder.createPersonal();
        Duration refreshRate = Duration.ofSeconds(30);
        builder.setRefreshRate(refreshRate);
        assertEquals(refreshRate, builder.getRefreshRate());
    }

    @Test
    public void testIfRefreshUpdatedAccess(){
       JavaDex javaDex = loginToJavaDex();
       assertNotNull(javaDex);
       String accessToken = javaDex.getAccessToken();
       //wait 5 secs
         try {
              Thread.sleep(10000);
         } catch (InterruptedException e) {
              e.printStackTrace();
         }
         assertNotEquals(accessToken, javaDex.getAccessToken());
    }

    
    /** 
     * @throws LoginException
     */
    @Test
    public void testBuild() throws LoginException {
        Dotenv dotenv = Dotenv.configure().filename(".env").load();
        JavaDexBuilder builder = JavaDexBuilder.createPersonal();
        builder.setClientId(dotenv.get("MANGADEX_CLIENT_ID"));
        builder.setClientSecret(dotenv.get("MANGADEX_CLIENT_SECRET"));
        builder.setUsername(dotenv.get("MANGADEX_USERNAME"));
        builder.setPassword(dotenv.get("MANGADEX_PASSWORD"));
        builder.setRefreshRate(Duration.ofSeconds(30));

        JavaDex javaDex = builder.build();

        assertNotNull(javaDex);
        assertNotNull(javaDex.getAccessToken());
        assertNotNull(javaDex.getRefreshToken());
    }

    @Test
    public void testReadChapters() throws IOException, InterruptedException, Exception {
        Dotenv dotenv = Dotenv.configure().filename(".env").load();
        JavaDexBuilder builder = JavaDexBuilder.createPersonal();
        builder.setClientId(dotenv.get("MANGADEX_CLIENT_ID"));
        builder.setClientSecret(dotenv.get("MANGADEX_CLIENT_SECRET"));
        builder.setUsername(dotenv.get("MANGADEX_USERNAME"));
        builder.setPassword(dotenv.get("MANGADEX_PASSWORD"));

        JavaDex javaDex = builder.build();

        assertNotNull(javaDex);

        Manga manga = javaDex.search("Magical Girl Raising Project: F2P").setLimit(1).complete().get(0);
        assertNotNull(manga);

        javaDex.retrieveReadChapters(manga.getIdRaw()).get().forEach(chapter -> {
            logger.debug("Chapter: {}", chapter);
            assertNotNull(chapter);
        });
    }

    @Test
    public void testRetrievingSelf() throws IOException, InterruptedException, Exception {
        Dotenv dotenv = Dotenv.configure().filename(".env").load();
        JavaDexBuilder builder = JavaDexBuilder.createPersonal();
        builder.setClientId(dotenv.get("MANGADEX_CLIENT_ID"));
        builder.setClientSecret(dotenv.get("MANGADEX_CLIENT_SECRET"));
        builder.setUsername(dotenv.get("MANGADEX_USERNAME"));
        builder.setPassword(dotenv.get("MANGADEX_PASSWORD"));

        JavaDex javaDex = builder.build();

        assertNotNull(javaDex);

        User user = javaDex.retrieveSelf().get();
        assertEquals(dotenv.get("MANGADEX_USERNAME"), user.getUsername());
        assertNotNull(user.getRoles());
    }

    @Test
    public void testFollowingManga() throws LoginException, HTTPRequestException{
        Dotenv dotenv = Dotenv.configure().filename(".env").load();
        JavaDexBuilder builder = JavaDexBuilder.createPersonal();
        builder.setClientId(dotenv.get("MANGADEX_CLIENT_ID"));
        builder.setClientSecret(dotenv.get("MANGADEX_CLIENT_SECRET"));
        builder.setUsername(dotenv.get("MANGADEX_USERNAME"));
        builder.setPassword(dotenv.get("MANGADEX_PASSWORD"));

        JavaDex javaDex = builder.build();

        assertNotNull(javaDex);

        FollowsAction action = javaDex.retrieveFollowingManga().setLimit(100);
        List<EntityMiddleman> entities = action.complete();
        List<Manga> mangas = entities.stream().map(entity -> (Manga) entity.getEntity()).toList();
        mangas.forEach(manga -> {
            System.out.println("Manga: " + manga.getDefaultTitle() + " (" + manga.getId() + ")");
            assertNotNull(manga);
        });
    }

    @Test
    public void testRetrieveChapter() throws IOException, InterruptedException {
        JavaDex javaDex = JavaDexBuilder.createGuest();
        assertNotNull(javaDex);

        ChapterAction action = javaDex.retrieveChapters().setTitle("mahiro and").setOffset(1000);
        List<Chapter> chapters = action.complete();
        chapters.forEach(chapter -> {
            System.out.println("Chapter: " + chapter.getTitle() + " (" + chapter.getId() + ")");
            assertNotNull(chapter);
        });
        javaDex.close();
    }

    @Test
    public void testGuest() throws IOException, InterruptedException, HTTPRequestException {
        JavaDex javaDex = JavaDexBuilder.createGuest();
        assertNotNull(javaDex);
    }

    @Test
    public void testSearchWithQuery() {
        JavaDex javaDex = JavaDexBuilder.createGuest();
        MangaAction mangaAction = javaDex.search("query");
        assertNotNull(mangaAction);
        // Assuming MangaAction has a method to get the query, which might not be the case. This is just an example.
        // assertEquals("query", mangaAction.getQuery());
    }

    private static JavaDex loginToJavaDex(){
        try {
            return JavaDexBuilder.createPersonal()
                .setClientId(dotenv.get("MANGADEX_CLIENT_ID"))
                .setClientSecret(dotenv.get("MANGADEX_CLIENT_SECRET"))
                .setUsername(dotenv.get("MANGADEX_USERNAME"))
                .setPassword(dotenv.get("MANGADEX_PASSWORD"))
                .setRefreshRate(Duration.ofSeconds(5))
                .build();
        } catch (LoginException e) {
            e.printStackTrace();
            return null;
        }
    }
}
