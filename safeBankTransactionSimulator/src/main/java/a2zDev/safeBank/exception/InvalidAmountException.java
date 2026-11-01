package a2zDev.safeBank.exception;

public class InvalidAmountException extends SafeBankTransactionException {
    public InvalidAmountException(String message) {
        super(message);
    }
}
