import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import exceptions.KeywordLibraryException;
import external.Keyword;
import external.KeywordLibrary;
import external.LibraryLoader;

public class Application {

	public static void main(String[] args) throws ClassNotFoundException, KeywordLibraryException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//		try {
//			Testfile tf = TestfileReader.read("test.tst");
//			System.out.println(tf);
//		} catch (IOException e) {
//			System.err.println(e.getMessage());
//		}

		String path = "E:/DeviceKeywordLibrary.jar";
		KeywordLibrary lib = LibraryLoader.loadLibrary(path, "DeviceKeywordLibrary");
		String author = lib.getAuthor();
		String desc = lib.getDescription();		
		
		System.out.println("Author: " + author);
		System.out.println("Description: " + desc);
		
		Keyword kw = lib.getKeywordByName("Setze Wert auf");
		System.out.println("Keyword gefunden? " + kw != null);
		System.out.println(kw);
		kw.invoke(lib.getInstance(), 2);
		
		kw = lib.getKeywordByName("Lese Wert");		
		System.out.println(kw);
		
		System.out.println(kw.invoke(lib.getInstance()));	
	}

}
