package a2zDev.safeBank.services;

import a2zDev.safeBank.data.model.Account;
import a2zDev.safeBank.data.model.Transaction;
import a2zDev.safeBank.data.model.TransactionType;
import a2zDev.safeBank.data.repositories.AccountRepository;
import a2zDev.safeBank.data.repositories.TransactionRepository;
import a2zDev.safeBank.dtos.response.TransactionResponse;
import a2zDev.safeBank.exception.InsufficientFundsException;
import a2zDev.safeBank.exception.InvalidAccountException;
import a2zDev.safeBank.exception.WithdrawalLimitExceededException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static a2zDev.safeBank.utils.Mapper.map;
import static a2zDev.safeBank.utils.Validator.validate;

public class TransactionServiceImpl {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    private static final BigDecimal WITHDRAWAL_CAP = new BigDecimal("100000");

    public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public TransactionResponse deposit(UUID accountId, BigDecimal amount) {
        validate(amount);
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new InvalidAccountException("Account not found"));
        BigDecimal updatedBalance = account.getBalance().add(amount);
        account.setBalance(updatedBalance);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setBalanceAfter(updatedBalance);
        Transaction savedTransaction = transactionRepository.save(account.getId(), transaction);

        return map(savedTransaction);
    }

    public TransactionResponse withdraw(UUID accountId, BigDecimal amount) {
        validate(amount);
        if (amount.compareTo(WITHDRAWAL_CAP) > 0) throw new WithdrawalLimitExceededException("Maximum withdrawal limit is ₦100,000");

        Account account = accountRepository.findById(accountId).orElseThrow(() -> new InvalidAccountException("Account not found"));
        if (amount.compareTo(account.getBalance()) > 0) throw new InsufficientFundsException("Insufficient account balance");

        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(TransactionType.WITHDRAW);
        transaction.setBalanceAfter(newBalance);
        Transaction savedTransaction = transactionRepository.save(account.getId(), transaction);

        return map(savedTransaction);
    }

    public List<TransactionResponse> getTransactionHistory(UUID accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new InvalidAccountException("Account not found"));
        List<TransactionResponse> allTransactions = new ArrayList<>();

        List<Transaction> transactions = transactionRepository.findByAccountId(account.getId());

        for (Transaction transaction : transactions) {
            allTransactions.add(map(transaction));
        }
        return allTransactions;
    }
}
