package manga;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.core.JavaDex;
import dev.kurumidisciples.javadex.api.core.JavaDexBuilder;
import dev.kurumidisciples.javadex.api.entities.Manga;
import dev.kurumidisciples.javadex.api.exceptions.http.HTTPClientErrorException;
import dev.kurumidisciples.javadex.api.exceptions.http.HTTPTimeoutException;
import dev.kurumidisciples.javadex.api.exceptions.http.HTTPUnauthorizedException;
import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import dev.kurumidisciples.javadex.internal.http.HTTPRequest;
import io.github.cdimascio.dotenv.Dotenv;
import kotlin.OptIn;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import java.util.List;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class StatsTest {
    
    private Dotenv dotenv = Dotenv.load();


    @Test
    public void testGetStats() throws InterruptedException, ExecutionException {
        JavaDex javaDex = JavaDexBuilder.createGuest();
        List<Manga> mangaList = javaDex.search("One Piece").complete();
        Manga manga = mangaList.get(0);
        assertNotNull(manga);
        assertNotNull(manga.retrieveStatistics().get());
    }
}
