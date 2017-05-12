package jsonParser.containers;

import java.util.ArrayList;

public abstract class BasicNode {
	
	protected String nodetype;
	protected String location;
    // private List<Comment> comments;
	
	protected abstract ArrayList<? extends BasicNode> getChildren();
	
	public ArrayList<BasicNode> searchTreeForType(ArrayList<BasicNode> list, String type)
	{
		
		if(this.getChildren()== null)
		{
			return list;
		}
		
		for(int i = 0; i < this.getChildren().size(); i++)
		{
			BasicNode currentNode = this.getChildren().get(i);
			
			if(currentNode.getNodetype() == type)
				list.add(currentNode);
			
			currentNode.searchTreeForType(list, type);
				
		}
		
		return list;
	}

	public String getNodetype() {
		return nodetype;
	}

	public String getLocation() {
		return location;
	}
	
	

}


