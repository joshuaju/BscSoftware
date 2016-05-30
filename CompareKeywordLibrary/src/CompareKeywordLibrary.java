import annotations.Keyword;
import annotations.KeywordLibrary;

@KeywordLibrary(Author = "Joshua Jungen", Description = "Diese Bibliothek stellt grundlegende Vergleichsmethoden bereit")
public class CompareKeywordLibrary {
	
	@Keyword(Description = "Vergleicht ob zwei Werte gleich sind", Name = "Ist gleich", Parameter = "", Return = "Es wird ein Fehler ausgelöst, wenn die Werte nicht gleich sind.")
	public void assignEqual(Object obj1, Object obj2){
		if (!obj1.equals(obj2)){
			throw new AssertionError("Werte sind nicht gleich: " + obj1 + " != "  + obj2);
		}
	}
	
	@Keyword(Description = "Vergleicht ob zwei Werte gleich sind", Name = "Ist ungefähr gleich", Parameter = "Die ersten beiden Werte werden miteinander verglichen. Der dritte Parameter ist der Toleranzbereich. ", Return = "Es wird ein Fehler ausgelöst, wenn die Differenz der Werte größer als der Tolernazbereich sind.")
	public void assignEqual(double val1, double val2, double abweichung){
		if (!(Math.abs(val1-val2) <= abweichung)){
			throw new AssertionError("Werte sind nicht gleich: " + val1 + " != "  + val2 + " Maximale Abweichung: " + abweichung);
		}
	}
	
	@Keyword(Description = "Vergleicht ob zwei Werte ungleich sind", Name = "Ist ungleich", Parameter = "", Return = "Es wird ein Fehler ausgelöst, wenn die Werte gleich sind.")
	public void assignNotEqual(Object obj1, Object obj2){
		if (obj1.equals(obj2)){
			throw new AssertionError("Werte sind nicht ungleich: " + obj1 + " != "  + obj2);
		}
	}
	
}
