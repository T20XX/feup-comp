package patternParser;

public class AssignmentExpression extends Expression{

	public AssignmentExpression(Type type, String value) {
		super(type, value);
		
	}

	@Override
	public String toString() {
		return "AssignmentExpression [type=" + type + ", value=" + value + ", children=" + children + "]";
	}
}
