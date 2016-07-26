package external;

import java.io.File;
import java.util.ArrayList;

import exceptions.keywordlibrary.KeywordLibraryException;

/**
 * This class extends {@link SimpleKeywordLibrary}. The stored keywords are
 * executable.
 * 
 * @author JJungen
 *
 */
public class ExecutableKeywordLibrary extends SimpleKeywordLibrary {

	private final Object thisinstance;

	private ArrayList<ExecutableKeyword> keywordList;

	ExecutableKeywordLibrary(Object instance, File file) throws KeywordLibraryException {
		super(instance.getClass(), file);
		thisinstance = instance;
		init();
	}

	ExecutableKeywordLibrary(Object instance, SimpleKeywordLibrary simple) throws KeywordLibraryException {
		super(simple);
		thisinstance = instance;
		init();
	}

	private void init() throws KeywordLibraryException {
		keywordList = new ArrayList<>();
		SimpleKeyword[] simpleKw = super.getKeywords();
		for (SimpleKeyword tmpKeyword : simpleKw) {
			keywordList.add(new ExecutableKeyword(thisinstance, tmpKeyword.method));
		}
	}

	/**
	 * 
	 * @return Instance of the keyword library class
	 */
	public Object getInstance() {
		return thisinstance;
	}

	/**
	 * Searches a keyword by name
	 * 
	 * @param name
	 *            Name of keyword
	 * @return Executable keyword or NULL
	 */
	public ExecutableKeyword getKeywordByName(String name) {
		for (ExecutableKeyword k : keywordList) {
			if (ExecutableKeyword.isNameEqual(k, name)) {
				return k;
			}
		}
		return null;
	}

	@Override
	public ExecutableKeyword[] getKeywords() {
		return keywordList.toArray(new ExecutableKeyword[0]);
	}

}
