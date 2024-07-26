import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;

import org.junit.jupiter.api.Test;

import dev.kurumidisciples.javadex.api.core.JavaDex;
import dev.kurumidisciples.javadex.api.core.JavaDexBuilder;
import dev.kurumidisciples.javadex.api.entities.Chapter;
import dev.kurumidisciples.javadex.api.entities.ScanlationGroup;
import dev.kurumidisciples.javadex.api.entities.content.Manga;
import dev.kurumidisciples.javadex.api.entities.enums.Locale;
import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import io.github.cdimascio.dotenv.Dotenv;

public class MangaTest {
    
    private static final Dotenv dotenv = Dotenv.configure().filename(".env").load();
    private static final JavaDex javadex = loginToJavaDex();

    
    /** 
     * @throws Exception
     */
    @Test
    public void testScanGroupfromChapter() throws Exception{
        // Test if the scanlation group is correctly retrieved from a chapter
        // This test is important because it ensures that the scanlation group is correctly retrieved from a chapter
        // This test will pass if the scanlation group is correctly retrieved from a chapter
        
        Manga manga = javadex.search("One Piece").setLimit(1).complete().get(0);
        assertNotNull(manga);
        Chapter chapter = manga.retrieveChapterByNumber(Locale.ENGLISH, 1).get().get(0);
        assertNotNull(chapter);
        ScanlationGroup group = chapter.retrieveScanlationGroups().get().get(0);
        assertNotNull(group);
    }

    @Test
    public void testIfPageProxyCanDownload() throws IOException, InterruptedException, ExecutionException{
        // Test if the page proxy can download
        // This test is important because it ensures that the page proxy can download
        // This test will pass if the page proxy gives read image data

        Manga manga = javadex.search("Magical Girl Raising Project").setLimit(1).complete().get(0);
        assertNotNull(manga);
        Chapter chapter = manga.retrieveChapterByNumber(Locale.ENGLISH, 1).get().get(0);
        assertNotNull(chapter);

        byte[] page = chapter.retrievePages().get().get(0).download().readAllBytes();

        // Convert byte array back to BufferedImage
        InputStream in = new ByteArrayInputStream(page);
        BufferedImage bImageFromConvert = ImageIO.read(in);
        assertNotNull(bImageFromConvert);
    }

    @Test
    public void testIfCoversCanBeRetrieved() throws Exception{
        // Test if the covers can be retrieved
        // This test is important because it ensures that the covers can be retrieved
        // This test will pass if the covers can be retrieved

        Manga manga = javadex.search("Magical Girl Raising Project: F2P").setLimit(1).complete().get(0);
        assertNotNull(manga);
        assertNotNull(manga.retrieveCovers(Locale.JAPANESE).get());
    }

    @Test
    public void testIfMangaCanBeFollowed() throws Exception{
        // Test if the manga can be followed
        // This test is important because it ensures that the manga can be followed
        // This test will pass if the manga can be followed
        Manga manga = javadex.search("Magical Girl Raising Project: F2P").setLimit(1).complete().get(0);
        assertNotNull(manga);
        assertTrue(javadex.followManga(manga).get());
    }

    @Test
    public void testIfEveryMangaPropertyAccessible() throws Exception{
        // Test if every manga property is accessible
        // This test is important because it ensures that every manga property is accessible
        // This test will pass if every manga property is accessible

        Manga manga = javadex.search("Destiny Unchain Online").setLimit(1).complete().get(0);
        assertNotNull(manga);
        assertNotNull(manga.getId());
        assertNotNull(manga.getIdRaw());
        assertNotNull(manga.getDefaultTitle());
        assertNotNull(manga.getAltTitles());
        assertNotNull(manga.getTags());
        assertNotNull(manga.getLinks());
        assertNotNull(manga.getPublicationDemographic());
        assertNotNull(manga.getStatus());
        assertNotNull(manga.doChapterNumbersResetOnNewVolume());
        assertNotNull(manga.retrieveFeed().get());
        assertNotNull(manga.retrieveChaptersOrdered(Locale.ENGLISH).get());
        assertNotNull(manga.retrieveCovers(Locale.ENGLISH).get());
        assertNotNull(manga.getRelationshipMap());

    }

    private static JavaDex loginToJavaDex(){
        try {
            return JavaDexBuilder.createPersonal()
                .setClientId(dotenv.get("MANGADEX_CLIENT_ID"))
                .setClientSecret(dotenv.get("MANGADEX_CLIENT_SECRET"))
                .setUsername(dotenv.get("MANGADEX_USERNAME"))
                .setPassword(dotenv.get("MANGADEX_PASSWORD"))
                .build();
        } catch (LoginException e) {
            e.printStackTrace();
            return null;
        }
    }
}
