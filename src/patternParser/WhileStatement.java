package patternParser;

public class WhileStatement extends BasicNode {

	public WhileStatement(Type type, String value) {
		super(type, value);
		// TODO Auto-generated constructor stub
	}
	
	public Statement getBody(){
		return (Statement) this.getChildren().get(1);
	}

}
