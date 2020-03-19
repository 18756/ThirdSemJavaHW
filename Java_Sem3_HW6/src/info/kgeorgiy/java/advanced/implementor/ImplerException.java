package info.kgeorgiy.java.advanced.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;

/**
 * Thrown by {@link Impler} when an error occurred.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ImplerException extends Exception {

    /**
     * default constructor
     */
    public ImplerException() {
    }

    /**
     * constructor receiving massage param
     * @param message exception message
     */
    public ImplerException(final String message) {
        super(message);
    }

    /**
     * constructor receiving massage and cause params
     * @param message exception message
     * @param cause exception cause
     */
    public ImplerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * constructor receiving cause param
     * @param cause exception cause
     */
    public ImplerException(final Throwable cause) {
        super(cause);
    }
}
