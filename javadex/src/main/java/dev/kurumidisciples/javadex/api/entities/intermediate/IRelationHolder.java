package dev.kurumidisciples.javadex.api.entities.intermediate;

import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;

/**
 * Represents an entity that holds relationships to other entities.
 * @author Hacking Pancakez
 */
public interface IRelationHolder {
    
    /**
     * Returns how the entity is related to other entities.
     */
    RelationshipMap getRelationshipMap();

}
