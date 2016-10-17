package terminal.exceptions.impl;

import terminal.exceptions.ParseException;

/**
 * @author Bernhard Halbartschlager
 */
public final class IllegalFormatException extends ParseException {

    public IllegalFormatException(String message) {
        super(message);
    }
}
