package patternParser;

import java.util.ArrayList;

public class BasicNode {
	
	public enum Type{
		Statement,
		StatementExpression,
		AssignementExpression,
		ConditionalExpression,
		AtomicExpression, 
		Primary,
		Identifier
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

}
