package javadex;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import dev.kurumidisciples.javadex.api.core.JavaDex;
import dev.kurumidisciples.javadex.api.core.JavaDexBuilder;
import dev.kurumidisciples.javadex.api.exceptions.AuthorizationException;

public class JavaDexTests {
    

    @Test
    public void testGuestInstance() throws Exception {
        JavaDex guest = JavaDexBuilder.createGuest();

        assertNotNull(guest);
    }
}
