package exceptions;

/** Рахунок з таким userId уже існує. */
public class DuplicateUserIdException extends AccountException {
    public DuplicateUserIdException(String userId) {
        super("Account with user id '" + userId + "' already exists");
    }
}
