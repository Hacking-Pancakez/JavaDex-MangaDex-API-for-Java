package dev.kurumidisciples.javadex.internal.actions.retrieve;

import java.io.IOException;
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
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Demographic;
import dev.kurumidisciples.javadex.api.exceptions.http.middlemen.HTTPRequestException;
import dev.kurumidisciples.javadex.internal.actions.Action;
import dev.kurumidisciples.javadex.internal.annotations.Size;
import dev.kurumidisciples.javadex.internal.http.HTTPRequest;
import dev.kurumidisciples.javadex.internal.http.adapters.OffsetDateTimeTypeAdapter;

/**
 * The MangaAction class provides methods for searching MangaDex API for manga titles and retrieving manga by ID.
 */
public class MangaAction extends Action<List<Manga>>{

    private static final Logger logger = LogManager.getLogger(MangaAction.class);
    private static final String API_BASE_URL = "https://api.mangadex.org/manga";

    private Integer limit;
    private Integer offset;
    private String title;
    private UUID authorOrArtist;
    private List<UUID> authors = new ArrayList<>();
    private List<UUID> artists = new ArrayList<>();
    private Integer year;
    private List<UUID> includedTags = new ArrayList<>();
    private String includedTagsMode;
    private List<UUID> excludedTags = new ArrayList<>();
    private String excludedTagsMode;
    private String status;
    private List<String> originalLanguages = new ArrayList<>();
    private List<String> excludedLanguages = new ArrayList<>();
    private List<String> availableTranslatedLanguages = new ArrayList<>();
    private Demographic demographic;
    private List<String> contentRatings = new ArrayList<>();
    private Boolean hasAvailableChapters;

    public MangaAction(String query) {
        this.title = formatString(query);
    }

    public MangaAction() {
        this.title = null;
    }

    public MangaAction(String query, int limit, int offset) {
        this.title = query;
        this.limit = limit;
        this.offset = offset;
    }

    public MangaAction setAuthorOrArtist(UUID authorOrArtist) {
        this.authorOrArtist = authorOrArtist;
        return this;
    }

    public MangaAction addAuthor(UUID author) {
        this.authors.add(author);
        return this;
    }

    /**
     * This method is not supported for this action.
     */
    @Override
    public MangaAction setIncludes(IncludesType... includes) {
        throw new UnsupportedOperationException("This method is not supported for this action.");
    }
    /**
     * Set the limit for the amount of results returned by the mangadex api
     * @param limit Must be between 0 and 100 inclusive. Defaults to 100.
     * @return SearchAction
     */
    @Override
    public MangaAction setLimit(@Size(min=1, max=100) Integer limit) {
        this.limit = limit;
        return this;
    }

    public MangaAction setQuery(String query) {
        this.title = formatString(query);
        return this;
    }
    /**
     * Set the offset for the search results. This is helpful because the api will only returns a maximum 100 results at a time.
     * @param offset
     * @return SearchAction
     */
    @Override
    public MangaAction setOffset(@Size(min=0) Integer offset) {
        this.offset = offset;
        return this;
    }
    /* TODO Should allow intake of a author class object */
    public MangaAction addArtist(UUID artist) {
        this.artists.add(artist);
        return this;
    }

    public MangaAction addAuthors(List<UUID> authors) {
        this.authors.addAll(authors);
        return this;
    }

    public MangaAction addArtists(List<UUID> artists) {
        this.artists.addAll(artists);
        return this;
    }

    public MangaAction setYear(int year) {
        this.year = year;
        return this;
    }

    public MangaAction setIncludedTagsMode(String mode) {
        this.includedTagsMode = mode;
        return this;
    }

    public MangaAction setExcludedTagsMode(String mode) {
        this.excludedTagsMode = mode;
        return this;
    }
    /**
     * @apiNote This method will be changed to accept MangaTag objects in the future.
     */
    public MangaAction addTag(UUID tag) {
        this.includedTags.add(tag);
        return this;
    }
    /**
     * @apiNote This method will be changed to accept MangaTag objects in the future.
     */
    public MangaAction addTags(UUID... tags) {
        for (UUID tag : tags) {
            this.includedTags.add(tag);
        }
        return this;
    }
    /**
     * @apiNote This method will be changed to accept MangaTag objects in the future.
     */
    public MangaAction addTags(List<UUID> tags) {
        this.includedTags.addAll(tags);
        return this;
    }
    /**
     * @apiNote This method will be changed to accept MangaTag objects in the future.
     */
    public MangaAction addExcludedTag(UUID tag) {
        this.excludedTags.add(tag);
        return this;
    }
    /**
     * @apiNote This method will be changed to accept MangaTag objects in the future.
     */
    public MangaAction addExcludedTags(List<UUID> tags) {
        this.excludedTags.addAll(tags);
        return this;
    }
    /**
     * @apiNote This method will be changed to accept MangaTag objects in the future.
     */
    public MangaAction addExcludedTags(UUID... tags) {
        for (UUID tag : tags) {
            this.excludedTags.add(tag);
        }
        return this;
    }

    public MangaAction setStatus(String status) {
        this.status = status;
        return this;
    }

    public MangaAction setDemographic(Demographic demographic) {
        this.demographic = demographic;
        return this;
    }

    public MangaAction addContentRating(String contentRating) {
        if (!this.contentRatings.contains(contentRating)) {
            this.contentRatings.add(contentRating);
        }
        return this;
    }

    public MangaAction addContentRatings(List<String> contentRatings) {
        for (String contentRating : contentRatings) {
            if (!this.contentRatings.contains(contentRating)) {
                this.contentRatings.add(contentRating);
            }
        }
        return this;
    }

    public MangaAction setAvailableChapters(boolean hasAvailableChapters) {
        this.hasAvailableChapters = hasAvailableChapters;
        return this;
    }

    public MangaAction setOriginalLanguages(List<String> originalLanguages) {
        this.originalLanguages = originalLanguages;
        return this;
    }

    public MangaAction setAvailableTranslatedLanguages(List<String> availableTranslatedLanguages) {
        this.availableTranslatedLanguages = availableTranslatedLanguages;
        return this;
    }

    public MangaAction setExcludedLanguages(List<String> excludedLanguages) {
        this.excludedLanguages = excludedLanguages;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public String getQuery() {
        return title;
    }

    public UUID getAuthorOrArtist() {
        return authorOrArtist;
    }

    public List<UUID> getAuthors() {
        return authors;
    }

    public List<UUID> getArtists() {
        return artists;
    }

    public Integer getYear() {
        return year;
    }

    public String getIncludedTagsMode() {
        return includedTagsMode;
    }

    public String getExcludedTagsMode() {
        return excludedTagsMode;
    }

    public List<UUID> getIncludedTags() {
        return includedTags;
    }

    public List<UUID> getExcludedTags() {
        return excludedTags;
    }

    public String getStatus() {
        return status;
    }

    public Demographic getDemographic() {
        return demographic;
    }

    public List<String> getContentRatings() {
        return contentRatings;
    }

    public Boolean hasAvailableChapters() {
        return hasAvailableChapters;
    }

    public List<String> getOriginalLanguages() {
        return originalLanguages;
    }

    public List<String> getAvailableTranslatedLanguages() {
        return availableTranslatedLanguages;
    }

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
     * @throws IOException if an error occurs during the request.
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
        if (status != null) queryString.append("status=").append(encodeValue(status)).append("&");
        if (demographic != null) queryString.append("demographic=").append(encodeValue(demographic.getValue())).append("&");
        if (hasAvailableChapters != null) queryString.append("hasAvailableChapters=").append(hasAvailableChapters).append("&");

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
            queryString.append("originalLanguages[]=").append(encodeValue(language)).append("&");
        }
        for (String language : excludedLanguages) {
            queryString.append("excludedLanguages[]=").append(encodeValue(language)).append("&");
        }
        for (String language : availableTranslatedLanguages) {
            queryString.append("availableTranslatedLanguages[]=").append(encodeValue(language)).append("&");
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
