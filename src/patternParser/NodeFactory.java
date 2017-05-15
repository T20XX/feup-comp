package patternParser;

public class NodeFactory {

	public BasicNode getNode(BasicNode.Type type, String value)
	{
		switch(type)
		{
		case AssignmentExpression:
			return new AssignmentExpression(type, value);
			
		case AtomicExpression:
			return new AtomicExpression(type, value);
			
		case Expression:
			return new Expression(type, value);
			
		case Identifier:
			return new Identifier(type, value);
			
		case Literal:
			return new Literal(type, value);
			
		case Primary:
			return new Primary(type, value);
			
		case Statement:
			return new Statement(type, value);
			
		case VariableDeclaratorId:
			return new VariableDeclaratorId(type, value);
		}
		
		
		return null;
	}

}
