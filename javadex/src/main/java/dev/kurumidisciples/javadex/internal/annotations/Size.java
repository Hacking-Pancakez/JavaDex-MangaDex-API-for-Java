package dev.kurumidisciples.javadex.internal.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Requires a number to have a min and max size.
 * <p>{@code @Size(min = 1, max = 10)}</p>
 *
 * @since 0.1.4
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Size {

    int min() default 0;

    int max() default Integer.MAX_VALUE;
}
