package ng.optisoft.rosabon.exception;

public class EntityAlreadyExistsException extends RuntimeException {

	public EntityAlreadyExistsException(String message) {
		super(message);
	}

	public EntityAlreadyExistsException() {
		super("entity already exists");
	}

}
