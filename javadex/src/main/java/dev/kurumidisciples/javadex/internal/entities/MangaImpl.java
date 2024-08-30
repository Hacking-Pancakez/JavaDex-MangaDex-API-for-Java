package dev.kurumidisciples.javadex.internal.entities;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
import dev.kurumidisciples.javadex.api.entities.Manga;
import dev.kurumidisciples.javadex.api.entities.Manga;
import dev.kurumidisciples.javadex.api.entities.enums.Locale;
import dev.kurumidisciples.javadex.api.entities.enums.State;
import dev.kurumidisciples.javadex.api.entities.enums.manga.LinkType;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.ContentRating;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Demographic;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Status;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Tag;
import dev.kurumidisciples.javadex.api.entities.intermediate.Entity;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;
import dev.kurumidisciples.javadex.api.entities.relationship.enums.RelationshipType;
import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import dev.kurumidisciples.javadex.api.proxies.CoverProxy;
import dev.kurumidisciples.javadex.api.statistics.StatisticsData;
import dev.kurumidisciples.javadex.internal.annotations.MustNotBeUnknown;
import dev.kurumidisciples.javadex.internal.annotations.NotLessThanOne;
import dev.kurumidisciples.javadex.internal.annotations.Size;
import dev.kurumidisciples.javadex.internal.factories.entities.ChapterFactory;
import dev.kurumidisciples.javadex.internal.http.HTTPRequest;

public class MangaImpl extends Entity implements Manga {


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
    private final int version;
    private final List<Locale> availableTranslatedLanguages;
    private final String latestUploadedChapterId;
    private final RelationshipMap relationshipMap;

    public MangaImpl(UUID id, String title, Map<Locale, String> description, Map<Locale, List<String>> altTitles, boolean isLocked, Locale originalLanguage, Number lastVolume, Number lastChapter, Demographic publicationDemographic, Status status, Long year, ContentRating contentRating, State state, Map<LinkType, String> links, boolean chapterNumbersResetOnNewVolume, OffsetDateTime createdAt, OffsetDateTime updatedAt, int version, String latestUploadedChapterId, List<Tag> tags, List<Locale> availableTranslatedLanguages, RelationshipMap relationshipMap, UUID author) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.altTitles = altTitles;
        this.isLocked = isLocked;
        this.originalLanguage = originalLanguage;
        this.lastVolume = lastVolume;
        this.lastChapter = lastChapter;
        this.publicationDemographic = publicationDemographic;
        this.status = status;
        this.year = year;
        this.contentRating = contentRating;
        this.state = state;
        this.links = links;
        this.chapterNumbersResetOnNewVolume = chapterNumbersResetOnNewVolume;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
        this.latestUploadedChapterId = latestUploadedChapterId;
        this.tags = tags;
        this.availableTranslatedLanguages = availableTranslatedLanguages;
        this.relationshipMap = relationshipMap;
        this.author = author;
    }

    /**
     * <p>Getter for the field <code>relationshipMap</code>.</p>
     *
     * @return a {@link dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap} object
     */
    public RelationshipMap getRelationshipMap() {
        return relationshipMap;
    }

    public Integer getVersion() {
        return version;
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
     * <p>Getter for the field <code>id</code>.</p>
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
     * <p>Getter for the field <code>description</code>.</p>
     * <p>A manga can have multiple descriptions in different languages.</p>
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
                    chaptersList.add(ChapterFactory.createEntity(chapter.getAsJsonObject()));
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
     * Retrieve the IDs of all chapters for the manga in the specified language.
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
     * Retrieve the chapters for the manga in the specified language, ordered by their chapter number.
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
                            return ChapterFactory.createEntity(response.getAsJsonObject("data"));
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
     * Retrieve the chapters for the manga in the specified language, ordered by their chapter number.
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
                    System.out.println(chapterElement);
                    chapters.add(ChapterFactory.createEntity(chapterElement.getAsJsonObject()));
                }
                return chapters;
            } catch (HTTPRequestException e) {
                logger.error("Could not retrieve chapter " + number + " for manga " + id, e);
                throw new CompletionException(e);
            }
        });
    }

    /**
     * Retrieves the current cover displayed on MangaDex.
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

    /**
     * Retrieve the StatisticsData object for the manga.
     */
    public CompletableFuture<StatisticsData> retrieveStatistics(){
        return CompletableFuture.supplyAsync(() -> {
            try {
                JsonObject response = gson.fromJson(HTTPRequest.get("https://api.mangadex.org/statistics/manga/" + id.toString()), JsonObject.class);
                return new StatisticsData(response.getAsJsonObject("data"));
            } catch (HTTPRequestException e) {
                logger.error("Could not retrieve statistics for manga " + id, e);
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
