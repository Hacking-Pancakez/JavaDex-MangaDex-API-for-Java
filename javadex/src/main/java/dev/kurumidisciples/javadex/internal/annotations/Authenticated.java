package dev.kurumidisciples.javadex.internal.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.kurumidisciples.javadex.api.exceptions.AuthorizationException;
import dev.kurumidisciples.javadex.api.core.JavaDex;



/**
 * <p>Indicates that the method requires authentication to be used.</p>
 * <p>When this annotation is present, the method will throw an {@link dev.kurumidisciples.javadex.api.exceptions.AuthorizationException} if the {@link dev.kurumidisciples.javadex.api.core.JavaDex} instance is not authenticated.</p>
 * <p>{@link dev.kurumidisciples.javadex.api.core.JavaDex.Mode#GUEST} instances will always throw an exception when this annotation is present.</p>
 *
 * @since 0.1.4
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Authenticated {
}
