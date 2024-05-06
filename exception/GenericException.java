package ng.optisoft.rosabon.exception;

import lombok.Getter;

@Getter
public class GenericException extends RuntimeException {
	
	private static final long serialVersionUID = 1670051583873052761L;
	private final String message;

    public GenericException(String message) {
        this.message = message;
    }
}
