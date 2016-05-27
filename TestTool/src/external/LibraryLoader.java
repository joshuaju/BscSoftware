package external;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import exceptions.ExceptionHandler;
import exceptions.KeywordLibraryException;

public class LibraryLoader {
	
	/**
	 * Läd eine KeywordLibrary aus dem angegebenen Jar-Verzeichnis. Der
	 * Dateiname entspricht dem Namen des Jar-Verzeichnisses
	 * 
	 * @param path
	 *            Pfad zur Jar-Datei
	 * @return Bibliothek
	 * @throws ClassNotFoundException
	 * @throws KeywordLibraryException
	 * @throws IOException
	 */
	public static KeywordLibrary loadLibrary(String path)
			throws ClassNotFoundException, KeywordLibraryException, IOException {
		File file = new File(path);
		String name = file.getName();
		name = name.substring(0, name.lastIndexOf('.'));		
		return loadLibrary(path, name);
	}

	/**
	 * Läd eine KeywordLibrary aus dem angegebenen Jar-Verzeichnis mit dem
	 * angegeben vollständigen Klassennamen.
	 * 
	 * @param path
	 *            Pfad zur Jar-Datei
	 * @param name
	 *            Vollständiger Name (Bsp: 'DeviceLibrary', oder
	 *            'example.DeviceLibrary' wenn die Klasse in einem paket ist
	 * @return Bibliothek
	 * @throws ClassNotFoundException
	 *             Die Klasse wurde nicht gefunden
	 * @throws KeywordLibraryException
	 *             Die Klasse ist keine gültige Bibliothek
	 * @throws IOException
	 *             Die Jar-Datei wurde nicht gefunden
	 */
	public static KeywordLibrary loadLibrary(String path, String name)
			throws ClassNotFoundException, KeywordLibraryException, IOException {
		File libFile = new File(path);

		if (!libFile.exists()) {
			throw ExceptionHandler.FileNotFound(libFile);
		}

		if (!libFile.isFile()) {
			throw ExceptionHandler.FileIsDirectory(libFile);
		}

		URL[] urls = null;
		try {
			urls = new URL[] { libFile.toURI().toURL() };
		} catch (MalformedURLException e) {
			throw ExceptionHandler.FileNotFound(libFile);
		}

		ClassLoader systemClassLoader = URLClassLoader.getSystemClassLoader();				
		URLClassLoader loader = URLClassLoader.newInstance(urls, systemClassLoader);
		loadAllClassesFromJar(path, loader);
		
		
		Class<?> libClass = null;
		try {
			libClass = loader.loadClass(name);
		} catch (ClassNotFoundException e) {
			throw ExceptionHandler.ClassnameNotInClassLoader(name);
		} finally {
			loader.close();
		}

		return loadLibrary(name, libClass);
	}

	private static void loadAllClassesFromJar(String path, URLClassLoader loader) throws IOException, ClassNotFoundException{
		JarFile jar = new JarFile(path);
		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()){
			JarEntry tmpEntry = entries.nextElement();
			String filename = tmpEntry.getName();
			if (filename.endsWith(".class")){							
				String classname = filename.replace("/", ".").substring(0, filename.length()-6);
				loader.loadClass(classname);
			}			
		}
	}
	
	/**
	 * Läd eine KeywordLibrary aus der angegebenen Klasse mit dem
	 * angegeben vollständigen Klassennamen.
	 * 
	 * @param libClass
	 *            Klasse der Bibliothek
	 * @param name
	 *            Vollständiger Name (Bsp: 'DeviceLibrary', oder
	 *            'example.DeviceLibrary' wenn die Klasse in einem paket ist
	 * @return Bibliothek
	 * @throws ClassNotFoundException
	 *             Die Klasse wurde nicht gefunden
	 * @throws KeywordLibraryException
	 *             Die Klasse ist keine gültige Bibliothek
	 * @throws IOException
	 *             Die Jar-Datei wurde nicht gefunden
	 */
	public static KeywordLibrary loadLibrary(String name, Class<?> libClass) throws KeywordLibraryException {
		Constructor<?> klConstr = null;
		Object libInstance = null;
		try {
			klConstr = libClass.getConstructor();
			libInstance = klConstr.newInstance();
		} catch (NoSuchMethodException | SecurityException e) {
			throw ExceptionHandler.NoDefaultContrucotr(name);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
		}

		KeywordLibrary lib = new KeywordLibrary(name, libInstance);

		return lib;
	}
}
