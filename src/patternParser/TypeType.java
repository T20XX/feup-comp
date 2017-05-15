package patternParser;

public class TypeType extends BasicNode {

	public TypeType(Type type, String value) {
		super(type, value);
		// TODO Auto-generated constructor stub
	}

	public BasicNode getTypeNode(){
		for (BasicNode basicNode : children) {
			if(basicNode.type == BasicNode.Type.PrimitiveType)
				return basicNode;
		}
		
		return null;
	}
}
