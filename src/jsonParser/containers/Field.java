package jsonParser.containers;

import java.util.ArrayList;

public class Field extends Reference {
	
	protected TypeReference type;

	@Override
	protected ArrayList<? extends BasicNode> getChildren() {
		return null;
	}
}
