package a2zDev.safeBank.data.repositories;

import a2zDev.safeBank.data.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionRepositoryTest {

    private TransactionRepository transactionRepository;
    private UUID accountId;
    private Transaction transaction;

    @BeforeEach
    public void setup() {
        transactionRepository = new TransactionRepository();
        accountId = UUID.randomUUID();
        transaction = new Transaction();
    }

    @Test
    public void saveShouldStoreTransaction() {
        Transaction savedTransaction = transactionRepository.save(accountId, transaction);

        assertNotNull(savedTransaction);
        assertEquals(transaction, savedTransaction);
        assertTrue(transactionRepository.existsByAccountId(accountId));
    }

    @Test
    public void saveShouldAddMultipleTransactionsToSameAccount() {
        Transaction secondTransaction = new Transaction();

        transactionRepository.save(accountId, transaction);
        transactionRepository.save(accountId, secondTransaction);

        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);

        assertEquals(2, transactions.size());
        assertTrue(transactions.contains(transaction));
        assertTrue(transactions.contains(secondTransaction));
    }

    @Test
    void findByAccountIdShouldReturnTransactions() {
        transactionRepository.save(accountId, transaction);

        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);

        assertEquals(1, transactions.size());
        assertEquals(transaction, transactions.get(0));
    }

    @Test
    void findByAccountIdShouldReturnEmptyListWhenNoTransactionsExist() {
        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);

        assertNotNull(transactions);
        assertTrue(transactions.isEmpty());
    }

    @Test
    public void findByAccountIdShouldReturnOnlyTransactionsForSpecifiedAccount() {
        UUID anotherAccountId = UUID.randomUUID();
        Transaction anotherTransaction = new Transaction();

        transactionRepository.save(accountId, transaction);
        transactionRepository.save(anotherAccountId, anotherTransaction);

        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);

        assertEquals(1, transactions.size());
        assertTrue(transactions.contains(transaction));
        assertFalse(transactions.contains(anotherTransaction));
    }

    @Test
    void existsByAccountIdShouldReturnTrueWhenTransactionsExist() {
        transactionRepository.save(accountId, transaction);

        assertTrue(transactionRepository.existsByAccountId(accountId));
    }

    @Test
    void existsByAccountIdShouldReturnFalseWhenNoTransactionsExist() {
        assertFalse(transactionRepository.existsByAccountId(accountId));
    }


}