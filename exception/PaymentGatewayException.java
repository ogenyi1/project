package ng.optisoft.rosabon.exception;

public class PaymentGatewayException extends RuntimeException {

    public PaymentGatewayException(String message) {
        super(message);
    }

    public PaymentGatewayException() {
        super("Error connecting to the payment gateway");
    }

}
