package jsonParser.containers;

import java.util.ArrayList;

public class BinaryOperator extends Expression {
	
	protected String operator;
    protected Expression lhs; 
	protected Expression rhs;
	
	@Override
	protected ArrayList<? extends BasicNode> getChildren() {
		return null;
	}


}
