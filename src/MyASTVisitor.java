import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import patternParser.*;

public class MyASTVisitor extends ASTVisitor {
	
	private CompilationUnit cu;
	private BasicNode nodeToFind;
	private ASTNode correspondingNode;

	public MyASTVisitor(CompilationUnit cu, BasicNode nodeToFind) {
		super();
		
		this.cu = cu;
		this.nodeToFind = nodeToFind;
		
		cu.accept(this);
	}

	@Override
	public boolean visit(IfStatement node) {
		
		return super.visit(node);
		
	}

	@Override
	public boolean visit(VariableDeclarationExpression node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}

	@Override
	public boolean visit(VariableDeclarationFragment node) {
		
		
		
		return false; // do not visit children
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}

	public BasicNode getNodeToFind() {
		return nodeToFind;
	}

	public ASTNode getCorrespondingNode() {
		return correspondingNode;
	}
	

}
