

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
import java.util.HashMap;
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

import patternParser.BasicNode;
import patternParser.BasicNode.Type;
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
			cu = eclipseAST(new String(Files.readAllBytes(Paths.get("input" + FS + JavaFilePath))).toCharArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BasicNode rootNode = getAST(root);
		
		findPattern(cu, rootNode);
	
		System.out.println(rootNode);
		
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
	
	private static void findPattern(CompilationUnit cu, BasicNode node){
		
		if(cu == null)
		{
			System.out.println("Given CompilationUnit is null.");
			return;
		}
		
		BasicNode.Type type = node.getType();
		
		MyASTVisitor visitor = new MyASTVisitor(cu, node);
		
		for(int i = 0; i < node.getChildren().size(); i++)
		{
			findPattern(cu, node.getChildren().get(i));
		}
		
		
		
	}
	
	
	public static BasicNode getAST(SimpleNode node) {
		
		BasicNode ret = BasicNode.parseFromString(node.toString());
		
		
		for (int i = 0; i < node.jjtGetNumChildren(); i++)
		{
			SimpleNode n = (SimpleNode) node.jjtGetChild(i);
			if(n != null)
			{
				ret.addChild(getAST(n));
			}
		}
		
		return ret;
		
	  }

}