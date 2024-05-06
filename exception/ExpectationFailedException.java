package ng.optisoft.rosabon.exception;

public class ExpectationFailedException extends RuntimeException {

	public ExpectationFailedException(String message) {
		super(message);
	}

	public ExpectationFailedException() {
		super("Expectation failed");
	}

}
