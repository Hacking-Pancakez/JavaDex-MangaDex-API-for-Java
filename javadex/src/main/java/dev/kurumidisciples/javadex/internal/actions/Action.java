package dev.kurumidisciples.javadex.internal.actions;

import java.util.concurrent.CompletableFuture;

import dev.kurumidisciples.javadex.api.entities.enums.IncludesType;

/**
 * Represents an action that can be completed and submitted.
 * @param <T> the type of the action.
 */
// TODO Add Order to actions
public abstract class Action<T> {

    /**
     * Completes the action and returns the result.
     * @return the result of the action.
     * @throws Exception if an error occurs while completing the action.
     */
    public abstract T complete() throws Exception;

    /**
     * Submits the action for execution.
     * This method returns a CompletableFuture that will be completed with the result of the action.
     * @return a CompletableFuture that will be completed with the result of the action.
     */
    public abstract CompletableFuture<T> submit();

    /**
     * Sets the limit of chapters to retrieve.
     * <p>Limit must be between 1 and 100.</p>
     * <p>Default is {@code 10}.</p>
     */
    public abstract Action<T> setLimit(Integer limit);

    /**
     * Sets the offset for the chapter action.
     * <p>Offset is the number of chapters to skip before returning the results.</p>
     * <p>Default is {@code 0}.</p>
     * @param offset the offset.
     * @return the action with the offset set.
     */
    public abstract Action<T> setOffset(Integer offset);

    /**
     * Includes additional data in the result of the action.
     * @param includes the types of data to include.
     * @return the action 
     */
    public abstract Action<T> setIncludes(IncludesType... includes);
}