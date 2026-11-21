package exceptions;

/**
 * Викидається, коли вхідний інтервал часу некоректний:
 * від'ємні години та/або хвилини, або хвилин більше 59 при двох аргументах.
 */
public class IllegalInputTimeSpanException extends TimeSpanException {
    public IllegalInputTimeSpanException(String message) {
        super(message);
    }
}
