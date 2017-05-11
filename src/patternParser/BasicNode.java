package patternParser;

import java.util.ArrayList;

public class BasicNode {
	
	public static enum Type{
		Statement,
		StatementExpression,
		AssignmentExpression,
		ConditionalExpression,
		AtomicExpression, 
		Primary,
		Identifier,
		Literal;
	}
	
	Type type;
	String value;
	ArrayList<BasicNode> children;
	
	public BasicNode(Type type, String value)
    {
		this.type = type;
        this.children = new ArrayList<>();
        this.value = value;
    }
	
	public void addChild(BasicNode child)
	{
		this.children.add(child);
	}

	
	public static BasicNode parseFromString(String s)
	{
		String stringType = null;
		String value = null;
		
		int typeEnd = s.indexOf('(');
		
		if(typeEnd == -1)
		{
			stringType = s;
		}
		else
		{
			stringType = s.substring(0, typeEnd);
			
			int valueEnd = s.indexOf(')');
			
			if(valueEnd != 1)
			{
				value = s.substring(typeEnd+1, valueEnd);
			}
		}
		
		Type type = BasicNode.Type.valueOf(stringType);
		
		return new BasicNode(type, value);
	}
	
	
	
	@Override
	public String toString() {
		return "BasicNode [type=" + type + ", value=" + value + ", children=" + children + "]";
	}

	public static void main(String[] args){
		System.out.println(parseFromString("Statement"));
	}
}
