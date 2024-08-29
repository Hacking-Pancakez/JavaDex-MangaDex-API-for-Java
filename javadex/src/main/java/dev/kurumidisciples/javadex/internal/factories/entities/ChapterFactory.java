package dev.kurumidisciples.javadex.internal.factories.entities;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.entities.Chapter;
import dev.kurumidisciples.javadex.api.entities.enums.Locale;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;
import dev.kurumidisciples.javadex.internal.entities.ChapterImpl;
import dev.kurumidisciples.javadex.internal.parsers.ChapterParser;

public class ChapterFactory {

    public static Chapter createEntity(@NotNull JsonObject jsonObject){
        ChapterParser parser = new ChapterParser(jsonObject);
        UUID id = parser.getId();
        Number volume = parser.getVolume();
        Number chapter = parser.getChapter();
        String title = parser.getTitle();
        int version = parser.getVersion();
        Locale translatedLanguage = parser.getTranslatedLanguage();
        int pages = parser.getPages();
        OffsetDateTime createdAt = parser.getCreatedAt();
        OffsetDateTime updatedAt = parser.getUpdatedAt();
        OffsetDateTime publishedAt = parser.getPublishAt();
        OffsetDateTime readableAt = parser.getReadableAt();
        RelationshipMap relationshipMap = parser.getRelationshipMap();
        return new ChapterImpl(id, volume, chapter, title, version, translatedLanguage, pages, createdAt, updatedAt, publishedAt, readableAt, relationshipMap);
    }

}
