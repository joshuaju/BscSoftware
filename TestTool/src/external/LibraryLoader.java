package external;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import exceptions.keywordlibrary.KeywordLibraryException;
import exceptions.keywordlibrary.KeywordLibraryExceptionHandler;

public class LibraryLoader implements Closeable {

	private static LibraryLoader instance = null;

	/**
	 * Gibt die aktuelle Instanz der Klasse zurück, oder erzeugt eine neue.
	 * 
	 * @return
	 * @throws KeywordLibraryException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static LibraryLoader getInstance() {
		if (instance == null) {
			instance = new LibraryLoader();
		}
		return instance;
	}

	/**
	 * Erzeugt eine neue Instanz nachdem die alte (falls vorhanden) beendet
	 * wurde.
	 * 
	 * @return
	 * @throws KeywordLibraryException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static LibraryLoader getNewInstance() {
		if (instance != null) {
			try {
				instance.close();
			} catch (IOException e) {
			} finally {
				instance = null;
			}
		}
		instance = new LibraryLoader();
		return instance;
	}

	/**
	 * Beendet eine alte Instanz (falls vorhanden) und erstellt eine neue mit
	 * dem angegebenen Verzeichnispfad. In dem Verzeichnispfad liegen alle
	 * referenzierten Klassen und standard Bibliotheken.
	 * 
	 * @param loadDir
	 *            Ordner der geladen wird
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws KeywordLibraryException
	 */
	public static LibraryLoader getInstance(String... loadDir)
			throws ClassNotFoundException, IOException, KeywordLibraryException {
		if (instance != null) {
			try {
				instance.close();
			} catch (IOException e) {
			} finally {
				instance = null;
			}
		}
		instance = new LibraryLoader(loadDir);
		return instance;
	}

	/**
	 * Standard Classloader, hier werden StandardBibliotheken und referenzierte
	 * Klassen reingeladen
	 */
	private final URLClassLoader defaultLoader;

	private final ArrayList<KeywordLibrary> defaultLibraries;

	/**
	 * Erzeugt eine neue Instanz ohne einen URLClassloader
	 * @throws KeywordLibraryException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private LibraryLoader() {
		defaultLibraries = new ArrayList<>();
		defaultLoader = new URLClassLoader(new URL[]{}, URLClassLoader.getSystemClassLoader());		
	}

	/**
	 * Erzeugt eine neue Instanz mit einem URLClassloader
	 * 
	 * @param loadDir
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws KeywordLibraryException
	 */
	private LibraryLoader(String... loadDir) throws ClassNotFoundException, IOException, KeywordLibraryException {
		defaultLibraries = new ArrayList<>();
		defaultLoader = initDefaultLoader(loadDir);
	}

	/**
	 * Überprüft, ob in den standardmäßig geladenen Klassen eine Bibliothek
	 * entdeckt und gespeichert wurde
	 * 
	 * @return
	 */
	public boolean hasDefaultLibraries() {
		if (defaultLibraries == null) {
			return false;
		}
		return defaultLibraries.size() > 0;
	}

	/**
	 * Gibt die Standard Bibliotheken zurück
	 * 
	 * @return
	 */
	public KeywordLibrary[] getDefaultLibraries() {
		return defaultLibraries.toArray(new KeywordLibrary[0]);
	}

	/**
	 * Erstellt einen URLClassloader mit allen Jar Dateien aus den angegebenen
	 * Verzeichnispfad. Alle Klassen der Jar Dateien werden in den ClassLoader
	 * geladen.
	 * 
	 * @param loadFilePaths
	 *            Ordner der vollständig geladen wird
	 * @return ClassLoader mit allen Klassen aller JarVerzeichnisse
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws KeywordLibraryException
	 */
	private URLClassLoader initDefaultLoader(String... loadFilePaths)
			throws ClassNotFoundException, IOException, KeywordLibraryException {
		
		ArrayList<File> allFiles = new ArrayList<>();
		for (String tmpFilePath: loadFilePaths){
			tmpFilePath = tmpFilePath.trim();
			if (tmpFilePath.length() == 0){
				continue;
			}
			File tmpFile = new File(tmpFilePath);
			if (!tmpFile.exists()){
				throw KeywordLibraryExceptionHandler.NoSuchFile(tmpFile);
			}
			
			if (tmpFile.isDirectory()) {
				File[] filesInDir = tmpFile.listFiles((fileDir, name) -> name.endsWith(".jar")); 
				allFiles.addAll(Arrays.asList(filesInDir));
			} else {				
				if (!tmpFile.getName().endsWith(".jar")){
					throw KeywordLibraryExceptionHandler.NoSuchJarFile(tmpFile);
				} 
				allFiles.add(tmpFile);
				
			}					
		}
				
		URL[] urls = makeURLArray(allFiles.toArray(new File[0]));
		URLClassLoader loader = new URLClassLoader(urls);
		for (File tmpFile : allFiles) {
			try {
				JarFile jar = new JarFile(tmpFile);
				loadAllClasses(jar, loader, true);
			} catch (IOException e) {
				throw KeywordLibraryExceptionHandler.JarFileProcessing(tmpFile, e);
			}
		}
		return loader;
	}

	/**
	 * Erzeugt ein URL-Array aus den angegeben Dateien
	 * 
	 * @param files
	 * @return
	 * @throws MalformedURLException
	 */
	private URL[] makeURLArray(File... files) throws MalformedURLException {
		URL[] urls = new URL[files.length];
		for (int i = 0; i < files.length; i++) {
			urls[i] = files[i].toURI().toURL();
		}
		return urls;
	}

	/**
	 * Läd alle Klassen der angegeben JarFile in den URLClassloader. Die URL zur
	 * JarFile muss beim erzeugen des URLClassLoaders mit angegeben werden.
	 * 
	 * @param jar
	 *            Jar Verzeichnis
	 * @param loader
	 * @param addToDefault
	 *            Gibt an, ob Bibliotheken aus dem verzeichnis in den standard
	 *            Bibliotheken gespeichert werden soll.
	 * @throws KeywordLibraryException
	 */
	private void loadAllClasses(JarFile jar, URLClassLoader loader, boolean addToDefault)
			throws KeywordLibraryException {
		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry tmpEntry = entries.nextElement();
			String filename = tmpEntry.getName();
			if (filename.endsWith(".class")) {
				String classname = filename.replace("/", ".").substring(0, filename.length() - 6);
				Class<?> tmpClass = null;
				try {
					tmpClass = loader.loadClass(classname);
				} catch (ClassNotFoundException e) {
					throw KeywordLibraryExceptionHandler.NoSuchClassInJarfile(classname, e);
				}
				if (addToDefault && tmpClass.isAnnotationPresent(annotations.KeywordLibrary.class)) {
					KeywordLibrary keywordLib = createLibraryInstance(tmpClass);
					defaultLibraries.add(keywordLib);
				}
			}
		}
	}

	@Override
	public void close() throws IOException {
		if (defaultLoader != null) {			
			defaultLoader.close();
		}
	}

	/**
	 * Läd eine KeywordLibrary aus dem angegebenen Jar-Verzeichnis-Pfad
	 * 
	 * @param path
	 *            Pfad zum Jar-Verzeichnis
	 * @return Instanz der Bibliothek
	 * @throws ClassNotFoundException
	 *             Die Klasse mit dem Namen des Jar-Verzeichnis wurde nicht in
	 *             diesem gefunden
	 * @throws IOException
	 *             Die Datei ist kein Jar-Verzeichnis, oder die URL ist ungültig
	 * @throws KeywordLibraryException
	 *             Die Klasse ist keine Bibliothek
	 */
	public KeywordLibrary loadLibrary(String path) throws ClassNotFoundException, IOException, KeywordLibraryException {
		File libFile = new File(path);

		if (!libFile.exists() || !libFile.isFile()) {
			throw KeywordLibraryExceptionHandler.NoSuchFile(libFile);
		}

		if (!path.endsWith(".jar")) {
			throw KeywordLibraryExceptionHandler.NoSuchJarFile(libFile);
		}
		return loadLibrary(libFile);
	}

	/**
	 * Läd eine KeywordLibrary aus dem angegebenen Jar-Verzeichnis
	 * 
	 * @param libFile
	 *            Datei
	 * @return Instanz der Bibliothek
	 * @throws ClassNotFoundException
	 *             Die Klasse mit dem Namen des Jar-Verzeichnis wurde nicht in
	 *             diesem gefunden
	 * @throws IOException
	 *             Die Datei ist kein Jar-Verzeichnis, oder die URL ist ungültig
	 * @throws KeywordLibraryException
	 *             Die Klasse ist keine Bibliothek
	 */
	private KeywordLibrary loadLibrary(File libFile)
			throws ClassNotFoundException, IOException, KeywordLibraryException {
		JarFile jarFile = new JarFile(libFile);

		URL[] urls = makeURLArray(libFile);
		URLClassLoader loader = new URLClassLoader(urls, defaultLoader);

		loadAllClasses(jarFile, loader, false);

		return createLibraryInstance(libFile, loader);
	}

	/**
	 * Erzeugt eine KeywordLibrary anhand des Namens der Klasse und einem
	 * ClassLoader
	 * 
	 * @param libFile
	 *            Datei des Jar-Verzeichnis der Bibliothek
	 * @param loader
	 *            Loader, der die Klasse kennt
	 * @return Instanz der Library
	 * @throws ClassNotFoundException
	 *             Die Klasse befinet sich nicht im ClassLoader
	 * @throws KeywordLibraryException
	 *             Die Klasse ist keine Bibliothek
	 */
	private KeywordLibrary createLibraryInstance(File libFile, ClassLoader loader)
			throws ClassNotFoundException, KeywordLibraryException {
		String libClassName = getLibraryNameFromFile(libFile);
		Class<?> libClass = null;
		try {
			libClass = loader.loadClass(libClassName);
		} catch (ClassNotFoundException e) {
			throw KeywordLibraryExceptionHandler.NoSuchClassInJarfile(libClassName, e);
		}

		return createLibraryInstance(libClass);
	}

	private KeywordLibrary createLibraryInstance(Class<?> libClass) throws KeywordLibraryException {
		String libClassName = libClass.getName();
		if (!libClass.isAnnotationPresent(annotations.KeywordLibrary.class)) {
			throw KeywordLibraryExceptionHandler.ClassIsNotAKeywordLibrary(libClass);
		}

		Constructor<?> libConstructor = null;
		try {
			libConstructor = libClass.getConstructor();
		} catch (NoSuchMethodException | SecurityException e) {
			throw KeywordLibraryExceptionHandler.NoDefaultConstructor(libClass);
		}

		Object libInstance = null;
		try {
			libInstance = libConstructor.newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw KeywordLibraryExceptionHandler.CouldNotInstantiate(libClass, e);
		}

		return new KeywordLibrary(libClassName, libInstance);
	}

	/**
	 * Der Name einer Bibliotheksklasse entspricht dem Namen des Jar
	 * Verzeichnisses
	 * 
	 * @param file
	 * @return
	 */
	private String getLibraryNameFromFile(File file) {
		String libname = file.getName();
		return libname.substring(0, libname.lastIndexOf('.'));
	}

}
