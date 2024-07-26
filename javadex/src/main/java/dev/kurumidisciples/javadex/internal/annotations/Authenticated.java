package dev.kurumidisciples.javadex.internal.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.kurumidisciples.javadex.api.exceptions.AuthorizationException;
import dev.kurumidisciples.javadex.api.core.JavaDex;



/**
 * <p>Indicates that the method requires authentication to be used.</p>
 * <p>When this annotation is present, the method will throw an {@link dev.kurumidisciples.javadex.api.exceptions.AuthorizationException} if the {@link dev.kurumidisciples.javadex.api.core.JavaDex} instance is not authenticated.</p>
 * @since 0.1.4
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Authenticated {
}
