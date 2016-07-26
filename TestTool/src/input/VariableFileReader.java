package input;

import java.io.File;
import java.io.IOException;

import exceptions.testfile.TestfileSyntaxException;

/**
 * This class reads files with global variables
 * 
 * @author JJungen
 *
 */
public class VariableFileReader extends AbstractFileReader {

	private static VariableFileReader vfReader = null;

	public static VariableFile readAll(String testfilePath, String... path)
			throws IOException, TestfileSyntaxException {
		if (vfReader == null) {
			vfReader = new VariableFileReader();
		}
		return vfReader._read(testfilePath, path);
	}

	private VariableFile _read(String testfilePath, String... paths) throws IOException, TestfileSyntaxException {
		VariableFile globalFile = new VariableFile();
		for (String path : paths) {
			path = path.replace("\"", "");
			File file;
			if (!path.contains(":")) {
				File testfile = new File(testfilePath);
				file = new File(testfile.getParentFile(), path);
			} else {
				file = new File(path);
			}

			String[] lines = getLinesFromFile(file);
			readLinesToVariableFile(lines, file, globalFile);
		}
		return globalFile;
	}

	private VariableFile readLinesToVariableFile(String[] lines, File file, VariableFile varFile)
			throws TestfileSyntaxException {
		TestfileParser syntaxer = new TestfileParser();
		try {
			syntaxer.checkVariableLines(lines);
		} catch (TestfileSyntaxException e) {
			throw new TestfileSyntaxException("Variablendatei " + file.getAbsolutePath() + ": " + e.getMessage());
		}

		String line;
		for (int i = 0; i < lines.length; i++) {
			line = lines[i];
			if (line.length() == 0 || line.startsWith(Testfile.TAG_COMMENT)) {
				continue;
			} else if (line.contains("=")) {
				String[] split = line.split(TestfileParser.OPERATOR_ASSIGN);
				String var = split[0].trim();
				String val = split[1].trim();

				var = var.replaceAll("[{}]", "");
				val = val.replace("\"", "");

				varFile.addVariable(var, val);
			}
		}
		return varFile;
	}

}
