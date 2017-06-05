package patternParser;

public class ReturnStatement extends BasicNode {

	public ReturnStatement(Type type, String value) {
		super(type, value);
		// TODO Auto-generated constructor stub
	}

	public String getReturnValue(){
		return getFirstChild().getFirstChild().getValue();
	}
}
