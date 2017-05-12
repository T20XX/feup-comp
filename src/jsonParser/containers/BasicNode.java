package jsonParser.containers;

import java.util.ArrayList;

public abstract class BasicNode {
	
	protected String nodetype;
	protected String location;
    // private List<Comment> comments;
	
	protected abstract ArrayList<? extends BasicNode> getChildren();

}


