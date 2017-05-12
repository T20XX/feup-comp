import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import patternParser.BasicNode;

public class MyASTVisitor extends ASTVisitor {
	
	private BasicNode nodeToFind;
	private ASTNode correspondingNode;

	public MyASTVisitor(BasicNode nodeToFind) {
		super();
		
		this.nodeToFind = nodeToFind;
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
		
		if(this.nodeToFind.getType() == BasicNode.Type.AssignmentExpression)
		{
			this.correspondingNode = node;
		}
		
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
