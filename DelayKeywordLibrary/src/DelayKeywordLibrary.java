import annotations.Keyword;
import annotations.KeywordLibrary;

@KeywordLibrary(Author = "Joshua Jungen", Description = "Stellt Schl�sselworte zum verz�gern bereit")
public class DelayKeywordLibrary {

	@Keyword(Description = "Wartet f�r die angegebene Zeit", Name = "Warte f�r", Parameter = "Wartezeit in Millisekunden")
	public void delay(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
	}	
}
