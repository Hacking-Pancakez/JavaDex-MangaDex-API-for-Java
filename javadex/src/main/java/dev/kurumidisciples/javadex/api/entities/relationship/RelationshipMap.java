package dev.kurumidisciples.javadex.api.entities.relationship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.entities.relationship.enums.RelationshipRelated;
import dev.kurumidisciples.javadex.api.entities.relationship.enums.RelationshipType;

/**
 * Represents a mapping of relationship types to lists of relationship data.
 * <p>This class extends {@link java.util.HashMap} to map {@link dev.kurumidisciples.javadex.api.entities.relationship.enums.RelationshipType} to {@link java.util.List} of {@link dev.kurumidisciples.javadex.api.entities.relationship.RelationshipData}.</p>
 * <p>It is constructed using a {@link com.google.gson.JsonArray} which is converted into the map structure.</p>
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public class RelationshipMap extends HashMap<RelationshipType, List<RelationshipData>> {

    /**
     * Constructs a new RelationshipMap from a JSON array.
     * The JSON array is expected to contain relationship objects which are then converted into a map
     * where each key is a {@link dev.kurumidisciples.javadex.api.entities.relationship.enums.RelationshipType} and the value is a list of {@link dev.kurumidisciples.javadex.api.entities.relationship.RelationshipData} related to that type.
     *
     * @param relationshipArray The JSON array containing relationship objects.
     */
    public RelationshipMap(JsonArray relationshipArray) {
        this.putAll(convertToMap(relationshipArray));
    }

    /**
     * Converts a JSON array of relationship objects into a map of relationship types to lists of relationship data.
     * Each object in the array is expected to have a "type" and an "id", and optionally a "related" field.
     * The "type" is used as the key in the map, and the "id" (and optionally "related") are used to construct {@link RelationshipData} objects.
     *
     * @param relationshipArray The JSON array containing relationship objects.
     * @return A map of {@link RelationshipType} to lists of {@link RelationshipData}.
     */
    private static Map<RelationshipType, List<RelationshipData>> convertToMap(JsonArray relationshipArray) {
        Map<RelationshipType, List<RelationshipData>> relationshipMap = new HashMap<>();

        for (JsonElement element : relationshipArray){
            JsonObject relationship = element.getAsJsonObject();
            RelationshipType type = RelationshipType.fromString(relationship.get("type").getAsString());

            RelationshipData data;
            if (relationship.has("related")){
                data = new RelationshipData(UUID.fromString(relationship.get("id").getAsString()), RelationshipRelated.fromString(relationship.get("related").getAsString()));
            } else {
                data = new RelationshipData(UUID.fromString(relationship.get("id").getAsString()));
            }

            if (relationshipMap.containsKey(type)){
                relationshipMap.get(type).add(data);
            } else {
                List<RelationshipData> ids = new ArrayList<>();
                ids.add(data);
                relationshipMap.put(type, ids);
            }
        }
        return relationshipMap;
    }
}
