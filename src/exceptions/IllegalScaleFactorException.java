package exceptions;

/**
 * Викидається, коли множник у методі {@code scale} некоректний (менший або рівний 0).
 */
public class IllegalScaleFactorException extends TimeSpanException {
    public IllegalScaleFactorException(String message) {
        super(message);
    }
}
