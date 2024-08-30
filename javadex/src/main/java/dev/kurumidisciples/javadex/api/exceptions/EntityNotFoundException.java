package dev.kurumidisciples.javadex.api.exceptions;

/**
 * Throw when an entity is not found. Typically a 404 error.
 * <p><b>NOTE:</b></p>
 * <p>Beginning to move away from HTTP exceptions being passed to the end user and instead using more descriptive exceptions in <code>0.1.5.0</code>.</p>
 * @author Hacking Pancakez
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException() {
        super();
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }
    
}
