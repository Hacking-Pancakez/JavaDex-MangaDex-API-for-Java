package annotation;
import dev.kurumidisciples.javadex.internal.annotations.NotLessThanOne;
import dev.kurumidisciples.javadex.internal.annotations.aspects.NotLessThanOneAspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class NotLessThanOneAspectTest {

    private NotLessThanOneAspect aspect;
    private JoinPoint joinPoint;
    private MethodSignature signature;
    private Method method;

    
    /** 
     * @throws NoSuchMethodException
     */
    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        aspect = new NotLessThanOneAspect();
        joinPoint = Mockito.mock(JoinPoint.class);
        signature = Mockito.mock(MethodSignature.class);
        when(joinPoint.getSignature()).thenReturn(signature);
        method = DummyClass.class.getMethod("dummyMethod", Integer.class);
        when(signature.getMethod()).thenReturn(method);
    }

    @Test
    public void testValidNumber() throws Throwable {
        when(joinPoint.getArgs()).thenReturn(new Object[]{5});
        aspect.validateNotLessThanOne(joinPoint);
    }

    @Test
    public void testInvalidNumber() throws Throwable {
        when(joinPoint.getArgs()).thenReturn(new Object[]{-1});
        try {
            aspect.validateNotLessThanOne(joinPoint);
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Parameter must not be less than one");
        }
    }

    @Test
    public void testNonNumber() throws Throwable {
        when(joinPoint.getArgs()).thenReturn(new Object[]{"test"});
        try {
            aspect.validateNotLessThanOne(joinPoint);
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("@NotLessThanOne can only be applied to numbers");
        }
    }

    @Test
    public void testWithoutAnnotation() throws Throwable {
        method = DummyClass.class.getMethod("dummyMethodWithoutAnnotation", Integer.class);
        when(signature.getMethod()).thenReturn(method);
        when(joinPoint.getArgs()).thenReturn(new Object[]{0});
        aspect.validateNotLessThanOne(joinPoint);
    }

    static class DummyClass {
        public void dummyMethod(@NotLessThanOne Integer number) {
        }

        public void dummyMethodWithoutAnnotation(Integer number) {
        }
    }
}