package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class MyFileReader {
	
	public static final String DEFAULT_CHAR_SET = "UTF-8";
	
	
	/**
	 * Given a File object, returns a String with the contents of the file.
	 * 
	 * <p>
	 * If an error occurs (ex.: the File argument does not represent a file) returns null and logs the cause.
	 * 
	 * @param file
	 *            a File object representing a file.
	 * @return a String with the contents of the file.
	 */
	public static String read(File file) {
		// Check null argument. If null, it would raise and exception and stop
		// the program when used to create the File object.
		if (file == null) {
			Logger.getLogger("info").info("Input 'file' is null.");
			return null;
		}

		StringBuilder stringBuilder = new StringBuilder();

		// Try to read the contents of the file into the StringBuilder

		try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),
				DEFAULT_CHAR_SET))) {

			// Read first character. It can't be cast to "char", otherwise the
			// -1 will be converted in a character.
			// First test for -1, then cast.
			int intChar = bufferedReader.read();
			while (intChar != -1) {
				char character = (char) intChar;
				stringBuilder.append(character);
				intChar = bufferedReader.read();
			}

		} catch (FileNotFoundException ex) {
			Logger.getLogger("info").info("FileNotFoundException: " + ex.getMessage());
			return null;

		} catch (IOException ex) {
			Logger.getLogger("info").info("IOException: " + ex.getMessage());
			return null;
		}

		return stringBuilder.toString();
	}


}
