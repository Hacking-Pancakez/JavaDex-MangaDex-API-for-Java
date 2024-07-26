package dev.kurumidisciples.javadex.api.entities.relationship;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.kurumidisciples.javadex.api.entities.relationship.enums.RelationshipRelated;

/**
 * This class represents the relationship data in the application.
 * It contains the ID of the relationship and the related entity.
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public class RelationshipData {
    
    private final UUID id;
    private final RelationshipRelated related;

    /**
     * Constructs a new RelationshipData with the specified ID and related entity.
     *
     * @param id the ID of the relationship
     * @param related the related entity
     */
    public RelationshipData(@NotNull UUID id, RelationshipRelated related) {
        this.id = id;
        this.related = related;
    }

    /**
     * Constructs a new RelationshipData with the specified ID and a null related entity.
     *
     * @param id the ID of the relationship
     */
    public RelationshipData(@NotNull UUID id){
        this(id, RelationshipRelated.UNKNOWN);
    }
    
    /**
     * Returns the ID of this relationship.
     *
     * @return the ID of this relationship
     */
    @NotNull
    public UUID getId() {
        return id;
    }

    /**
     * Returns the related entity of this relationship, or null if there is none.
     *
     * @return the related entity of this relationship, or null if there is none
     */
    @Nullable
    public RelationshipRelated getRelated() {
        return related;
    }
}
