package dev.kurumidisciples.javadex.api.entities.intermediate.middlemen;

import dev.kurumidisciples.javadex.api.entities.Author;
import dev.kurumidisciples.javadex.api.entities.Chapter;
import dev.kurumidisciples.javadex.api.entities.ScanlationGroup;
import dev.kurumidisciples.javadex.api.entities.User;
import dev.kurumidisciples.javadex.api.entities.content.Manga;
import dev.kurumidisciples.javadex.api.entities.intermediate.Entity;

/**
 * The {@code EntityMiddleman} class serves as a wrapper for various entity types within the JavaDex API.
 * It provides methods to safely cast the contained {@link Entity}
 * to more specific entity types such as {@link Manga},
 * {@link ScanlationGroup}, {@link Author},
 * {@link User}, and {@link Chapter}.
 * This class ensures that the entity is of the expected type before casting, throwing an {@link IllegalStateException}
 * if the entity cannot be cast to the requested type.
 * 
 * @see Entity
 * @see Manga
 * @see ScanlationGroup
 * @see Author
 * @see User
 * @see Chapter
 */
public class EntityMiddleman {
    
    private final Entity entity;

    /**
     * Constructs a new {@code EntityMiddleman} with the specified entity.
     * 
     * @param entity The entity to be wrapped by this {@code EntityMiddleman}.
     */
    public EntityMiddleman(Entity entity) {
        this.entity = entity;
    }

     /**
     * Returns the wrapped entity as a {@link Manga} if it is an instance of {@code Manga},
     * otherwise throws an {@link IllegalStateException}.
     * 
     * @return The wrapped entity as a {@code Manga}.
     * @throws IllegalStateException If the wrapped entity is not an instance of {@code Manga}.
     */
    public Manga getAsManga(){
        if (entity instanceof Manga) {
            return (Manga) entity;
        } else {
            throw new IllegalStateException("Entity is not a Manga");
        }
    }

    /**
     * Returns the original wrapped entity.
     * 
     * @return The wrapped entity.
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Returns the wrapped entity as a {@link ScanlationGroup} if it is an instance of {@code ScanlationGroup},
     * otherwise throws an {@link IllegalStateException}.
     * 
     * @return The wrapped entity as a {@code ScanlationGroup}.
     * @throws IllegalStateException If the wrapped entity is not an instance of {@code ScanlationGroup}.
     */
    public ScanlationGroup getAsScanlationGroup(){
        if (entity instanceof ScanlationGroup) {
            return (ScanlationGroup) entity;
        } else {
            throw new IllegalStateException("Entity is not a ScanlationGroup");
        }
    }

    /**
     * Returns the wrapped entity as an {@link Author} if it is an instance of {@code Author},
     * otherwise throws an {@link IllegalStateException}.
     * 
     * @return The wrapped entity as an {@code Author}.
     * @throws IllegalStateException If the wrapped entity is not an instance of {@code Author}.
     */
    public Author getAsAuthor(){
        if (entity instanceof Author) {
            return (Author) entity;
        } else {
            throw new IllegalStateException("Entity is not an Author");
        }
    }

    /**
     * Returns the wrapped entity as a {@link User} if it is an instance of {@code User},
     * otherwise throws an {@link IllegalStateException}.
     * 
     * @return The wrapped entity as a {@code User}.
     * @throws IllegalStateException If the wrapped entity is not an instance of {@code User}.
     */
    public User getAsUser(){
        if (entity instanceof User) {
            return (User) entity;
        } else {
            throw new IllegalStateException("Entity is not a User");
        }
    }

    /**
     * Returns the wrapped entity as a {@link Chapter} if it is an instance of {@code Chapter},
     * otherwise throws an {@link IllegalStateException}.
     * 
     * @return The wrapped entity as a {@code Chapter}.
     * @throws IllegalStateException If the wrapped entity is not an instance of {@code Chapter}.
     */
    public Chapter getAsChapter(){
        if (entity instanceof Chapter) {
            return (Chapter) entity;
        } else {
            throw new IllegalStateException("Entity is not a Chapter");
        }
    }

    /**
     * Returns the wrapped entity as its correct subclass, if possible,
     * otherwise throws an {@link IllegalStateException}.
     * 
     * @return The wrapped entity as its correct subclass.
     * @throws IllegalStateException If the wrapped entity is not an instance of a valid subclass.
     */
    public Object getAsCorrectSubclass(){
        if (entity instanceof Manga) {
            return getAsManga();
        } else if (entity instanceof ScanlationGroup) {
            return getAsScanlationGroup();
        } else if (entity instanceof Author) {
            return getAsAuthor();
        } else if (entity instanceof User) {
            return getAsUser();
        } else if (entity instanceof Chapter) {
            return getAsChapter();
        } else {
            throw new IllegalStateException("Entity is not a valid subclass");
        }
    }
}
