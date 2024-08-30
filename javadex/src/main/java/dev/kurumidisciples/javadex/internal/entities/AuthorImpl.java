package dev.kurumidisciples.javadex.internal.entities;

import java.net.URL;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.apache.maven.api.annotations.Nullable;

import dev.kurumidisciples.javadex.api.entities.Author;
import dev.kurumidisciples.javadex.api.entities.intermediate.Entity;

public class AuthorImpl extends Entity implements Author {

    
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

    public AuthorImpl(UUID id, OffsetDateTime createdAt, OffsetDateTime updatedAt, String name, URL imageUrl, String twitter, String pixiv, String melonBox, String fanbox, String nicoVideo, String booth, String skeb, String fantia, String tumblr, String youtube, String weibo, String naver, URL website, int version) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.name = name;
        this.imageUrl = imageUrl;
        this.twitter = twitter;
        this.pixiv = pixiv;
        this.melonBox = melonBox;
        this.fanbox = fanbox;
        this.nicoVideo = nicoVideo;
        this.booth = booth;
        this.skeb = skeb;
        this.fantia = fantia;
        this.tumblr = tumblr;
        this.youtube = youtube;
        this.weibo = weibo;
        this.naver = naver;
        this.website = website;
        this.version = version;
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
     * Get the raw UUID of the author
     *
     * @return a {@link java.lang.String} object
     */
    public String getIdRaw() {
        return id.toString();
    }

    /**
     * Returns the name of the author
     *
     * @return a {@link java.lang.String} object
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the URL of the author's image if available. Null if not available
     *
     * @return a {@link java.net.URL} object
     */
    @Nullable
    public URL getImageUrl() {
        return imageUrl;
    }

    /**
     * Returns the author's twitter.
     *
     * @return a {@link java.lang.String} object
     */
    public String getTwitter() {
        return twitter;
    }

    /**
     * Returns the author's pixiv.
     *
     * @return a {@link java.lang.String} object
     */
    public String getPixiv() {
        return pixiv;
    }

    /**
     * Returns the author's melonbox.
     *
     * @return a {@link java.lang.String} object
     */
    public String getMelonBox() {
        return melonBox;
    }

    /**
     * Returns the author's fanbox.
     *
     * @return a {@link java.lang.String} object
     */
    public String getFanbox() {
        return fanbox;
    }

    /**
     * Returns the author's nico video.
     *
     * @return a {@link java.lang.String} object
     */
    public String getNicoVideo() {
        return nicoVideo;
    }

    /**
     * Returns the author's booth.
     *
     * @return a {@link java.lang.String} object
     */
    public String getBooth() {
        return booth;
    }

    /**
     * Returns the author's skeb.
     *
     * @return a {@link java.lang.String} object
     */
    public String getSkeb() {
        return skeb;
    }

    /**
     * Returns the author's fantia.
     *
     * @return a {@link java.lang.String} object
     */
    public String getFantia() {
        return fantia;
    }

    /**
     * Returns the author's tumblr.
     *
     * @return a {@link java.lang.String} object
     */
    public String getTumblr() {
        return tumblr;
    }

    /**
     * Returns the author's youtube.
     *
     * @return a {@link java.lang.String} object
     */
    public String getYoutube() {
        return youtube;
    }

    /**
     * Returns the author's weibo.
     *
     * @return a {@link java.lang.String} object
     */
    public String getWeibo() {
        return weibo;
    }

    /**
     * Returns the author's naver.
     *
     * @return a {@link java.lang.String} object
     */
    public String getNaver() {
        return naver;
    }

    /**
     * Returns the author's website.
     *
     * @return a {@link java.net.URL} object
     */
    public URL getWebsite() {
        return website;
    }

    /**
     * {@inheritDoc}
     */
    public Integer getVersion() {
        return version;
    }


}
