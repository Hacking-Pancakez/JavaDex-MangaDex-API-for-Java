package dev.kurumidisciples.javadex.internal.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * An annotation that indicates that the annotated parameter must be greater than or equal to 1
 * and will throw an {@link java.lang.IllegalArgumentException} if it is not.
 *
 * @since 0.1.4
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface NotLessThanOne {
}
