package input;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import exceptions.testfile.TestfileExceptionHandler;

abstract class AbstractFileReader {

	/**
	 * Retrieves a file from the specified path
	 * 
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 *             File not found or file is directory
	 */
	public File getFileFromPath(String path) throws FileNotFoundException {
		File file = new File(path);
		if (!file.exists() || !file.isFile()) {
			throw TestfileExceptionHandler.NoSuchFile(file);
		}

		return file;
	}

	/**
	 * Retrieves all lines from a file
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public String[] getLinesFromFile(File file) throws IOException {
		List<String> lines = new ArrayList<>();

		try {
			lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw TestfileExceptionHandler.CouldNotReadFile(file, e);
		}
		return lines.toArray(new String[0]);
	}

}
