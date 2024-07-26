package dev.kurumidisciples.javadex.api.exceptions;

/**
 * This exception is thrown when a user is not authorized to perform an action.
 * Typically thrown when a user tries to access a method while not being logged in.
 */
public class AuthorizationException extends IllegalArgumentException{
    
    public AuthorizationException(String message) {
        super(message);
    }
    
    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public AuthorizationException(Throwable cause) {
        super(cause);
    }
    
    public AuthorizationException() {
        super();
    }
}
