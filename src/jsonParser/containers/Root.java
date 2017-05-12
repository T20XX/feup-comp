package jsonParser.containers;

import java.util.ArrayList;

public class Root extends BasicNode {
	
	protected ArrayList<CompilationUnit> compilation_units;

	@Override
	protected ArrayList<? extends BasicNode> getChildren() {
		return compilation_units;
	}

}
 