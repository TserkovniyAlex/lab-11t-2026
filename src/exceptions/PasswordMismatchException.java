package exceptions;

/** Пароль та підтвердження паролю не збігаються. */
public class PasswordMismatchException extends AccountException {
    public PasswordMismatchException() {
        super("Password and password confirmation do not match");
    }
}
