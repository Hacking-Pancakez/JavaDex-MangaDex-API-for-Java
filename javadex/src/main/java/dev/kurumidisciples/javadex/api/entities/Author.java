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
 *
 * @see In the MangaDex API, an author and an artist are one and the same. There are no separate entities for authors and artists.
 * @author Hacking Pancakez
 * @version $Id: $Id
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

    /**
     * <p>Constructor for Author.</p>
     *
     * @param jsonObject a {@link com.google.gson.JsonObject} object
     */
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

    /** {@inheritDoc} */
    @Override
    public UUID getId() {
        return id;
    }

    /** {@inheritDoc} */
    @Override
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    /** {@inheritDoc} */
    @Override
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * <p>getIdRaw.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getIdRaw() {
        return id.toString();
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Getter for the field <code>imageUrl</code>.</p>
     *
     * @return a {@link java.net.URL} object
     */
    public URL getImageUrl() {
        return imageUrl;
    }

    /**
     * <p>Getter for the field <code>twitter</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getTwitter() {
        return twitter;
    }

    /**
     * <p>Getter for the field <code>pixiv</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getPixiv() {
        return pixiv;
    }

    /**
     * <p>Getter for the field <code>melonBox</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getMelonBox() {
        return melonBox;
    }

    /**
     * <p>Getter for the field <code>fanbox</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getFanbox() {
        return fanbox;
    }

    /**
     * <p>Getter for the field <code>nicoVideo</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getNicoVideo() {
        return nicoVideo;
    }

    /**
     * <p>Getter for the field <code>booth</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getBooth() {
        return booth;
    }

    /**
     * <p>Getter for the field <code>skeb</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getSkeb() {
        return skeb;
    }

    /**
     * <p>Getter for the field <code>fantia</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getFantia() {
        return fantia;
    }

    /**
     * <p>Getter for the field <code>tumblr</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getTumblr() {
        return tumblr;
    }

    /**
     * <p>Getter for the field <code>youtube</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getYoutube() {
        return youtube;
    }

    /**
     * <p>Getter for the field <code>weibo</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getWeibo() {
        return weibo;
    }

    /**
     * <p>Getter for the field <code>naver</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getNaver() {
        return naver;
    }

    /**
     * <p>Getter for the field <code>website</code>.</p>
     *
     * @return a {@link java.net.URL} object
     */
    public URL getWebsite() {
        return website;
    }

    /**
     * <p>Getter for the field <code>version</code>.</p>
     *
     * @return a int
     */
    public int getVersion() {
        return version;
    }
}
