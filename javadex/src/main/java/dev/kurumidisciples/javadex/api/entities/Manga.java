package dev.kurumidisciples.javadex.api.entities;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;

import dev.kurumidisciples.javadex.api.entities.enums.Locale;
import dev.kurumidisciples.javadex.api.entities.enums.State;
import dev.kurumidisciples.javadex.api.entities.enums.manga.LinkType;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.ContentRating;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Demographic;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Status;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Tag;
import dev.kurumidisciples.javadex.api.entities.intermediate.IRelationHolder;
import dev.kurumidisciples.javadex.api.entities.intermediate.ISnowflake;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;
import dev.kurumidisciples.javadex.api.proxies.CoverProxy;
import dev.kurumidisciples.javadex.api.statistics.StatisticsData;
import dev.kurumidisciples.javadex.internal.annotations.MustNotBeUnknown;

/**
 * Represents a Manga object in the MangaDex API. This interface provides methods to retrieve various details
 * and metadata about a manga, including its state, titles, chapters, and relationships with other entities.
 * @author Hacking Pancakez
 */
public interface Manga extends ISnowflake, IRelationHolder {

    /**
     * Returns the {@link RelationshipMap} of the manga, which describes how this manga relates to other entities.
     *
     * @return the relationship map of the manga.
     */
    @Override
    RelationshipMap getRelationshipMap();

    /**
     * Indicates whether the chapter number resets when a new volume is released.
     *
     * @return true if the chapter number resets with each new volume, false otherwise.
     */
    boolean doChapterNumbersResetOnNewVolume();

    /**
     * Returns the {@link State} of the manga in MangaDex. This can be used to determine if the manga is published,
     * in draft state, or in another state.
     *
     * @return the state of the manga.
     */
    State getState();

    /**
     * Returns the list of available translated languages for the manga.
     *
     * @return a list of {@link Locale} objects representing the available translated languages.
     */
    List<Locale> getAvailableTranslatedLanguages();

    /**
     * Returns the default title of the manga, which is the title most prominently displayed on MangaDex.
     *
     * @return the default title of the manga.
     */
    String getDefaultTitle();

    /**
     * Returns a map of external links related to the manga. The keys represent the {@link LinkType}, 
     * and the values are the corresponding URLs.
     *
     * @return a map of external links associated with the manga.
     */
    Map<LinkType, String> getLinks();

    /**
     * Returns the UUID of the author of the manga.
     *
     * @return the UUID of the author.
     */
    UUID getAuthor();

    /**
     * Returns a map of descriptions for the manga, where the keys are {@link Locale} objects representing the language,
     * and the values are the corresponding descriptions in those languages.
     *
     * @return a map of descriptions for the manga.
     */
    Map<Locale, String> getDescriptions();

    /**
     * Returns a map of alternate titles for the manga. The keys represent the {@link Locale} objects,
     * and the values are lists of alternate titles in those languages.
     *
     * @return a map of alternate titles for the manga.
     */
    Map<Locale, List<String>> getAltTitles();

    /**
     * Indicates whether the manga is locked on MangaDex.
     *
     * @return true if the manga is locked, false otherwise.
     */
    boolean isLocked();

    /**
     * Returns the original language of the manga.
     *
     * @return the {@link Locale} representing the original language of the manga.
     */
    Locale getOriginalLanguage();

    /**
     * Returns the last volume number available for the manga.
     *
     * @return the last volume number, or null if not available.
     */
    Number getLastVolume();

    /**
     * Returns the last chapter number available for the manga.
     *
     * @return the last chapter number, or null if not available.
     */
    Number getLastChapter();

    /**
     * Returns the content rating of the manga, which indicates the suitable audience for the manga.
     *
     * @return the {@link ContentRating} of the manga.
     */
    ContentRating getContentRating();

    /**
     * Returns the publication demographic of the manga, which indicates the target demographic for the manga.
     *
     * @return the {@link Demographic} of the manga.
     */
    Demographic getPublicationDemographic();

    /**
     * Returns the status of the manga, such as ongoing, completed, etc.
     *
     * @return the {@link Status} of the manga.
     */
    Status getStatus();

    /**
     * Returns the year in which the manga was first published.
     *
     * @return the year of first publication, or null if not available.
     */
    Long getYear();

    /**
     * Returns the UUID of the latest uploaded chapter of the manga.
     *
     * @return the UUID of the latest uploaded chapter.
     */
    UUID getLatestUploadedChapterId();

    /**
     * Returns a list of tags associated with the manga.
     *
     * @return a list of {@link Tag} objects representing the tags of the manga.
     */
    List<Tag> getTags();

    /**
     * Retrieves the chapter count for the manga in the specified language.
     *
     * @param lang the {@link Locale} representing the language for which to retrieve the chapter count.
     * @return a {@link CompletableFuture} that will complete with the chapter count.
     */
    CompletableFuture<Integer> retrieveChapterCount(@MustNotBeUnknown @NotNull Locale lang);

    /**
     * Retrieves the IDs of all chapters for the manga in the specified language.
     *
     * @param lang the {@link Locale} representing the language for which to retrieve the chapter IDs.
     * @return a {@link CompletableFuture} that will complete with a list of chapter IDs.
     */
    CompletableFuture<List<UUID>> retrieveChaptersIds(@MustNotBeUnknown @NotNull Locale lang);

    /**
     * Retrieves the chapters for the manga in the specified language, ordered by their chapter number.
     *
     * @param lang the {@link Locale} representing the language for which to retrieve the chapters.
     * @return a {@link CompletableFuture} that will complete with a list of ordered chapters.
     */
    CompletableFuture<List<Chapter>> retrieveChaptersOrdered(@MustNotBeUnknown @NotNull Locale lang);

    /**
     * Retrieves the feed of chapters for the manga.
     *
     * @return a {@link CompletableFuture} that will complete with a list of chapters in the feed.
     */
    CompletableFuture<List<Chapter>> retrieveFeed();

    /**
     * Retrieves the current cover of the manga.
     *
     * @return a {@link CompletableFuture} that will complete with a {@link CoverProxy} representing the current cover.
     */
    CompletableFuture<CoverProxy> retrieveCurrentCover();

    /**
     * Retrieves the statistics of the manga, including views, ratings, and other relevant metrics.
     *
     * @return a {@link CompletableFuture} that will complete with a {@link StatisticsData} object representing the statistics.
     */
    CompletableFuture<StatisticsData> retrieveStatistics();

    /**
     * Retrieves the chapter with the specified number for the manga in the specified language.
     * Will return an empty list if no chapter with the specified number is found.
     * @return a {@link CompletableFuture} that will complete with a list of chapters with the specified number. Can be empty.
     */
    CompletableFuture<List<Chapter>> retrieveChapterByNumber(@MustNotBeUnknown @NotNull Locale lang, int number);
}
