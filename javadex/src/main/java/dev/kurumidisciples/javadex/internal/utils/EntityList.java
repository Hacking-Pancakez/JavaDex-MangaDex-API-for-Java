package dev.kurumidisciples.javadex.internal.utils;

import java.util.ArrayList;
import java.util.List;

import dev.kurumidisciples.javadex.api.entities.intermediate.middlemen.EntityMiddleman;

/**
 * Represents a list of entities.
 */
public class EntityList<T> extends ArrayList<T> {

    /**
     * Transforms the EntityMiddleman objects into their correct subclasses.
     * @return a List of the correct subclasses.
     */
    public List<Object> toCorrectSubclasses() {
        List<Object> list = new ArrayList<>();
        for (T entity : this) {
            list.add(((EntityMiddleman) entity).getAsCorrectSubclass());
        }
        return list;
    }
}