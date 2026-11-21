package exceptions;

/** Неможливо видалити рахунок з ненульовим балансом. */
public class NonZeroBalanceException extends AccountException {
    public NonZeroBalanceException(double balance) {
        super(String.format("Cannot delete account with non-zero balance (%.2f)", balance));
    }
}
