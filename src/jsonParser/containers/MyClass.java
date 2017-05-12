package jsonParser.containers;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class MyClass extends BasicNode {
	
	protected String name;	
	protected ArrayList<Member> members;
	
	@Override
	protected ArrayList<? extends BasicNode> getChildren() {
		return null;
	}

	
}
