package annotation;

import dev.kurumidisciples.javadex.internal.annotations.MustNotBeUnknown;
import dev.kurumidisciples.javadex.internal.annotations.aspects.MustNotBeUnknownAspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

import static org.mockito.Mockito.when;

public class MustNotBeUnknownAspectTest {

    private MustNotBeUnknownAspect aspect;
    private JoinPoint joinPoint;
    private MethodSignature signature;
    private Method method;

    
    /** 
     * @throws NoSuchMethodException
     */
    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        aspect = new MustNotBeUnknownAspect();
        joinPoint = Mockito.mock(JoinPoint.class);
        signature = Mockito.mock(MethodSignature.class);
        when(joinPoint.getSignature()).thenReturn(signature);
    }

    @Test
    public void testValidEnumConstant() throws Throwable {
        method = DummyClass.class.getMethod("dummyMethod", DummyEnum.class);
        when(signature.getMethod()).thenReturn(method);
        when(joinPoint.getArgs()).thenReturn(new Object[]{DummyEnum.VALID});
        aspect.validateMustNotBeUnknown(joinPoint);
    }

    @Test
    public void testInvalidEnumConstant() throws Throwable {
        method = DummyClass.class.getMethod("dummyMethod", DummyEnum.class);
        when(signature.getMethod()).thenReturn(method);
        when(joinPoint.getArgs()).thenReturn(new Object[]{DummyEnum.UNKNOWN});
        try {
            aspect.validateMustNotBeUnknown(joinPoint);
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Parameter must not be unknown");
        }
    }

    @Test
    public void testWithNonEnumParameter() throws Throwable {
        method = DummyClass.class.getMethod("dummyMethodNonEnum", String.class);
        when(signature.getMethod()).thenReturn(method);
        when(joinPoint.getArgs()).thenReturn(new Object[]{"test"});
        try {
            aspect.validateMustNotBeUnknown(joinPoint);
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("@MustNotBeUnknown can only be applied to enums");
        }
    }

    @Test
    public void testWithoutAnnotation() throws Throwable {
        method = DummyClass.class.getMethod("dummyMethodWithoutAnnotation", DummyEnum.class);
        when(signature.getMethod()).thenReturn(method);
        when(joinPoint.getArgs()).thenReturn(new Object[]{DummyEnum.VALID});
        aspect.validateMustNotBeUnknown(joinPoint);
    }

    enum DummyEnum {
        VALID, UNKNOWN
    }

    static class DummyClass {
        public void dummyMethod(@MustNotBeUnknown DummyEnum dummyEnum) {
        }

        public void dummyMethodNonEnum(@MustNotBeUnknown String test) {
        }

        public void dummyMethodWithoutAnnotation(DummyEnum dummyEnum) {
        }
    }
}