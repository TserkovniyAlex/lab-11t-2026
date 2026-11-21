package exceptions;

/**
 * Базове виключення для всіх помилок, пов'язаних з інтервалом часу TimeSpan.
 */
public class TimeSpanException extends RuntimeException {
    public TimeSpanException(String message) {
        super(message);
    }
}
