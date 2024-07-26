package dev.kurumidisciples.javadex.api.entities.enums;

/**
 * Represents the type of includes that can be requested from the API.
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


    public String getType() {
        return type;
    }

    public static IncludesType fromType(String type) {
        for (IncludesType includesType : values()) {
            if (includesType.getType().equals(type)) {
                return includesType;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return type;
    }
}
