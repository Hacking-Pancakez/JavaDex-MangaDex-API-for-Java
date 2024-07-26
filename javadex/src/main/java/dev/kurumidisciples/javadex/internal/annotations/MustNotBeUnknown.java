package dev.kurumidisciples.javadex.internal.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Requires the annotated parameter when given a enum type to not have a value of {@code unknown}.
 * @since 0.1.4
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE_USE})
public @interface MustNotBeUnknown {
}
