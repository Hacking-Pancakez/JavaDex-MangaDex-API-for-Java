package dev.kurumidisciples.javadex.internal.factories.entities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.entities.Author;
import dev.kurumidisciples.javadex.internal.entities.AuthorImpl;

public class AuthorFactory {

    public static Author createEntity(@NotNull JsonObject jsonObject){
        
        JsonObject data = jsonObject.getAsJsonObject("data");
        UUID id = UUID.fromString(data.get("id").getAsString());
        JsonObject attributes = data.getAsJsonObject("attributes");
        OffsetDateTime createdAt = OffsetDateTime.parse(attributes.get("createdAt").getAsString());
        OffsetDateTime updatedAt = OffsetDateTime.parse(attributes.get("updatedAt").getAsString());
        String name = attributes.get("name").getAsString();
        URL imageUrl = createURL(!attributes.get("imageUrl").isJsonNull() ? attributes.get("imageUrl").getAsString() : null);
        String twitter = attributes.has("twitter") && !attributes.get("twitter").isJsonNull() ? attributes.get("twitter").getAsString() : null;
        String pixiv = attributes.has("pixiv") ? attributes.get("pixiv").getAsString() : null;
        String melonBox = attributes.has("melonBox") ? attributes.get("melonBox").getAsString() : null;
        String fanbox = attributes.has("fanbox") ? attributes.get("fanbox").getAsString() : null;
        String nicoVideo = attributes.has("nicoVideo") ? attributes.get("nicoVideo").getAsString() : null;
        String booth = attributes.has("booth") ? attributes.get("booth").getAsString() : null;
        String skeb = attributes.has("skeb") ? attributes.get("skeb").getAsString() : null;
        String fantia = attributes.has("fantia") ? attributes.get("fantia").getAsString() : null;
        String tumblr = attributes.has("tumblr") ? attributes.get("tumblr").getAsString() : null;
        String youtube = attributes.has("youtube") ? attributes.get("youtube").getAsString() : null;
        String weibo = attributes.has("weibo") ? attributes.get("weibo").getAsString() : null;
        String naver = attributes.has("naver") ? attributes.get("naver").getAsString() : null;
        URL website = createURL(attributes.has("website") ? attributes.get("website").getAsString() : null);
        int version = attributes.get("version").getAsInt();
        
        return new AuthorImpl(id, createdAt, updatedAt, name, imageUrl, twitter, pixiv, melonBox, fanbox, nicoVideo, booth, skeb, fantia, tumblr, youtube, weibo, naver, website, version);

    }

    private static URL createURL(String urlStr) {
        try {
            return new URL(urlStr);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
