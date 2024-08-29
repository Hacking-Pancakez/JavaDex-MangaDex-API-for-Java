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

import dev.kurumidisciples.javadex.api.entities.intermediate.IRelationHolder;
import dev.kurumidisciples.javadex.api.entities.intermediate.ISnowflake;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;
import dev.kurumidisciples.javadex.api.entities.relationship.enums.RelationshipType;


/**
 * Represents a proxy for a cover entity on MangaDex, providing access to cover details
 * and functionality to download the cover image.
 *
 * <p>This class encapsulates the data of a MangaDex cover, such as its ID, creation 
 * and update timestamps, volume, locale, and associated file details. It also 
 * includes methods to download the cover image either as an {@link InputStream}, 
 * save it to a specified {@link Path}, or write it to a {@link File}.</p>
 * 
 * <p>Instances of this class are typically created by parsing a {@link JsonObject}
 * that contains cover data.</p>
 *
 * <p>Author: Hacking Pancakez</p>
 */
public class CoverProxy implements ISnowflake, IRelationHolder {
    private static final String BASE_URL = "https://uploads.mangadex.org/covers/";

    private final UUID coverId;
    private final OffsetDateTime updatedAt;
    private final OffsetDateTime createdAt;
    private final Number volume;
    private final String locale;
    private final String fileName;
    private final String description;
    private final int version;
    private final RelationshipMap relationshipMap;
    private final UUID mangaId;

    /**
     * Constructs a new {@code CoverProxy} instance by parsing the provided JSON data.
     *
     * @param data a {@link JsonObject} containing the cover data
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
     * Returns the raw string representation of the cover's UUID.
     *
     * @return the cover's UUID as a string
     */
    @Override
    public String getIdRaw() {
        return coverId.toString();
    }

    /** {@inheritDoc} */
    @Override
    public UUID getId() {
        return coverId;
    }

    /**
     * Returns the timestamp when this cover was last updated.
     *
     * @return the {@link OffsetDateTime} of the last update
     */
    @Override
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Returns the timestamp when this cover was created.
     *
     * @return the {@link OffsetDateTime} of creation
     */
    @Override
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the volume number associated with this cover, if available.
     *
     * @return the volume number, or {@code null} if not specified
     */
    public Number getVolume() {
        return volume;
    }

    /**
     * Returns the locale associated with this cover, if available.
     *
     * @return the locale as a {@link String}, or {@code null} if not specified
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Returns the file name of this cover.
     *
     * @return the file name as a {@link String}
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Returns the description of this cover, if available.
     *
     * @return the description as a {@link String}, or {@code null} if not specified
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the version number of this cover.
     *
     * @return the version number as an {@link Integer}
     */
    @Override
    public Integer getVersion() {
        return version;
    }

    /**
     * Returns the map of relationships associated with this cover.
     *
     * @return a {@link RelationshipMap} object containing the cover's relationships
     */
    @Override
    public RelationshipMap getRelationshipMap() {
        return relationshipMap;
    }

    /**
     * Constructs and returns the URL for downloading this cover image.
     *
     * @return the cover image URL as a {@link String}
     */
    public String getUrl() {
        return BASE_URL + mangaId.toString() + "/" + getFileName();
    }

    /**
     * Downloads the cover image as an {@link InputStream}.
     *
     * @return an {@link InputStream} of the cover image
     * @throws IOException if an I/O error occurs
     */
    public InputStream download() throws IOException {
        URL url = new URL(getUrl());
        URLConnection conn = url.openConnection();
        return conn.getInputStream();
    }

    /**
     * Downloads the cover image and saves it to the specified {@link Path}.
     *
     * @param path the {@link Path} to save the cover image to
     * @return a {@link CompletableFuture} that completes with the path to the saved image
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
     * Downloads the cover image and saves it to the specified {@link File}.
     *
     * @param file the {@link File} to save the cover image to
     * @return a {@link CompletableFuture} that completes with the saved file
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
