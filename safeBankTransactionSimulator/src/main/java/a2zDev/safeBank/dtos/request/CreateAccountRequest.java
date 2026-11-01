package a2zDev.safeBank.dtos.request;

import java.math.BigDecimal;

public class CreateAccountRequest {

    private String accountHolderName;
    private BigDecimal openingBalance;

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public BigDecimal getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }
}
