package dev.kurumidisciples.javadex.internal.actions.create;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.core.authentication.Token;
import dev.kurumidisciples.javadex.api.entities.Author;
import dev.kurumidisciples.javadex.api.entities.Manga;
import dev.kurumidisciples.javadex.api.entities.enums.IncludesType;
import dev.kurumidisciples.javadex.api.entities.enums.Locale;
import dev.kurumidisciples.javadex.api.entities.enums.manga.LinkType;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.ContentRating;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Demographic;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Status;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Tag;
import dev.kurumidisciples.javadex.api.exceptions.MangaCreationException;
import dev.kurumidisciples.javadex.internal.actions.Action;
import dev.kurumidisciples.javadex.internal.annotations.MustNotBeUnknown;
import dev.kurumidisciples.javadex.internal.factories.entities.MangaFactory;
import dev.kurumidisciples.javadex.internal.http.HTTPRequest;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 
 * <p>This class handles the creation of a new Manga entity using the MangaDex API. It collects all necessary information
 * such as titles, descriptions, authors, artists, and other attributes required for manga creation. It then submits the
 * request to the API and returns the created Manga object if successful.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * MangaCreation mangaCreation = javadex.createManga(Locale.ENGLISH, "My Manga Title");
 * mangaCreation.setYear(2021)
 *              .setOriginalLanguage(Locale.JAPANESE)
 *              .setContentRating(ContentRating.SAFE)
 *              .setDemographic(Demographic.SHOUNEN)
 *              .setStatus(Status.ONGOING)
 *              .addAuthor("author_id")
 *              .addArtist("artist_id")
 *              .addTag(Tag.SUPERNATURAL)
 *              .submit()
 *              .thenAccept(manga -> {
 *                  // Handle the created manga object
 *              });
 * }</pre>
 * 
 * <p><b>From MangaDex's API documentation:</b></p>
 * <blockquote>
 * Similar to how the Chapter Upload works, after a Mangas creation with the Manga Create endpoint it is in a "draft" state, needs to be submitted (committed) and get either approved or rejected by Staff.
 * The purpose of this is to force at least one CoverArt uploaded in the original language for the Manga Draft and to discourage troll entries. You can use the list-drafts endpoint to investigate the status of your submitted Manga Drafts. Rejected Drafts are occasionally cleaned up at an irregular interval. You can edit Drafts at any time, even after they have been submitted. 
 * Because a Manga that is in the draft state is not available through the search, there are slightly different endpoints to list or retrieve Manga Drafts, but outside from that the schema is identical to a Manga that is published.
 * </blockquote>
 * 
 * @author Hacking Pancakez
 * @since 0.1.5.0.BETA.1
 * @warn This class is not complete and may break unexpectedly during use.
 */
public class MangaCreation extends Action<Manga> {

    private static final String MANGA_CREATION_URL = "https://api.mangadex.org/manga";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private static final Logger logger = LogManager.getLogger(MangaCreation.class);

    private Map<Locale, List<String>> titles = new HashMap<>();
    private Map<Locale, List<String>> altTitles = new HashMap<>();
    private Map<Locale, String> descriptions = new HashMap<>();
    private List<String> authors = List.of();
    private List<String> artists = List.of();
    private Map<LinkType, String> links = new HashMap<>();
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
     * Constructs a MangaCreation instance with a single title.
     *
     * @param auth   The authorization token required for the API call.
     * @param locale The locale of the manga title.
     * @param title  The title of the manga.
     */
    public MangaCreation(Token auth, @MustNotBeUnknown Locale locale, String title) {
        this.titles.put(locale, List.of(title));
        this.authorization = auth;
    }

    /**
     * Constructs a MangaCreation instance with multiple titles.
     *
     * @param auth   The authorization token required for the API call.
     * @param titles A map of locales and their corresponding titles.
     */
    public MangaCreation(Token auth, Map<Locale, List<String>> titles) {
        this.titles = titles;
        this.authorization = auth;
    }

    /**
     * Adds an alternative title for the manga.
     *
     * @param locale The locale of the alternative title.
     * @param title  The alternative title.
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
     * Sets alternative titles for the manga.
     *
     * @param altTitles A map of locales and their corresponding alternative titles.
     * @return MangaCreation
     */
    public MangaCreation setAltTitles(Map<@MustNotBeUnknown Locale, List<String>> altTitles) {
        this.altTitles = altTitles;
        return this;
    }

    /**
     * Adds a description for the manga.
     *
     * @param locale      The locale of the description.
     * @param description The description.
     * @return MangaCreation
     */
    public MangaCreation addDescription(@MustNotBeUnknown Locale locale, String description) {
        this.descriptions.put(locale, description);
        return this;
    }

    /**
     * Sets descriptions for the manga.
     *
     * @param descriptions A map of locales and their corresponding descriptions.
     * @return MangaCreation
     */
    public MangaCreation setDescriptions(Map<@MustNotBeUnknown Locale, String> descriptions) {
        this.descriptions = descriptions;
        return this;
    }

    /**
     * Adds an author to the manga.
     *
     * @param author The ID of the author.
     * @return MangaCreation
     */
    public MangaCreation addAuthor(String author) {
        this.authors.add(author);
        return this;
    }

    /**
     * Sets authors for the manga.
     *
     * @param authors A list of author IDs.
     * @return MangaCreation
     */
    public MangaCreation setAuthors(List<String> authors) {
        this.authors = authors;
        return this;
    }

    /**
     * Adds an author to the manga by UUID.
     *
     * @param authorId The UUID of the author.
     * @return MangaCreation
     */
    public MangaCreation addAuthor(UUID authorId) {
        return addAuthor(authorId.toString());
    }

    /**
     * Adds an author to the manga by Author object.
     *
     * @param author The Author object.
     * @return MangaCreation
     */
    public MangaCreation addAuthor(Author author) {
        return addAuthor(author.getId());
    }

    /**
     * Adds an artist to the manga.
     *
     * @param artist The ID of the artist.
     * @return MangaCreation
     */
    public MangaCreation addArtist(String artist) {
        this.artists.add(artist);
        return this;
    }

    /**
     * Sets artists for the manga.
     *
     * @param artists A list of artist IDs.
     * @return MangaCreation
     */
    public MangaCreation setArtists(List<String> artists) {
        this.artists = artists;
        return this;
    }

    /**
     * Adds an artist to the manga by UUID.
     *
     * @param artistId The UUID of the artist.
     * @return MangaCreation
     */
    public MangaCreation addArtist(UUID artistId) {
        return addArtist(artistId.toString());
    }

    /**
     * Adds an artist to the manga by Author object.
     *
     * @param artist The Author object.
     * @return MangaCreation
     */
    public MangaCreation addArtist(Author artist) {
        return addArtist(artist.getId());
    }

    /**
     * Adds a link to the manga.
     *
     * @param type The type of the link.
     * @param link The URL of the link.
     * @return MangaCreation
     */
    public MangaCreation addLink(@MustNotBeUnknown LinkType type, @NotNull String link) {
        this.links.put(type, link);
        return this;
    }

    /**
     * Sets links for the manga.
     *
     * @param links A map of link types and their corresponding URLs.
     * @return MangaCreation
     */
    public MangaCreation setLinks(Map<@MustNotBeUnknown LinkType, String> links) {
        this.links = links;
        return this;
    }

    /**
     * Adds a tag to the manga.
     *
     * @param tag The tag to be added.
     * @return MangaCreation
     */
    public MangaCreation addTag(Tag tag) {
        this.tags.add(tag);
        return this;
    }

    /**
     * Sets tags for the manga.
     *
     * @param tags A list of tags.
     * @return MangaCreation
     */
    public MangaCreation setTags(List<Tag> tags) {
        this.tags = tags;
        return this;
    }

    /**
     * Sets the year for the manga.
     *
     * @param year The year of publication.
     * @return MangaCreation
     */
    public MangaCreation setYear(int year) {
        this.year = year;
        return this;
    }

    /**
     * Sets the original language of the manga.
     *
     * @param originalLanguage The original language.
     * @return MangaCreation
     */
    public MangaCreation setOriginalLanguage(@MustNotBeUnknown Locale originalLanguage) {
        this.originalLanguage = originalLanguage;
        return this;
    }

    /**
     * Sets the content rating for the manga.
     *
     * @param contentRating The content rating.
     * @return MangaCreation
     */
    public MangaCreation setContentRating(ContentRating contentRating) {
        this.contentRating = contentRating;
        return this;
    }

    /**
     * Sets the last volume number for the manga.
     *
     * @param lastVolume The last volume number.
     * @return MangaCreation
     */
    public MangaCreation setLastVolume(Number lastVolume) {
        this.lastVolume = lastVolume;
        return this;
    }

    /**
     * Sets the last chapter number for the manga.
     *
     * @param lastChapter The last chapter number.
     * @return MangaCreation
     */
    public MangaCreation setLastChapter(Number lastChapter) {
        this.lastChapter = lastChapter;
        return this;
    }

    /**
     * Sets the demographic for the manga.
     *
     * @param demographic The publication demographic.
     * @return MangaCreation
     */
    public MangaCreation setDemographic(Demographic demographic) {
        this.demographic = demographic;
        return this;
    }

    /**
     * Sets the status for the manga.
     *
     * @param status The status of the manga.
     * @return MangaCreation
     */
    public MangaCreation setStatus(Status status) {
        this.status = status;
        return this;
    }

    /**
     * Submits the manga creation request asynchronously.
     *
     * @return A CompletableFuture containing the created Manga object.
     */
    @Override
    public CompletableFuture<Manga> submit() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return complete();
            } catch (MangaCreationException e) {
                logger.error("Failed to create manga.", e);
                return null;
            }
        });
    }

    /**
     * Completes the manga creation request and returns the created Manga object.
     *
     * @return The created Manga object.
     * @throws MangaCreationException if the manga creation fails.
     */
    @Override
    public Manga complete() throws MangaCreationException {
        Gson gson = new Gson();
        validate();
        RequestBody body = RequestBody.create(toJson().toString(), JSON_MEDIA_TYPE);

        try {
            Response response = HTTPRequest.postResponse(MANGA_CREATION_URL, toJson().toString(), Optional.of("Bearer " + authorization.getAccessToken()));
            if (response.isSuccessful()) {
                JsonObject responseData = gson.fromJson(response.body().string(), JsonObject.class);
                return MangaFactory.createEntity(responseData);
            } else {
                logger.error("Failed to create manga. Response: " + response.body().string());
                throw new MangaCreationException("Failed to create manga.", new Exception(response.body().string()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MangaCreationException("Failed to create manga.", e);
        }
    }

    /**
     * Validates the fields before making the request.
     *
     * @throws IllegalArgumentException if any required field is missing or invalid.
     */
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

    /**
     * Converts the MangaCreation object to a JSON representation.
     *
     * @return A JsonObject representing the MangaCreation object.
     */
    private JsonObject toJson() {
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        if (titles != null) {
            json.add("title", gson.toJsonTree(titles));
        }
        if (altTitles != null) {
            json.add("altTitles", gson.toJsonTree(altTitles));
        }
        if (descriptions != null) {
            json.add("description", gson.toJsonTree(descriptions));
        }
        if (authors != null) {
            json.add("authors", gson.toJsonTree(authors));
        }
        if (artists != null) {
            json.add("artists", gson.toJsonTree(artists));
        }
        if (links != null) {
            json.add("links", gson.toJsonTree(links));
        }
        if (tags != null) {
            JsonArray tagsArray = new JsonArray();
            for (Tag tag : tags) {
                tagsArray.add(tag.getId().toString());
            }
            json.add("tags", tagsArray);
        }
        json.addProperty("year", year);
        if (originalLanguage != null) {
            json.addProperty("originalLanguage", originalLanguage.getLanguage());
        }
        if (contentRating != null) {
            json.addProperty("contentRating", contentRating.toString());
        }
        if (lastVolume != null) {
            json.addProperty("lastVolume", lastVolume.toString());
        }
        if (lastChapter != null) {
            json.addProperty("lastChapter", lastChapter.toString());
        }
        if (demographic != null) {
            json.addProperty("demographic", demographic.toString());
        }
        if (status != null) {
            json.addProperty("status", status.toString());
        }
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
