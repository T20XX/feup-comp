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

	public String getLeftOperand(){
        for (BasicNode basicNode : children) {
            if(basicNode.type == BasicNode.Type.VariableDeclaratorId)
                return basicNode.getFirstChild().getValue();
        }
        
        return null;
    }
	
	public String getRightOperand(){
        for (BasicNode basicNode : children) {
            if(basicNode.type == BasicNode.Type.Expression)
                return basicNode.getFirstChild().getFirstChild().getValue();
        }
        
        return null;
    }
	
	public String getOperator(){
		return value;
	}
	
	public boolean leftOperandIsPattern(){
        for (BasicNode basicNode : children) {
            if(basicNode.type == BasicNode.Type.VariableDeclaratorId)
                return basicNode.getFirstChild().type == BasicNode.Type.Pattern;
        }
        return false;
    }
	
	public boolean rightOperandIsPattern(){
        for (BasicNode basicNode : children) {
            if(basicNode.type == BasicNode.Type.Expression)
                return basicNode.getFirstChild().getFirstChild().type == BasicNode.Type.Pattern;
        }
        return false;
    }
	

}
