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
 * <p><b>Note:</b> This class does not retrieve all entities as objects in the list; it only contains the list as a {@link RelationshipMap}.</p>
 * <p>For example, each item in the list will only include minimal information:</p>
 * <pre><code> {
 *    "id": "be0f4d1c-17f9-4615-a1d5-996672cb3e80",
 *    "type": "manga"
 * }</code></pre>
 * <p>To obtain data about an entity, you will need to retrieve it through other means.</p>
 * 
 * @since 0.1.2
 */
public class MDList extends Entity implements ISnowflake {
    
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

    @Override
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public String getIdRaw(){
        return id.toString();
    }

    public String getName() {
        return name;
    }

    public int getVersion() {
        return version;
    }

    public RelationshipMap getRelationshipMap() {
        return relationshipMap;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public boolean isPublic(){
        return visibility == Visibility.PUBLIC;
    }

    public boolean isPrivate(){
        return visibility == Visibility.PRIVATE;
    }

    public UUID getCreatorId(){
        RelationshipData creator = relationshipMap.get(RelationshipType.USER).get(0);
        return creator.getId();
    }

    public String getCreatorIdRaw(){
        return getCreatorId().toString();
    }
    
    public List<UUID> getMangaIds(){
        return relationshipMap.get(RelationshipType.MANGA).stream().map(RelationshipData::getId).collect(Collectors.toList());
    }
}
