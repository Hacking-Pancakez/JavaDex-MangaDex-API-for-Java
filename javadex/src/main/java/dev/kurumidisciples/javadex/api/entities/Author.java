package dev.kurumidisciples.javadex.api.entities;

import java.net.URL;

import dev.kurumidisciples.javadex.api.entities.intermediate.ISnowflake;

public interface Author extends ISnowflake {
    /**
     * The name of the author.
     */
    String getName();
    /**
     * The unique identifier of the author in {@link java.lang.String} form.
     */
    String getIdRaw();
    /**
     * The URL of the author's profile image. Is typically {@code null} because MangaDex does not provide a way to set an author's image.
     */
    default URL getImageUrl(){
        return null;
    }
    /**
     * The author's Twitter url.
     */
    String getTwitter();
    /**
     * The author's Pixiv url.
     */
    String getPixiv();
    /**
     * The author's MelonBox url.
     */
    String getMelonBox();
    /**
     * The author's Fanbox url.
     */
    String getFanbox();
    /**
     * The author's NicoVideo url.
     */
    String getNicoVideo();
    /**
     * The author's Tumblr url.
     */
    String getTumblr();
    /**
     * The author's Booth url.
     */
    String getBooth();
    /**
     * The author's Youtube url.
     */
    String getYoutube();
    /**
     * The author's Fantia url.
     */
    String getFantia();
    /**
     * The author's Weibo url.
     */
    String getWeibo();
    /**
     * The author's Naver url.
     */
    String getNaver();
    /**
     * The author's personal website.
     */
    URL getWebsite();
    
}
