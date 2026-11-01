
package a2zDev.safeBank;

import a2zDev.safeBank.BankMenu;
import a2zDev.safeBank.data.repositories.AccountRepository;
import a2zDev.safeBank.data.repositories.TransactionRepository;
import a2zDev.safeBank.services.AccountServiceImpl;
import a2zDev.safeBank.services.TransactionServiceImpl;

public class Main {

    public static void main(String[] args) {

        AccountRepository accountRepository = new AccountRepository();
        TransactionRepository transactionRepository = new TransactionRepository();

        AccountServiceImpl accountService = new AccountServiceImpl(accountRepository);

        TransactionServiceImpl transactionService = new TransactionServiceImpl(accountRepository, transactionRepository);

        BankMenu bankMenu = new BankMenu(accountService, transactionService);

        bankMenu.start();
    }
}
