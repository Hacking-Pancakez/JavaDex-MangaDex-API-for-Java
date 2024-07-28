package dev.kurumidisciples.javadex.api.entities;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.entities.intermediate.Entity;
import dev.kurumidisciples.javadex.api.entities.intermediate.ISnowflake;
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
     * <p>getIdRaw.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getIdRaw(){
        return id.toString();
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
     * <p>Getter for the field <code>visibility</code>.</p>
     *
     * @return a {@link dev.kurumidisciples.javadex.api.entities.MDList.Visibility} object
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * <p>isPublic.</p>
     *
     * @return a boolean
     */
    public boolean isPublic(){
        return visibility == Visibility.PUBLIC;
    }

    /**
     * <p>isPrivate.</p>
     *
     * @return a boolean
     */
    public boolean isPrivate(){
        return visibility == Visibility.PRIVATE;
    }

    /**
     * <p>getCreatorId.</p>
     *
     * @return a {@link java.util.UUID} object
     */
    public UUID getCreatorId(){
        RelationshipData creator = relationshipMap.get(RelationshipType.USER).get(0);
        return creator.getId();
    }

    /**
     * <p>getCreatorIdRaw.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getCreatorIdRaw(){
        return getCreatorId().toString();
    }
    
    /**
     * <p>getMangaIds.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<UUID> getMangaIds(){
        return relationshipMap.get(RelationshipType.MANGA).stream().map(RelationshipData::getId).collect(Collectors.toList());
    }
}
