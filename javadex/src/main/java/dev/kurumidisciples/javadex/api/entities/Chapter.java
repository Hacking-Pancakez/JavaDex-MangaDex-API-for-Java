package dev.kurumidisciples.javadex.api.entities;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.maven.api.annotations.Nullable;

import dev.kurumidisciples.javadex.api.core.JavaDex;
import dev.kurumidisciples.javadex.api.entities.enums.Locale;
import dev.kurumidisciples.javadex.api.entities.intermediate.IPublishable;
import dev.kurumidisciples.javadex.api.entities.intermediate.IRelationHolder;
import dev.kurumidisciples.javadex.api.entities.intermediate.ISnowflake;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;
import dev.kurumidisciples.javadex.api.proxies.PageProxy;

// TODO rename to Chapter after old Chapter is removed
public interface Chapter extends IPublishable, ISnowflake, IRelationHolder {
    
    /**
     * Returns the volume associated with the chapter. Could be {@code null} if the chapter is not part of a volume.
     */
    @Nullable
    Number getVolume();

    /**
     * Returns the chapter number.
     */
    Number getChapterNumber();

    /**
     * <p>Returns the title of the chapter. Typically localized in the language of the chapter.<p>
     * <p>Could be {@code null} if the chapter has no title.</p>
     */
    @Nullable
    String getTitle();

    /**
     * Returns the localization of the chapter.
     */
    Locale getTranslatedLanguage();

    /**
     * Returns the number of pages in the chapter.
     */
    int getPages();

    /**
     * Returns the relationship map of the chapter. Outlining how the chapter is related to other entities.
     */
    RelationshipMap getRelationshipMap();

    /**
     * Retrieves the manga associated with the chapter.
     */
    CompletableFuture<Manga> getAssociatedManga() throws InterruptedException, ExecutionException;

    /**
     * Retrieves the pages of the chapter in proxy form.
     */
    CompletableFuture<List<PageProxy>> retrievePages() throws ExecutionException;

    /**
     * Retrieves the user that uploaded the chapter.
     */
    CompletableFuture<User> retrieveUploader() throws InterruptedException, ExecutionException;

    /**
     * Retrieves the scanlation groups that scanlated the chapter.
     * <p>A chapter can have multiple scanlation groups that worked on it, or sometimes no scanlation group at all.</p>
     */
    CompletableFuture<List<ScanlationGroup>> retrieveScanlationGroups() throws InterruptedException, ExecutionException;

    /**
     * Marks the chapter as read for the given JavaDex instance.
     * @param loggedInInstance Needs to be authenticated with valid credentials.
     * @return {@code true} if the chapter was successfully marked as read, {@code false} otherwise.
     */
    CompletableFuture<Boolean> markAsRead(JavaDex loggedInInstance);
}
