package dev.kurumidisciples.javadex.api.exceptions;

/**
 * Represents an exception thrown when an error occurs during manga creation.
 */
public class MangaCreationException extends RuntimeException {

    public MangaCreationException(String message) {
        super(message);
    }

    public MangaCreationException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
