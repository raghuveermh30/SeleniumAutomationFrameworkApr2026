package errors;

/**
 * Custom unchecked exception used across the framework to signal
 * configuration or setup errors (e.g. invalid browser or environment name).
 */
public class FrameworkException extends RuntimeException{

    /**
     * Creates a new FrameworkException with the given message.
     *
     * @param msg description of the error
     */
    public FrameworkException(String msg){
        super(msg);
    }
}
