package patternParser;

public class Primary extends BasicNode {

	public Primary(Type type, String value) {
		super(type, value);
		// TODO Auto-generated constructor stub
	}
	
	public BasicNode getPattern(){
		for (BasicNode basicNode : children) {
			if(basicNode.type == BasicNode.Type.Pattern)
				return basicNode;
		}
		
		return null;
	}
}
