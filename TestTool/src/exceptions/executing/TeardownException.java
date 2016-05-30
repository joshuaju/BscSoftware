package exceptions.executing;

public class TeardownException extends Exception {

	private static final long serialVersionUID = 1L;

	public TeardownException(String message) {
		super(message);
	}

	public TeardownException(String message, Throwable cause) {
		super(message, cause);
	}

}
