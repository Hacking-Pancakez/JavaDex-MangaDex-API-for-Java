package dev.kurumidisciples.javadex.api.entities.enums.manga;

import dev.kurumidisciples.javadex.api.entities.content.Manga;

/**
 * Represents the type of link that is being used in {@link Manga}'s attributes.
 * @see <a href="https://api.mangadex.org/docs/3-enumerations/#manga-links-data">MangaDex API - Manga Links Static Data</a>
 * @since 0.1.3
 */
public enum LinkType {
    
    /** Represents a link to <a href="https://anilist.co/manga/{id}">AniList</a>; stored as id. */
    ANILIST("al"),
    /** Represents a link to <a href="https://www.anime-planet.com/manga/{slug}">Anime Planet</a>; stored as slug. */
    ANIME_PLANET("ap"),
    /** Represents a link to <a href="https://www.mangaupdates.com/series.html?id={id}">Manga Updates</a>; stored as id. */
    MANGA_UPDATES("mu"),
    /** Represents a link to <a href="https://bookwalker.jp/{slug}">BOOK☆WALKER</a>; stored as slug.*/
    BOOKWALKER("bw"),
    /** Represents a link to <a href="https://www.novelupdates.com/series/{slug}">MangaDex</a>; stored as slug. */
    NOVEL_UPDATES("nu"),
    /** Represents a link to <a href="https://kitsu.io/api/edge/manga/{id}">Kitsu (INTEGER)</a> or <a href="https://kitsu.io/api/edge/manga?filter[slug]={slug}">Kitsu (SLUG)</a>. If integer, use id version of the URL, otherwise use slug one. */
    KITSU("kT"),
    /** Represents a link to <a href="https://www.amazon.co.jp/">Amazon JP</a>; stored as full url. */
    AMAZON("amz"),
    /** Represents a link to <a href="https://www.ebookjapan.yahoo.co.jp">eBook Japan</a>; stored as full url. */
    EBOOK_JAPAN("ebj"),
    /** Represents a link to <a href="https://myanimelist.net/manga/{id}">MyAnimeList</a>; stored as id. */
    MY_ANIME_LIST("mal"),
    /** Represents a link to <a href="https://www.cdjapan.co.jp/">CDJapan</a>; stored as full url.*/
    CDJAPAN("cdj"),
    /** Stored as full URL, untranslated content URL (original language) */
    RAW("raw"),
    /** Stored as full URL, official english licenced URL */
    ENGTL("engtl"),
    /**Represents a link that has yet to be implemented in the wrapper. <b>Check response from API if parsed.</b>*/
    UNKNOWN("unknown");


    private final String type;

    LinkType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static LinkType getByType(String type) {
        for (LinkType linkType : LinkType.values()) {
            if (linkType.getType().equals(type)) {
                return linkType;
            }
        }
        return UNKNOWN;
    }
}
