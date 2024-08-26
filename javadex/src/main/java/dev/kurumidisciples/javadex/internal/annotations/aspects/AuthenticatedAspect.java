package dev.kurumidisciples.javadex.internal.annotations.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import dev.kurumidisciples.javadex.api.core.JavaDex;
import dev.kurumidisciples.javadex.api.exceptions.AuthorizationException;

import org.aspectj.lang.JoinPoint;

/**
 * <p>AuthenticatedAspect class.</p>
 *
 * @version $Id: $Id
 */
@Aspect
public class AuthenticatedAspect {
    /**
     * <p>validateAuthenticated.</p>
     *
     * @param joinPoint a {@link JoinPoint} object
     * @throws Throwable if any.
     */
    @Before("@annotation(dev.kurumidisciples.javadex.internal.annotations.Authenticated)")
    public void validateAuthenticated(JoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        if (target instanceof JavaDex) {
            JavaDex javaDex = (JavaDex) target;
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