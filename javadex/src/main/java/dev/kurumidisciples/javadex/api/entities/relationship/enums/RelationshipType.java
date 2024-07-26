package dev.kurumidisciples.javadex.api.entities.relationship.enums;

/**
 * This enum represents the different types of relationships that can exist in the application.
 * Each enum constant corresponds to a specific type of relationship.
 */
public enum RelationshipType {
    ARTIST("artist"),
    AUTHOR("author"),
    SCANLATION_GROUP("scanlation_group"),
    MANGA("manga"),
    LEADER("leader"),
    MEMBER("member"),
    COVER_ART("cover_art"),
    USER("user");

    private final String type;

    /**
     * Constructs a new RelationshipType with the specified type.
     *
     * @param type the type of the relationship
     */
    RelationshipType(String type) {
        this.type = type;
    }

    /**
     * Returns the type of this relationship.
     *
     * @return the type of this relationship
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the RelationshipType that corresponds to the specified type, or null if there is none.
     *
     * @param type the type to find
     * @return the RelationshipType that corresponds to the specified type, or null if there is none
     */
    public static RelationshipType fromString(String type) {
        for (RelationshipType t : values()) {
            if (t.getType().equals(type)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Returns the type of this relationship as a string.
     *
     * @return the type of this relationship as a string
     */
    @Override
    public String toString() {
        return type;
    }
}