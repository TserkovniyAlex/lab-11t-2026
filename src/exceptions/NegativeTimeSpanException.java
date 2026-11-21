package exceptions;

/**
 * Викидається, коли значення поточного інтервалу часу стає від'ємним.
 */
public class NegativeTimeSpanException extends TimeSpanException {
    public NegativeTimeSpanException(String message) {
        super(message);
    }
}
