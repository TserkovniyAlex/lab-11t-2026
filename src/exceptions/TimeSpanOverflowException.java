package exceptions;

/**
 * Додаткове виключення: результат операції з інтервалом часу
 * не вміщується в допустимий діапазон (переповнення int).
 */
public class TimeSpanOverflowException extends TimeSpanException {
    public TimeSpanOverflowException(String message) {
        super(message);
    }
}
