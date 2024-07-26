package dev.kurumidisciples.javadex.internal.annotations.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

import dev.kurumidisciples.javadex.internal.annotations.NotLessThanOne;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * <p>NotLessThanOneAspect class.</p>
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
@Aspect
public class NotLessThanOneAspect {

    /**
     * <p>validateNotLessThanOne.</p>
     *
     * @param joinPoint a {@link org.aspectj.lang.JoinPoint} object
     * @throws java.lang.Throwable if any.
     */
    @Before("execution(* *(.., @dev.kurumidisciples.javadex.api.internal.annotations.NotLessThanOne (*), ..))")
    public void validateNotLessThanOne(JoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof NotLessThanOne) {
                    if (args[i] instanceof Number) {
                        Number number = (Number) args[i];
                        if (number.intValue() < 1) {
                            throw new IllegalArgumentException("Parameter must not be less than one: " + args[i]);
                        }
                    } else {
                        throw new IllegalArgumentException("@NotLessThanOne can only be applied to numbers: " + args[i]);
                    }
                }
            }
        }
    }
}
