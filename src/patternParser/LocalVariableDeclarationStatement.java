package patternParser;

public class LocalVariableDeclarationStatement extends Statement {
	
	public LocalVariableDeclarationStatement(Type type, String value) {
		super(type, value);
		// TODO Auto-generated constructor stub
	}
	
	public BasicNode getTypeType(){
		for (BasicNode basicNode : children) {
			if(basicNode.type == BasicNode.Type.TypeType)
				return basicNode;
		}
		
		return null;
	}
	
	public BasicNode getVariableDeclarator(){
		for (BasicNode basicNode : children) {
			if(basicNode.type == BasicNode.Type.VariableDeclarator)
				return basicNode;
		}
		
		return null;	
	}


}
