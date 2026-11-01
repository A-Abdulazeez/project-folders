package a2zDev.safeBank.exception;

public class InvalidAccountException extends SafeBankTransactionException {
    public InvalidAccountException(String message) {
        super(message);
    }
}
