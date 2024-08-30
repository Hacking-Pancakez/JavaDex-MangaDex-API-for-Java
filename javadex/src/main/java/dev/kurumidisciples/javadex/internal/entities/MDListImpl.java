package dev.kurumidisciples.javadex.internal.entities;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import dev.kurumidisciples.javadex.api.entities.MDList;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;
import dev.kurumidisciples.javadex.api.entities.relationship.enums.RelationshipType;
import dev.kurumidisciples.javadex.api.proxies.MangaProxy;
import dev.kurumidisciples.javadex.api.proxies.UserProxy;

public class MDListImpl implements MDList{
    

    private final UUID id;
    private final String name;
    private final int version;
    private final RelationshipMap relationshipMap;
    private final Visibility visibility;

    public MDListImpl(UUID id, String name, int version, RelationshipMap relationshipMap, Visibility visibility) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.relationshipMap = relationshipMap;
        this.visibility = visibility;
    }

    /**
     * Not supported for this entity
     */
    @Override
    public OffsetDateTime getCreatedAt() {
        throw new UnsupportedOperationException("Not supported for this entity");
    }
    /**
     * Not supported for this entity
     */
    @Override
    public OffsetDateTime getUpdatedAt() {
        throw new UnsupportedOperationException("Not supported for this entity");
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getIdRaw(){
        return id.toString();
    }

    public String getName() {
        return name;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public RelationshipMap getRelationshipMap() {
        return relationshipMap;
    }

    @Override
    public Visibility getVisibility() {
        return visibility;
    }

    @Override
    public UUID getCreatorId(){
        return relationshipMap.get(RelationshipType.USER).get(0).getId();
    }

    @Override
    public boolean isPrivate(){
        return visibility == Visibility.PRIVATE;
    }

    @Override
    public boolean isPublic(){
        return visibility == Visibility.PUBLIC;
    }

    @Override
    public String getCreatorIdRaw(){
        return getCreatorId().toString();
    }

    @Override
    public List<MangaProxy> getMangas(){
        return relationshipMap.get(RelationshipType.MANGA).stream().map(data -> new MangaProxy(data.getId())).collect(Collectors.toList());
    }

    @Override
    public UserProxy getCreator(){
        return new UserProxy(getCreatorId());
    }
}
