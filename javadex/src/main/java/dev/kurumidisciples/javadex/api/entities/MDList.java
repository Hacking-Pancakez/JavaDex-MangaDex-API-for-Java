package dev.kurumidisciples.javadex.api.entities;

import java.util.List;
import java.util.UUID;

import dev.kurumidisciples.javadex.api.entities.intermediate.IRelationHolder;
import dev.kurumidisciples.javadex.api.entities.intermediate.ISnowflake;
import dev.kurumidisciples.javadex.api.proxies.MangaProxy;
import dev.kurumidisciples.javadex.api.proxies.UserProxy;

/**
 * Represents a user-created list in MangaDex.
 * <p><b>Note:</b> This class does not retrieve all entities as their correspoding objects in the list; it only contains the list as a {@link dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap}.</p>
 * <p>For example, each item in the list will only include minimal information:</p>
 * <pre><code> {
 *    "id": "be0f4d1c-17f9-4615-a1d5-996672cb3e80",
 *    "type": "manga"
 * }</code></pre>
 * <p>To obtain data about an entity, you will need to retrieve it through the {@link MangaProxy#retrieve()} method.</p>
 *
 * @since 0.1.2
 * @author Hacking Pancakez
 */
public interface MDList extends ISnowflake, IRelationHolder {
    

    public enum Visibility {
        PUBLIC, PRIVATE;

        public static Visibility fromString(String value){
            return value.equalsIgnoreCase("public") ? PUBLIC : PRIVATE;
        }
    }

    /**
     * Returns the custom name of the list.
     */
    String getName();
    /**
     * Returns the visibility of the list on the MangaDex platform.
     */
    Visibility getVisibility();
    /**
     * Returns whether the list is public.
     */
    boolean isPublic();
    /**
     * Returns whether the list is private.
     */
    boolean isPrivate();
    /**
     * Returns the ID of the user who created the list.
     */
    UUID getCreatorId();
    /**
     * Returns the ID of the user who created the list as a raw string.
     */
    String getCreatorIdRaw();
    /**
     * Returns the manga in the list as a list of {@link MangaProxy} objects.
     */
    List<MangaProxy> getMangas();
    /**
     * Returns the user who created the list as a {@link UserProxy} object.
     */
    UserProxy getCreator();


}
