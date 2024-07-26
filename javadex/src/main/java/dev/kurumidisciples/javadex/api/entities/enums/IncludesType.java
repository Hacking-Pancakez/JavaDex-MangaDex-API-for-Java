package dev.kurumidisciples.javadex.api.entities.enums;

/**
 * Represents the type of includes that can be requested from the API.
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public enum IncludesType {
    MANGA("manga"),
    COVER_ART("cover_art"),
    AUTHOR("author"),
    ARTIST("artist"),
    TAG("tag"),
    CREATOR("creator"),
    LEADER("leader"),
    SCANLATION_GROUP("scanlation_group"),
    USER("user"),
    MEMBER("member");


    private final String type;

    IncludesType(String type) {
        this.type = type;
    }


    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getType() {
        return type;
    }

    /**
     * <p>fromType.</p>
     *
     * @param type a {@link java.lang.String} object
     * @return a {@link dev.kurumidisciples.javadex.api.entities.enums.IncludesType} object
     */
    public static IncludesType fromType(String type) {
        for (IncludesType includesType : values()) {
            if (includesType.getType().equals(type)) {
                return includesType;
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return type;
    }
}
