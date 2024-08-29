package annotation;

import dev.kurumidisciples.javadex.api.core.JavaDex;
import dev.kurumidisciples.javadex.api.exceptions.AuthorizationException;
import dev.kurumidisciples.javadex.internal.annotations.aspects.AuthenticatedAspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Aspect
public class AuthenticatedAspectTest {

    @Mock
    private JavaDex javaDex;

    @Mock
    private JoinPoint joinPoint;

    private AuthenticatedAspect authenticatedAspect;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        authenticatedAspect = new AuthenticatedAspect();
    }

    @Test
    public void testValidateAuthenticated_ThrowsAuthorizationException_WhenJavaDexIsGuest() throws Throwable {
        when(javaDex.getMode()).thenReturn(JavaDex.Mode.GUEST);
        when(joinPoint.getTarget()).thenReturn(javaDex);

        assertThrows(AuthorizationException.class, () -> {
            authenticatedAspect.validateAuthenticated(joinPoint);
        });

        verify(javaDex).close();
    }

    @Test
    public void testValidateAuthenticated_ThrowsAuthorizationException_WhenNoJavaDexInstanceFound() throws Throwable {
        when(joinPoint.getTarget()).thenReturn(new Object());

        assertThrows(AuthorizationException.class, () -> {
            authenticatedAspect.validateAuthenticated(joinPoint);
        });
    }

    @Test
    public void testValidateAuthenticated_ClosesJavaDex_WhenJavaDexIsAuthenticated() throws Throwable {
        when(javaDex.getMode()).thenReturn(JavaDex.Mode.AUTHORIZED);
        when(joinPoint.getTarget()).thenReturn(javaDex);

        authenticatedAspect.validateAuthenticated(joinPoint);

        verify(javaDex).close();
    }
}