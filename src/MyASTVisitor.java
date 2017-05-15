import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import patternParser.BasicNode;
import patternParser.LocalVariableDeclarationStatement;
import patternParser.TypeType;
import patternParser.VariableDeclarator;
import patternParser.VariableDeclaratorId;

public class MyASTVisitor extends ASTVisitor {
	
	private BasicNode nodeToFind;
	private ASTNode correspondingNode;
	private boolean found;
	private int position;

	public MyASTVisitor(ASTNode nodeToSearch, BasicNode nodeToFind) {
		super();
		
		this.nodeToFind = nodeToFind;
		
		nodeToSearch.accept(this);
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		
		this.found = false;
		
		if(this.nodeToFind.getType() == BasicNode.Type.LocalVariableDeclarationStatement){
			
			TypeType typeType = (TypeType)((LocalVariableDeclarationStatement) nodeToFind).getTypeType();
			VariableDeclarator variableDeclarator = (VariableDeclarator)((LocalVariableDeclarationStatement) nodeToFind).getVariableDeclarator();
		
			MyASTVisitor typeTypeVisitor = new MyASTVisitor(node, typeType.getFirstChild());
			MyASTVisitor variableDeclaratorVisitor = new MyASTVisitor(node, variableDeclarator);
			
			this.found = typeTypeVisitor.isFound() && variableDeclaratorVisitor.isFound();
			
			if(found)
			{
				System.out.println("Found match on position: " + node.getStartPosition());
			}
		}	
		
		return true;
	}
	
	
	public boolean visit(PrimitiveType node)
	{
		this.found = false;
		
		if(this.nodeToFind.getType() == BasicNode.Type.PrimitiveType){
			
			if (node.getPrimitiveTypeCode().toString().equalsIgnoreCase(nodeToFind.getValue())){
				this.found = true;
			}
		}
		
		return true;
	}
	
	public boolean visit(VariableDeclarationFragment node)
	{
		if(this.nodeToFind.getType() == BasicNode.Type.VariableDeclarator)
		{
			VariableDeclaratorId variableDeclaratorId = (VariableDeclaratorId) ((VariableDeclarator) nodeToFind).getVariableDeclaratorId();
			if(node.getName().toString().equals(variableDeclaratorId.getIdentifier().getValue()))
			{				
				this.found = true;
			}
		}
		
		return true;
	}


	public BasicNode getNodeToFind() {
		return nodeToFind;
	}

	public ASTNode getCorrespondingNode() {
		return correspondingNode;
	}

	public boolean isFound() {
		return found;
	}

}
