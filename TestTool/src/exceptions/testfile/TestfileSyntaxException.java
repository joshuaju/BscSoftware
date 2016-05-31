package exceptions.testfile;

public class TestfileSyntaxException extends Exception {

	private static final long serialVersionUID = 1L;

	public TestfileSyntaxException(String msg) {
		super(msg);
	}
	
	public static TestfileSyntaxException InvalidAssignLine(){
		return new TestfileSyntaxException("Ung�ltige Zuweisung");
	}
		
	public static TestfileSyntaxException InvalidVarToAssign(){
		return new TestfileSyntaxException("Ung�ltige Variable f�r Zuweisung");
	}
	
	public static TestfileSyntaxException InvalidArgForAssign(){
		return new TestfileSyntaxException("Ung�ltiger Parameter f�r Zuweisung");
	}
	
	public static TestfileSyntaxException InvalidKeyword(){
		return new TestfileSyntaxException("Ung�ltiges Schl�sselwort");
	}
	
	public static TestfileSyntaxException InvalidArgForKeyword(int argNumber){
		return new TestfileSyntaxException("Ung�ltiger Paramater f�r Schl�sselwort: Param. " + argNumber);
	}
	
}
