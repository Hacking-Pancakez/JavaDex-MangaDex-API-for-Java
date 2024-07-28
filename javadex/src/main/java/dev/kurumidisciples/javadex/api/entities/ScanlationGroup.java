package dev.kurumidisciples.javadex.api.entities;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.entities.intermediate.Entity;
import dev.kurumidisciples.javadex.api.entities.intermediate.ISnowflake;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipData;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;
import dev.kurumidisciples.javadex.api.entities.relationship.enums.RelationshipType;

/**
 * Represents a ScanlationGroup entity.
 *
 * @author Hacking Pancakez
 */
public class ScanlationGroup extends Entity {

    private final UUID id;
    private final String name;
    private final String website;
    private final String ircServer;
    private final String ircChannel;
    private final String discord;
    private final String contactEmail;
    private final String description;
    private final String twitter;
    private final String mangaUpdates;
    private final List<String> focusedLanguage = new ArrayList<>();
    private final boolean locked;
    private final boolean exLicensed;
    private final boolean official;
    private final boolean inactive;
    private final Integer version;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;
    private final RelationshipMap relationshipMap;

    /**
     * <p>Constructor for ScanlationGroup.</p>
     *
     * @param data a {@link com.google.gson.JsonObject} object
     */
    public ScanlationGroup(JsonObject data) {
        this.id = UUID.fromString(data.get("id").getAsString());
        JsonObject attributes = data.getAsJsonObject("attributes");
        this.name = attributes.get("name").getAsString();
        this.website = attributes.has("website") && !attributes.get("website").isJsonNull() ? attributes.get("website").getAsString() : null;
        this.ircServer = attributes.has("ircServer") && !attributes.get("ircServer").isJsonNull() ? attributes.get("ircServer").getAsString() : null;
        this.ircChannel = attributes.has("ircChannel") && !attributes.get("ircChannel").isJsonNull() ? attributes.get("ircChannel").getAsString() : null;
        this.discord = attributes.has("discord") && !attributes.get("discord").isJsonNull() ? attributes.get("discord").getAsString() : null;
        this.contactEmail = attributes.has("contactEmail") && !attributes.get("contactEmail").isJsonNull() ? attributes.get("contactEmail").getAsString() : null;
        this.description = attributes.has("description") && !attributes.get("description").isJsonNull() ? attributes.get("description").getAsString() : null;
        this.twitter = attributes.has("twitter") && !attributes.get("twitter").isJsonNull() ? attributes.get("twitter").getAsString() : null;
        this.mangaUpdates = attributes.has("mangaUpdates") && !attributes.get("mangaUpdates").isJsonNull() ? attributes.get("mangaUpdates").getAsString() : null;
        this.locked = attributes.get("locked").getAsBoolean();
        this.official = attributes.get("official").getAsBoolean();
        this.inactive = attributes.get("inactive").getAsBoolean();
        this.version = attributes.get("version").getAsInt();
        this.exLicensed = attributes.has("exLicensed") ? attributes.get("exLicensed").getAsBoolean() : false;
        this.createdAt = OffsetDateTime.parse(attributes.get("createdAt").getAsString());
        this.updatedAt = OffsetDateTime.parse(attributes.get("updatedAt").getAsString());
        this.relationshipMap = new RelationshipMap(data.getAsJsonArray("relationships"));
    }

    /**
     * Returns the relationship map of the group. This map contains the relationships of the group with other entities.
     *
     * @return a {@link dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap} object
     */
    public RelationshipMap getRelationshipMap() {
        return relationshipMap;
    }

    /**
     * Returns the UUID of the group as a string.
     *
     * @return a {@link java.lang.String} object
     */
    public String getIdRaw() {
        return id.toString();
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

    /**
     * {@inheritDoc}
     *
     * Returns the UUID of the group.
     */
    @Override
    public UUID getId() {
        return id;
    }

    /**
     * Returns the website as set by the group.
     *
     * @return a {@link java.lang.String} object
     */
    public String getWebsite() {
        return website;
    }

    /**
     * <p>Getter for the field <code>ircServer</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getIrcServer() {
        return ircServer;
    }

    /**
     * <p>Getter for the field <code>ircChannel</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getIrcChannel() {
        return ircChannel;
    }

    /**
     * Returns the invite code for the discord server provided by the group.
     *
     * @return a {@link java.lang.String} object
     */
    public String getDiscord() {
        return discord;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Getter for the field <code>contactEmail</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getContactEmail() {
        return contactEmail;
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
     * <p>Getter for the field <code>twitter</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getTwitter() {
        return twitter;
    }

    /**
     * <p>Getter for the field <code>mangaUpdates</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getMangaUpdates() {
        return mangaUpdates;
    }

    /**
     * <p>Getter for the field <code>focusedLanguage</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<String> getFocusedLanguage() {
        return focusedLanguage;
    }

    /**
     * Returns whether the group is locked. This usually means that no one besides group members can upload chapters.
     *
     * @return a boolean
     */
    public boolean isLocked() {
        return locked;
    }
    
    /**
     * <p>isOfficial.</p>
     *
     * @return a boolean
     */
    public boolean isOfficial() {
        return official;
    }

    /**
     * <p>isExLicensed.</p>
     *
     * @return a boolean
     */
    public boolean isExLicensed() {
        return exLicensed;
    }

    /**
     * <p>isInactive.</p>
     *
     * @return a boolean
     */
    public boolean isInactive() {
        return inactive;
    }

    /**
     * Returns the version of the entity. Indicating how many times the group has been updated.
     *
     * @return a {@link java.lang.Integer} object
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * Returns the members of the group. <b>This includes both members and the leader.</b>
     *
     * @return A list of UUIDs of the members of the group.
     */
    public List<UUID> getMembers() {
        List<UUID> memberIds = new ArrayList<>();

        Stream.of(RelationshipType.MEMBER, RelationshipType.LEADER)
            .map(relationshipMap::get)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .map(RelationshipData::getId)
            .forEach(memberIds::add);

        return memberIds;
    }
    /**
     * Returns the leader of the group.
     *
     * @return The UUID of the leader of the group.
     */
    public UUID getLeader(){
        return relationshipMap.get(RelationshipType.LEADER).get(0).getId();
    }
}
