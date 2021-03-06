package patternParser;

public class NodeFactory {

	public BasicNode getNode(BasicNode.Type type, String value) throws Exception
	{
		switch(type)
		{	
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
			
		case Start:
			return new Start(type, value);
			
		case Rule:
			return new Rule(type, value);
			
		case ParExpression:
			return new ParExpression(type, value);
			
		case Pattern:
			return new Pattern(type, value);
				
		case LocalVariableDeclarationStatement:
			return new LocalVariableDeclarationStatement(type, value);
			
		case VariableDeclarator:
			return new VariableDeclarator(type, value);
			
		case TypeType:
			return new TypeType(type, value);
			
		case PrimitiveType:
			return new PrimitiveType(type, value);
			
		case VariableInitializer:
			return new VariableInitializer(type, value);

		case IfStatement:
			return new IfStatement(type, value);
			
		case ForStatement:
			return new ForStatement(type, value);
			
		case WhileStatement:
			return new WhileStatement(type, value);

		case ConditionalExpression:
			return new ConditionalExpression(type, value);
			
		case Block:
			return new Block(type, value);
			
		case ReturnStatement:
			return new ReturnStatement(type, value);
			
		}
		
		throw new Exception("Missing matching case in NodeFactory for type: " + type.toString());
		
	}

}
