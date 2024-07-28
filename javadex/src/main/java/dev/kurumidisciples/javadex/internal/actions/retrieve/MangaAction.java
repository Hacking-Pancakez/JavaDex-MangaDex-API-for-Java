package dev.kurumidisciples.javadex.internal.actions.retrieve;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.entities.content.Manga;
import dev.kurumidisciples.javadex.api.entities.enums.IncludesType;
import dev.kurumidisciples.javadex.api.entities.enums.Locale;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Demographic;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Status;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Mode;
import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import dev.kurumidisciples.javadex.internal.actions.Action;
import dev.kurumidisciples.javadex.internal.annotations.Size;
import dev.kurumidisciples.javadex.internal.http.HTTPRequest;
import dev.kurumidisciples.javadex.internal.http.adapters.OffsetDateTimeTypeAdapter;

/**
 * The MangaAction class provides methods for searching MangaDex API for manga titles and retrieving manga by ID.
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public class MangaAction extends Action<List<Manga>>{

    private static final Logger logger = LogManager.getLogger(MangaAction.class);
    private static final String API_BASE_URL = "https://api.mangadex.org/manga";

    private Integer limit;
    private Integer offset;
    private String title;
    private List<UUID> mangaIds = new ArrayList<>(); //limited to 100 per request
    private UUID authorOrArtist;
    private List<UUID> authors = new ArrayList<>();
    private List<UUID> artists = new ArrayList<>();
    private Integer year;
    private List<UUID> includedTags = new ArrayList<>();
    private String includedTagsMode;
    private List<UUID> excludedTags = new ArrayList<>();
    private String excludedTagsMode;
    private Status status;
    private List<String> originalLanguages = new ArrayList<>();
    private List<String> excludedLanguages = new ArrayList<>();
    private List<Locale> availableTranslatedLanguages = new ArrayList<>();
    private List<Demographic> demographic = new ArrayList<>();
    private List<String> contentRatings = new ArrayList<>();
    private Boolean hasAvailableChapters;

    /**
     * <p>Constructor for MangaAction.</p>
     *
     * @param query a {@link java.lang.String} object
     */
    public MangaAction(String query) {
        this.title = formatString(query);
    }

    /**
     * <p>Constructor for MangaAction.</p>
     */
    public MangaAction() {
        this.title = null;
    }

    /**
     * <p>Constructor for MangaAction.</p>
     *
     * @param query a {@link java.lang.String} object
     * @param limit a int
     * @param offset a int
     */
    public MangaAction(String query, int limit, int offset) {
        this.title = query;
        this.limit = limit;
        this.offset = offset;
    }

    /**
     * <p>Setter for the field <code>authorOrArtist</code>.</p>
     *
     * @param authorOrArtist a {@link java.util.UUID} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction setAuthorOrArtist(UUID authorOrArtist) {
        this.authorOrArtist = authorOrArtist;
        return this;
    }

    /**
     * <p>addAuthor.</p>
     *
     * @param author a {@link java.util.UUID} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction addAuthor(UUID author) {
        this.authors.add(author);
        return this;
    }

    /**
     * Add a id to the list of {@link Manga}'s to search for.
     * <p><b>WARNING: This method is limited to 100 manga ids per request.</b></p>
     * @param id The id of the manga to add to the search.
     * @return The current instance of the MangaAction.
     */
    public MangaAction addMangaId(UUID id) {
        if (mangaIds.size() < 100) {
            mangaIds.add(id);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * <p><b>This method is not supported for this action.</b></p>
     */
    @Override
    public MangaAction setIncludes(IncludesType... includes) {
        throw new UnsupportedOperationException("This method is not supported for this action.");
    }
    /**
     * {@inheritDoc}
     *
     * Set the limit for the amount of results returned by the mangadex api
     */
    @Override
    public MangaAction setLimit(@Size(min=0, max=100) Integer limit) {
        this.limit = limit;
        return this;
    }

    /**
     * 
     *
     * @param query a {@link java.lang.String} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction setQuery(String query) {
        this.title = formatString(query);
        return this;
    }
    /**
     * {@inheritDoc}
     *
     * Set the offset for the search results. This is helpful because the api will only returns a maximum 100 results at a time.
     */
    @Override
    public MangaAction setOffset(@Size(min=0) Integer offset) {
        this.offset = offset;
        return this;
    }
    /* TODO Should allow intake of a author class object */
    /**
     * <p>addArtist.</p>
     *
     * @param artist a {@link java.util.UUID} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction addArtist(UUID artist) {
        this.artists.add(artist);
        return this;
    }

    /**
     * <p>addAuthors.</p>
     *
     * @param authors a {@link java.util.List} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction addAuthors(List<UUID> authors) {
        this.authors.addAll(authors);
        return this;
    }

    /**
     * <p>addArtists.</p>
     *
     * @param artists a {@link java.util.List} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction addArtists(List<UUID> artists) {
        this.artists.addAll(artists);
        return this;
    }

    /**
     * <p>Setter for the field <code>year</code>.</p>
     *
     * @param year a int
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction setYear(int year) {
        this.year = year;
        return this;
    }

    /**
     * <p>Setter for the field <code>includedTagsMode</code>.</p>
     * 
     * @issueProne This method has not been fully tested yet.
     * @param mode a {@link java.lang.String} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction setIncludedTagsMode(String mode) {
        this.includedTagsMode = mode;
        return this;
    }

    /**
     * <p>Setter for the field <code>excludedTagsMode</code>.</p>
     * @issueProne This method has not been fully tested yet.
     * @see Mode
     * @param mode a {@link java.lang.String} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction setExcludedTagsMode(String mode) {
        this.excludedTagsMode = mode;
        return this;
    }
    /**
     * Adds a tag to the included tags list.
     *
     * @param tag a {@link java.util.UUID} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction addTag(UUID tag) {
        this.includedTags.add(tag);
        return this;
    }
    /**
     * <p>addTags.</p>
     *
     * @param tags a {@link java.util.UUID} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction addTags(UUID... tags) {
        for (UUID tag : tags) {
            this.includedTags.add(tag);
        }
        return this;
    }
    /**
     * Adds tags to include in the search results.
     *
     * @param tags a {@link java.util.List} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction addTags(List<UUID> tags /* TODO switch to Manga.Tag */) {
        this.includedTags.addAll(tags); //TODO avoid duplicates
        return this;
    }
    /**
     * Adds a tag to exclude from the search results.
     *
     * @param tag a {@link java.util.UUID} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction addExcludedTag(UUID tag) {
        this.excludedTags.add(tag);
        return this;
    }
    /**
     * <p>addExcludedTags.</p>
     *
     * @param tags a {@link java.util.List} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction addExcludedTags(List<UUID> tags) {
        this.excludedTags.addAll(tags);
        return this;
    }
    /**
     * <p>addExcludedTags.</p>
     *
     * @param tags a {@link java.util.UUID} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction addExcludedTags(UUID... tags) {
        for (UUID tag : tags) {
            this.excludedTags.add(tag);
        }
        return this;
    }

    /**
     * <p>Setter for the field <code>status</code>.</p>
     *
     * @param status a {@link java.lang.String} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction setStatus(Status status) {
        this.status = status;
        return this;
    }

    /**
     * <p>Setter for the field <code>demographic</code>.</p>
     *
     * @param demographic a {@link dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Demographic} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction addDemographic(Demographic demographic) {
        this.demographic.add(demographic);
        return this;
    }

    /**
     * <p>addContentRating.</p>
     *
     * @param contentRating a {@link java.lang.String} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction addContentRating(String contentRating) {
        if (!this.contentRatings.contains(contentRating)) {
            this.contentRatings.add(contentRating);
        }
        return this;
    }

    /**
     * <p>addContentRatings.</p>
     *
     * @param contentRatings a {@link java.util.List} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction addContentRatings(List<String> contentRatings) {
        for (String contentRating : contentRatings) {
            if (!this.contentRatings.contains(contentRating)) {
                this.contentRatings.add(contentRating);
            }
        }
        return this;
    }

    /**
     * <p>setAvailableChapters.</p>
     *
     * @param hasAvailableChapters a boolean
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction setAvailableChapters(boolean hasAvailableChapters) {
        this.hasAvailableChapters = hasAvailableChapters;
        return this;
    }

    /**
     * <p>Setter for the field <code>originalLanguages</code>.</p>
     *
     * @param originalLanguages a {@link java.util.List} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction setOriginalLanguages(List<String> originalLanguages) {
        this.originalLanguages = originalLanguages;
        return this;
    }

    /**
     * <p>Setter for the field <code>availableTranslatedLanguages</code>.</p>
     *
     * @param availableTranslatedLanguages a {@link java.util.List} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction setAvailableTranslatedLanguages(List<Locale> availableTranslatedLanguages) {
        this.availableTranslatedLanguages = availableTranslatedLanguages;
        return this;
    }

    /**
     * <p>Setter for the field <code>excludedLanguages</code>.</p>
     *
     * @param excludedLanguages a {@link java.util.List} object
     * @return a {@link dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction} object
     */
    public MangaAction setExcludedLanguages(List<String> excludedLanguages) {
        this.excludedLanguages = excludedLanguages;
        return this;
    }

    /**
     * <p>Getter for the field <code>limit</code>.</p>
     *
     * @return a {@link java.lang.Integer} object
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * <p>Getter for the field <code>ids</code>.</p>
     * @return a {@link java.util.List} object of {@link java.util.UUID} objects
     */
    public List<UUID> getMangaIds() {
        return mangaIds;
    }

    /**
     * <p>Getter for the field <code>offset</code>.</p>
     *
     * @return a {@link java.lang.Integer} object
     */
    public Integer getOffset() {
        return offset;
    }

    /**
     * <p>getQuery.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getQuery() {
        return title;
    }

    /**
     * <p>Getter for the field <code>authorOrArtist</code>.</p>
     *
     * @return a {@link java.util.UUID} object
     */
    public UUID getAuthorOrArtist() {
        return authorOrArtist;
    }

    /**
     * <p>Getter for the field <code>authors</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<UUID> getAuthors() {
        return authors;
    }

    /**
     * <p>Getter for the field <code>artists</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<UUID> getArtists() {
        return artists;
    }

    /**
     * <p>Getter for the field <code>year</code>.</p>
     *
     * @return a {@link java.lang.Integer} object
     */
    public Integer getYear() {
        return year;
    }

    /**
     * <p>Getter for the field <code>includedTagsMode</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getIncludedTagsMode() {
        return includedTagsMode;
    }

    /**
     * <p>Getter for the field <code>excludedTagsMode</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getExcludedTagsMode() {
        return excludedTagsMode;
    }

    /**
     * <p>Getter for the field <code>includedTags</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<UUID> getIncludedTags() {
        return includedTags;
    }

    /**
     * <p>Getter for the field <code>excludedTags</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<UUID> getExcludedTags() {
        return excludedTags;
    }

    /**
     * <p>Getter for the field <code>status</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public Status getStatus() {
        return status;
    }

    /**
     * <p>Getter for the field <code>demographic</code>.</p>
     *
     * @return a {@link dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Demographic} object
     */
    public List<Demographic> getDemographics() {
        return demographic;
    }

    /**
     * <p>Getter for the field <code>contentRatings</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<String> getContentRatings() {
        return contentRatings;
    }

    /**
     * <p>hasAvailableChapters.</p>
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean hasAvailableChapters() {
        return hasAvailableChapters;
    }

    /**
     * <p>Getter for the field <code>originalLanguages</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<String> getOriginalLanguages() {
        return originalLanguages;
    }

    /**
     * <p>Getter for the field <code>availableTranslatedLanguages</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<Locale> getAvailableTranslatedLanguages() {
        return availableTranslatedLanguages;
    }

    /**
     * <p>Getter for the field <code>excludedLanguages</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<String> getExcludedLanguages() {
        return excludedLanguages;
    }

    /**
     * Submits the search request asynchronously.
     *
     * @return A CompletableFuture containing a list of Manga objects.
     */
    public CompletableFuture<List<Manga>> submit() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return complete();
            } catch (HTTPRequestException e) {
                throw new CompletionException(e);
            }
        });
    }

    /**
     * Completes the search request and retrieves the results.
     *
     * @return A list of Manga objects.
     * @throws dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException if any.
     */
    public List<Manga> complete() throws HTTPRequestException {
        logger.debug("Submitting search request with parameters: {}", this);
        String queryString = buildQueryString();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeAdapter())
                .create();

        JsonObject response = gson.fromJson(HTTPRequest.get(API_BASE_URL + "?" + queryString), JsonObject.class);
        JsonArray data = response.getAsJsonArray("data");
        List<Manga> mangaList = new ArrayList<>();

        data.forEach(element -> mangaList.add(new Manga(element.getAsJsonObject())));

        if (mangaList.isEmpty()) {
            logger.warn("No manga entities found for the search query: {}", title);
        } else {
            logger.debug("Search request completed. Retrieved {} manga items.", mangaList.size());
        }

        return mangaList;
    }


    /**
     * Retrieves a Manga object for the specified ID from MangaDex API.
     *
     * @param id A String representing the manga ID.
     * @return A CompletableFuture containing the Manga object.
     */
    public static CompletableFuture<Manga> getMangaById(String id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.debug("Retrieving manga by ID: {}", id);
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeAdapter())
                        .create();
                JsonObject response = gson.fromJson(HTTPRequest.get("https://api.mangadex.org/manga/" + id), JsonObject.class);
                return new Manga(response.getAsJsonObject("data"));
            } catch (Exception e) {
                logger.error("Unable to retrieve Manga with ID: {}", id, e);
                return null;
            }
        });
    }

    /**
     * Builds the query string for the GET request.
     *
     * @return The query string.
     */
    private String buildQueryString() {
        StringBuilder queryString = new StringBuilder();
        if (title != null) queryString.append("title=").append(encodeValue(title)).append("&");
        if (limit != null) queryString.append("limit=").append(limit).append("&");
        if (offset != null) queryString.append("offset=").append(offset).append("&");
        if (year != null) queryString.append("year=").append(year).append("&");
        if (status != null) queryString.append("status=").append(encodeValue(status.getValue())).append("&");
        if (hasAvailableChapters != null) queryString.append("hasAvailableChapters=").append(hasAvailableChapters).append("&");
        for (Demographic demo : demographic) {
            queryString.append("publicationDemographic[]=").append(encodeValue(demo.getValue())).append("&");
        }
        for (UUID id : mangaIds) {
            queryString.append("ids[]=").append(id.toString()).append("&");
        }
        for (UUID author : authors) {
            queryString.append("authors[]=").append(author.toString()).append("&");
        }
        for (UUID artist : artists) {
            queryString.append("artists[]=").append(artist.toString()).append("&");
        }
        for (UUID tag : includedTags) {
            queryString.append("includedTags[]=").append(tag.toString()).append("&");
        }
        if (includedTagsMode != null) queryString.append("includedTagsMode=").append(encodeValue(includedTagsMode)).append("&");
        for (UUID tag : excludedTags) {
            queryString.append("excludedTags[]=").append(tag.toString()).append("&");
        }
        if (excludedTagsMode != null) queryString.append("excludedTagsMode=").append(encodeValue(excludedTagsMode)).append("&");
        for (String language : originalLanguages) {
            queryString.append("originalLanguage[]=").append(encodeValue(language)).append("&");
        }
        for (String language : excludedLanguages) {
            queryString.append("excludedLanguage[]=").append(encodeValue(language)).append("&");
        }
        for (Locale language : availableTranslatedLanguages) {
            queryString.append("availableTranslatedLanguage[]=").append(encodeValue(language.getLanguage())).append("&");
        }
        for (String contentRating : contentRatings) {
            queryString.append("contentRating[]=").append(encodeValue(contentRating)).append("&");
        }

        // Remove the last "&" if present
        if (queryString.length() > 0 && queryString.charAt(queryString.length() - 1) == '&') {
            queryString.setLength(queryString.length() - 1);
        }

        return queryString.toString();
    }

    /**
     * Formats the given input string by replacing spaces with '%20'.
     *
     * @param input The input string to format.
     * @return The formatted string.
     */
    private static String formatString(String input) {
        return input;
        //return input.replace(" ", "%20");
    }

    /**
     * Encodes a value to be used in a URL query string.
     *
     * @param value The value to encode.
     * @return The encoded value.
     */
    private static String encodeValue(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "SearchAction{" +
                "limit=" + limit +
                ", offset=" + offset +
                ", title='" + title + '\'' +
                ", authorOrArtist=" + authorOrArtist +
                ", authors=" + authors +
                ", artists=" + artists +
                ", year=" + year +
                ", includedTags=" + includedTags +
                ", includedTagsMode='" + includedTagsMode + '\'' +
                ", excludedTags=" + excludedTags +
                ", excludedTagsMode='" + excludedTagsMode + '\'' +
                ", status='" + status + '\'' +
                ", originalLanguages=" + originalLanguages +
                ", excludedLanguages=" + excludedLanguages +
                ", availableTranslatedLanguages=" + availableTranslatedLanguages +
                ", demographic='" + demographic + '\'' +
                ", contentRatings=" + contentRatings +
                ", hasAvailableChapters=" + hasAvailableChapters +
                '}';
    }
}
