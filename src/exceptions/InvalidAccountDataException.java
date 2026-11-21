package exceptions;

/** Некоректні дані рахунку (порожнє ім'я, userId, від'ємний ліміт тощо). */
public class InvalidAccountDataException extends AccountException {
    public InvalidAccountDataException(String message) {
        super(message);
    }
}
