

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gson.Gson;

import patternsGrammar.ParseException;
import patternsGrammar.Parser;
import patternsGrammar.SimpleNode;

public class PAT {

	public static final String FS = System.getProperty("file.separator");
	public static final String DEFAULT_CHAR_SET = "UTF-8";

	public static void main(String[] args) {

		if(args.length != 2){
			System.out.println("Usage: PAT <DSL File> <Java File>");
			return;
		}

		String DSLFilePath = args[0];
		String JavaFilePath = args[1];
		
		InputStream input = System.in;
	    if(args.length > 0)
			try {
				input = new FileInputStream("input" + FS + DSLFilePath);
				   
			    Parser parser = new Parser(input);
			    SimpleNode root = parser.Statement();
			    root.dump("");
			} catch (FileNotFoundException | ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		// Generate AST from java code 
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", ".." + FS + "lib" + FS + "spoon2ast.jar", ".." + FS + "input" + FS + JavaFilePath)
				.directory(new File("." + FS + "output"))
				.inheritIO();
		try {
			Process p = pb.start();
			p.waitFor();
			p.getOutputStream();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		String JavaASTPath = "." + FS + "output" + FS + "ast.json";

		File json = new File(JavaASTPath);

		Map<String, Object> jsonJavaRootObject = new Gson().fromJson(read(json), Map.class);

		//System.out.println("JSON:\n" + jsonJavaRootObject);

		ArrayList<ArrayList<Map<String, Object>>> codeSections = getCodeSectionsFromRoot(jsonJavaRootObject);

		for(ArrayList<Map<String, Object>> codeSection : codeSections){
			for(Map<String, Object> statement : codeSection){
				System.out.println(statement);
			}
			System.out.println();
		}

	}

	@SuppressWarnings("unchecked")
	public static ArrayList<ArrayList<Map<String, Object>>> getCodeSectionsFromRoot(Map<String, Object> jsonJavaRootObject) {
		ArrayList<ArrayList<Map<String, Object>>> statements = new ArrayList<ArrayList<Map<String, Object>>>();

		//Compilation Units
		ArrayList<Map<String, Object>> compilation_units = (ArrayList<Map<String, Object>>) jsonJavaRootObject.get("compilation_units");

		//Types
		ArrayList<Map<String, Object>> types = (ArrayList<Map<String, Object>>) compilation_units.get(0).get("types");

		//Members
		ArrayList<Map<String, Object>> members = (ArrayList<Map<String, Object>>) types.get(0).get("members");

		//Bodies
		ArrayList<Map<String, Object>> bodies = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> member : members){
			bodies.add((Map<String, Object>) member.get("body"));
		}

		//Bodies
		for(Map<String, Object> body : bodies){
			statements.add((ArrayList<Map<String, Object>>) body.get("statements"));
		}

		return statements;
	}

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