package exceptions;

/** Введено неправильний пароль до рахунку. */
public class AuthenticationException extends AccountException {
    public AuthenticationException() {
        super("Incorrect password");
    }
}
