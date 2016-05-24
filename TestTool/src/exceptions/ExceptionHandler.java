package exceptions;

public class ExceptionHandler {

	public static TestfileSyntaxException InvalidCharacterException(int lineNumber, String line){
		return new TestfileSyntaxException("Ungültiges Zeichen in Zeile " + lineNumber + ": " + line);		
	}
	
	public static TestfileSyntaxException InvalidLineException(int lineNumber, String line){
		return new TestfileSyntaxException("Ungültige Zeile " + lineNumber + ": " + line);		
	}
	
	public static TestfileContentException InvalidContentException(String... errorMessages){
		String msg = "Fehler in Testdatei: ";
		for (int i = 0; i < errorMessages.length; i++){			
			msg += i + ". " + errorMessages[i];
		}
		return new TestfileContentException(msg);
	}
	
}
