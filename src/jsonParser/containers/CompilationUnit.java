package jsonParser.containers;

import java.util.ArrayList;

public class CompilationUnit extends BasicNode {
	
	protected ArrayList<BasicNode> types;

	@Override
	protected ArrayList<? extends BasicNode> getChildren() {
		return types;
	}
	
	
}
