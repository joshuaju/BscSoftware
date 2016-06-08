package external;

import java.util.ArrayList;

import exceptions.keywordlibrary.KeywordLibraryException;

public class ExecutableKeywordLibrary extends SimpleKeywordLibrary {

	private final Object thisinstance;

	private ArrayList<ExecutableKeyword> keywordList;

	ExecutableKeywordLibrary(Object instance) throws KeywordLibraryException {
		super(instance.getClass());		
		thisinstance = instance;		
		init();		
	}
	
	ExecutableKeywordLibrary(Object instance,SimpleKeywordLibrary simple) throws KeywordLibraryException{
		super(simple);		
		thisinstance = instance;
		init();
	}
	
	private void init() throws KeywordLibraryException{
		keywordList = new ArrayList<>();		
		SimpleKeyword[] simpleKw = super.getKeywords();
		for (SimpleKeyword tmpKeyword: simpleKw) {			
			keywordList.add(new ExecutableKeyword(thisinstance, tmpKeyword.method));			
		}
	}
	
	/**
	 * Gibt eine Instanz der Bibliothek zur�ck.
	 * @return Instanz
	 */
	public Object getInstance(){
		return thisinstance;
	}
	
	/**
	 * Gibt ein Keyword zur�ck
	 * @param name Name des Keywords
	 * @return Wenn vorhanden ein Keyword, sonst NULL
	 */
	public ExecutableKeyword getKeywordByName(String name){		
		for (ExecutableKeyword k: keywordList){
			if (ExecutableKeyword.isNameEqual(k, name)){
				return k;
			}
		}
		return null;
	}	
	
	@Override
	public ExecutableKeyword[] getKeywords(){
		return keywordList.toArray(new ExecutableKeyword[0]);
	}
	
}