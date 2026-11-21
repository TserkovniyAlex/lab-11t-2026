package exceptions;

/** Спроба встановити від'ємний (або некоректний) баланс рахунку. */
public class NegativeBalanceException extends AccountException {
    public NegativeBalanceException(Double balance) {
        super("Account balance cannot be negative: " + balance);
    }
}
