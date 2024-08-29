package dev.kurumidisciples.javadex.internal.entities;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.kurumidisciples.javadex.api.core.JavaDex;
import dev.kurumidisciples.javadex.api.entities.Chapter;
import dev.kurumidisciples.javadex.api.entities.Manga;
import dev.kurumidisciples.javadex.api.entities.ScanlationGroup;
import dev.kurumidisciples.javadex.api.entities.User;
import dev.kurumidisciples.javadex.api.entities.enums.Locale;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipData;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;
import dev.kurumidisciples.javadex.api.entities.relationship.enums.RelationshipType;
import dev.kurumidisciples.javadex.api.proxies.PageProxy;
import dev.kurumidisciples.javadex.internal.actions.retrieve.MangaAction;
import dev.kurumidisciples.javadex.internal.factories.GroupRelationFactory;
import dev.kurumidisciples.javadex.internal.factories.PageFactory;
import dev.kurumidisciples.javadex.internal.factories.UploaderFactory;
 
// TODO move service logic to seperate class. Single responsibility principle.
public class ChapterImpl implements Chapter{
    
    private static final Logger logger = LogManager.getLogger(ChapterImpl.class);

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

    public ChapterImpl(UUID id, Number volume, Number chapter, String title, int version, Locale translatedLanguage, int pages, OffsetDateTime createdAt, OffsetDateTime updatedAt, OffsetDateTime publishedAt, OffsetDateTime readableAt, RelationshipMap relationshipMap) {
        this.id = id;
        this.volume = volume;
        this.chapter = chapter;
        this.title = title;
        this.version = version;
        this.translatedLanguage = translatedLanguage;
        this.pages = pages;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.publishedAt = publishedAt;
        this.readableAt = readableAt;
        this.relationshipMap = relationshipMap;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public Number getVolume() {
        return volume;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public Locale getTranslatedLanguage() {
        return translatedLanguage;
    }

    @Override
    public int getPages() {
        return pages;
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

    @Override
    public RelationshipMap getRelationshipMap() {
        return relationshipMap;
    }

    @Override
    public String getIdRaw() {
        return id.toString();
    }

    @Override
    public Number getChapterNumber() {
        return chapter;
    }

    @Override
     public CompletableFuture<List<PageProxy>> retrievePages() throws ExecutionException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return PageFactory.getPages(this);
            } catch (InterruptedException e) {
                logger.error("An error occurred while retrieving pages", e);
                return null;
            }
        });
    }

    @Override
    public CompletableFuture<Manga> getAssociatedManga() {
        return MangaAction.retrieveMangaById(getRelationshipMap().get(RelationshipType.MANGA).get(0).toString());
    }

    @Override
    public CompletableFuture<User> retrieveUploader() throws InterruptedException, ExecutionException {
        return CompletableFuture.supplyAsync(() -> {
            return UploaderFactory.retrieveUploader(this);
        });
    }

    @Override
    public CompletableFuture<List<ScanlationGroup>> retrieveScanlationGroups() throws InterruptedException, ExecutionException {
        return CompletableFuture.supplyAsync(() -> {
            try{
                if (relationshipMap.get(RelationshipType.SCANLATION_GROUP) == null) return Collections.emptyList();
                List<RelationshipData> scanlationGroupRelationships = relationshipMap.get(RelationshipType.SCANLATION_GROUP);
                List<ScanlationGroup> scanlationGroups = new ArrayList<>();
                for (RelationshipData relationshipData : scanlationGroupRelationships){
                    try {
                        scanlationGroups.add(GroupRelationFactory.getScanlationGroup(relationshipData));
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
    
    @Override
    public CompletableFuture<Boolean> markAsRead(JavaDex loggedInInstance){
        return CompletableFuture.supplyAsync(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                try {
                    try (loggedInInstance) {
                        loggedInInstance.markChapterAsRead((Chapter) this).get();
                    }
                    return true;
                }catch (IOException | InterruptedException | ExecutionException e) {
                    return false;
                }
            }
        });
    }
}
