package exceptions.keywordlibrary;

public class KeywordLibraryException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public KeywordLibraryException(){
		super();
	}
	
	public KeywordLibraryException(String msg){
		super(msg);
	}
	
	public KeywordLibraryException(String msg, Exception cause){
		super(msg, cause);
	}
	

	
}
