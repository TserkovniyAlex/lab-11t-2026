
import exceptions.AccountException;
import exceptions.AccountNotFoundException;
import exceptions.DuplicateUserIdException;
import exceptions.InvalidAccountDataException;
import exceptions.InvalidAmountException;
import exceptions.NonZeroBalanceException;
import exceptions.PasswordMismatchException;
import model.BankAccount;
import model.CheckingAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientInterface {

    private final List<BankAccount> accounts = new ArrayList<>();

    private final Scanner scanner = new Scanner(System.in);

    /** Генератор унікальних номерів рахунків. */
    private long nextAccountNumber = 1000000001L;

    private final List<String> options = List.of(
            "1 - Create account",
            "2 - Delete account",
            "3 - Make account deposit",
            "4 - Make account withdrawal",
            "5 - Print account info",
            "6 - Exit"
    );

    public void start() {
        while (true) {
            printMainMenu();
            String option;
            try {
                option = readLine();
            } catch (NoSuchElementException e) {
                // кінець вхідного потоку - завершуємо роботу
                return;
            }

            int choice;
            try {
                choice = Integer.parseInt(option);
            } catch (NumberFormatException e) {
                System.err.println("Error: Please enter a valid choice (1 thru 6)\n");
                continue;
            }

            try {
                switch (choice) {
                    case 1 -> addNewAccount();
                    case 2 -> deleteAccount();
                    case 3 -> deposit();
                    case 4 -> withdraw();
                    case 5 -> printInfo();
                    case 6 -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.err.println("Error: Please enter a valid choice (1 thru 6)\n");
                }
            } catch (AccountException e) {
                System.err.println("Error: " + e.getMessage() + "\n");
            } catch (NoSuchElementException e) {
                return;
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n****************");
        System.out.println("Banking system: ");
        System.out.println("****************");
        options.forEach(System.out::println);
        System.out.print("\n>>> Choose your option: ");
    }

    private void addNewAccount() throws AccountException {
        System.out.println("\n****************");
        System.out.println("Creating new account: ");
        System.out.println("****************");

        System.out.print(">>> Enter customer name: ");
        var name = readLine();
        System.out.print(">>> Enter user id: ");
        var userId = readLine();
        System.out.print(">>> Enter user password: ");
        var password = readPassword();
        System.out.print(">>> Confirm password: ");
        var confirmPassword = readPassword();

        Long id = parseUserId(userId);
        if (findAccount(id) != null) {
            throw new DuplicateUserIdException(userId);
        }
        BankAccount.validatePassword(password);
        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException();
        }

        CheckingAccount account = new CheckingAccount(name, nextAccountNumber, id, password);
        nextAccountNumber++;
        accounts.add(account);
        System.out.println("\nAccount was successfully created. Account number: " + account.getAccountNumber());
    }

    private void deleteAccount() throws AccountException {
        System.out.println("\n****************");
        System.out.println("Deleting account: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userId = readLine();
        System.out.print(">>> Enter user password: ");
        var password = readPassword();

        BankAccount account = authenticate(userId, password);
        if (account.getBalanceAsMoney().signum() != 0) {
            throw new NonZeroBalanceException(account.getAccountBalance());
        }
        accounts.remove(account);
        System.out.println("\nAccount '" + userId + "' was successfully deleted");
    }

    private void deposit() throws AccountException {
        System.out.println("\n****************");
        System.out.println("Making a deposit: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userId = readLine();
        System.out.print(">>> Enter user password: ");
        var password = readPassword();
        System.out.print(">>> Enter amount: ");
        var amount = readLine();

        BankAccount account = authenticate(userId, password);
        account.deposit(parseAmount(amount));
        System.out.printf("%nDeposit was successful. New balance: %.2f%n", account.getAccountBalance());
    }

    private void withdraw() throws AccountException {
        System.out.println("\n****************");
        System.out.println("Making a withdrawal: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userId = readLine();
        System.out.print(">>> Enter user password: ");
        var password = readPassword();
        System.out.print(">>> Enter amount: ");
        var amount = readLine();

        BankAccount account = authenticate(userId, password);
        account.withdraw(parseAmount(amount));
        System.out.printf("%nWithdrawal was successful. New balance: %.2f%n", account.getAccountBalance());
    }

    private void printInfo() throws AccountException {
        System.out.println("\n****************");
        System.out.println("Printing account info: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userId = readLine();
        System.out.print(">>> Enter user password: ");
        var password = readPassword();

        BankAccount account = authenticate(userId, password);

        System.out.println("\nAccount info: ");
        System.out.println(" - customer name: " + account.getCustomerName());
        System.out.println(" - account number: " + account.getAccountNumber());
        System.out.printf(" - account balance: %.2f%n", account.getAccountBalance());
        if (account instanceof CheckingAccount checking) {
            System.out.println(" - account type: Checking");
            System.out.printf(" - account daily withdrawal limit: %.2f%n", checking.getDailyWithdrawalLimit());
            System.out.printf(" - available to withdraw today: %.2f%n", checking.getAvailableToWithdrawToday());
        } else {
            System.out.println(" - account type: Basic");
        }

        System.out.print("\nPress Enter to return to main menu...");
        readLine();
    }

    // ---------- допоміжні методи ----------

    /**
     * Зчитує весь рядок (а не одне слово), тому ім'я з пробілом
     * (наприклад, "Alex Smith") не "перетікає" в наступне поле.
     */
    private String readLine() {
        return scanner.nextLine().trim();
    }

    /**
     * Пароль зчитується "як є", без trim(): пробіли є частиною пароля,
     * тому "password1" і "password1 " - різні паролі.
     */
    private String readPassword() {
        return scanner.nextLine();
    }

    private BankAccount findAccount(Long userId) {
        for (BankAccount account : accounts) {
            if (account.getUserId().equals(userId)) {
                return account;
            }
        }
        return null;
    }

    /** Знаходить рахунок та перевіряє пароль. */
    private BankAccount authenticate(String userId, String password) throws AccountException {
        BankAccount account = findAccount(parseUserId(userId));
        if (account == null) {
            throw new AccountNotFoundException(userId);
        }
        account.authenticate(password);
        return account;
    }

    private Long parseUserId(String userId) throws InvalidAccountDataException {
        try {
            long id = Long.parseLong(userId);
            if (id <= 0) {
                throw new InvalidAccountDataException("User id must be a positive number");
            }
            return id;
        } catch (NumberFormatException e) {
            throw new InvalidAccountDataException("User id must be a number, got '" + userId + "'");
        }
    }

    private double parseAmount(String amount) throws InvalidAmountException {
        try {
            return Double.parseDouble(amount.replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new InvalidAmountException("'" + amount + "' is not a valid number");
        }
    }
}
