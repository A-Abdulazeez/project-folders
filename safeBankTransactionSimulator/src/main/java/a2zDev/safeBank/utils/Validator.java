package a2zDev.safeBank.utils;

import a2zDev.safeBank.dtos.request.CreateAccountRequest;
import a2zDev.safeBank.exception.InvalidAccountNameException;
import a2zDev.safeBank.exception.InvalidAmountException;

import java.math.BigDecimal;

public class Validator {

    public static void validate(CreateAccountRequest request) {
        if (request.getAccountHolderName() == null || request.getAccountHolderName().isBlank()) throw new InvalidAccountNameException("Account holder name Cannot be Blank");
        if (request.getOpeningBalance() == null || request.getOpeningBalance().compareTo(BigDecimal.ZERO) < 0) throw new InvalidAmountException("Opening Balance cannot be negative");
    }
}
