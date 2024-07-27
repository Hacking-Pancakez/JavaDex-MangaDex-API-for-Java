package dev.kurumidisciples.javadex.internal.parsers;

import com.google.gson.JsonObject;
import dev.kurumidisciples.javadex.api.entities.enums.Locale;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;

import java.time.OffsetDateTime;
import java.util.UUID;

public class ChapterParser {

    private final JsonObject chapterJson;

    public ChapterParser(JsonObject chapterJson) {
        this.chapterJson = chapterJson;
    }

    public UUID getId() {
        return UUID.fromString(chapterJson.get("id").getAsString());
    }

    public Number getVolume() {
        JsonObject attributes = chapterJson.getAsJsonObject("attributes");
        return attributes.has("volume") && !attributes.get("volume").isJsonNull() ? attributes.get("volume").getAsNumber() : 0;
    }

    public Number getChapter() {
        JsonObject attributes = chapterJson.getAsJsonObject("attributes");
        return attributes.has("chapter") && !attributes.get("chapter").isJsonNull() ? attributes.get("chapter").getAsNumber() : 0;
    }

    public String getTitle() {
        JsonObject attributes = chapterJson.getAsJsonObject("attributes");
        return attributes.has("title") && !attributes.get("title").isJsonNull() ? attributes.get("title").getAsString() : null;
    }

    public int getVersion() {
        return chapterJson.getAsJsonObject("attributes").get("version").getAsInt();
    }

    public Locale getTranslatedLanguage() {
        return Locale.getByLanguage(chapterJson.getAsJsonObject("attributes").get("translatedLanguage").getAsString());
    }

    public int getPages() {
        return chapterJson.getAsJsonObject("attributes").get("pages").getAsInt();
    }

    public OffsetDateTime getCreatedAt() {
        return OffsetDateTime.parse(chapterJson.getAsJsonObject("attributes").get("createdAt").getAsString());
    }

    public OffsetDateTime getUpdatedAt() {
        return OffsetDateTime.parse(chapterJson.getAsJsonObject("attributes").get("updatedAt").getAsString());
    }

    public OffsetDateTime getPublishAt() {
        return OffsetDateTime.parse(chapterJson.getAsJsonObject("attributes").get("publishAt").getAsString());
    }

    public OffsetDateTime getReadableAt() {
        return OffsetDateTime.parse(chapterJson.getAsJsonObject("attributes").get("readableAt").getAsString());
    }

    public RelationshipMap getRelationshipMap() {
        return new RelationshipMap(chapterJson.getAsJsonArray("relationships"));
    }
}
