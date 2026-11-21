package exceptions;

/** Сума зняття перевищує денний ліміт. */
public class DailyLimitExceededException extends AccountException {
    public DailyLimitExceededException(double limit, double available, double amount) {
        super(String.format(
                "Daily withdrawal limit exceeded: limit is %.2f, available today %.2f, requested %.2f",
                limit, available, amount));
    }
}
