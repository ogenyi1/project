package ng.optisoft.rosabon.exception;

public class ConflictException extends RuntimeException {

	public ConflictException(String message) {
		super(message);
	}

	public ConflictException() {
		super("a conflict was found");
	}

}
