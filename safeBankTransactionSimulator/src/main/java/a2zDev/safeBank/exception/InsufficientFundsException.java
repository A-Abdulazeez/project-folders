package a2zDev.safeBank.exception;

public class InsufficientFundsException extends SafeBankTransactionException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
