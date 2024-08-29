package dev.kurumidisciples.javadex.api.entities;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.apache.maven.api.annotations.Nullable;

import dev.kurumidisciples.javadex.api.entities.enums.Locale;
import dev.kurumidisciples.javadex.api.entities.intermediate.IRelationHolder;
import dev.kurumidisciples.javadex.api.entities.intermediate.ISnowflake;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;

public interface ScanlationGroup extends ISnowflake, IRelationHolder {
    
    /**
     * Returns the relationship map of the group. Indicates how the group is related to other entities.
     */
    @Override
   RelationshipMap getRelationshipMap();
   /**
    * Returns the website of the group. <b>Might not be a vaild URL.</b>
    */
   String getWebsite();
   /**
    * Returns the IRC server of the group.
    */
   String getIrcServer();
    /**
     * Returns the IRC channel of the group.
     */
   String getIrcChannel();
   /**
    * Returns the invite code for the group's Discord server.
    */
   String getDiscord();
    /**
     * Returns the name of the group.
     */
   String getName();
    /**
     * Returns the email of the group.
     */
   String getContactEmail();
   /**
    * Returns the description of the group.
    */
   String getDescription();
   /**
    * Returns the twitter url of the group.
    */
   String getTwitter();
    /**
     * Returns the manga updates url of the group.
     */
   String getMangaUpdates();
   /**
    * Returns the languages the group is focused on.
    */
   List<Locale> getFocusedLanguages();
   /**
    * Indicates whether anyone not apart of the group can upload under as that group.
    */
   boolean isLocked();
   /**
    * Typically indicates whether the group is an official publisher of licensed content.
    */
   boolean isOfficial();
    /**
     * Indicates whether the group is verified.
     */
   boolean isVerified();
   /**
    * Info needed.
    * <p>Should respected ISO 8601 duration specification: https://en.wikipedia.org/wiki/ISO_8601#Durations</p>
    */
   Duration publishDelay();
   /**
    * IDK
    */
   boolean isExLicensed();
   /**
    * After an unknown period of inactivity, the group is marked as inactive.
    */
   boolean isInactive();
   /**
    * Indicates how many times the group has been modified.
    */
   Integer getVersion();
   /**
    * Returns the members of the group as UUIDs.
    */
    @Nullable
   List<UUID> getMembers();
    /**
     * Returns the leader of the group as a UUID.
     */
    @Nullable
   UUID getLeader();
}
