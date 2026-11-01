package a2zDev.safeBank.data.repositories;

import a2zDev.safeBank.data.model.Transaction;

import java.util.*;

public class TransactionRepository {

    private final Map<UUID, List<Transaction>> transactions = new HashMap<>();

    public Transaction save(UUID accountId, Transaction transaction) {
        if  (!transactions.containsKey(accountId)) {
            transactions.put(accountId, new ArrayList<>());
        }

        transactions.get(accountId).add(transaction);
        return transaction;
    }

    public List<Transaction> findByAccountId(UUID accountId) {
        return new ArrayList<>(transactions.getOrDefault(accountId, Collections.emptyList()));
    }

    public boolean existsByAccountId(UUID accountId) {
        return transactions.containsKey(accountId);
    }
}
