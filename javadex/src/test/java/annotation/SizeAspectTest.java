package annotation;

import dev.kurumidisciples.javadex.internal.annotations.Size;
import dev.kurumidisciples.javadex.internal.annotations.aspects.SizeAspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

import static org.mockito.Mockito.when;

public class SizeAspectTest {

    private SizeAspect aspect;
    private JoinPoint joinPoint;
    private MethodSignature signature;

    @BeforeEach
    public void setUp() {
        aspect = new SizeAspect();
        joinPoint = Mockito.mock(JoinPoint.class);
        signature = Mockito.mock(MethodSignature.class);
        when(joinPoint.getSignature()).thenReturn(signature);
    }

    
    /** 
     * @throws Throwable
     */
    @Test
    public void testValidNumberParameter() throws Throwable {
        Method method = DummyClass.class.getMethod("dummyMethodWithSize", Integer.class);
        when(signature.getMethod()).thenReturn(method);
        when(joinPoint.getArgs()).thenReturn(new Object[]{5});
        aspect.validateSize(joinPoint);
    }

    @Test
    public void testInvalidNumberParameter() throws Throwable {
        Method method = DummyClass.class.getMethod("dummyMethodWithSize", Integer.class);
        when(signature.getMethod()).thenReturn(method);
        when(joinPoint.getArgs()).thenReturn(new Object[]{10});
        try {
            aspect.validateSize(joinPoint);
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("is out of the specified range");
        }
    }

    @Test
    public void testNonNumberParameter() throws Throwable {
        Method method = DummyClass.class.getMethod("dummyMethodWithSizeNonNumber", String.class);
        when(signature.getMethod()).thenReturn(method);
        when(joinPoint.getArgs()).thenReturn(new Object[]{"test"});
        try {
            aspect.validateSize(joinPoint);
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Size annotation is applicable only to numbers");
        }
    }

    @Test
    public void testWithoutAnnotation() throws Throwable {
        Method method = DummyClass.class.getMethod("dummyMethodWithoutSize", Integer.class);
        when(signature.getMethod()).thenReturn(method);
        when(joinPoint.getArgs()).thenReturn(new Object[]{5});
        aspect.validateSize(joinPoint);
    }

    @Test
    public void testBoundaryConditions() throws Throwable {
        Method method = DummyClass.class.getMethod("dummyMethodWithSize", Integer.class);
        when(signature.getMethod()).thenReturn(method);
        when(joinPoint.getArgs()).thenReturn(new Object[]{1}); // Lower boundary
        aspect.validateSize(joinPoint);
        when(joinPoint.getArgs()).thenReturn(new Object[]{5}); // Upper boundary
        aspect.validateSize(joinPoint);
    }

    static class DummyClass {
        public void dummyMethodWithSize(@Size(min = 1, max = 5) Integer number) {
        }

        public void dummyMethodWithSizeNonNumber(@Size(min = 1, max = 5) String text) {
        }

        public void dummyMethodWithoutSize(Integer number) {
        }
    }
}