import javax.swing.JOptionPane;

import annotations.Keyword;
import annotations.KeywordLibrary;

@KeywordLibrary(Author = "Joshua Jungen", Description = "Stellt Dialog zur Eingabe von Werten w�hrend des Testlaufs bereit")
public class DialogKeywordLibrary {

	@Keyword(Description = "�ffnet eine Dialog mit einer Nachricht. Der Nutzer muss best�tigen, damit fortgefahren wird", Name = "Warte auf Best�tigung")
	public void showConfirmDialog(String message) {
		int result = JOptionPane.showConfirmDialog(null, message, "Best�tigen", JOptionPane.OK_CANCEL_OPTION);
		if (result != JOptionPane.OK_OPTION){
			throw new AssertionError("Der Benutzer hat den Test abgebrochen");
		}		
	}

	@Keyword(Description = "�ffnet einen Dialog der den Tester zum eingeben eines Textes auffordert", Name = "Frage nach Text f�r", Parameter = "Eine Nachricht, die im Dialog angezeigt wird", Return = "Den im Dialog eingetragen Wert")
	public String showStringDialog(String message) {
		String text = JOptionPane.showInputDialog(null, message, "Text eingeben", JOptionPane.QUESTION_MESSAGE);
		if (text == null){
			throw new AssertionError("Dialog wurde ohne Wert beendet");
		}
		return text;
	}

	@Keyword(Description = "�ffnet einen Dialog der den Tester zum eingeben einer ganzen Zahl auffordert", Name = "Frage nach ganzer Zahl f�r", Parameter = "Eine Nachricht, die den einzutragenden Wert beschreibt", Return = "Den im Dialog eingetragen Wert")
	public Integer showIntegerDialog(String message) {
		String text = JOptionPane.showInputDialog(null, message, "Ganze Zahl eingeben", JOptionPane.QUESTION_MESSAGE);
		if (text == null){
			throw new AssertionError("Dialog wurde ohne Wert beendet");
		}
		Integer result = Integer.parseInt(text);			
		return result;
	}

	@Keyword(Description = "�ffnet einen Dialog der den Tester zum eingeben einer Kommazahl auffordert", Name = "Frage nach Kommazahl f�r", Parameter = "Eine Nachricht, die im Dialog angezeigt wird", Return = "Den im Dialog eingetragen Wert")
	public Double showDoubleDialog(String message) {
		String text = JOptionPane.showInputDialog(null, message, "Kommazahl eingeben", JOptionPane.QUESTION_MESSAGE);		
		if (text == null){
			throw new AssertionError("Dialog wurde ohne Wert beendet");
		}
		Double result = Double.parseDouble(text);			
		return result;
	}

}
