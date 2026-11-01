package a2zDev.safeBank.data.repositories;

import a2zDev.safeBank.data.model.Account;

import java.util.*;

public class AccountRepository {

    private final Map<UUID, Account> accounts = new HashMap<>();

    public Account save(Account account) {
        accounts.put(account.getId(), account);
        return account;
    }

    public Optional<Account> findById(UUID id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }
}
