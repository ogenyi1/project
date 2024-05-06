package ng.optisoft.rosabon.exception;

public class DynamicAccountException extends RuntimeException {

    public DynamicAccountException(String message) {
        super(message);
    }

    public DynamicAccountException() {
        super("error generating dynamic account. Please try again");
    }
}
