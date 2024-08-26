package dev.kurumidisciples.javadex.api.entities;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.entities.intermediate.Entity;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipData;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;
import dev.kurumidisciples.javadex.api.entities.relationship.enums.RelationshipType;

/**
 * Represents a user-created list in MangaDex.
 * <p><b>Note:</b> This class does not retrieve all entities as objects in the list; it only contains the list as a {@link dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap}.</p>
 * <p>For example, each item in the list will only include minimal information:</p>
 * <pre><code> {
 *    "id": "be0f4d1c-17f9-4615-a1d5-996672cb3e80",
 *    "type": "manga"
 * }</code></pre>
 * <p>To obtain data about an entity, you will need to retrieve it through other means.</p>
 *
 * @since 0.1.2
 * @author Hacking Pancakez
 */
public class MDList extends Entity {
    
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;
    private final UUID id;
    private final String name;
    private final int version;
    private final RelationshipMap relationshipMap;
    private final Visibility visibility;

    public enum Visibility {
        PUBLIC, PRIVATE;

        public static Visibility fromString(String value){
            return value.equalsIgnoreCase("public") ? PUBLIC : PRIVATE;
        }
    }

    /**
     * <p>Constructor for MDList.</p>
     *
     * @param data a {@link com.google.gson.JsonObject} object
     */
    public MDList(JsonObject data){
        id = UUID.fromString(data.get("id").getAsString());
        JsonObject attributes = data.getAsJsonObject("attributes");
        name = attributes.get("name").getAsString();
        version = attributes.get("version").getAsInt();
        visibility = Visibility.fromString(attributes.get("visibility").getAsString());
        createdAt = OffsetDateTime.parse(attributes.get("createdAt").getAsString());
        updatedAt = OffsetDateTime.parse(attributes.get("updatedAt").getAsString());
        relationshipMap = new RelationshipMap(data.getAsJsonArray("relationships"));
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

    /** {@inheritDoc} */
    @Override
    public UUID getId() {
        return id;
    }

    /**
     * Get the Unique ID of the list as a string.
     *
     * @return a {@link java.lang.String} object
     */
    public String getIdRaw(){
        return id.toString();
    }

    /**
     * Get the name of the list.
     *
     * @return a {@link java.lang.String} object
     */
    public String getName() {
        return name;
    }

    /**
     * Get the version of the list. Can indicate how many times the list has been modified.
     *
     * @return a int
     */
    public int getVersion() {
        return version;
    }

    /**
     * Returns the relationship map of the list which contains the Manga Ids in of the list and sometimes other objects.
     *
     * @return a {@link dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap} object
     */
    public RelationshipMap getRelationshipMap() {
        return relationshipMap;
    }

    /**
     * Returns the visibility of the list. If the list is public, it is visible to the public. If the list is private, it is not visible to the public.
     *
     * @return a {@link dev.kurumidisciples.javadex.api.entities.MDList.Visibility} object
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * If the list is public and, therefore, visible to the public.
     *
     * @return a boolean
     */
    public boolean isPublic(){
        return visibility == Visibility.PUBLIC;
    }

    /**
     * If the list is private and, therefore, not visible to the public.
     *
     * @return a boolean
     */
    public boolean isPrivate(){
        return visibility == Visibility.PRIVATE;
    }

    /**
     * Getter for the ID of the creator of the list.
     *
     * @return a {@link java.util.UUID} object
     */
    public UUID getCreatorId(){
        RelationshipData creator = relationshipMap.get(RelationshipType.USER).get(0);
        return creator.getId();
    }

    /**
     * Getter for the ID of the creator of the list as a string.
     *
     * @return a {@link java.lang.String} object
     */
    public String getCreatorIdRaw(){
        return getCreatorId().toString();
    }
    
    /**
     * Get IDs of all manga in the list.
     *
     * @return a {@link java.util.List} object
     */
    public List<UUID> getMangaIds(){
        return relationshipMap.get(RelationshipType.MANGA).stream().map(RelationshipData::getId).collect(Collectors.toList());
    }
}
