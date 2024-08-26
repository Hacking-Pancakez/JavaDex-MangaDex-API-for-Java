package dev.kurumidisciples.javadex.api.entities.relationship.enums;

/**
 * Represents the type of relationship that is being used in {@link dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap}'s attributes.
 * <p><b>NOTE:</b></p>
 * <p>The JavaDoc for the enums are taken from <a href="https://api.mangadex.org/docs/3-enumerations/#manga-related-enum">MangaDex API - Manga Related Enum.</a></p>
 *
 * @since 0.1.2
 * @author Hacking Pancakez
 */
public enum RelationshipRelated {
    /**A self-published derivative work based on this manga*/
    DOUJINSHI("doujinshi"),
    /**A manga taking place in the same fictional world as this manga*/
    SHARED_UNIVERSE("shared_universe"),
    /**A side work contemporaneous with the narrative of this manga*/
    SIDE_STORY("side_story"),
    /**An official derivative work based on this manga*/
    SPIN_OFF("spin_off"),
    /**A colored variant of this manga*/
    COLORED("colored"),
    /**The previous entry in the same series*/
    PREQUEL("prequel"),
    /**The next entry in the same series*/
    SEQUEL("sequel"),
    /**The official serialization of this manga*/
    SERIALIZATION("serialization"),
    /**A monochrome variant of this manga*/
    MONOCHROME("monochrome"),
    /**The original version of this manga before its official serialization*/
    PRESERIALIZATION("preserialization"),
    /**The original narrative this manga is based on*/
    MAIN_STORY("main_story"),
    /**The original work this spin-off manga has been adapted from*/
    ADAPTED_FROM("adapted_from"),
    /**The original work this self-published derivative manga is based on*/
    BASED_ON("based_on"),
    /**A manga based on the same intellectual property as this manga*/
    SAME_FRANCHISE("same_franchise"),
    /**A different version of this manga with no other specific distinction*/
    ALTERNATE_VERSION("alternate_version"),
    /**Represents a unknown relation; typically passed when the wrapper encounters an unimplemented relation*/
    UNKNOWN("unknown"),
    /**An alternative take of the story in this manga*/
    ALTERNATE_STORY("alternate_story");


    private final String related;

    RelationshipRelated(String related) {
        this.related = related;
    }


    /**
     * <p>Getter for the field <code>related</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getRelated() {
        return related;
    }

    /**
     * <p>fromString.</p>
     *
     * @param text a {@link java.lang.String} object
     * @return a {@link dev.kurumidisciples.javadex.api.entities.relationship.enums.RelationshipRelated} object
     */
    public static RelationshipRelated fromString(String text) {
        for (RelationshipRelated b : RelationshipRelated.values()) {
            if (b.getRelated() != null && b.getRelated().equals(text)) {
                return b;
            }
        }
        return UNKNOWN;
    }
}
