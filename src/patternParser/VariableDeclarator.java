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


}
