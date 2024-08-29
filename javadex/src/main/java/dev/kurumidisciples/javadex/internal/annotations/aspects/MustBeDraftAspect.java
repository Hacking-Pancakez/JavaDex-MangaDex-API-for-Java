package dev.kurumidisciples.javadex.internal.annotations.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import dev.kurumidisciples.javadex.api.entities.Manga;
import dev.kurumidisciples.javadex.api.entities.enums.State;
import dev.kurumidisciples.javadex.internal.annotations.MustBeDraft;

/**
 * <p>MustBeDraftAspect class.</p>
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
@Aspect
public class MustBeDraftAspect {
    

    /**
     * Validates that the {@link Manga} is in a draft state.
     *
     * @param joinPoint a {@link JoinPoint} object
     * @throws Throwable if any.
     */ //TODO fix advice aspect
    @Before("execution(* *(.., @dev.kurumidisciples.javadex.internal.annotations.MustBeDraft (*), ..))")
    public void validateMustBeDraft(JoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof MustBeDraft) {
                    if (args[i] instanceof Manga) {
                        Manga manga = (Manga) args[i];
                        if (manga.getState() != State.DRAFT) {
                            throw new IllegalArgumentException("Manga must be in a draft state.");
                        }
                    } else {
                        throw new IllegalArgumentException("@MustBeDraft can only be applied to Manga: " + args[i]);
                    }
                }
            }
        }
    }
}
