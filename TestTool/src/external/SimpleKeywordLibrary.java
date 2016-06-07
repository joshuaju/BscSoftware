package external;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import exceptions.keywordlibrary.KeywordLibraryException;
import exceptions.keywordlibrary.KeywordLibraryExceptionHandler;

public class SimpleKeywordLibrary {

	private final String thisname;
	private final Class<?> thisclass;

	private String author;
	private String description;
	private ArrayList<SimpleKeyword> keywordList;

	public SimpleKeywordLibrary(Class<?> thisclass) throws KeywordLibraryException {
		this.thisclass = thisclass;
		this.thisname = thisclass.getSimpleName();
		keywordList = new ArrayList<>();
		
		annotations.KeywordLibrary klAnnotation = thisclass.getAnnotation(annotations.KeywordLibrary.class);
		if (klAnnotation == null) {
			throw KeywordLibraryExceptionHandler.ClassIsNotAKeywordLibrary(thisclass);
		}
		this.author = klAnnotation.Author();
		this.description = klAnnotation.Description();
		Method[] methods = thisclass.getMethods();
		for (Method m : methods) {
			if (ExecutableKeyword.isKeywordMethod(m)) {
				keywordList.add(new SimpleKeyword(m));
			}
		}
	}
	
	public SimpleKeywordLibrary(SimpleKeywordLibrary simple){
		this.thisclass = simple.getClass();
		this.thisname = simple.getName();
		this.author = simple.getAuthor();
		this.description = simple.getDescription();
		this.keywordList = new ArrayList<>(Arrays.asList(simple.getKeywords()));
	}

	/**
	 * Gibt die Klasse der Bibliothek zurück
	 * 
	 * @return Klasse
	 */
	public Class<?> getLibraryClass() {
		return thisclass;
	}

	/**
	 * Gibt den Namen der Bibliothek zurück. Der Name einer Bibliothek wird in
	 * der Testdatei festgelegt.
	 * 
	 * @return Name
	 */
	public String getName() {
		return thisname;
	}

	/**
	 * Gibt den Names des Erstellers der Bibliothek zurück
	 * 
	 * @return Name
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Gibt die Beschreibung der Bibliothek zurück
	 * 
	 * @return Beschreibung
	 */
	public String getDescription() {
		return description;
	}

	public SimpleKeyword[] getKeywords() {
		return keywordList.toArray(new SimpleKeyword[0]);
	}

}
