package a2zDev.safeBank.exception;

public class InvalidAccountNameException extends SafeBankTransactionException {
    public InvalidAccountNameException(String message) {
        super(message);
    }
}
