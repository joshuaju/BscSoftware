package exceptions.testfile;

public class TestfileSyntaxException extends Exception {

	private static final long serialVersionUID = 1L;

	public TestfileSyntaxException(String msg) {
		super(msg);
	}
	
	public TestfileSyntaxException(int line, String msg){
		super("Zeile " + line + ": " + msg);
	}

	public static TestfileSyntaxException InvalidAssignLine() {
		return new TestfileSyntaxException("Ung�ltige Zuweisung");
	}

	public static TestfileSyntaxException InvalidVarToAssign() {
		return new TestfileSyntaxException("Ung�ltige Variable f�r Zuweisung");
	}

	public static TestfileSyntaxException InvalidArgForAssign() {
		return new TestfileSyntaxException("Ung�ltiger Parameter f�r Zuweisung");
	}

	public static TestfileSyntaxException InvalidKeyword() {
		return new TestfileSyntaxException("Ung�ltiges Schl�sselwort");
	}

	public static TestfileSyntaxException InvalidArgForKeyword(int argNumber) {
		return new TestfileSyntaxException("Ung�ltiger Paramater f�r Schl�sselwort: Param. " + argNumber);
	}

	public static TestfileSyntaxException InvalidArgForRepeat() {
		return new TestfileSyntaxException("Ung�ltiger Parameter f�r die Wiederholungsanzahl");
	}

	public static TestfileSyntaxException InvalidVariableFile() {
		return new TestfileSyntaxException("Ung�ltige Pfadangabe f�r globale Variablen");
	}

	public static TestfileSyntaxException InvalidLibraryFile() {
		return new TestfileSyntaxException("Ung�ltiger Pfad oder Name f�r eine Bibliothek");
	}
	
	public static TestfileSyntaxException InvalidLine() {
		return new TestfileSyntaxException("Ung�ltige Zeile");
	}
}
