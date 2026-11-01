
package a2zDev.safeBank;

import a2zDev.safeBank.dtos.request.CreateAccountRequest;
import a2zDev.safeBank.dtos.response.AccountResponse;
import a2zDev.safeBank.dtos.response.TransactionResponse;
import a2zDev.safeBank.services.AccountServiceImpl;
import a2zDev.safeBank.services.TransactionServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class BankMenu {

    private final AccountServiceImpl accountService;
    private final TransactionServiceImpl transactionService;
    private final Scanner scanner = new Scanner(System.in);

    public BankMenu(AccountServiceImpl accountService, TransactionServiceImpl transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("\n===== SAFEBANK SIMULATION =====");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. Transaction History");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            String userChoice = scanner.nextLine();

            try {
                switch (userChoice) {
                    case "1" -> createAccount();
                    case "2" -> deposit();
                    case "3" -> withdraw();
                    case "4" -> checkBalance();
                    case "5" -> transactionHistory();
                    case "6" -> {
                        running = false;
                        System.out.println("Thank you for using SafeBank.");
                    }
                    default -> System.out.println("Invalid menu selection.");
                }
            }

            catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void createAccount() {
        CreateAccountRequest request = new CreateAccountRequest();

        System.out.print("Account holder name: ");
        request.setAccountHolderName(scanner.nextLine());

        System.out.print("Opening balance: ");
        request.setOpeningBalance(new BigDecimal(scanner.nextLine()));

        AccountResponse response = accountService.createAccount(request);

        System.out.println("Account created successfully!");
        System.out.println("Account ID: " + response.getAccountId());
        System.out.println("Account holder: " + response.getAccountHolderName());
        System.out.println("Balance: ₦" + response.getOpeningBalance());
    }

    private void deposit() {
        UUID accountId = readAccountId();
        BigDecimal amount = readAmount();

        TransactionResponse response = transactionService.deposit(accountId, amount);

        System.out.println("Deposit successful!");
        System.out.println("New balance: ₦" + response.getBalanceAfter());
    }

    private void withdraw() {
        UUID accountId = readAccountId();
        BigDecimal amount = readAmount();

        TransactionResponse response = transactionService.withdraw(accountId, amount);

        System.out.println("Withdrawal successful!");
        System.out.println("New balance: ₦" + response.getBalanceAfter());
    }

    private void checkBalance() {
        UUID accountId = readAccountId();

        AccountResponse response = accountService.getAccount(accountId);

        System.out.println("Account holder: " + response.getAccountHolderName());
        System.out.println("Current balance: ₦" + response.getOpeningBalance());
    }

    private void transactionHistory() {
        UUID accountId = readAccountId();

        List<TransactionResponse> transactions = transactionService.getTransactionHistory(accountId);

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println("\n===== TRANSACTION HISTORY =====");

        for (TransactionResponse transaction : transactions) {
            System.out.println("------------------------------");
            System.out.println("Transaction ID: " + transaction.getTransactionId());
            System.out.println("Type: " + transaction.getType());
            System.out.println("Amount: ₦" + transaction.getAmount());
            System.out.println("Balance after: ₦" + transaction.getBalanceAfter());
            System.out.println("Date: " + transaction.getCreatedAt());
        }
    }

    private UUID readAccountId() {
        System.out.print("Enter account ID: ");

        try {
            return UUID.fromString(scanner.nextLine());
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid account ID.");
        }
    }

    private BigDecimal readAmount() {
        System.out.print("Enter amount: ");

        try {
            return new BigDecimal(scanner.nextLine());
        }

        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid numeric amount.");
        }
    }
}
