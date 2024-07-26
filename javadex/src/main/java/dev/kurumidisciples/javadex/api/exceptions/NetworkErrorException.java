package dev.kurumidisciples.javadex.api.exceptions;

/**
 * This exception is thrown when a network error occurs. Encapsulates all exceptions that can occur during network operations.
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public class NetworkErrorException extends RuntimeException {

    /**
     * <p>Constructor for NetworkErrorException.</p>
     */
    public NetworkErrorException() {
        super();
    }

    /**
     * <p>Constructor for NetworkErrorException.</p>
     *
     * @param message a {@link java.lang.String} object
     */
    public NetworkErrorException(String message) {
        super(message);
    }

    /**
     * <p>Constructor for NetworkErrorException.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param cause a {@link java.lang.Throwable} object
     */
    public NetworkErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>Constructor for NetworkErrorException.</p>
     *
     * @param cause a {@link java.lang.Throwable} object
     */
    public NetworkErrorException(Throwable cause) {
        super(cause);
    }
}
