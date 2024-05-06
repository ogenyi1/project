package ng.optisoft.rosabon.exception;

public class InsufficientWemaFundsException extends RuntimeException {

    public InsufficientWemaFundsException(String message) {
        super(message);
    }

    public InsufficientWemaFundsException() {
        super("we are unable to process your request at this time.\nPlease try again later");
    }

}
