package dev.kurumidisciples.javadex.api.entities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.entities.intermediate.Entity;
import dev.kurumidisciples.javadex.api.entities.intermediate.ISnowflake;

// TODO CreateUrl should be apart of its own utility class, add biography field

/**
 * Represents an author and artist of a manga.
 * @apiNote In the MangaDex API, an author and an artist are one and the same. There are no separate entities for authors and artists.
 */
public class Author extends Entity implements ISnowflake {

    private final UUID id;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;
    private final String name;
    private final URL imageUrl;
    private final String twitter;
    private final String pixiv;
    private final String melonBox;
    private final String fanbox;
    private final String nicoVideo;
    private final String booth;
    private final String skeb;
    private final String fantia;
    private final String tumblr;
    private final String youtube;
    private final String weibo;
    private final String naver;
    private final URL website;
    private final int version;

    public Author(JsonObject jsonObject) {
        JsonObject data = jsonObject.getAsJsonObject("data");
        this.id = UUID.fromString(data.get("id").getAsString());
        JsonObject attributes = data.getAsJsonObject("attributes");
        this.createdAt = OffsetDateTime.parse(attributes.get("createdAt").getAsString());
        this.updatedAt = OffsetDateTime.parse(attributes.get("updatedAt").getAsString());
        this.name = attributes.get("name").getAsString();
        this.imageUrl = createURL(!attributes.get("imageUrl").isJsonNull() ? attributes.get("imageUrl").getAsString() : null);
        this.twitter = attributes.has("twitter") && !attributes.get("twitter").isJsonNull() ? attributes.get("twitter").getAsString() : null;
        this.pixiv = attributes.has("pixiv") ? attributes.get("pixiv").getAsString() : null;
        this.melonBox = attributes.has("melonBox") ? attributes.get("melonBox").getAsString() : null;
        this.fanbox = attributes.has("fanbox") ? attributes.get("fanbox").getAsString() : null;
        this.nicoVideo = attributes.has("nicoVideo") ? attributes.get("nicoVideo").getAsString() : null;
        this.booth = attributes.has("booth") ? attributes.get("booth").getAsString() : null;
        this.skeb = attributes.has("skeb") ? attributes.get("skeb").getAsString() : null;
        this.fantia = attributes.has("fantia") ? attributes.get("fantia").getAsString() : null;
        this.tumblr = attributes.has("tumblr") ? attributes.get("tumblr").getAsString() : null;
        this.youtube = attributes.has("youtube") ? attributes.get("youtube").getAsString() : null;
        this.weibo = attributes.has("weibo") ? attributes.get("weibo").getAsString() : null;
        this.naver = attributes.has("naver") ? attributes.get("naver").getAsString() : null;
        this.website = createURL(attributes.has("website") ? attributes.get("website").getAsString() : null);
        this.version = attributes.get("version").getAsInt();
    }

    private URL createURL(String urlStr) {
        try {
            return new URL(urlStr);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getIdRaw() {
        return id.toString();
    }

    public String getName() {
        return name;
    }

    public URL getImageUrl() {
        return imageUrl;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getPixiv() {
        return pixiv;
    }

    public String getMelonBox() {
        return melonBox;
    }

    public String getFanbox() {
        return fanbox;
    }

    public String getNicoVideo() {
        return nicoVideo;
    }

    public String getBooth() {
        return booth;
    }

    public String getSkeb() {
        return skeb;
    }

    public String getFantia() {
        return fantia;
    }

    public String getTumblr() {
        return tumblr;
    }

    public String getYoutube() {
        return youtube;
    }

    public String getWeibo() {
        return weibo;
    }

    public String getNaver() {
        return naver;
    }

    public URL getWebsite() {
        return website;
    }

    public int getVersion() {
        return version;
    }
}
