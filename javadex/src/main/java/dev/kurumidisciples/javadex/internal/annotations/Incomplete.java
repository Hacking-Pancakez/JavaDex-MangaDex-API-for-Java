package dev.kurumidisciples.javadex.internal.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Marks a method, class, constructor, or field as incomplete.</p>
 * <p>When present the end user should be aware that the method, class, constructor, or field may act unexpectedly during operation.</p>
 * @since 0.1.5.0.BETA.2
 * @author Hacking Pancakez
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD})
public @interface Incomplete {
}
