package a2zDev.safeBank.services;

import a2zDev.safeBank.data.model.Account;
import a2zDev.safeBank.data.model.Transaction;
import a2zDev.safeBank.data.model.TransactionType;
import a2zDev.safeBank.data.repositories.AccountRepository;
import a2zDev.safeBank.data.repositories.TransactionRepository;
import a2zDev.safeBank.dtos.response.TransactionResponse;
import a2zDev.safeBank.exception.InsufficientFundsException;
import a2zDev.safeBank.exception.InvalidAccountException;
import a2zDev.safeBank.exception.InvalidAmountException;
import a2zDev.safeBank.exception.WithdrawalLimitExceededException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionServiceImplTest {

    private TransactionServiceImpl transactionService;
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    private Account account;

    @BeforeEach
    void setUp() {
        accountRepository = new AccountRepository();
        transactionRepository = new TransactionRepository();

        transactionService = new TransactionServiceImpl(accountRepository, transactionRepository);

        account = new Account();
        account.setAccountHolderName("Abdullahi");
        account.setBalance(new BigDecimal("50000"));
        accountRepository.save(account);
    }

    @Test
    public void depositShouldIncreaseAccountBalance() {
        transactionService.deposit(account.getId(), new BigDecimal("10000"));

        assertEquals(new BigDecimal("60000"), account.getBalance());
    }

    @Test
    public void depositShouldReturnTransactionResponse() {
        TransactionResponse response = transactionService.deposit(account.getId(), new BigDecimal("10000"));

        assertNotNull(response);
        assertNotNull(response.getTransactionId());
        assertEquals(TransactionType.DEPOSIT, response.getType());
        assertEquals(new BigDecimal("10000"), response.getAmount());
        assertEquals(new BigDecimal("60000"), response.getBalanceAfter());
        assertNotNull(response.getCreatedAt());
    }

    @Test
    public void depositShouldSaveTransactionToRepository() {
        transactionService.deposit(account.getId(), new BigDecimal("10000"));
        List<Transaction> transactions = transactionRepository.findByAccountId(account.getId());

        assertEquals(1, transactions.size());
        assertEquals(TransactionType.DEPOSIT, transactions.get(0).getType());
    }

    @Test
    public void depositShouldRejectZeroAmount() {
        assertThrows(InvalidAmountException.class, () -> transactionService.deposit(account.getId(), BigDecimal.ZERO));

        assertEquals(new BigDecimal("50000"), account.getBalance());
        assertTrue(transactionRepository.findByAccountId(account.getId()).isEmpty());
    }

    @Test
    public void depositShouldRejectNegativeAmount() {
        assertThrows(InvalidAmountException.class, () -> transactionService.deposit(account.getId(), new BigDecimal("-500")));

        assertEquals(new BigDecimal("50000"), account.getBalance());
        assertTrue(transactionRepository.findByAccountId(account.getId()).isEmpty());
    }

    @Test
    public void depositShouldRejectNullAmount() {
        assertThrows(InvalidAmountException.class, () -> transactionService.deposit(account.getId(), null));
    }

    @Test
    public void depositShouldRejectUnknownAccount() {
        assertThrows(InvalidAccountException.class, () -> transactionService.deposit(UUID.randomUUID(), new BigDecimal("10000")));
    }

    @Test
    public void multipleDepositsShouldCreateSeparateTransactions() {
        transactionService.deposit(account.getId(), new BigDecimal("10000"));
        transactionService.deposit(account.getId(), new BigDecimal("5000"));

        List<Transaction> transactions = transactionRepository.findByAccountId(account.getId());

        assertEquals(2, transactions.size());
        assertEquals(new BigDecimal("65000"), account.getBalance());
    }

    @Test
    public void withdrawShouldDecreaseAccountBalance() {
        transactionService.withdraw(account.getId(), new BigDecimal("10000"));
        assertEquals(new BigDecimal("40000"), account.getBalance());
    }

    @Test
    public void withdrawShouldReturnTransactionResponse() {
        TransactionResponse response = transactionService.withdraw(account.getId(), new BigDecimal("10000"));

        assertNotNull(response);
        assertEquals(TransactionType.WITHDRAW, response.getType());
        assertEquals(new BigDecimal("10000"), response.getAmount());
        assertEquals(new BigDecimal("40000"), response.getBalanceAfter());
        assertNotNull(response.getCreatedAt());
    }

    @Test
    public void withdrawalShouldSaveTransactionToRepository() {
        transactionService.withdraw(account.getId(), new BigDecimal("10000"));
        List<Transaction> transactions = transactionRepository.findByAccountId(account.getId());

        assertEquals(1, transactions.size());
        assertEquals(TransactionType.WITHDRAW, transactions.get(0).getType());
    }

    @Test
    public void withdrawShouldRejectZeroAmount() {
        assertThrows(InvalidAmountException.class, () -> transactionService.withdraw(account.getId(), BigDecimal.ZERO));

        assertEquals(new BigDecimal("50000"), account.getBalance());
        assertTrue(transactionRepository.findByAccountId(account.getId()).isEmpty());
    }

    @Test
    public void withdrawalShouldRejectNegativeAmount() {
        assertThrows(InvalidAmountException.class, () -> transactionService.withdraw(account.getId(), new BigDecimal("-500")));

        assertEquals(new BigDecimal("50000"), account.getBalance());
        assertTrue(transactionRepository.findByAccountId(account.getId()).isEmpty());
    }

    @Test
    public void withdrawShouldRejectNullAmount() {
        assertThrows(InvalidAmountException.class, () -> transactionService.withdraw(account.getId(), null));
    }

    @Test
    public void withdrawShouldRejectAmountAboveWithdrawalLimit() {
        account.setBalance(new BigDecimal("200000"));
        assertThrows(WithdrawalLimitExceededException.class, () -> transactionService.withdraw(account.getId(), new BigDecimal("120000")));

        assertEquals(new BigDecimal("200000"), account.getBalance());
        assertTrue(transactionRepository.findByAccountId(account.getId()).isEmpty());
    }

    @Test
    public void withdrawShouldRejectAmountAboveAvailableBalance() {
        assertThrows(InsufficientFundsException.class, () -> transactionService.withdraw(account.getId(), new BigDecimal("60000")));

        assertEquals(new BigDecimal("50000"), account.getBalance());
        assertTrue(transactionRepository.findByAccountId(account.getId()).isEmpty());
    }

    @Test
    public void withdrawalShouldRejectUnknownAccount() {
        assertThrows(InvalidAccountException.class, () -> transactionService.withdraw(UUID.randomUUID(), new BigDecimal("10000")));
    }

    @Test
    public void withdrawShouldAllowAmountEqualToWithdrawalLimit() {
        account.setBalance(new BigDecimal("150000"));
        transactionService.withdraw(account.getId(), new BigDecimal("100000"));

        assertEquals(new BigDecimal("50000"), account.getBalance());
    }

    @Test
    public void successfulWithdrawalShouldWorkAfterFailedWithdrawal() {
        assertThrows(InsufficientFundsException.class, () -> transactionService.withdraw(account.getId(), new BigDecimal("60000")));

        transactionService.withdraw(account.getId(), new BigDecimal("10000"));

        assertEquals(new BigDecimal("40000"), account.getBalance());
        assertEquals(1, transactionRepository.findByAccountId(account.getId()).size());
    }

    @Test
    public void getTransactionHistoryShouldReturnAllAccountTransactions() {
        transactionService.deposit(account.getId(), new BigDecimal("10000"));
        transactionService.withdraw(account.getId(), new BigDecimal("5000"));

        List<TransactionResponse> history = transactionService.getTransactionHistory(account.getId());

        assertEquals(2, history.size());
        assertEquals(TransactionType.DEPOSIT, history.get(0).getType());
        assertEquals(TransactionType.WITHDRAW, history.get(1).getType());
    }

    @Test
    public void getTransactionHistoryShouldReturnEmptyListWhenNoTransactionsExist() {
        List<TransactionResponse> history = transactionService.getTransactionHistory(account.getId());

        assertNotNull(history);
        assertTrue(history.isEmpty());
    }

    @Test
    public void getTransactionHistoryShouldReturnOnlyTransactionsForSpecifiedAccount() {
        Account anotherAccount = new Account();
        anotherAccount.setAccountHolderName("Buruji");
        anotherAccount.setBalance(new BigDecimal("30000"));
        accountRepository.save(anotherAccount);

        transactionService.deposit(account.getId(), new BigDecimal("5000"));
        transactionService.deposit(anotherAccount.getId(), new BigDecimal("10000"));

        List<TransactionResponse> history = transactionService.getTransactionHistory(account.getId());

        assertEquals(1, history.size());
        assertEquals(new BigDecimal("5000"), history.get(0).getAmount());
    }

    @Test
    public void getTransactionHistoryShouldThrowExceptionForUnknownAccount() {
        assertThrows(InvalidAccountException.class, () -> transactionService.getTransactionHistory(UUID.randomUUID()));
    }

}