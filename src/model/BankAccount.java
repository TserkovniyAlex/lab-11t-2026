package model;

import exceptions.AuthenticationException;
import exceptions.InvalidAccountDataException;
import exceptions.InvalidAmountException;
import exceptions.InvalidPasswordException;
import exceptions.NegativeBalanceException;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Базовий (абстрактний) банківський рахунок згідно з UML діаграмою.
 * Баланс рахунку не може бути від'ємним і дорівнює 0 при створенні об'єкта.
 */
public abstract class BankAccount {

    public static final int MIN_PASSWORD_LENGTH = 8;

    private String customerName;
    private Long accountNumber;
    private Long userId;
    private String userPassword;
    private Double accountBalance;

    public BankAccount(String customerName, Long accountNumber, Long userId, String userPassword) {
        setCustomerName(customerName);
        setAccountNumber(accountNumber);
        setUserId(userId);
        setUserPassword(userPassword);
        this.accountBalance = 0.0;
    }

    public abstract void deposit(double amount);

    public abstract void withdraw(double amount);

    /**
     * Перевіряє, що пароль має щонайменше 8 символів і містить хоча б одну цифру.
     */
    public static void validatePassword(String password) throws InvalidPasswordException {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new InvalidPasswordException(
                    "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long");
        }
        if (password.chars().noneMatch(Character::isDigit)) {
            throw new InvalidPasswordException("Password must contain at least one digit");
        }
    }

    /** Повертає true, якщо пароль правильний. */
    public boolean checkPassword(String password) {
        return userPassword.equals(password);
    }

    /** Викидає виключення, якщо пароль неправильний. */
    public void authenticate(String password) throws AuthenticationException {
        if (!checkPassword(password)) {
            throw new AuthenticationException();
        }
    }

    /** Перевірка суми операції: число, більше 0. */
    protected static void validateAmount(double amount) throws InvalidAmountException {
        if (Double.isNaN(amount) || amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than 0");
        }
        if (Double.isInfinite(amount)) {
            throw new InvalidAmountException("Amount is too large");
        }
        if (hasMoreThanTwoDecimals(amount)) {
            throw new InvalidAmountException("Amount cannot have more than 2 decimal places");
        }
    }

    /** true, якщо в сумі більше 2 знаків після коми (наприклад, 0.005). */
    protected static boolean hasMoreThanTwoDecimals(double value) {
        return BigDecimal.valueOf(value).stripTrailingZeros().scale() > 2;
    }

    /**
     * Переводить double у BigDecimal з точністю до копійок.
     * Усі грошові обчислення виконуються через BigDecimal, щоб уникнути
     * неточностей double (наприклад, 0.30 - 0.10 - 0.20 != 0).
     */
    protected static BigDecimal toMoney(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }

    /** Баланс у вигляді BigDecimal (точно до копійок). */
    public BigDecimal getBalanceAsMoney() {
        return toMoney(accountBalance);
    }

    /**
     * Встановлює баланс з BigDecimal.
     * Поле accountBalance має тип Double (згідно з UML), тому перевіряємо,
     * що значення точно (до копійки) вміщується в Double і не стає Infinity.
     */
    protected void setBalanceFromMoney(BigDecimal balance) {
        double value = balance.doubleValue();
        if (Double.isInfinite(value) || toMoney(value).compareTo(balance) != 0) {
            throw new InvalidAmountException("Account balance is too large to be stored precisely");
        }
        setAccountBalance(value);
    }

    // ---------- гетери та сетери ----------

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        if (customerName == null || customerName.isBlank()) {
            throw new InvalidAccountDataException("Customer name cannot be empty");
        }
        this.customerName = customerName;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        if (accountNumber == null || accountNumber <= 0) {
            throw new InvalidAccountDataException("Account number must be a positive number");
        }
        this.accountNumber = accountNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new InvalidAccountDataException("User id must be a positive number");
        }
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        validatePassword(userPassword);
        this.userPassword = userPassword;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        if (accountBalance == null || accountBalance.isNaN() || accountBalance < 0) {
            throw new NegativeBalanceException(accountBalance);
        }
        if (accountBalance.isInfinite()) {
            throw new InvalidAmountException("Account balance is too large");
        }
        if (hasMoreThanTwoDecimals(accountBalance)) {
            throw new InvalidAmountException("Account balance cannot have more than 2 decimal places");
        }
        this.accountBalance = accountBalance;
    }

    @Override
    public String toString() {
        return String.format("%s{customerName='%s', accountNumber=%d, userId=%d, accountBalance=%.2f}",
                getClass().getSimpleName(), customerName, accountNumber, userId, accountBalance);
    }
}
