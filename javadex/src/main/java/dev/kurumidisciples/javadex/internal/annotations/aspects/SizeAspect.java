package dev.kurumidisciples.javadex.internal.annotations.aspects;

import dev.kurumidisciples.javadex.internal.annotations.Size;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import java.lang.reflect.Parameter;

@Aspect
public class SizeAspect {

    @Before("execution(* *(.., @dev.kurumidisciples.javadex.internal.annotations.Size (*), ..))")
    public void validateSize(JoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < parameters.length; i++) {
            Size size = parameters[i].getAnnotation(Size.class);
            if (size != null) {
                validateParameter(args[i], size);
            }
        }
    }

    private void validateParameter(Object parameter, Size size) {
        if (!(parameter instanceof Number)) {
            throw new IllegalArgumentException("Size annotation is applicable only to numbers.");
        }

        Number number = (Number) parameter;
        if (number.doubleValue() < size.min() || number.doubleValue() > size.max()) {
            throw new IllegalArgumentException("Parameter value " + number + " is out of the specified range [" + size.min() + ", " + size.max() + "].");
        }
    }
}