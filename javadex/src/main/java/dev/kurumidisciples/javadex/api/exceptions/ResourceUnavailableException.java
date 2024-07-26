package dev.kurumidisciples.javadex.api.exceptions;


/**
 * This exception is thrown when a resource is unavailable.
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public class ResourceUnavailableException extends RuntimeException {

    /**
     * <p>Constructor for ResourceUnavailableException.</p>
     */
    public ResourceUnavailableException() {
        super();
    }

    /**
     * <p>Constructor for ResourceUnavailableException.</p>
     *
     * @param message a {@link java.lang.String} object
     */
    public ResourceUnavailableException(String message) {
        super(message);
    }

    /**
     * <p>Constructor for ResourceUnavailableException.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param cause a {@link java.lang.Throwable} object
     */
    public ResourceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>Constructor for ResourceUnavailableException.</p>
     *
     * @param cause a {@link java.lang.Throwable} object
     */
    public ResourceUnavailableException(Throwable cause) {
        super(cause);
    }
}
