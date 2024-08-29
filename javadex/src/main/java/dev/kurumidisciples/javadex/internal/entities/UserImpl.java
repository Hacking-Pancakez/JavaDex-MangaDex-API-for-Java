package dev.kurumidisciples.javadex.internal.entities;

import java.util.List;
import java.util.UUID;

import dev.kurumidisciples.javadex.api.entities.User;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;

public class UserImpl implements User {
    
    private final List<Role> roles;
    private final UUID id;
    private final String username;
    private final RelationshipMap relationshipMap;
    private final Integer version;
    

    public UserImpl(UUID id, String username, List<Role> roles, RelationshipMap relationshipMap, Integer version) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.relationshipMap = relationshipMap;
        this.version = version;
    }

    @Override
    public List<Role> getRoles() {
        return roles;
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
    public String getUsername() {
        return username;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public RelationshipMap getRelationshipMap() {
        return relationshipMap;
    }

}
