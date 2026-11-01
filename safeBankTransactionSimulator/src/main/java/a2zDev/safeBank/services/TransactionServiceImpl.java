package a2zDev.safeBank.services;

import a2zDev.safeBank.data.model.Account;
import a2zDev.safeBank.data.model.Transaction;
import a2zDev.safeBank.data.model.TransactionType;
import a2zDev.safeBank.data.repositories.AccountRepository;
import a2zDev.safeBank.data.repositories.TransactionRepository;
import a2zDev.safeBank.dtos.response.TransactionResponse;
import a2zDev.safeBank.exception.InvalidAccountException;

import java.math.BigDecimal;
import java.util.UUID;

import static a2zDev.safeBank.utils.Mapper.map;
import static a2zDev.safeBank.utils.Validator.validate;

public class TransactionServiceImpl {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

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
}
