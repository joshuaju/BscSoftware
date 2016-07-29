package external;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import annotations.KeywordLibrary;
import application.property.UserPreferences;
import exceptions.keywordlibrary.KeywordLibraryException;
import exceptions.keywordlibrary.KeywordLibraryExceptionHandler;

/**
 * This class offers methods to load keyword libraries.
 * 
 * @author JJungen
 *
 */
public class LibraryLoader implements Closeable {

	private static LibraryLoader instance = null;

	/**
	 * Creates a new instance
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws KeywordLibraryException
	 */
	public static LibraryLoader createInstance() throws ClassNotFoundException, IOException, KeywordLibraryException {
		if (instance != null) {
			try {
				instance.close();
			} catch (IOException e) {
			} finally {
				instance = null;
			}
		}
		String defLibraries = UserPreferences.get().getOrDefault(UserPreferences.STD_LIBRARY_DIR);
		instance = new LibraryLoader(defLibraries);
		System.out.println("Created Library Loader with libraries from: " + defLibraries);
		return instance;
	}

	/**
	 * Returns the current instance or NULL
	 * 
	 * @return
	 */
	public static LibraryLoader getInstance() {
		return instance;
	}

	/**
	 * ClassLoader for all default keyword libraries
	 */
	private final URLClassLoader defaultLoader;

	/**
	 * List of default keyword libraries
	 */
	private final ArrayList<ExecutableKeywordLibrary> defaultLibraries;

	private LibraryLoader(String... loadDir) throws ClassNotFoundException, IOException, KeywordLibraryException {
		defaultLibraries = new ArrayList<>();
		defaultLoader = initDefaultLoader(loadDir);
	}

	public boolean hasDefaultLibraries() {
		if (defaultLibraries == null) {
			return false;
		}
		return defaultLibraries.size() > 0;
	}

	/**
	 * Returns all default keyword libraries
	 * 
	 * @return
	 */
	public Collection<ExecutableKeywordLibrary> getDefaultLibraries() {
		return defaultLibraries;
	}

	/**
	 * Erstellt einen URLClassloader mit allen Jar Dateien aus den angegebenen
	 * Verzeichnispfad. Alle Klassen der Jar Dateien werden in den ClassLoader
	 * geladen. Alle KeywordLibrary werden zu den Standard-Bibliotheken
	 * hinzugefügt.
	 * 
	 * @see LibraryLoader#getDefaultLibraries()
	 * 
	 * @param loadFilePaths
	 *            Ordner der vollständig geladen wird
	 * @return ClassLoader mit allen Klassen aller JarVerzeichnisse
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws KeywordLibraryException
	 */
	private URLClassLoader initDefaultLoader(String... loadFilePaths) throws KeywordLibraryException, IOException {
		File[] allFiles = getJarFiles(loadFilePaths);
		URLClassLoader loader = getURLClassLoader(allFiles);

		for (File tmpFile : allFiles) {
			try {
				Class<?>[] loadedClasses = loadClasses(tmpFile, loader);
				for (Class<?> tmpClass : loadedClasses) {
					if (tmpClass.isAnnotationPresent(annotations.KeywordLibrary.class)) {
						SimpleKeywordLibrary simpleLibrary = createSimpleLibraryInstance(tmpClass, tmpFile);
						ExecutableKeywordLibrary instantiatedLibrary = createExecutableLibraryInstance(simpleLibrary);
						defaultLibraries.add(instantiatedLibrary);
						System.out.println("Loaded: " + instantiatedLibrary.getName());
					}
				}
			} catch (IOException e) {
				throw KeywordLibraryExceptionHandler.JarFileProcessing(tmpFile, e);
			}
		}
		return loader;
	}

	/**
	 * Load an {@link ExecutableKeywordLibrary}-Instance from the specified path
	 * 
	 * @param path
	 *            Path to KeywordLibrary Jar-File
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws KeywordLibraryException
	 */
	public ExecutableKeywordLibrary loadExecutableKeywordLibrary(String path)
			throws ClassNotFoundException, IOException, KeywordLibraryException {

		SimpleKeywordLibrary simpleLibrary = loadSimpleKeywordLibrary(path);
		return createExecutableLibraryInstance(simpleLibrary);
	}

	/**
	 * Load a {@link SimpleKeywordLibrary}-Instance from the specified path
	 * 
	 * @param path
	 *            Path to KeywordLibrary Jar-File
	 * @return
	 * @throws IOException
	 * @throws KeywordLibraryException
	 */
	public SimpleKeywordLibrary loadSimpleKeywordLibrary(String path) throws IOException, KeywordLibraryException {
		File file = getJarFile(path);
		URLClassLoader loader = getURLClassLoader(file);

		loadClasses(file, loader);

		Class<?> libraryClass = getLibraryClass(file, loader);
		return createSimpleLibraryInstance(libraryClass, file);
	}

	/**
	 * Load a Jar-File from the specified path
	 * 
	 * @param path
	 *            Path to Jar-File
	 * @return
	 * @throws FileNotFoundException
	 *             File does not exists or is not a Jar-File
	 */
	private File getJarFile(String path) throws FileNotFoundException {
		String libraryDir = UserPreferences.get().getOrDefault(UserPreferences.LIBRARY_DIR);
		path = getRelativePath(path);
		File libFile = new File(libraryDir, path);

		if (!libFile.exists() || !libFile.isFile()) {
			throw KeywordLibraryExceptionHandler.NoSuchFile(libFile);
		}

		if (!path.endsWith(".jar")) {
			throw KeywordLibraryExceptionHandler.NoSuchJarFile(libFile);
		}

		return libFile;
	}

	/**
	 * Return all Jar-File from the specified paths. Directories are recursively
	 * searched for Jar-Files.
	 * 
	 * @param path
	 *            Paths to files or directories
	 * @return
	 * @throws FileNotFoundException
	 */
	private File[] getJarFiles(String... path) throws FileNotFoundException {
		ArrayList<File> files = new ArrayList<>(path.length);
		for (String tmpPath : path) {
			if (tmpPath != null) {
				tmpPath = tmpPath.trim();
				if (tmpPath.length() > 0) {
					File tmpFile = new File(tmpPath);
					if (tmpFile.exists()) {
						if (tmpFile.isFile()) {
							tmpFile = getJarFile(tmpPath);
						} else {
							ArrayList<File> dirJarFiles = new ArrayList<>();
							File[] dirFiles = tmpFile.listFiles();
							for (File dirFile : dirFiles) {
								if (dirFile.isDirectory()) {
									getJarFiles(dirFile.list());
								} else {
									dirJarFiles.add(dirFile);
								}
							}
							files.addAll(dirJarFiles);
						}
					}
				}
			}
		}
		return files.toArray(new File[0]);
	}

	/**
	 * Creates an {@link URLClassLoader} with URLs to default keyword libraries
	 * 
	 * @param file
	 * @return
	 * @throws MalformedURLException
	 */
	private URLClassLoader getURLClassLoader(File... file) throws MalformedURLException {
		URL[] urls = createURLArray(file);
		URLClassLoader loader = null;
		if (defaultLoader != null) {
			loader = new URLClassLoader(urls, defaultLoader);
		} else {
			loader = new URLClassLoader(urls);
		}
		return loader;
	}

	/**
	 * Load all classes from a Jar-File to the specified {@link URLClassLoader}
	 * 
	 * @param file
	 *            keyword library Jar-File
	 * @param loader
	 * @return loaded Classes
	 * @throws KeywordLibraryException
	 * @throws IOException
	 */
	private Class<?>[] loadClasses(File file, URLClassLoader loader) throws KeywordLibraryException, IOException {
		ArrayList<Class<?>> loadedClasses = new ArrayList<>();
		JarFile jar = new JarFile(file);

		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry tmpEntry = entries.nextElement();
			String filename = tmpEntry.getName();
			if (filename.endsWith(".class")) {
				String classname = filename.replace("/", ".").substring(0, filename.length() - 6);
				Class<?> tmpClass = loadClass(classname, loader);
				loadedClasses.add(tmpClass);
			}
		}
		jar.close();
		return loadedClasses.toArray(new Class<?>[0]);
	}

	/**
	 * Load a class by it complete name (package names and class name) from the
	 * specified {@link URLClassLoader}.
	 * 
	 * @param classname
	 * @param loader
	 * @return loaded Class
	 * @throws KeywordLibraryException
	 */
	private Class<?> loadClass(String classname, URLClassLoader loader) throws KeywordLibraryException {
		Class<?> tmpClass = null;
		try {
			tmpClass = loader.loadClass(classname);
		} catch (ClassNotFoundException e) {
			throw KeywordLibraryExceptionHandler.NoSuchClassInJarfile(classname, e);
		}
		return tmpClass;
	}

	/**
	 * Load the keyword library class from a Jar-File.
	 * 
	 * @param libraryFile
	 *            Filename needs to obey naming convention to work (e.g. if
	 *            Classpath is 'com/example/FooLibrary.class' the filename has
	 *            to be 'com.example.FooLibrary')
	 * @param loader
	 *            All classes from keyword library must be loaded
	 *            {@link #loadClasses(File, URLClassLoader)} to the ClassLoader
	 * @return loaded Class
	 * @throws KeywordLibraryException
	 */
	private Class<?> getLibraryClass(File libraryFile, ClassLoader loader) throws KeywordLibraryException {
		String libraryName = getLibraryNameFromFile(libraryFile);
		Class<?> libClass = null;
		try {
			libClass = loader.loadClass(libraryName);
		} catch (ClassNotFoundException e) {
			throw KeywordLibraryExceptionHandler.NoSuchClassInJarfile(libraryName, e);
		}
		return libClass;
	}

	/**
	 * Create a {@link SimpleKeywordLibrary}
	 * 
	 * @param libraryClass
	 *            Class with {@link KeywordLibrary}-Annotation
	 * @param file
	 *            File of this class
	 * @return
	 * @throws KeywordLibraryException
	 */
	private SimpleKeywordLibrary createSimpleLibraryInstance(Class<?> libraryClass, File file)
			throws KeywordLibraryException {
		if (!libraryClass.isAnnotationPresent(annotations.KeywordLibrary.class)) {
			throw KeywordLibraryExceptionHandler.ClassIsNotAKeywordLibrary(libraryClass);
		}

		return new SimpleKeywordLibrary(libraryClass, file);
	}

	/**
	 * Create a {@link ExecutableKeywordLibrary}
	 * 
	 * @param simple
	 * @return
	 * @throws KeywordLibraryException
	 */
	private ExecutableKeywordLibrary createExecutableLibraryInstance(SimpleKeywordLibrary simple)
			throws KeywordLibraryException {
		Constructor<?> libConstructor = null;
		try {
			libConstructor = simple.getLibraryClass().getConstructor();
		} catch (NoSuchMethodException | SecurityException e) {
			throw KeywordLibraryExceptionHandler.NoDefaultConstructor(simple.getLibraryClass());
		}

		Object libInstance = null;

		try {
			libInstance = libConstructor.newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw KeywordLibraryExceptionHandler.CouldNotInstantiate(simple.getLibraryClass(), e);
		}

		ExecutableKeywordLibrary library = new ExecutableKeywordLibrary(libInstance, simple);
		return library;
	}

	/**
	 * Retrieve keyword library name from the filename.
	 * 
	 * @param file
	 * @return
	 */
	private String getLibraryNameFromFile(File file) {
		String libname = file.getName();
		return libname.substring(0, libname.lastIndexOf('.'));
	}

	private URL[] createURLArray(File... files) throws MalformedURLException {
		URL[] urls = new URL[files.length];
		for (int i = 0; i < files.length; i++) {
			urls[i] = files[i].toURI().toURL();
		}
		return urls;
	}

	/**
	 * Retrieves a relative path to the keyword library directory
	 * 
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 */
	private String getRelativePath(String path) throws FileNotFoundException {
		String libraryDir = UserPreferences.get().getOrDefault(UserPreferences.LIBRARY_DIR);
		File libraryDirFile = new File(libraryDir);
		File pathFile = new File(path);

		String absFilePath = pathFile.getAbsolutePath();
		String absDirPath = libraryDirFile.getAbsolutePath();
		if (absFilePath.startsWith(absDirPath)) {
			return absFilePath.substring(absDirPath.length());
		}
		return path;
	}

	@Override
	public void close() throws IOException {
		if (defaultLoader != null) {
			defaultLoader.close();
		}
	}
}
