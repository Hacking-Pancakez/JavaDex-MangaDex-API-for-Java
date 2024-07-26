package dev.kurumidisciples.javadex.api.proxies;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.entities.intermediate.ISnowflake;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;
import dev.kurumidisciples.javadex.api.entities.relationship.enums.RelationshipType;

/**
 * Represents a CoverProxy entity.
 */
public class CoverProxy implements ISnowflake {
    private static final String BASE_URL = "https://uploads.mangadex.org/covers/";

    private UUID coverId;
    private OffsetDateTime updatedAt;
    private OffsetDateTime createdAt;
    private Double volume;
    private String locale;
    private String fileName;
    private String description;
    private int version;
    private RelationshipMap relationshipMap;
    private UUID mangaId;

    public CoverProxy(JsonObject data) {
        this.coverId = UUID.fromString(data.get("id").getAsString());
        JsonObject attributes = data.getAsJsonObject("attributes");
        this.description = attributes.has("description") ? attributes.get("description").getAsString() : null;
        this.volume = attributes.has("volume") && !attributes.get("volume").isJsonNull() ? attributes.get("volume").getAsDouble() : null;
        this.locale = attributes.has("locale") ? attributes.get("locale").getAsString() : null;
        this.fileName = attributes.get("fileName").getAsString();
        this.updatedAt = OffsetDateTime.parse(attributes.get("updatedAt").getAsString());
        this.createdAt = OffsetDateTime.parse(attributes.get("createdAt").getAsString());
        this.version = attributes.get("version").getAsInt();
        this.relationshipMap = new RelationshipMap(data.getAsJsonArray("relationships"));
        this.mangaId = relationshipMap.get(RelationshipType.MANGA).get(0).getId();
    }

    public String getIdRaw() {
        return coverId.toString();
    }
    @Override
    public UUID getId() {
        return coverId;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public Double getVolume() {
        return volume;
    }

    public String getLocale() {
        return locale;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDescription() {
        return description;
    }

    public int getVersion() {
        return version;
    }

    public RelationshipMap getRelationshipMap() {
        return relationshipMap;
    }

    public String getUrl() {
        return BASE_URL + mangaId.toString() + "/" + getFileName();
    }

    public InputStream download() throws IOException {
        URL url = new URL(getUrl());
        URLConnection conn = url.openConnection();
        return conn.getInputStream();
    }

    public CompletableFuture<Path> downloadToPath(Path path) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                InputStream in = download();
                Path filePath = path.resolve("cover" + ".jpg");
                Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
                return filePath;
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        });
    }

    public CompletableFuture<File> downloadToFile(File file) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                InputStream in = download();
                Path filePath = file.toPath();
                Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
                return file;
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        });
    }
}
