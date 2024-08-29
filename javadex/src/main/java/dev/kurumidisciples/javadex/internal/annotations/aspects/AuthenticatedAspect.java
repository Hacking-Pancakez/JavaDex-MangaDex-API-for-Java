package dev.kurumidisciples.javadex.internal.annotations.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import dev.kurumidisciples.javadex.api.core.JavaDex;
import dev.kurumidisciples.javadex.api.exceptions.AuthorizationException;

/**
 * Handles logic for the {@link dev.kurumidisciples.javadex.internal.annotations.Authenticated} annotation.
 */
@Aspect
public class AuthenticatedAspect {
    /**
     * validates that the {@link JavaDex} instance is authenticated.
     *
     * @param joinPoint the join point
     * @throws Throwable if the {@link JavaDex} instance is not authenticated.
     */
    @Before("@annotation(dev.kurumidisciples.javadex.internal.annotations.Authenticated)")
    public void validateAuthenticated(JoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        if (target instanceof JavaDex javaDex) {
            if (javaDex.getMode() == JavaDex.Mode.GUEST) {
                javaDex.close();
                throw new AuthorizationException("The JavaDex instance is not authenticated.");
            }
            javaDex.close();
        } else {
            throw new AuthorizationException("No JavaDex instance found in method target.");
        }
    }
}