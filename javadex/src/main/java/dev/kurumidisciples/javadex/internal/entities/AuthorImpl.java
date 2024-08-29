package dev.kurumidisciples.javadex.internal.entities;

import java.net.URL;
import java.time.OffsetDateTime;
import java.util.UUID;

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
    public Integer getVersion() {
        return version;
    }


}
