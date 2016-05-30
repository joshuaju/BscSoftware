package exceptions.executing;

public class SetupException extends Exception {

	private static final long serialVersionUID = 1L;

	public SetupException(String message) {
		super(message);
	}

	public SetupException(String message, Throwable cause) {
		super(message, cause);
	}

}
