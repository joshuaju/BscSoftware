package external;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import exceptions.keywordlibrary.KeywordLibraryException;
import exceptions.keywordlibrary.KeywordLibraryExceptionHandler;

/**
 * This class offers information on a single keyword library. Further, the
 * library's keywords are created and stored.
 * 
 * @author JJungen
 *
 */
public class SimpleKeywordLibrary {

	private final String thisname;
	private final Class<?> thisclass;

	private String author;
	private String description;
	private ArrayList<SimpleKeyword> keywordList;

	private final File file;

	public SimpleKeywordLibrary(Class<?> thisclass, File file) throws KeywordLibraryException {
		this.thisclass = thisclass;
		this.thisname = thisclass.getSimpleName();
		this.file = file;
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

	public SimpleKeywordLibrary(SimpleKeywordLibrary simple) {
		this.thisclass = simple.getClass();
		this.thisname = simple.getName();
		this.author = simple.getAuthor();
		this.description = simple.getDescription();
		this.keywordList = new ArrayList<>(Arrays.asList(simple.getKeywords()));
		this.file = simple.file;
	}

	public Class<?> getLibraryClass() {
		return thisclass;
	}

	/**
	 * 
	 * @return name of keyword library
	 */
	public String getName() {
		return thisname;
	}

	/**
	 * 
	 * @return name of author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * 
	 * @return description of library
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @return all keywords
	 */
	public SimpleKeyword[] getKeywords() {
		return keywordList.toArray(new SimpleKeyword[0]);
	}

	/**
	 * 
	 * @return path to library file
	 */
	public String getAbsolutePath() {
		return file.getAbsolutePath();
	}

}
