package exceptions;

/**
 * Додаткове виключення: викидається, коли замість об'єкта TimeSpan передано null.
 */
public class NullTimeSpanException extends TimeSpanException {
    public NullTimeSpanException(String message) {
        super(message);
    }
}
