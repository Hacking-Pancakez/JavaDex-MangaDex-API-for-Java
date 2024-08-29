package dev.kurumidisciples.javadex.api.entities;

import java.util.List;
import java.util.UUID;

import dev.kurumidisciples.javadex.api.entities.intermediate.IRelationHolder;
import dev.kurumidisciples.javadex.api.entities.intermediate.ISnowflake;
import dev.kurumidisciples.javadex.api.entities.relationship.RelationshipMap;
import dev.kurumidisciples.javadex.api.proxies.MangaProxy;

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

    String getName();
    RelationshipMap getRelationshipMap();
    Visibility getVisibility();
    boolean isPublic();
    boolean isPrivate();
    UUID getCreatorId();
    String getCreatorIdRaw();
    List<MangaProxy> getMangas();


}
