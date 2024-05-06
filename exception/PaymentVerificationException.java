package ng.optisoft.rosabon.exception;

public class PaymentVerificationException extends RuntimeException {
    public PaymentVerificationException() {
        super("Unable to verify payment");
    }

    public PaymentVerificationException(String message) {
        super(message);
    }
}
