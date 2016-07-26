package input;

import exceptions.testfile.TestfileException;

/**
 * This class stores a line and the related line number.    
 * @author JJungen
 *
 */
public class Testline {

	public final int number;
	public final String text;

	public Testline(int number, String text) throws TestfileException {
		this.number = number;
		this.text = text.trim();
	}
	
}
