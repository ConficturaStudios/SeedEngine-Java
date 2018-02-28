package source.render.shader.material;

/**
 * An exception that is thrown when an error occurs in material compilation
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class MaterialCompilationException extends Exception {
    /**
     * Constructs a new exception with {@code "Invalid ini data"} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public MaterialCompilationException() {
        super("Material compilation error encountered");
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public MaterialCompilationException(String message) {
        super(message);
    }
}
