package exceptions.testfile;

public class TestfileSyntaxException extends Exception {

	private static final long serialVersionUID = 1L;

	public TestfileSyntaxException(String msg) {
		super(msg);
	}
	
	public static TestfileSyntaxException InvalidAssignLine(){
		return new TestfileSyntaxException("Ungültige Zuweisung");
	}
		
	public static TestfileSyntaxException InvalidVarToAssign(){
		return new TestfileSyntaxException("Ungültige Variable für Zuweisung");
	}
	
	public static TestfileSyntaxException InvalidArgForAssign(){
		return new TestfileSyntaxException("Ungültiger Parameter für Zuweisung");
	}
	
	public static TestfileSyntaxException InvalidKeyword(){
		return new TestfileSyntaxException("Ungültiges Schlüsselwort");
	}
	
	public static TestfileSyntaxException InvalidArgForKeyword(int argNumber){
		return new TestfileSyntaxException("Ungültiger Paramater für Schlüsselwort: Param. " + argNumber);
	}
	
}
