package dev.kurumidisciples.javadex.api.entities.content;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.entities.Chapter;
import dev.kurumidisciples.javadex.api.entities.enums.*;
import dev.kurumidisciples.javadex.api.entities.enums.Locale;
import dev.kurumidisciples.javadex.api.entities.enums.manga.LinkType;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.ContentRating;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Demographic;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Status;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Tag;
import dev.kurumidisciples.javadex.api.entities.intermediate.Entity;
import dev.kurumidisciples.javadex.api.entities.intermediate.ISnowflake;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;
import dev.kurumidisciples.javadex.api.entities.relationship.enums.RelationshipType;
import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import dev.kurumidisciples.javadex.api.proxies.CoverProxy;
import dev.kurumidisciples.javadex.internal.annotations.MustNotBeUnknown;
import dev.kurumidisciples.javadex.internal.annotations.NotLessThanOne;
import dev.kurumidisciples.javadex.internal.annotations.Size;
import dev.kurumidisciples.javadex.internal.http.HTTPRequest;
import dev.kurumidisciples.javadex.internal.parsers.MangaParsers;

/**
 * Represents a Manga entity in the MangaDex API.
 * <p>Could represent a <b>draft version</b> of a manga, these are not yet published and are not visible to the public. They will not have any retrievable chapters and/or pages. Use the method {@code getState()} to check if it is a draft.</p>
 *
 * @see Can represent any readable content in MangaDex, encapsulating manga, manhwa, manhua, and doujinshi.
 * @see <a href="https://api.mangadex.org/docs/03-manga/creation/#creation">Creation</a>
 * @see <a href="https://api.mangadex.org/docs/03-manga/search/">Search</a>
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public class Manga extends Entity {

    


    private static final Logger logger = LogManager.getLogger(Manga.class);
    private static final Gson gson = new Gson();

    private final Map<LinkType, String> links;
    private final UUID id;
    private final List<Tag> tags;
    private final String title;
    private final UUID author;
    private final Map<Locale, String> description;
    private final Map<Locale, List<String>> altTitles;
    private final boolean isLocked;
    private final Locale originalLanguage;
    private final Number lastVolume;
    private final Number lastChapter;
    private final Demographic publicationDemographic;
    private final Status status;
    private final Long year;
    private final ContentRating contentRating;
    private final State state;
    private final boolean chapterNumbersResetOnNewVolume;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;
    private final long version;
    private final List<Locale> availableTranslatedLanguages;
    private final String latestUploadedChapterId;
    private final RelationshipMap relationshipMap;

    /**
     * <p>Constructor for Manga.</p>
     *
     * @param mangaJson a {@link com.google.gson.JsonObject} object
     */
    public Manga(@NotNull JsonObject mangaJson) {
        JsonObject attributes = mangaJson.getAsJsonObject("attributes");
        this.id = UUID.fromString(mangaJson.get("id").getAsString());
        this.title = attributes.getAsJsonObject("title").has("en") ? attributes.getAsJsonObject("title").get("en").getAsString() : "No title";

        this.description = MangaParsers.parseDescription(attributes.getAsJsonObject("description"));
        this.altTitles = MangaParsers.parseAltTitles(attributes.getAsJsonArray("altTitles"));
        this.isLocked = attributes.get("isLocked").getAsBoolean();
        this.originalLanguage = Locale.getByLanguage(attributes.get("originalLanguage").getAsString());
        this.lastVolume = attributes.has("lastVolume") && !attributes.get("lastVolume").isJsonNull() ? attributes.get("lastVolume").getAsNumber() : null;
        this.lastChapter = attributes.has("lastChapter") && !attributes.get("lastChapter").isJsonNull() ? attributes.get("lastChapter").getAsNumber() : null;
        this.publicationDemographic = Demographic.getDemographic(attributes.has("publicationDemographic") && !attributes.get("publicationDemographic").isJsonNull() ? attributes.get("publicationDemographic").getAsString() : "Unknown");
        this.status = Status.getStatus(attributes.get("status").getAsString());
        this.year = attributes.has("year") && !attributes.get("year").isJsonNull() ? attributes.get("year").getAsLong() : null;
        this.contentRating = ContentRating.getContentRating(attributes.get("contentRating").getAsString());
        this.state = State.getByValue(attributes.get("state").getAsString());
        this.links = MangaParsers.parseLinks(attributes.getAsJsonObject("links"));
        this.chapterNumbersResetOnNewVolume = attributes.get("chapterNumbersResetOnNewVolume").getAsBoolean();
        this.createdAt = OffsetDateTime.parse(attributes.get("createdAt").getAsString());
        this.updatedAt = OffsetDateTime.parse(attributes.get("updatedAt").getAsString());
        this.version = attributes.get("version").getAsLong();
        this.latestUploadedChapterId = attributes.has("latestUploadedChapter") && !attributes.get("latestUploadedChapter").isJsonNull() ? attributes.get("latestUploadedChapter").getAsString() : null;
        this.tags = MangaParsers.parseTags(attributes.getAsJsonArray("tags"));
        this.availableTranslatedLanguages = MangaParsers.parseAvailableTranslatedLanguages(attributes.getAsJsonArray("availableTranslatedLanguages"));
        this.relationshipMap = new RelationshipMap(mangaJson.getAsJsonArray("relationships"));
        this.author = relationshipMap.get(RelationshipType.AUTHOR).get(0).getId();
    }

    /**
     * <p>Getter for the field <code>relationshipMap</code>.</p>
     *
     * @return a {@link dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap} object
     */
    public RelationshipMap getRelationshipMap() {
        return relationshipMap;
    }

    /**
     * <p>doChapterNumbersResetOnNewVolume.</p>
     *
     * @return a boolean
     */
    public boolean doChapterNumbersResetOnNewVolume() {
        return chapterNumbersResetOnNewVolume;
    }

    /**
     * <p>The state property of the manga object.</p>
     *
     * @return a {@link State} object
     */
    public State getState() {
        return state;
    }

    /**
     * <p>Returns the avaliable languages the manga can be read in.</p>
     * @return a list of available languages
     */
    public List<Locale> getAvailableTranslatedLanguages() {
        return availableTranslatedLanguages;
    }

    /**
     * <p>retrieveCovers.</p>
     *
     * @param lang a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @param limit a int
     * @param offset a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<CoverProxy>> retrieveCovers(@NotNull @MustNotBeUnknown Locale lang, @Size(min=0, max=100) int limit,@Size(min=0) int offset) {
        if (state == State.DRAFT) throw new UnsupportedOperationException("Cannot retrieve covers for a draft manga.");
        return CompletableFuture.supplyAsync(() -> {
            try {
                JsonObject response = gson.fromJson(HTTPRequest.get("https://api.mangadex.org/cover?limit=" + limit + "&offset=" + offset + "&manga[]=" + getId() + "&locales[]=" + lang.getLanguage()), JsonObject.class);
                List<CoverProxy> covers = new ArrayList<>();
                JsonArray coverArray = response.getAsJsonArray("data");
                for (JsonElement coverElement : coverArray) {
                    covers.add(new CoverProxy(coverElement.getAsJsonObject()));
                }
                return covers;
            } catch (HTTPRequestException e) {
                logger.error("Could not retrieve all covers for manga " + id, e);
                throw new CompletionException(e);
            }
        });
    }

    /**
     * <p>retrieveCovers.</p>
     *
     * @param lang a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<CoverProxy>> retrieveCovers(Locale lang) {
        return retrieveCovers(lang, 100, 0);
    }

    /**
     * <p>retrieveCovers.</p>
     *
     * @param lang a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @param limit a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<CoverProxy>> retrieveCovers(Locale lang, int limit) {
        return retrieveCovers(lang, limit, 0);
    }

    /** {@inheritDoc} */
    @Override
    public UUID getId() {
        return id;
    }

    /**
     * <p>getIdRaw.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getIdRaw() {
        return id.toString();
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
     * The title of the manga found in the <code>title</code> property of the api response. 
     * <p><prev><code>
     * "title": {
     *           "en": "One Piece"
     *      },</code></prev></p>
     * <p>This is typically in the English language or in Japanese romanization.</p>
     *
     * @return a {@link String} the default title of the manga
     */
    public String getDefaultTitle() {
        return title;
    }

    /**
     * <p>Links to outside resources connected with the manga.</p>
     * <p>These could be links to the official website, the author's website, or the manga's page on other websites.</p>
     * @return
     * @warn Not all links have the full URL, some are just the IDs or SLUGs of the resource.
     */
    public Map<LinkType, String> getLinks() {
        return links;
    }

    /**
     * <p>Getter for the field <code>author</code>.</p>
     *
     * @return a {@link java.util.UUID} object
     */
    public UUID getAuthor() {
        return author;
    }

    /**
     * <p>Getter for the field <code>description</code>.</p>
     *
     * @param lang a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @return a {@link java.lang.String} object
     */
    public String getDescription(Locale lang) {
        return description.get(lang);
    }

    /**
     * <p>getDescriptions.</p>
     *
     * @return a {@link java.util.Map} object
     */
    public Map<Locale, String> getDescriptions() {
        return description;
    }

    /**
     * <p>All other possible titles for the manga.</p>
     * <p>These could be in different languages and have different translations of the title.</p>
     *
     * @return a {@link Map} object
     */
    public Map<Locale, List<String>> getAltTitles() {
        return altTitles;
    }

    /**
     * <p>Defines whether the manga can be uploaded to by anyone.</p>
     * <p>Manga are typically locked because it has been requested by the official publisher.</p>
     *
     * @return true if the manga is locked, false otherwise
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * <p>Getter for the field <code>originalLanguage</code>.</p>
     *
     * @return a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     */
    public Locale getOriginalLanguage() {
        return originalLanguage;
    }

    /**
     * <p>Getter for the field <code>lastVolume</code>.</p>
     *
     * @return a {@link java.lang.Number} object
     */
    public Number getLastVolume() {
        return lastVolume;
    }

    /**
     * <p>Getter for the field <code>lastChapter</code>.</p>
     *
     * @return a {@link java.lang.Number} object
     */
    public Number getLastChapter() {
        return lastChapter;
    }

    /**
     * <p>Getter for the field <code>contentRating</code>.</p>
     *
     * @return a {@link dev.kurumidisciples.javadex.api.entities.enums.manga.filters.ContentRating} object
     */
    public ContentRating getContentRating() {
        return contentRating;
    }

    /**
     * <p>Getter for the field <code>publicationDemographic</code>.</p>
     *
     * @return a {@link dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Demographic} object
     */
    public Demographic getPublicationDemographic() {
        return publicationDemographic;
    }

    /**
     * <p>Getter for the field <code>status</code>.</p>
     *
     * @return a {@link dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Status} object
     */
    public Status getStatus() {
        return status;
    }

    /**
     * <p>Getter for the field <code>year</code>.</p>
     *
     * @return a {@link java.lang.Long} object
     */
    public Long getYear() {
        return year;
    }

    /**
     * <p>Getter for the field <code>latestUploadedChapterId</code>.</p>
     *
     * @return a {@link java.util.UUID} object
     */
    public UUID getLatestUploadedChapterId() {
        return UUID.fromString(latestUploadedChapterId);
    }

    /**
     * <p>Getter for the field <code>tags</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * <p>retrieveFeed.</p>
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<Chapter>> retrieveFeed() {
        if (state == State.DRAFT) throw new UnsupportedOperationException("Cannot retrieve feed for a draft manga.");
        return CompletableFuture.supplyAsync(() -> {
            try {
                JsonObject response = gson.fromJson(HTTPRequest.get("https://api.mangadex.org/manga/" + getId() + "/feed"), JsonObject.class);
                if (isError(response)) {
                    throw new RuntimeException("Error retrieving manga feed: " + response.getAsJsonArray("errors").get(0).getAsJsonObject().get("detail").getAsString());
                }
                JsonArray chapters = response.getAsJsonArray("data");
                List<Chapter> chaptersList = new ArrayList<>();
                for (JsonElement chapter : chapters) {
                    chaptersList.add(new Chapter(chapter.getAsJsonObject()));
                }
                return chaptersList;
            } catch (HTTPRequestException e) {
                logger.error("Error retrieving feed", e);
                throw new CompletionException(e);
            }
        });
    }

    /**
     * <p>retrieveChapterCount.</p>
     *
     * @param lang a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<Integer> retrieveChapterCount(Locale lang) {
        if (state == State.DRAFT) throw new UnsupportedOperationException("Cannot retrieve chapter count for a draft manga.");
        return CompletableFuture.supplyAsync(() -> {
            try {
                JsonObject response = gson.fromJson(HTTPRequest.get("https://api.mangadex.org/manga/" + getId() + "/aggregate?translatedLanguage[]=" + lang.getLanguage()), JsonObject.class);
                List<UUID> chaptersList = parseIds(response);
                return chaptersList.size();
            } catch (HTTPRequestException e) {
                logger.error("Error retrieving chapter count", e);
                throw new CompletionException(e);
            }
        });
    }

    /**
     * <p>retrieveChaptersIds.</p>
     *
     * @param lang a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<UUID>> retrieveChaptersIds(Locale lang) {
        if (state == State.DRAFT) throw new UnsupportedOperationException("Cannot retrieve chapters for a draft manga.");
        return CompletableFuture.supplyAsync(() -> {
            try {
                JsonObject response = gson.fromJson(HTTPRequest.get("https://api.mangadex.org/manga/" + getId() + "/aggregate?translatedLanguage[]=" + lang.getLanguage()), JsonObject.class);
                List<UUID> chaptersList = parseIds(response);
                Collections.reverse(chaptersList);
                return chaptersList;
            } catch (HTTPRequestException e) {
                logger.error("Error retrieving chapter IDs", e);
                throw new CompletionException(e);
            }
        });
    }

    /**
     * <p>retrieveChaptersOrdered.</p>
     *
     * @param language a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<Chapter>> retrieveChaptersOrdered(@MustNotBeUnknown @NotNull Locale language) {
        if (state == State.DRAFT) throw new UnsupportedOperationException("Cannot retrieve chapters for a draft manga.");
        return retrieveChaptersIds(language).thenComposeAsync(ids -> {
            if (ids == null) return CompletableFuture.completedFuture(null);

            List<CompletableFuture<Chapter>> chapterFutures = ids.stream().map(id ->
                    CompletableFuture.supplyAsync(() -> {
                        try {
                            JsonObject response = gson.fromJson(HTTPRequest.get("https://api.mangadex.org/chapter/" + id), JsonObject.class);
                            return new Chapter(response.getAsJsonObject("data"));
                        } catch (HTTPRequestException e) {
                            logger.error("An error occurred while attempting to retrieve chapter " + id, e);
                            throw new CompletionException(e);
                        }
                    }, Executors.newCachedThreadPool())
            ).collect(Collectors.toList());

            return CompletableFuture.allOf(chapterFutures.toArray(new CompletableFuture[0]))
                    .thenApply(v -> chapterFutures.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList()));
        });
    }

    /**
     * <p>retrieveChapterByNumber.</p>
     *
     * @param lang a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     * @param number a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<Chapter>> retrieveChapterByNumber(@NotNull Locale lang, @NotLessThanOne int number) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Chapter> chapters = new ArrayList<>();
                String apiCall = "https://api.mangadex.org/chapter?limit=100&offset=0&manga=" + id + "&translatedLanguage[]=" + lang.getLanguage() + "&chapter=" + number;
                JsonObject response = gson.fromJson(HTTPRequest.get(apiCall), JsonObject.class);
                if (isError(response)) return null;
                JsonArray chapterArray = response.getAsJsonArray("data");
                if (chapterArray.size() == 0) logger.warn("No chapters found for manga " + id + " with number " + number + " in language " + lang.getLanguage() + ".");
                for (JsonElement chapterElement : chapterArray) {
                    chapters.add(new Chapter(chapterElement.getAsJsonObject()));
                }
                return chapters;
            } catch (HTTPRequestException e) {
                logger.error("Could not retrieve chapter " + number + " for manga " + id, e);
                throw new CompletionException(e);
            }
        });
    }

    /**
     * <p>retrieveCurrentCover.</p>
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<CoverProxy> retrieveCurrentCover() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String coverId = relationshipMap.get(RelationshipType.COVER_ART).get(0).getId().toString();
                JsonObject response = gson.fromJson(HTTPRequest.get("https://api.mangadex.org/cover/" + coverId), JsonObject.class);
                return new CoverProxy(response.getAsJsonObject("data"));
            } catch (HTTPRequestException e) {
                logger.error("Could not retrieve all covers for manga " + id, e);
                throw new CompletionException(e);
            }
        });
    }

    private static boolean isError(JsonObject response) {
        return response.get("result").getAsString().equals("error");
    }

    private static List<UUID> parseIds(JsonObject jsonObject) {
        List<UUID> uuids = new ArrayList<>();
        JsonObject volumes = jsonObject.getAsJsonObject("volumes");

        for (Map.Entry<String, JsonElement> volumeEntry : volumes.entrySet()) {
            JsonObject chapters = volumeEntry.getValue().getAsJsonObject().getAsJsonObject("chapters");
            for (Map.Entry<String, JsonElement> chapterEntry : chapters.entrySet()) {
                String id = chapterEntry.getValue().getAsJsonObject().get("id").getAsString();
                uuids.add(UUID.fromString(id));
            }
        }
        return uuids;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Manga{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", altTitles=" + altTitles +
                ", isLocked=" + isLocked +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", lastVolume='" + lastVolume + '\'' +
                ", lastChapter='" + lastChapter + '\'' +
                ", publicationDemographic=" + publicationDemographic +
                ", status=" + status +
                ", year=" + year +
                ", contentRating=" + contentRating +
                ", state='" + state + '\'' +
                ", chapterNumbersResetOnNewVolume=" + chapterNumbersResetOnNewVolume +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", version=" + version +
                ", availableTranslatedLanguages=" + availableTranslatedLanguages +
                ", latestUploadedChapterId='" + latestUploadedChapterId + '\'' +
                ", relationshipMap=" + relationshipMap +
                '}';
    }
}
