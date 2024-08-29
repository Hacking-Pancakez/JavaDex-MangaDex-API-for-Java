package dev.kurumidisciples.javadex.internal.factories.entities;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonObject;

import dev.kurumidisciples.javadex.api.entities.User;
import dev.kurumidisciples.javadex.api.entities.User.Role;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;
import dev.kurumidisciples.javadex.internal.entities.UserImpl;

public class UserFactory {
    
    public static User createEntity(@NotNull JsonObject data){
        UUID id = UUID.fromString(data.get("id").getAsString());
        JsonObject attributes = data.getAsJsonObject("attributes");
        String username = attributes.get("username").getAsString();
        List<Role> roles = attributes.getAsJsonArray("roles").asList().stream().map(e -> Role.getRole(e.getAsString())).collect(Collectors.toList());
        Integer version = attributes.get("version").getAsInt();
        RelationshipMap relationshipMap = new RelationshipMap(data.getAsJsonArray("relationships"));
        return new UserImpl(id, username, roles, relationshipMap, version);
    }
}
