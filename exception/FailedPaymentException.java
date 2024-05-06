package ng.optisoft.rosabon.exception;

public class FailedPaymentException extends RuntimeException {
    public FailedPaymentException() {
        super("Payment failed");
    }

    public FailedPaymentException(String message) {
        super(message);
    }
}
