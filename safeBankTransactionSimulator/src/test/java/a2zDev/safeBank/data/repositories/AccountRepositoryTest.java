package a2zDev.safeBank.data.repositories;

import a2zDev.safeBank.data.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AccountRepositoryTest {

    private AccountRepository accountRepository;

    @BeforeEach
    public void setUp() {
        accountRepository = new AccountRepository();
    }

    @Test
    public void saveShouldStoreAccount() {
        Account account = new Account();
        account.setAccountHolderName("Azeez");
        account.setBalance(new BigDecimal("50000"));
        Account savedAccount = accountRepository.save(account);

        assertNotNull(savedAccount);
        assertEquals(account.getId(), savedAccount.getId());
    }

    @Test
    public void findByIdShouldReturnAccountWhenItExists() {
        Account account = new Account();
        account.setAccountHolderName("Azeez");
        account.setBalance(new BigDecimal("50000"));
        Account savedAccount = accountRepository.save(account);

        Optional<Account> foundAccount = accountRepository.findById(account.getId());

        assertTrue(foundAccount.isPresent());
        assertEquals(account.getId(), foundAccount.get().getId());
    }

    @Test
    public void findByIdShouldReturnEmptyWhenAccountDoesNotExist() {
        Optional<Account> result = accountRepository.findById(UUID.randomUUID());

        assertTrue(result.isEmpty());
    }

    @Test
    public void findAllShouldReturnAllSavedAccounts() {
        Account accountOne = new Account();
        accountOne.setAccountHolderName("Azeez");
        accountOne.setBalance(new BigDecimal("50000"));
        Account savedAccountOne = accountRepository.save(accountOne);

        Account accountTwo = new Account();
        accountTwo.setAccountHolderName("Buruji");
        accountTwo.setBalance(new BigDecimal("70000"));
        Account savedAccountTwo = accountRepository.save(accountTwo);

        List<Account> accounts = accountRepository.findAll();

        assertEquals(2, accounts.size());
        assertTrue(accounts.contains(savedAccountOne));
        assertTrue(accounts.contains(savedAccountTwo));
    }

    @Test
    public void findAllShouldReturnEmptyListWhenNoAccountsExist() {
        List<Account> accounts = accountRepository.findAll();

        assertTrue(accounts.isEmpty());
    }




}