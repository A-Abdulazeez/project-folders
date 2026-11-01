package a2zDev.safeBank.services;

import a2zDev.safeBank.data.model.Account;
import a2zDev.safeBank.data.repositories.AccountRepository;
import a2zDev.safeBank.dtos.request.CreateAccountRequest;
import a2zDev.safeBank.dtos.response.AccountResponse;

import static a2zDev.safeBank.utils.Validator.validate;
import static a2zDev.safeBank.utils.Mapper.map;

public class AccountServiceImpl {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;

    }


    public AccountResponse createAccount(CreateAccountRequest request) {
        validate(request);
        Account account = map(request);
        Account createdAccount = accountRepository.save(account);

        return map(createdAccount);
    }
}
