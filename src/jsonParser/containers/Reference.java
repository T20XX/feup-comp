package jsonParser.containers;

import java.util.ArrayList;

public abstract class Reference extends BasicNode {

	protected String name;
	
	protected abstract ArrayList<? extends BasicNode> getChildren();
	
}
