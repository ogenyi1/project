package ng.optisoft.rosabon.exception;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String message) {
        super(message);
    }

    public InsufficientFundsException() {
        super("insufficient funds");
    }

}
