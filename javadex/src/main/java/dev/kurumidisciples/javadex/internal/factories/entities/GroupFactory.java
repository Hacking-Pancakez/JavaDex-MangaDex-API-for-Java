package dev.kurumidisciples.javadex.internal.factories.entities;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.entities.ScanlationGroup;
import dev.kurumidisciples.javadex.api.entities.enums.Locale;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;
import dev.kurumidisciples.javadex.internal.entities.ScanlationGroupImpl;

public class GroupFactory {
    

    public static ScanlationGroup createEntity(@NotNull JsonObject data){
        UUID id = UUID.fromString(data.get("id").getAsString());
        JsonObject attributes = data.getAsJsonObject("attributes");
        String name = attributes.get("name").getAsString();
        String website = attributes.has("website") && !attributes.get("website").isJsonNull() ? attributes.get("website").getAsString() : null;
        String ircServer = attributes.has("ircServer") && !attributes.get("ircServer").isJsonNull() ? attributes.get("ircServer").getAsString() : null;
        String ircChannel = attributes.has("ircChannel") && !attributes.get("ircChannel").isJsonNull() ? attributes.get("ircChannel").getAsString() : null;
        String discord = attributes.has("discord") && !attributes.get("discord").isJsonNull() ? attributes.get("discord").getAsString() : null;
        String contactEmail = attributes.has("contactEmail") && !attributes.get("contactEmail").isJsonNull() ? attributes.get("contactEmail").getAsString() : null;
        String description = attributes.has("description") && !attributes.get("description").isJsonNull() ? attributes.get("description").getAsString() : null;
        String twitter = attributes.has("twitter") && !attributes.get("twitter").isJsonNull() ? attributes.get("twitter").getAsString() : null;
        String mangaUpdates = attributes.has("mangaUpdates") && !attributes.get("mangaUpdates").isJsonNull() ? attributes.get("mangaUpdates").getAsString() : null;
        boolean locked = attributes.get("locked").getAsBoolean();
        boolean official = attributes.get("official").getAsBoolean();
        boolean inactive = attributes.get("inactive").getAsBoolean();
        boolean isVerified = attributes.get("verified").getAsBoolean();
        int version = attributes.get("version").getAsInt();
        boolean exLicensed = attributes.has("exLicensed") ? attributes.get("exLicensed").getAsBoolean() : false;
        OffsetDateTime createdAt = OffsetDateTime.parse(attributes.get("createdAt").getAsString());
        OffsetDateTime updatedAt = OffsetDateTime.parse(attributes.get("updatedAt").getAsString());
        Duration publishDelay = attributes.has("publishDelay") && !attributes.get("publishDelay").isJsonNull() ? Duration.parse(attributes.get("publishDelay").getAsString()) : null;
        RelationshipMap relationshipMap = new RelationshipMap(data.getAsJsonArray("relationships"));
        List<Locale> focusedLanguage = attributes.has("focusedLanguage") ? attributes.getAsJsonArray("focusedLanguage").asList().stream().map(e -> Locale.getByLanguage(e.getAsString())).collect(Collectors.toList()) : null;

        return new ScanlationGroupImpl(id, name, website, ircServer, ircChannel, discord, contactEmail, description, twitter, mangaUpdates, focusedLanguage, locked, exLicensed, official, inactive, version, createdAt, updatedAt, relationshipMap, isVerified, publishDelay);
    }
}
