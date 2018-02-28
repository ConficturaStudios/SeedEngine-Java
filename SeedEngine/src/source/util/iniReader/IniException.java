package source.util.iniReader;

/**
 * A class for encapsulating .ini file read errors
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class IniException extends Exception {
    /**
     * Constructs a new exception with {@code "Invalid ini data"} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public IniException() {
        super("Invalid ini data");
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public IniException(String message) {
        super(message);
    }
}
