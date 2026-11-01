package a2zDev.safeBank.exception;

public class WithdrawalLimitExceededException extends SafeBankTransactionException {
    public WithdrawalLimitExceededException(String message) {
        super(message);
    }
}
