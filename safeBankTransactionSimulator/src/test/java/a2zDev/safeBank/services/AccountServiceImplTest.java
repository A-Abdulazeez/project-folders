package a2zDev.safeBank.services;

import a2zDev.safeBank.data.repositories.AccountRepository;
import a2zDev.safeBank.dtos.request.CreateAccountRequest;
import a2zDev.safeBank.dtos.response.AccountResponse;
import a2zDev.safeBank.exception.InvalidAccountException;
import a2zDev.safeBank.exception.InvalidAccountNameException;
import a2zDev.safeBank.exception.InvalidAmountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceImplTest {

    private AccountServiceImpl accountService;
    private CreateAccountRequest request;

    @BeforeEach
    public void setUp() {
        AccountRepository accountRepository = new AccountRepository();
        accountService = new AccountServiceImpl(accountRepository);

        request = new CreateAccountRequest();
        request.setAccountHolderName("Abdullahi");
        request.setOpeningBalance(new BigDecimal("50000.00"));
    }

    @Test
    public void createAccountShouldCreateAccountSuccessfully() {
        AccountResponse response = accountService.createAccount(request);

        assertNotNull(response);
        assertNotNull(response.getAccountId());
        assertEquals("Abdullahi", response.getAccountHolderName());
        assertEquals(new BigDecimal("50000.00"), response.getOpeningBalance());
    }

    @Test
    public void createAccountShouldRejectBlankAccountHolderName() {
        request.setAccountHolderName(" ");
        assertThrows(InvalidAccountNameException.class, () -> accountService.createAccount(request));
    }

    @Test
    void createAccountShouldRejectNullAccountHolderName() {
        request.setAccountHolderName(null);
        assertThrows(InvalidAccountNameException.class, () -> accountService.createAccount(request));
    }

    @Test
    void createAccountShouldRejectNegativeOpeningBalance() {
        request.setOpeningBalance(new BigDecimal("-500"));
        assertThrows(InvalidAmountException.class, () -> accountService.createAccount(request));
    }

    @Test
    void createAccountShouldAcceptZeroOpeningBalance() {
        request.setOpeningBalance(BigDecimal.ZERO);
        AccountResponse response = accountService.createAccount(request);
        assertNotNull(response);
        assertEquals(BigDecimal.ZERO, response.getOpeningBalance());
    }

    @Test
    void createAccountShouldGenerateDifferentIdsForDifferentAccounts() {
        AccountResponse firstAccount = accountService.createAccount(request);

        CreateAccountRequest secondRequest = new CreateAccountRequest();
        secondRequest.setAccountHolderName("John");
        secondRequest.setOpeningBalance(new BigDecimal("10000"));
        AccountResponse secondAccount = accountService.createAccount(secondRequest);
        assertNotEquals(firstAccount.getAccountId(), secondAccount.getAccountId());
    }

    @Test
    public void getAccountShouldReturnAccountSuccessfully() {
        AccountResponse createdAccount = accountService.createAccount(request);
        AccountResponse response = accountService.getAccount(createdAccount.getAccountId());

        assertNotNull(response);
        assertNotNull(response.getAccountId());
        assertEquals("Abdullahi", response.getAccountHolderName());
        assertEquals(new BigDecimal("50000.00"), response.getOpeningBalance());
    }

    @Test
    public void getAccountShouldThrowExceptionWhenAccountDoesNotExist() {
        assertThrows(InvalidAccountException.class, () -> accountService.getAccount(UUID.randomUUID()));
    }

}