package exceptions;

/** Пароль не відповідає вимогам (щонайменше 8 символів та одна цифра). */
public class InvalidPasswordException extends AccountException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
