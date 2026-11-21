package exceptions;

/** Рахунок із вказаним userId не існує. */
public class AccountNotFoundException extends AccountException {
    public AccountNotFoundException(String userId) {
        super("Account with user id '" + userId + "' not found");
    }
}
