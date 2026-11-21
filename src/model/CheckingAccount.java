package model;

import exceptions.DailyLimitExceededException;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAccountDataException;
import exceptions.InvalidAmountException;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Рахунок з денним лімітом на зняття коштів.
 * Ліміт рахується сумарно за поточний день.
 * Грошові обчислення виконуються через BigDecimal, щоб не накопичувались
 * похибки double.
 */
public class CheckingAccount extends BankAccount {

    public static final double DEFAULT_DAILY_WITHDRAWAL_LIMIT = 300;

    private double dailyWithdrawalLimit = DEFAULT_DAILY_WITHDRAWAL_LIMIT;

    private BigDecimal withdrawnToday = BigDecimal.ZERO;
    private LocalDate lastWithdrawalDate = LocalDate.now();

    public CheckingAccount(String customerName, Long accountNumber, Long userId, String userPassword) {
        super(customerName, accountNumber, userId, userPassword);
    }

    @Override
    public void deposit(double amount) throws InvalidAmountException {
        validateAmount(amount);
        setBalanceFromMoney(getBalanceAsMoney().add(toMoney(amount)));
    }

    @Override
    public void withdraw(double amount)
            throws InvalidAmountException, DailyLimitExceededException, InsufficientFundsException {
        validateAmount(amount);
        BigDecimal sum = toMoney(amount);

        BigDecimal available = availableToday();
        if (sum.compareTo(available) > 0) {
            throw new DailyLimitExceededException(dailyWithdrawalLimit, available.doubleValue(), amount);
        }
        BigDecimal balance = getBalanceAsMoney();
        if (sum.compareTo(balance) > 0) {
            throw new InsufficientFundsException(balance.doubleValue(), amount);
        }
        setBalanceFromMoney(balance.subtract(sum));
        withdrawnToday = withdrawnToday.add(sum);
    }

    /** Скільки ще можна зняти сьогодні в межах денного ліміту. */
    public double getAvailableToWithdrawToday() {
        return availableToday().doubleValue();
    }

    private BigDecimal availableToday() {
        LocalDate today = LocalDate.now();
        if (!today.equals(lastWithdrawalDate)) {
            lastWithdrawalDate = today;
            withdrawnToday = BigDecimal.ZERO;
        }
        return toMoney(dailyWithdrawalLimit).subtract(withdrawnToday).max(BigDecimal.ZERO);
    }

    public double getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    public void setDailyWithdrawalLimit(double dailyWithdrawalLimit) {
        if (Double.isNaN(dailyWithdrawalLimit) || Double.isInfinite(dailyWithdrawalLimit)
                || dailyWithdrawalLimit < 0) {
            throw new InvalidAccountDataException("Daily withdrawal limit must be a non-negative number");
        }
        if (hasMoreThanTwoDecimals(dailyWithdrawalLimit)) {
            throw new InvalidAccountDataException("Daily withdrawal limit cannot have more than 2 decimal places");
        }
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }
}
