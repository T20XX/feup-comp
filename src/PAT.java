

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jsonParser.adapters.MyNodeDeserializer;
import jsonParser.containers.BasicNode;
import jsonParser.containers.Member;
import jsonParser.containers.Reference;
import jsonParser.containers.Root;
import patternsGrammar.ParseException;
import patternsGrammar.Parser;
import patternsGrammar.SimpleNode;
import utils.MyFileReader;

public class PAT {

	public static final String FS = System.getProperty("file.separator");
	

	public static void main(String[] args) {

		if(args.length != 2){
			System.out.println("Usage: PAT <DSL File> <Java File>");
			return;
		}

		String DSLFilePath = args[0];
		String JavaFilePath = args[1];
		
		SimpleNode root = null;
		InputStream input = System.in;
		if(args.length > 0)
			try {
				input = new FileInputStream("input" + FS + DSLFilePath);

				Parser parser = new Parser(input);
				root = parser.Statement();
				root.dump("");
			} catch (FileNotFoundException | ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
		
		CompilationUnit cu = null;
		
		try {
			cu = eclipseAST(new String(Files.readAllBytes(Paths.get("/home/jazz/MyRepos/FEUP/3a-2s/feup-comp/input/" + JavaFilePath))).toCharArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		findIf(cu);
		
		System.out.println("AST String: ");
		System.out.println(getASTString(root, ""));
		
		if(true)
		{
			return;
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
		
		MyNodeDeserializer typeAdapter = new MyNodeDeserializer();
		
		Gson gsonRead = new GsonBuilder()
				.registerTypeAdapter(BasicNode.class, typeAdapter)
				.registerTypeAdapter(Reference.class, typeAdapter)
				.registerTypeAdapter(Expression.class, typeAdapter)
				.registerTypeAdapter(Member.class, typeAdapter)
				.setPrettyPrinting()
				.create();
				
		Root jsonRootObject = gsonRead.fromJson(MyFileReader.read(json), Root.class);
		
		Gson gsonWrite = new GsonBuilder()
				.setPrettyPrinting()
				.disableHtmlEscaping()
				.create();
		System.out.println("Root: \n" +  gsonWrite.toJson(jsonRootObject));
		
				
		
		

		Map<String, Object> jsonJavaRootObject = gsonRead.fromJson(MyFileReader.read(json), Map.class);

		//System.out.println("JSON:\n" + jsonJavaRootObject);

		ArrayList<ArrayList<Map<String, Object>>> codeSections = getCodeSectionsFromRoot(jsonJavaRootObject);

		for(ArrayList<Map<String, Object>> codeSection : codeSections){
			for(Map<String, Object> statement : codeSection){
				System.out.println(statement);
			}
			System.out.println();
		}

		System.out.println(root.jjtGetValue());
		//PATTERNS
		for(int i=0; i < root.jjtGetNumChildren(); i++){
			((SimpleNode) root.jjtGetChild(i)).dump("");
		}

		//CICLO DE COMPARAR ASTS!
		for(ArrayList<Map<String, Object>> codeSection : codeSections){
			for(Map<String, Object> statement : codeSection){
				if(sameType(statement.get("nodetype").toString(),root.jjtGetValue().toString())){
					System.out.println("ENCONTREI UM IF");
				}
			}
			System.out.println();
		}

	}

	@SuppressWarnings("unchecked")
	private static ArrayList<ArrayList<Map<String, Object>>> getCodeSectionsFromRoot(Map<String, Object> jsonJavaRootObject) {
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

	private static SimpleNode cleanRoot(SimpleNode root) {
		SimpleNode cleanedRoot = new SimpleNode(0);

		for(int i=0; i < root.jjtGetNumChildren(); i++){
			if(((SimpleNode) root.jjtGetChild(i)).jjtGetValue() != null)
				cleanedRoot.jjtAddChild(root.jjtGetChild(i),0);
			cleanRoot((SimpleNode) root.jjtGetChild(i));
		}

		return cleanedRoot;
	}





	private static boolean sameType(String javaType, String dslType){
		if(javaType.equals("If") && dslType.equals("if"))
			return true;
		else if(javaType.equals("LocalVariable") && dslType.equals("="))
			return true;


		return false;
	}
	
	private static CompilationUnit eclipseAST(char[] unit){
		
		ASTParser parser = ASTParser.newParser(AST.JLS8); 
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit); // set source
		
		parser.setResolveBindings(true); // we need bindings later on
		parser.setBindingsRecovery(true);
		
		Map options = JavaCore.getOptions();
		parser.setCompilerOptions(options);
		
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		
		return cu;	
		
	}
	
	private static void findIf(CompilationUnit cu)
	{
		if(cu == null)
		{
			System.out.println("Given CompilationUnit is null.");
			return;
		}

		ASTVisitor visitor = (new ASTVisitor() {
			 
			/*public boolean visit(VariableDeclarationFragment node) {
				SimpleName name = node.getName();
				this.names.add(name.getIdentifier());
				System.out.println("Declaration of '" + name + "' at line"
						+ cu.getLineNumber(name.getStartPosition()));
				return true; // do not continue 
			}

			public boolean visit(SimpleName node) {
				if (this.names.contains(node.getIdentifier())) {
					System.out.println("Usage of '" + node + "' at line "
							+ cu.getLineNumber(node.getStartPosition()));
				}
				return true;
			}*/
			
			/*
			public boolean visit(MethodDeclaration node) {
				SimpleName name = node.getName();
				this.names.add(name.getIdentifier());
				System.out.println("Declaration of '" + name + "' at line"
						+ cu.getLineNumber(name.getStartPosition()));
				return true; // do not continue 
			}
			
			public boolean visit(ConditionalExpression node) {
				Expression expr = node.getExpression();
				this.names.add(expr.toString());
				System.out.println("Declaration of '" + expr + "' at line"
						+ cu.getLineNumber(expr.getStartPosition()));
				return true; // do not continue 
			}
			
			*/
			public boolean visit(IfStatement node) {
				Expression expr = node.getExpression();
				
				expr.accept(new ASTVisitor(){
					public boolean visit(InfixExpression node) {
						
						SimpleName simpleName = (SimpleName) node.getLeftOperand();
						
						if(simpleName.getIdentifier().equals("a"))
						{
							System.out.println("Found if at line " + cu.getLineNumber(simpleName.getStartPosition()));
						}
						
							return true; // do not continue 
					}
				});
				
				return true;
			}
			
		});
		
		cu.accept(visitor);
	}
	
	public static String getASTString(SimpleNode node, String prefix) {
		
		String ret = "";
		
		ret = ret + node.toString();
		prefix = prefix + "-";
		
		
		for (int i = 0; i < node.jjtGetNumChildren(); i++)
		{
			SimpleNode n = (SimpleNode) node.jjtGetChild(i);
			if(n != null)
			{
				ret = ret + "\n";
				ret = ret + prefix;
				ret = ret + getASTString(n, prefix);
			}
		}
		
		return ret;
		
	  }
}