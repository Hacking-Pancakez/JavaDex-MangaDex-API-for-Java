package dev.kurumidisciples.javadex.internal.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.kurumidisciples.javadex.api.entities.enums.State;


/**
 * Requires the Manga to be in a {@link dev.kurumidisciples.javadex.api.entities.enums.State#DRAFT} and will throw an {@link java.lang.IllegalArgumentException} if it is not.
 *
 * @since 0.1.4
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE})
public @interface MustBeDraft {
}
