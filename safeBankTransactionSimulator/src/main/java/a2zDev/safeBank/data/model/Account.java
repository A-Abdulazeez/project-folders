package a2zDev.safeBank.data.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {

    private UUID id;
    private String accountHolderName;
    private BigDecimal balance;

    public Account() {
        this.id = UUID.randomUUID();
    }


    public UUID getId() {
        return id;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
