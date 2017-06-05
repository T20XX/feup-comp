package patternParser;

public class VariableDeclarator extends BasicNode {

	public VariableDeclarator(Type type, String value) {
		super(type, value);
		// TODO Auto-generated constructor stub
	}
	
	public BasicNode getVariableDeclaratorId(){
		for (BasicNode basicNode : children) {
			if(basicNode.type == BasicNode.Type.VariableDeclaratorId)
				return basicNode;
		}
		
		return null;
	}
	
	public String getOperand(){
		for (BasicNode basicNode : children) {
			if(basicNode.type == BasicNode.Type.VariableDeclaratorId)
				return basicNode.getFirstChild().value;
		}
		
		return null;
	}

	public boolean operandIsPattern() {
		for (BasicNode basicNode : children) {
			if(basicNode.type == BasicNode.Type.VariableDeclaratorId)
				return basicNode.getFirstChild().type == BasicNode.Type.Pattern;
		}
		return false;
	}


}
