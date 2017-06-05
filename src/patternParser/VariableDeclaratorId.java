package patternParser;

public class VariableDeclaratorId extends Identifier {

	public VariableDeclaratorId(Type type, String value) {
		super(type, value);
		// TODO Auto-generated constructor stub
	}

	public BasicNode getIdentifier(){
		for (BasicNode basicNode : children) {
			if(basicNode.type == BasicNode.Type.Identifier)
				return basicNode;
		}
		
		return null;	
	}
	
	public BasicNode getPattern(){
		for (BasicNode basicNode : children) {
			if(basicNode.type == BasicNode.Type.Pattern)
				return basicNode;
		}
		
		return null;
	}
}
