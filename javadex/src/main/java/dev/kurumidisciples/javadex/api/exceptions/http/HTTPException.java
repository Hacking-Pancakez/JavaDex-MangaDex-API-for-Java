package dev.kurumidisciples.javadex.api.exceptions.http;

import java.io.IOException;

/**
 * <p>HTTPException class.</p>
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
@Deprecated
public class HTTPException extends IOException {
    private final int responseCode;

    /**
     * <p>Constructor for HTTPException.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param responseCode a int
     */
    public HTTPException(String message, int responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    /**
     * <p>Getter for the field <code>responseCode</code>.</p>
     *
     * @return a int
     */
    public int getResponseCode() {
        return responseCode;
    }
}

