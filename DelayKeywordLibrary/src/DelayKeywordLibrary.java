import annotations.Keyword;
import annotations.KeywordLibrary;

@KeywordLibrary(Author = "Joshua Jungen", Description = "Stellt Schlüsselworte zum verzögern bereit")
public class DelayKeywordLibrary {

	@Keyword(Description = "Wartet für die angegebene Zeit", Name = "Warte für", Parameter = "Wartezeit in Millisekunden")
	public void delay(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
	}	
}
