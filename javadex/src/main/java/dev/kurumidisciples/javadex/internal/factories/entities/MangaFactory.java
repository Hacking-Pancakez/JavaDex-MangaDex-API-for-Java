package dev.kurumidisciples.javadex.internal.factories.entities;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.entities.Manga;
import dev.kurumidisciples.javadex.api.entities.enums.Locale;
import dev.kurumidisciples.javadex.api.entities.enums.State;
import dev.kurumidisciples.javadex.api.entities.enums.manga.LinkType;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.ContentRating;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Demographic;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Status;
import dev.kurumidisciples.javadex.api.entities.enums.manga.filters.Tag;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;
import dev.kurumidisciples.javadex.api.entities.relationship.enums.RelationshipType;
import dev.kurumidisciples.javadex.internal.entities.MangaImpl;
import dev.kurumidisciples.javadex.internal.parsers.MangaParsers;

public class MangaFactory {
    

    public static Manga createEntity(@NotNull JsonObject jsonObject){
        JsonObject attributes = jsonObject.getAsJsonObject("attributes");
        UUID id = UUID.fromString(jsonObject.get("id").getAsString());
        String title = attributes.getAsJsonObject("title").has("en") ? attributes.getAsJsonObject("title").get("en").getAsString() : "No title";

        Map<Locale, String> description = MangaParsers.parseDescription(attributes.getAsJsonObject("description"));
        Map<Locale, List<String>> altTitles = MangaParsers.parseAltTitles(attributes.getAsJsonArray("altTitles"));
        boolean isLocked = attributes.get("isLocked").getAsBoolean();
        Locale originalLanguage = Locale.getByLanguage(attributes.get("originalLanguage").getAsString());
        Number lastVolume = attributes.has("lastVolume") && !attributes.get("lastVolume").isJsonNull() ? attributes.get("lastVolume").getAsNumber() : null;
        Number lastChapter = attributes.has("lastChapter") && !attributes.get("lastChapter").isJsonNull() ? attributes.get("lastChapter").getAsNumber() : null;
        Demographic publicationDemographic = Demographic.getDemographic(attributes.has("publicationDemographic") && !attributes.get("publicationDemographic").isJsonNull() ? attributes.get("publicationDemographic").getAsString() : "Unknown");
        Status status = Status.getStatus(attributes.get("status").getAsString());
        long year = attributes.has("year") && !attributes.get("year").isJsonNull() ? attributes.get("year").getAsLong() : null;
        ContentRating contentRating = ContentRating.getContentRating(attributes.get("contentRating").getAsString());
        State state = State.getByValue(attributes.get("state").getAsString());
        Map<LinkType, String> links = MangaParsers.parseLinks(attributes.getAsJsonObject("links"));
        boolean chapterNumbersResetOnNewVolume = attributes.get("chapterNumbersResetOnNewVolume").getAsBoolean();
        OffsetDateTime createdAt = OffsetDateTime.parse(attributes.get("createdAt").getAsString());
        OffsetDateTime updatedAt = OffsetDateTime.parse(attributes.get("updatedAt").getAsString());
        int version = attributes.get("version").getAsInt();
        String latestUploadedChapterId = attributes.has("latestUploadedChapter") && !attributes.get("latestUploadedChapter").isJsonNull() ? attributes.get("latestUploadedChapter").getAsString() : null;
        List<Tag> tags = MangaParsers.parseTags(attributes.getAsJsonArray("tags"));
        List<Locale> availableTranslatedLanguages = MangaParsers.parseAvailableTranslatedLanguages(attributes.getAsJsonArray("availableTranslatedLanguages"));
        RelationshipMap relationshipMap = new RelationshipMap(jsonObject.getAsJsonArray("relationships"));
        UUID author = relationshipMap.get(RelationshipType.AUTHOR).get(0).getId();

        return new MangaImpl(id, title, description, altTitles, isLocked, originalLanguage, lastVolume, lastChapter, publicationDemographic, status, year, contentRating, state, links, chapterNumbersResetOnNewVolume, createdAt, updatedAt, version, latestUploadedChapterId, tags, availableTranslatedLanguages, relationshipMap, author);
    }
}
