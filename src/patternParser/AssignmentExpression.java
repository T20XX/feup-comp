package patternParser;

public class AssignmentExpression extends Expression{

	public AssignmentExpression(Type type, String value) {
		super(type, value);
	}
	
	public BasicNode getVariableDeclaratorId(){
		for (BasicNode basicNode : children) {
			if(basicNode.type == BasicNode.Type.VariableDeclaratorId)
				return basicNode;
		}
		
		return null;
	}
	
	public BasicNode getInitializer(){
		for (BasicNode basicNode : children) {
			if(basicNode.type == BasicNode.Type.Primary)
				return basicNode;
		}
		
		return null;	
	}

	@Override
	public String toString() {
		return "AssignmentExpression [type=" + type + ", value=" + value + ", children=" + children + "]";
	}
}
