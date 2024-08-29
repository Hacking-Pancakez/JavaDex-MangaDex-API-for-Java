package dev.kurumidisciples.javadex.internal.factories.entities;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.entities.MDList;
import dev.kurumidisciples.javadex.api.entities.MDList.Visibility;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;
import dev.kurumidisciples.javadex.internal.entities.MDListImpl;

public class MDListFactory {
    

    public static MDList createEntity(@NotNull JsonObject data){
        UUID id = UUID.fromString(data.get("id").getAsString());
        JsonObject attributes = data.getAsJsonObject("attributes");
        String name = attributes.get("name").getAsString();
        int version = attributes.get("version").getAsInt();
        Visibility visibility = Visibility.fromString(attributes.get("visibility").getAsString());
        RelationshipMap relationshipMap = new RelationshipMap(data.getAsJsonArray("relationships"));

        return new MDListImpl(id, name, version, relationshipMap, visibility);
    }
}
