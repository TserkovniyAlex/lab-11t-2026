package exceptions;

/** Сума операції некоректна (не число або не більше 0). */
public class InvalidAmountException extends AccountException {
    public InvalidAmountException(String message) {
        super(message);
    }
}
