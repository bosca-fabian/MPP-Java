package ProiectMPP.Model.Validators;

/**
 * Custom validation exception that extends the RuntimeException
 */
public class ValidationException extends RuntimeException{
    /**
     * Constructor of validation exception class no parameters
     */
    public ValidationException() {
    }

    /**
     * Constructor of validation exception class with message as parameters
     * @param message the message
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * constructor for validationexception, parameters the message and the cause
     * @param message the message of the exception
     * @param cause the cause of the exception
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * constructor for validationexception, throwable cause
     * @param cause the cause of the exception
     */

    public ValidationException(Throwable cause) {
        super(cause);
    }
    /**
     * constructor for validationexception, all parameters
     * @param message message of the exception
     * @param cause cause of the exception
     * @param enableSuppression whether to enable supression of the exception
     * @param writableStackTrace habar n-am ce e asta
     */

    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
