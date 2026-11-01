package a2zDev.safeBank.utils;

import a2zDev.safeBank.data.model.Account;
import a2zDev.safeBank.dtos.request.CreateAccountRequest;
import a2zDev.safeBank.dtos.response.AccountResponse;

public class Mapper {

    public static Account map(CreateAccountRequest request) {
        Account account = new Account();
        account.setAccountHolderName(request.getAccountHolderName());
        account.setBalance(request.getOpeningBalance());

        return account;
    }

    public static AccountResponse map(Account account) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setAccountHolderName(account.getAccountHolderName());
        accountResponse.setOpeningBalance(account.getBalance());
        accountResponse.setAccountId(account.getId());

        return accountResponse;
    }
}
