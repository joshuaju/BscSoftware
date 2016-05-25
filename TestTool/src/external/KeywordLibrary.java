package external;

import java.lang.reflect.Method;
import java.util.ArrayList;

import exceptions.ExceptionHandler;
import exceptions.KeywordLibraryException;

public class KeywordLibrary {

	private final String thisname;
	private final Class<?> thisclass;
	private final Object thisinstance;

	private String author;
	private String description;
	private ArrayList<Keyword> keywordList;

	KeywordLibrary(String name, Object instance) throws KeywordLibraryException {
		thisname = name;
		thisinstance = instance;
		thisclass = instance.getClass();
		keywordList = new ArrayList<>();
		initKeywordLibrary();
		initKeywordList();		
	}
	
	/**
	 * Hilfsmethode: Initialisiert die KeywordLibrary. 
	 * 
	 * @throws KeywordLibraryException
	 */
	private void initKeywordLibrary() throws KeywordLibraryException {
		annotations.KeywordLibrary klAnnotation = thisclass.getAnnotation(annotations.KeywordLibrary.class);
		if (klAnnotation == null) {
			throw ExceptionHandler.ClassIsNotAKeywordLibrary(thisname);
		}
		this.author = klAnnotation.Author();
		this.description = klAnnotation.Description();			
	}
	
	/**
	 * Hilfsmethode: Füllt die Variablen keywordMethodNames und keywordMethodMap
	 * @throws KeywordLibraryException 
	 */
	private void initKeywordList() throws KeywordLibraryException{
		Method[] methods = thisclass.getMethods();
		for (Method m : methods) {
			if (Keyword.isKeywordMethod(m)){
				keywordList.add(new Keyword(m));
			}
		}		
		
	}

	/**
	 * Gibt den Names des Erstellers der Bibliothek zurück
	 * @return Name
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Gibt die Beschreibung der Bibliothek zurück
	 * @return Beschreibung
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Gibt eine Instanz der Bibliothek zurück.
	 * @return Instanz
	 */
	public Object getInstance(){
		return thisinstance;
	}
	
	/**
	 * Gibt ein Keyword zurück
	 * @param name Name des Keywords
	 * @return Wenn vorhanden ein Keyword, sonst NULL
	 */
	public Keyword getKeywordByName(String name){		
		for (Keyword k: keywordList){
			if (Keyword.isNameEqual(k, name)){
				return k;
			}
		}
		return null;
	}
		
	
}
