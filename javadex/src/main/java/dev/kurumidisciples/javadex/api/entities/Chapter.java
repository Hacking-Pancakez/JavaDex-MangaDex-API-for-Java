package dev.kurumidisciples.javadex.api.entities;

import java.io.IOException;
import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.core.JavaDex;
import dev.kurumidisciples.javadex.api.entities.content.Manga;
import dev.kurumidisciples.javadex.api.entities.enums.Locale;
import dev.kurumidisciples.javadex.api.entities.intermediate.Entity;
import dev.kurumidisciples.javadex.api.entities.intermediate.IPublishable;
import dev.kurumidisciples.javadex.api.entities.intermediate.ISnowflake;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipData;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;
import dev.kurumidisciples.javadex.api.entities.relationship.enums.RelationshipType;
import dev.kurumidisciples.javadex.api.proxies.PageProxy;
import dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction;
import dev.kurumidisciples.javadex.internal.factory.GroupFactory;
import dev.kurumidisciples.javadex.internal.factory.PageFactory;
import dev.kurumidisciples.javadex.internal.factory.UserFactory;

/**
 * Represents a Chapter entity. Chapters are uploaded by users and are associated with a Manga.
 * Implements {@link dev.kurumidisciples.javadex.api.entities.intermediate.ISnowflake} and {@link dev.kurumidisciples.javadex.api.entities.intermediate.IPublishable} interfaces for ID and publication time handling.
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public class Chapter extends Entity implements ISnowflake, IPublishable {

    private static final Logger logger = LogManager.getLogger(Chapter.class);

    private final UUID id;
    private final Number volume;
    private final Number chapter;
    private final String title;
    private final int version;
    private final Locale translatedLanguage;
    private final int pages;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;
    private final OffsetDateTime publishedAt;
    private final OffsetDateTime readableAt;
    private final RelationshipMap relationshipMap;

    /**
     * Constructs a Chapter object from JSON data.
     * Validates the JSON structure before initializing the object.
     *
     * @param data JsonObject containing chapter data.
     * @throws java.lang.IllegalArgumentException if the provided data does not represent a chapter.
     */
    public Chapter(@NotNull JsonObject data) {
        if (!isChapterData(data)) throw new IllegalArgumentException("Invalid chapter data"); // TODO remove

        JsonObject attributes = data.getAsJsonObject("attributes");

        this.id = UUID.fromString(data.get("id").getAsString());
        this.volume = attributes.has("volume") && !attributes.get("volume").isJsonNull() ? attributes.get("volume").getAsNumber() : 0;
        this.chapter = attributes.has("chapter") && !attributes.get("chapter").isJsonNull() ? attributes.get("chapter").getAsNumber() : BigInteger.ZERO;
        this.title = attributes.has("title") && !attributes.get("title").isJsonNull() ? attributes.get("title").getAsString() : null;
        this.version = attributes.get("version").getAsInt();
        this.translatedLanguage = Locale.getByLanguage(attributes.get("translatedLanguage").getAsString());
        this.pages = attributes.get("pages").getAsInt();
        this.createdAt = OffsetDateTime.parse(attributes.get("createdAt").getAsString());
        this.updatedAt = OffsetDateTime.parse(attributes.get("updatedAt").getAsString());
        this.publishedAt = OffsetDateTime.parse(attributes.get("publishAt").getAsString());
        this.readableAt = OffsetDateTime.parse(attributes.get("readableAt").getAsString());
        this.relationshipMap = new RelationshipMap(data.getAsJsonArray("relationships"));
    }

    /**
     * Retrieves the associated Manga entity for this chapter.
     *
     * @return A CompletableFuture containing the associated Manga.
     */
    public CompletableFuture<Manga> getAssociatedManga() {
        return MangaAction.getMangaById(getRelationshipMap().get(RelationshipType.MANGA).get(0).toString());
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
     * <p>Getter for the field <code>volume</code>.</p>
     *
     * @return a {@link java.lang.Number} object
     */
    public Number getVolume() {
        return volume;
    }
    /**
     * <p>getIdRaw.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getIdRaw() {
        return id.toString();
    }

    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return a {@link java.util.UUID} object
     */
    public UUID getId() {
        return id;
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

    /** {@inheritDoc} */
    @Override
    public OffsetDateTime getPublishAt() {
        return publishedAt;
    }

    /** {@inheritDoc} */
    @Override
    public OffsetDateTime getReadableAt() {
        return readableAt;
    }

    /**
     * <p>getChapterNumber.</p>
     *
     * @return a {@link java.lang.Number} object
     */
    public Number getChapterNumber() {
        return chapter;
    }
    /**
     *Could be null
     *
     * @return a {@link java.lang.String} object
     */
    public String getTitle() {
        return title;
    }

    /**
     * <p>Getter for the field <code>version</code>.</p>
     *
     * @return a int
     */
    public int getVersion() {
        return version;
    }

    /**
     * <p>Getter for the field <code>translatedLanguage</code>.</p>
     *
     * @return a {@link dev.kurumidisciples.javadex.api.entities.enums.Locale} object
     */
    public Locale getTranslatedLanguage() {
        return translatedLanguage;
    }

    /**
     * <p>Getter for the field <code>pages</code>.</p>
     *
     * @return a int
     */
    public int getPages() {
        return pages;
    }

    private static boolean isChapterData(JsonObject data) {
        return data.get("type").getAsString().equals("chapter");
    }

    /**
     * Retrieves the pages of this chapter.
     *
     * @return A CompletableFuture containing a list of PageProxy objects representing the pages.
     * @throws java.util.concurrent.ExecutionException if the computation threw an exception.
     */
    public CompletableFuture<List<PageProxy>> retrievePages() throws ExecutionException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return PageFactory.getPages(this);
            } catch (InterruptedException e) {
                // TODO Handle the InterruptedException here
                e.printStackTrace();
                return null;
            }
        });
    }

    /**
     * Retrieves the uploader of this chapter.
     *
     * @return A CompletableFuture containing the User who uploaded the chapter.
     * @throws java.lang.InterruptedException if the current thread was interrupted while waiting.
     * @throws java.util.concurrent.ExecutionException if the computation threw an exception.
     */
    public CompletableFuture<User> retrieveUploader() throws InterruptedException, ExecutionException {
        return CompletableFuture.supplyAsync(() -> {
            return UserFactory.retrieveUploader(this);
        });
    }

    /**
     * Retrieves the scanlation groups associated with this chapter.
     *
     * @return A CompletableFuture containing a list of ScanlationGroup objects.
     * @throws java.lang.InterruptedException if the current thread was interrupted while waiting.
     * @throws java.util.concurrent.ExecutionException if the computation threw an exception.
     */
    public CompletableFuture<List<ScanlationGroup>> retrieveScanlationGroups() throws InterruptedException, ExecutionException {
        return CompletableFuture.supplyAsync(() -> {
            try{
                if (relationshipMap.get(RelationshipType.SCANLATION_GROUP) == null) return Collections.emptyList();
                List<RelationshipData> scanlationGroupRelationships = relationshipMap.get(RelationshipType.SCANLATION_GROUP);
                List<ScanlationGroup> scanlationGroups = new ArrayList<ScanlationGroup>();
                for (RelationshipData relationshipData : scanlationGroupRelationships){
                    try {
                        scanlationGroups.add(GroupFactory.getScanlationGroup(relationshipData));
                    } catch (InterruptedException e) {
                        logger.error("An error occurred while retrieving scanlation groups", e);
                    }
                }
                return scanlationGroups;
            } catch (IOException e){
                logger.error("An error occurred while retrieving scanlation groups", e);
                return null;
            }
        });
    }

    /**
     * Marks this chapter as read for the given JavaDex instance.
     *
     * @param loggedInInstance The JavaDex instance of the logged-in user.
     * @return A CompletableFuture containing a Boolean indicating whether the chapter was successfully marked as read.
     */
    public CompletableFuture<Boolean> markAsRead(JavaDex loggedInInstance){
        return CompletableFuture.supplyAsync(() -> {
            try {
                loggedInInstance.markChapterAsRead(this).get();
                loggedInInstance.close();
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }
}
