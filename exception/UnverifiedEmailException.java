package ng.optisoft.rosabon.exception;

public class UnverifiedEmailException extends RuntimeException {
    public UnverifiedEmailException() {

        //FRD number 28
        super("Your account is pending verification. Please check your email for the verification link");
    }

    public UnverifiedEmailException(String message) {
        super(message);
    }
}
