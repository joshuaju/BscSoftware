package exceptions.keywordlibrary;

public class KeywordException extends KeywordLibraryException {
	
	private static final long serialVersionUID = 1L;

	public KeywordException(String msg){
		super(msg);
	}
	
	public KeywordException(String msg, Throwable cause){
		super(msg, cause);
	}
	
}
