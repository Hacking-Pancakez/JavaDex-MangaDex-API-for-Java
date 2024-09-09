package dev.kurumidisciples.javadex.api.exceptions;

/**
 * This exception is thrown when a user is not authorized to perform an action.
 * Typically thrown when a user tries to access a method while not being logged in.
 *
 * @author Hacking Pancakez
 */
public class AuthorizationException extends IllegalArgumentException{
    
    /**
     * <p>Constructor for AuthorizationException.</p>
     *
     * @param message a {@link java.lang.String} object
     */
    public AuthorizationException(String message) {
        super(message);
    }
    
    /**
     * <p>Constructor for AuthorizationException.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param cause a {@link java.lang.Throwable} object
     */
    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * <p>Constructor for AuthorizationException.</p>
     *
     * @param cause a {@link java.lang.Throwable} object
     */
    public AuthorizationException(Throwable cause) {
        super(cause);
    }
    
    /**
     * <p>Constructor for AuthorizationException.</p>
     */
    public AuthorizationException() {
        super();
    }
}
