import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.core.JavaDex;
import dev.kurumidisciples.javadex.api.core.JavaDexBuilder;
import dev.kurumidisciples.javadex.api.entities.content.Manga;
import dev.kurumidisciples.javadex.api.exceptions.http.HTTPClientErrorException;
import dev.kurumidisciples.javadex.api.exceptions.http.HTTPTimeoutException;
import dev.kurumidisciples.javadex.api.exceptions.http.HTTPUnauthorizedException;
import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import dev.kurumidisciples.javadex.internal.http.HTTPRequest;
import kotlin.OptIn;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import java.util.List;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class HTTPRequestTest {

   

    
    /** 
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testPostJson() throws IOException, InterruptedException {
       
       JsonObject json = new JsonObject();
       json.addProperty("Id", 1234);
       json.addProperty("Customer", "John Doe");
       json.addProperty("Quantity", 1);
       json.addProperty("Price", 10.00);

       Response response = HTTPRequest.postResponse("https://reqbin.com/echo/post/json", json.toString(), Optional.empty());
       assertEquals(200, response.code());
    }


    @Test
    public void testClientErrorException() throws IOException, InterruptedException {
        JsonObject json = new JsonObject();
        json.addProperty("Id", 1234);
        json.addProperty("Customer", "John Doe");
        json.addProperty("Quantity", 1);
        json.addProperty("Price", 10.00);

        assertThrows(HTTPClientErrorException.class, () -> {
            HTTPRequest.get("https://api.mangadex.org/manga?tite=");
        });
    }

    @Test
    public void testMangaDexResponseTime() throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        Response response = HTTPRequest.getResponse("https://api.mangadex.org/ping", Optional.empty());
        long end = System.currentTimeMillis();
        assertEquals(200, response.code());
        long responseTime = end - start;
        System.out.println("API Response time: " + responseTime + "ms");
    }
}