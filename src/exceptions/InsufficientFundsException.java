package exceptions;

/** На рахунку недостатньо коштів: баланс не може стати від'ємним. */
public class InsufficientFundsException extends AccountException {
    public InsufficientFundsException(double balance, double amount) {
        super(String.format("Insufficient funds: balance is %.2f, requested %.2f", balance, amount));
    }
}
