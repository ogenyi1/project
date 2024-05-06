package ng.optisoft.rosabon.exception;

public class EmailAlreadyExistsException extends RuntimeException {

	public EmailAlreadyExistsException(String message) {
		super(message);
	}

	public EmailAlreadyExistsException() {
		super("Email already exists");
	}

}
