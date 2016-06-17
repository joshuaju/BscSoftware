import annotations.Keyword;
import annotations.KeywordLibrary;

@KeywordLibrary(
		Author = "Joshua Jungen", 
		Description = "Wird bei der Entwicklung des Testwerkzeug zum ausprobieren verwendet")
public class DeviceKeywordLibrary {

	private int wert = 0;

	@Keyword(Description = "Setzt den Wert neu", Name = "Setze Wert auf", Parameter = "Neuer Wert", Return = "")
	public void setValue(int value) {
		this.wert = value;
	}

	@Keyword(Description = "Liest den Wert aus", Name = "Lese Wert", Parameter = "", Return = "Aktueller Wert")
	public int getValue() {
		return this.wert;
	}

}
