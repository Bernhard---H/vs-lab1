package terminal.exceptions.impl;

import terminal.exceptions.ParseException;

/**
 * @author Bernhard Halbartschlager
 */
public final class CommandNotFoundException  extends ParseException {
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     * later retrieval by the {@link #getMessage()} method.
     */
    public CommandNotFoundException(String message) {
        super(message);
    }
}
