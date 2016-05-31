package testfile;

import java.io.File;
import java.io.IOException;

import exceptions.testfile.TestfileException;
import exceptions.testfile.TestfileExceptionHandler;

public class VariableFileReader extends AbstractFileReader {

	private static VariableFileReader vfReader = null;

	public static VariableFile readAll(String... path) throws IOException {
		if (vfReader == null) {
			vfReader = new VariableFileReader();
		}
		return vfReader._read(path);
	}

	private VariableFile _read(String... paths) throws IOException {		
		VariableFile globalFile = new VariableFile();
		for (String path: paths){
			path = path.replace("\"", "");
			File file = getFileFromPath(path);
			String[] lines = getLinesFromFile(file);
			readLinesToVariableFile(lines, file.getName(), globalFile);
		}			
		return globalFile;
	}

	private VariableFile readLinesToVariableFile(String[] lines, String fileName, VariableFile varFile) throws TestfileException {
		String line;
		int lineNumber;
		for (int i = 0; i < lines.length; i++) {
			line = lines[i];
			lineNumber = i + 1;
			if (line.startsWith(Testfile.TAG_COMMENT)){
				continue;
			} else  if (line.contains("=")) {
				String[] split = line.split("=");
				if (split.length == 2) {
					String var = split[0].trim();
					String val = split[1].trim();

					if (Testline.isValidVariable(var) && Testline.isValidValue(val)) {
						var = var.replaceAll("[{}]", "");
						val = val.replace("\"", "");

						varFile.addVariable(var, val);
						continue;
					}
				}
			}
			throw TestfileExceptionHandler.InvalidVariableFileLine(lineNumber, fileName, line);
		}
		return varFile;		
	}

}
