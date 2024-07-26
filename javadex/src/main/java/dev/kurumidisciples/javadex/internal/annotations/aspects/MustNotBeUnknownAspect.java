package dev.kurumidisciples.javadex.internal.annotations.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

import dev.kurumidisciples.javadex.internal.annotations.MustNotBeUnknown;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
public class MustNotBeUnknownAspect {
    
    @Before("execution(* *(.., @dev.kurumidisciples.javadex.internal.annotations.MustNotBeUnknown (*), ..))")
    public void validateMustNotBeUnknown(JoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof MustNotBeUnknown) {
                    if (args[i] instanceof Enum) {
                        Enum<?> enumValue = (Enum<?>) args[i];
                        if (enumValue.toString().equals("UNKNOWN")) {
                            throw new IllegalArgumentException("Parameter must not be unknown: " + args[i]);
                        }
                    } else {
                        throw new IllegalArgumentException("@MustNotBeUnknown can only be applied to enums: " + args[i]);
                    }
                }
            }
        }
    }
}
