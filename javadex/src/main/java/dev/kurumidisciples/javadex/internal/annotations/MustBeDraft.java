package dev.kurumidisciples.javadex.internal.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.kurumidisciples.javadex.api.entities.enums.State;


/**
 * Requires the Manga to be in a {@link State#DRAFT} and will throw an {@link IllegalArgumentException} if it is not.
 * @since 0.1.4
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface MustBeDraft {
}
