package dev.kurumidisciples.javadex.internal.actions.create;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.google.errorprone.annotations.DoNotCall;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.core.authentication.Token;
import dev.kurumidisciples.javadex.api.entities.Author;
import dev.kurumidisciples.javadex.api.entities.content.Manga;
import dev.kurumidisciples.javadex.api.entities.enums.IncludesType;
import dev.kurumidisciples.javadex.api.entities.enums.Locale;
import dev.kurumidisciples.javadex.api.entities.enums.manga.LinkType;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.ContentRating;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Demographic;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Status;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Tag;
import dev.kurumidisciples.javadex.api.exceptions.MangaCreationException;
import dev.kurumidisciples.javadex.internal.actions.Action;
import dev.kurumidisciples.javadex.api.entities.enums.State;
import dev.kurumidisciples.javadex.internal.annotations.MustNotBeUnknown;
import dev.kurumidisciples.javadex.internal.http.HTTPRequest;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * <p>MangaCreation class.</p>
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public class MangaCreation extends Action<Manga> {

    private static final String MANGA_CREATION_URL = "https://api.mangadex.org/manga";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private static final Logger logger = LogManager.getLogger(MangaCreation.class);

    private Map<Locale, List<String>> titles;
    private Map<Locale, List<String>> altTitles;
    private Map<Locale, String> descriptions;
    private List<String> authors;
    private List<String> artists;
    private Map<LinkType, String> links;
    private List<Tag> tags;
    private int year;
    private Locale originalLanguage;
    private ContentRating contentRating;
    private Number lastVolume;
    private Number lastChapter;
    private Demographic demographic;
    private Status status;
    private final int version = 1;

    private Token authorization;

    /**
     * <p>Constructor for MangaCreation.</p>
     *
     * @param locale a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @param title a {@link java.lang.String} object
     */
    public MangaCreation(Token auth, @MustNotBeUnknown Locale locale, String title) {
        this.titles.put(locale, List.of(title));
        this.authorization = auth;
    }


    /**
     * <p>Constructor for MangaCreation.</p>
     *
     * @param titles a {@link java.util.Map} object
     */
    public MangaCreation(Token auth, Map<Locale, List<String>> titles) {
        this.titles = titles;
        this.authorization = auth;
    }

    
    /**
     * <p>addAltTitle.</p>
     *
     * @param locale a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @param title a {@link java.lang.String} object
     * @return MangaCreation
     */
    public MangaCreation addAltTitle(@MustNotBeUnknown Locale locale, String title) {
        if (this.altTitles.containsKey(locale)) {
            this.altTitles.get(locale).add(title);
        } else {
            this.altTitles.put(locale, List.of(title));
        }
        return this;
    }

    
    /**
     * <p>Setter for the field <code>altTitles</code>.</p>
     *
     * @param altTitles a {@link java.util.Map} object
     * @return MangaCreation
     */
    public MangaCreation setAltTitles(Map<@MustNotBeUnknown Locale, List<String>> altTitles) {
        this.altTitles = altTitles;
        return this;
    }

    
    /**
     * <p>addDescription.</p>
     *
     * @param locale a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @param description a {@link java.lang.String} object
     * @return MangaCreation
     */
    public MangaCreation addDescription(@MustNotBeUnknown Locale locale, String description) {
        this.descriptions.put(locale, description);
        return this;
    }

    
    /**
     * <p>Setter for the field <code>descriptions</code>.</p>
     *
     * @param descriptions a {@link java.util.Map} object
     * @return MangaCreation
     */
    public MangaCreation setDescriptions(Map<@MustNotBeUnknown Locale, String> descriptions) {
        this.descriptions = descriptions;
        return this;
    }

    /**
     * <p>addAuthor.</p>
     *
     * @param author a {@link java.lang.String} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation addAuthor(String author) {
        this.authors.add(author);
        return this;
    }

    /**
     * <p>Setter for the field <code>authors</code>.</p>
     *
     * @param authors a {@link java.util.List} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation setAuthors(List<String> authors) {
        this.authors = authors;
        return this;
    }

    /**
     * <p>addAuthor.</p>
     *
     * @param authorId a {@link java.util.UUID} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation addAuthor(UUID authorId) {
       return addAuthor(authorId.toString());
    }

    /**
     * <p>addAuthor.</p>
     *
     * @param author a {@link dev.kurumidisciples.javadex.api.entities.Author} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation addAuthor(Author author) {
        return addAuthor(author.getId());
    }


    /**
     * <p>addArtist.</p>
     *
     * @param artist a {@link java.lang.String} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation addArtist(String artist) {
        this.artists.add(artist);
        return this;
    }

    /**
     * <p>Setter for the field <code>artists</code>.</p>
     *
     * @param artists a {@link java.util.List} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation setArtists(List<String> artists) {
        this.artists = artists;
        return this;
    }

    /**
     * <p>addArtist.</p>
     *
     * @param artistId a {@link java.util.UUID} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation addArtist(UUID artistId) {
        return addArtist(artistId.toString());
    }

    /**
     * <p>addArtist.</p>
     *
     * @param artist a {@link dev.kurumidisciples.javadex.api.entities.Author} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation addArtist(Author artist) {
        return addArtist(artist.getId());
    }

    /**
     * <p>addLink.</p>
     *
     * @param type a {@link dev.kurumidisciples.javadex.api.entities.enums.manga.LinkType} object
     * @param link a {@link java.lang.String} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation addLink(@MustNotBeUnknown LinkType type, @NotNull String link) {
        this.links.put(type, link);
        return this;
    }

    /**
     * <p>Setter for the field <code>links</code>.</p>
     *
     * @param links a {@link java.util.Map} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation setLinks(Map<@MustNotBeUnknown LinkType, String> links) {
        this.links = links;
        return this;
    }

    /**
     * <p>addTag.</p>
     *
     * @param tag a {@link dev.kurumidisciples.javadex.api.entities.content.Manga.Tag} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation addTag(Tag tag) {
        this.tags.add(tag);
        return this;
    }

    /**
     * <p>Setter for the field <code>tags</code>.</p>
     *
     * @param tags a {@link java.util.List} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation setTags(List<Tag> tags) {
        this.tags = tags;
        return this;
    }

    /**
     * <p>Setter for the field <code>year</code>.</p>
     *
     * @param year a int
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation setYear(int year) {
        this.year = year;
        return this;
    }

    /**
     * <p>Setter for the field <code>originalLanguage</code>.</p>
     *
     * @param originalLanguage a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation setOriginalLanguage(@MustNotBeUnknown Locale originalLanguage) {
        this.originalLanguage = originalLanguage;
        return this;
    }

    /**
     * <p>Setter for the field <code>contentRating</code>.</p>
     *
     * @param contentRating a {@link dev.kurumidisciples.javadex.api.entities.enums.manga.filters.ContentRating} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation setContentRating(ContentRating contentRating) {
        this.contentRating = contentRating;
        return this;
    }

    /**
     * <p>Setter for the field <code>lastVolume</code>.</p>
     *
     * @param lastVolume a {@link java.lang.Number} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation setLastVolume(Number lastVolume) {
        this.lastVolume = lastVolume;
        return this;
    }

    /**
     * <p>Setter for the field <code>lastChapter</code>.</p>
     *
     * @param lastChapter a {@link java.lang.Number} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation setLastChapter(Number lastChapter) {
        this.lastChapter = lastChapter;
        return this;
    }

    /**
     * <p>Setter for the field <code>demographic</code>.</p>
     *
     * @param demographic a {@link dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Demographic} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation setDemographic(Demographic demographic) {
        this.demographic = demographic;
        return this;
    }

    /**
     * <p>Setter for the field <code>status</code>.</p>
     *
     * @param status a {@link dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Status} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.create.MangaCreation} object
     */
    public MangaCreation setStatus(Status status) {
        this.status = status;
        return this;
    }


    

    /** {@inheritDoc} 
     * 
     * @return will return a manga object in the {@link State.DRAFT} state if successful.
    */
    @Override
    public CompletableFuture<Manga> submit(){
        return CompletableFuture.supplyAsync(() -> {
            try {
                return complete();
            } catch (MangaCreationException e) {
                logger.error("Failed to create manga.", e);
                return null;
            }
        });
    }

    /** {@inheritDoc} 
     * 
     * @return will return a manga object in the {@link State.DRAFT} state if successful.
     * @throws MangaCreationException if the manga creation fails such as if the request fails or the user lacks permissions for such operation.
    */
    @Override
    public Manga complete() throws MangaCreationException{
        Gson gson = new Gson();
        validate();
        RequestBody body = RequestBody.create(toJson().toString(), JSON_MEDIA_TYPE);

        try {
            Response response = HTTPRequest.postResponse(MANGA_CREATION_URL, toJson().toString(), Optional.of("Bearer " + authorization.getAccessToken()));
            if (response.isSuccessful()) {
                JsonObject responseData = gson.fromJson(response.body().string(), JsonObject.class);
                Manga manga = new Manga(responseData);
                return manga;
            } else {
                logger.error("Failed to create manga. Response: " + response.body().string());
                throw new MangaCreationException("Failed to create manga.", new Exception(response.body().string()));
            }
        } catch (Exception e) {
            throw new MangaCreationException("Failed to create manga.", e);
        }
    }

    private void validate() {
        if (titles.isEmpty()) {
            throw new IllegalArgumentException("Titles cannot be empty.");
        }
        if (descriptions.isEmpty()) {
            throw new IllegalArgumentException("Descriptions cannot be empty.");
        }
        if (authors.isEmpty()) {
            throw new IllegalArgumentException("Authors cannot be empty.");
        }
        if (artists.isEmpty()) {
            throw new IllegalArgumentException("Artists cannot be empty.");
        }
        if (links.isEmpty()) {
            throw new IllegalArgumentException("Links cannot be empty.");
        }
        if (tags.isEmpty()) {
            throw new IllegalArgumentException("Tags cannot be empty.");
        }
        if (year == 0) {
            throw new IllegalArgumentException("Year cannot be 0.");
        }
        if (originalLanguage == null) {
            throw new IllegalArgumentException("Original language cannot be null.");
        }
        if (contentRating == null) {
            throw new IllegalArgumentException("Content rating cannot be null.");
        }
        if (lastVolume == null) {
            throw new IllegalArgumentException("Last volume cannot be null.");
        }
        if (lastChapter == null) {
            throw new IllegalArgumentException("Last chapter cannot be null.");
        }
        if (demographic == null) {
            throw new IllegalArgumentException("Demographic cannot be null.");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }
    }


    private JsonObject toJson(){
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        json.add("title", gson.toJsonTree(titles));
        json.add("altTitles", gson.toJsonTree(altTitles));
        json.add("description", gson.toJsonTree(descriptions));
        json.add("authors", gson.toJsonTree(authors));
        json.add("artists", gson.toJsonTree(artists));
        json.add("links", gson.toJsonTree(links));
        json.add("tags", gson.toJsonTree(tags));
        json.addProperty("year", year);
        json.addProperty("originalLanguage", originalLanguage.getLanguage());
        json.addProperty("contentRating", contentRating.toString());
        json.addProperty("lastVolume", lastVolume.toString());
        json.addProperty("lastChapter", lastChapter.toString());
        json.addProperty("demographic", demographic.toString());
        json.addProperty("status", status.toString());
        json.addProperty("version", version);
        return json;
    }










    /** {@inheritDoc} */
    @Override
    public MangaCreation setLimit(Integer limit) {
        throw new UnsupportedOperationException("This action does not support setting a limit.");
    }

    /** {@inheritDoc} */
    @Override
    public MangaCreation setOffset(Integer offset) {
        throw new UnsupportedOperationException("This action does not support setting an offset.");
    }

    /** {@inheritDoc} */
    @Override
    public MangaCreation setIncludes(IncludesType... includes) {
        throw new UnsupportedOperationException("This action does not support setting includes.");
    }



}
