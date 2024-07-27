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
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipData;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;
import dev.kurumidisciples.javadex.api.entities.relationship.enums.RelationshipType;
import dev.kurumidisciples.javadex.api.proxies.PageProxy;
import dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction;
import dev.kurumidisciples.javadex.internal.factory.GroupFactory;
import dev.kurumidisciples.javadex.internal.factory.PageFactory;
import dev.kurumidisciples.javadex.internal.factory.UserFactory;
import dev.kurumidisciples.javadex.internal.parsers.ChapterParser;

/**
 * Represents a Chapter entity. Chapters are uploaded by users and are associated with a Manga.
 * Implements {@link dev.kurumidisciples.javadex.api.entities.intermediate.ISnowflake} and {@link dev.kurumidisciples.javadex.api.entities.intermediate.IPublishable} interfaces for ID and publication time handling.
 * 
 */
public class Chapter extends Entity implements IPublishable {

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
        ChapterParser parser = new ChapterParser(data);
        this.id = parser.getId();
        this.volume = parser.getVolume();
        this.chapter = parser.getChapter();
        this.title = parser.getTitle();
        this.version = parser.getVersion();
        this.translatedLanguage = parser.getTranslatedLanguage();
        this.pages = parser.getPages();
        this.createdAt = parser.getCreatedAt();
        this.updatedAt = parser.getUpdatedAt();
        this.publishedAt = parser.getPublishAt();
        this.readableAt = parser.getReadableAt();
        this.relationshipMap = parser.getRelationshipMap();
    }

    public CompletableFuture<Manga> getAssociatedManga() {
        return MangaAction.getMangaById(getRelationshipMap().get(RelationshipType.MANGA).get(0).toString());
    }

    public RelationshipMap getRelationshipMap() {
        return relationshipMap;
    }

    public Number getVolume() {
        return volume;
    }

    public String getIdRaw() {
        return id.toString();
    }

    public UUID getId() {
        return id;
    }

    @Override
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public OffsetDateTime getPublishAt() {
        return publishedAt;
    }

    @Override
    public OffsetDateTime getReadableAt() {
        return readableAt;
    }

    public Number getChapterNumber() {
        return chapter;
    }

    public String getTitle() {
        return title;
    }

    public int getVersion() {
        return version;
    }

    public Locale getTranslatedLanguage() {
        return translatedLanguage;
    }

    public int getPages() {
        return pages;
    }

    private static boolean isChapterData(JsonObject data) {
        return data.get("type").getAsString().equals("chapter");
    }

    public CompletableFuture<List<PageProxy>> retrievePages() throws ExecutionException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return PageFactory.getPages(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public CompletableFuture<User> retrieveUploader() throws InterruptedException, ExecutionException {
        return CompletableFuture.supplyAsync(() -> {
            return UserFactory.retrieveUploader(this);
        });
    }

    public CompletableFuture<List<ScanlationGroup>> retrieveScanlationGroups() throws InterruptedException, ExecutionException {
        return CompletableFuture.supplyAsync(() -> {
            try{
                if (relationshipMap.get(RelationshipType.SCANLATION_GROUP) == null) return Collections.emptyList();
                List<RelationshipData> scanlationGroupRelationships = relationshipMap.get(RelationshipType.SCANLATION_GROUP);
                List<ScanlationGroup> scanlationGroups = new ArrayList<>();
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
