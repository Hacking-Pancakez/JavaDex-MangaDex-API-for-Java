package dev.kurumidisciples.javadex.internal.entities;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import dev.kurumidisciples.javadex.api.entities.ScanlationGroup;
import dev.kurumidisciples.javadex.api.entities.enums.Locale;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipData;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;
import dev.kurumidisciples.javadex.api.entities.relationship.enums.RelationshipType;

public class ScanlationGroupImpl implements ScanlationGroup {
    
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
    private List<Locale> focusedLanguage = new ArrayList<>();
    private final boolean locked;
    private final boolean exLicensed;
    private final boolean official;
    private final boolean inactive;
    private final boolean isVerified;
    private final Integer version;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;
    private final RelationshipMap relationshipMap;
    private final Duration publishDelay;

    public ScanlationGroupImpl(UUID id, String name, String website, String ircServer, String ircChannel, String discord, String contactEmail, String description, String twitter, String mangaUpdates, List<Locale> focusedLanguage, boolean locked, boolean exLicensed, boolean official, boolean inactive, Integer version, OffsetDateTime createdAt, OffsetDateTime updatedAt, RelationshipMap relationshipMap, boolean isVerified, Duration publishDelay) {
        this.id = id;
        this.name = name;
        this.website = website;
        this.ircServer = ircServer;
        this.ircChannel = ircChannel;
        this.discord = discord;
        this.contactEmail = contactEmail;
        this.description = description;
        this.twitter = twitter;
        this.mangaUpdates = mangaUpdates;
        this.focusedLanguage = focusedLanguage;
        this.locked = locked;
        this.exLicensed = exLicensed;
        this.official = official;
        this.inactive = inactive;
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.relationshipMap = relationshipMap;
        this.isVerified = isVerified;
        this.publishDelay = publishDelay;
    }


    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getIdRaw(){
        return id.toString();
    }

    @Override
    public RelationshipMap getRelationshipMap() {
        return relationshipMap;
    }

    @Override
    public String getWebsite() {
        return website;
    }

    @Override
    public String getIrcServer() {
        return ircServer;
    }

    @Override
    public String getIrcChannel() {
        return ircChannel;
    }

    @Override
    public String getDiscord() {
        return discord;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getContactEmail() {
        return contactEmail;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getTwitter() {
        return twitter;
    }

    @Override
    public String getMangaUpdates() {
        return mangaUpdates;
    }

    @Override
    public List<Locale> getFocusedLanguages() {
        return focusedLanguage;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public boolean isExLicensed() {
        return exLicensed;
    }

    @Override
    public boolean isOfficial() {
        return official;
    }

    @Override
    public boolean isInactive() {
        return inactive;
    }

    @Override
    public Integer getVersion() {
        return version;
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

    @Override
    public boolean isVerified() {
        return isVerified;
    }

    @Override
    public UUID getLeader() {
        return relationshipMap.get(RelationshipType.LEADER).get(0).getId();
    }

    @Override
    public Duration publishDelay() {
        return publishDelay;
    }



}
