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
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
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

    /**
     * <p>Constructor for CoverProxy.</p>
     *
     * @param data a {@link com.google.gson.JsonObject} object
     */
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

    
    /**
     * <p>getIdRaw.</p>
     *
     * @return String
     */
    public String getIdRaw() {
        return coverId.toString();
    }
    /** {@inheritDoc} */
    @Override
    public UUID getId() {
        return coverId;
    }

    /**
     * <p>Getter for the field <code>updatedAt</code>.</p>
     *
     * @return a {@link java.time.OffsetDateTime} object
     */
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * <p>Getter for the field <code>createdAt</code>.</p>
     *
     * @return a {@link java.time.OffsetDateTime} object
     */
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * <p>Getter for the field <code>volume</code>.</p>
     *
     * @return a {@link java.lang.Double} object
     */
    public Double getVolume() {
        return volume;
    }

    /**
     * <p>Getter for the field <code>locale</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getLocale() {
        return locale;
    }

    /**
     * <p>Getter for the field <code>fileName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * <p>Getter for the field <code>description</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getDescription() {
        return description;
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
     * <p>Getter for the field <code>relationshipMap</code>.</p>
     *
     * @return a {@link dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap} object
     */
    public RelationshipMap getRelationshipMap() {
        return relationshipMap;
    }

    /**
     * <p>getUrl.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getUrl() {
        return BASE_URL + mangaId.toString() + "/" + getFileName();
    }

    /**
     * <p>download.</p>
     *
     * @return a {@link java.io.InputStream} object
     * @throws java.io.IOException if any.
     */
    public InputStream download() throws IOException {
        URL url = new URL(getUrl());
        URLConnection conn = url.openConnection();
        return conn.getInputStream();
    }

    /**
     * <p>downloadToPath.</p>
     *
     * @param path a {@link java.nio.file.Path} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
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

    /**
     * <p>downloadToFile.</p>
     *
     * @param file a {@link java.io.File} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
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
