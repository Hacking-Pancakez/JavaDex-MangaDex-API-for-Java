package dev.kurumidisciples.javadex.internal.factory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dev.kurumidisciples.javadex.api.entities.Chapter;
import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import dev.kurumidisciples.javadex.api.proxies.PageProxy;
import dev.kurumidisciples.javadex.internal.http.HTTPRequest;

public class PageFactory {

    private static final String API_SERVER = "https://api.mangadex.org/at-home/server/";
    private static final String UPLOADS_SERVER = "https://uploads.mangadex.org/data/";
    private static final Logger LOGGER = LogManager.getLogger(PageFactory.class);
    
    
        public static List<PageProxy> getPages(Chapter chapter) throws InterruptedException {
            try {
                String jsonResponse = HTTPRequest.get(API_SERVER + chapter.getId());
                JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
                JsonObject chapterData = jsonObject.getAsJsonObject("chapter");
                String hash = chapterData.get("hash").getAsString();
                JsonArray data = chapterData.getAsJsonArray("data");

                return IntStream.range(0, data.size())
                        .mapToObj(i -> new PageProxy(String.valueOf(i + 1), chapter, buildPageUrl(hash, data.get(i).getAsString())))
                        .collect(Collectors.toList());
            } catch (HTTPRequestException e) {
                LOGGER.warn("Could not successfully build pages", e);
                return Collections.emptyList();
            }
        }

    private static String buildPageUrl(String hash, String dataPart) {
        return UPLOADS_SERVER + hash + "/" + dataPart;
    }
}
