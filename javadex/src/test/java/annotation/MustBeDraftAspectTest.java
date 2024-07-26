package annotation;

import dev.kurumidisciples.javadex.api.core.JavaDexBuilder;
import dev.kurumidisciples.javadex.api.entities.content.Manga;
import dev.kurumidisciples.javadex.api.entities.enums.State;
import dev.kurumidisciples.javadex.internal.annotations.MustBeDraft;

import dev.kurumidisciples.javadex.internal.annotations.aspects.MustBeDraftAspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MustBeDraftAspectTest {

    private MustBeDraftAspect aspect;
    private JoinPoint joinPoint;
    private MethodSignature signature;
    private Method method;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        aspect = new MustBeDraftAspect();
        joinPoint = Mockito.mock(JoinPoint.class);
        signature = Mockito.mock(MethodSignature.class);
        when(joinPoint.getSignature()).thenReturn(signature);
        method = DummyClass.class.getMethod("dummyMethod", Manga.class);
        when(signature.getMethod()).thenReturn(method);
    }

    @Test
    public void testValidDraftManga() throws Throwable {
        Manga draftManga = mock(Manga.class);
        when(draftManga.getState()).thenReturn(State.DRAFT);
        when(joinPoint.getArgs()).thenReturn(new Object[]{draftManga});
        aspect.validateMustBeDraft(joinPoint);
    }

    @Test
    public void testInvalidStateManga() throws Throwable {
        Manga publishedManga = mock(Manga.class);
        when(publishedManga.getState()).thenReturn(State.PUBLISHED);
        when(joinPoint.getArgs()).thenReturn(new Object[]{publishedManga});
        try {
            aspect.validateMustBeDraft(joinPoint);
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Manga must be in a draft state");
        }
    }

    @Test
    public void testNonMangaParameter() throws Throwable {
        when(joinPoint.getArgs()).thenReturn(new Object[]{"Not a Manga"});
        try {
            aspect.validateMustBeDraft(joinPoint);
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("@MustBeDraft can only be applied to Manga");
        }
    }

    static class DummyClass {
        public void dummyMethod(@MustBeDraft Manga manga) {
        }
    }
}