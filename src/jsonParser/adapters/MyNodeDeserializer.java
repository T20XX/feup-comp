package jsonParser.adapters;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import jsonParser.containers.*;

public class MyNodeDeserializer implements JsonDeserializer<BasicNode> {

	@Override
	public BasicNode deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		
		try {
			JsonObject jsonObj = jsonElement.getAsJsonObject();  //vão buscar o objeto atual como um JsonObject para poderem ir buscar a propriedade que querem
			JsonElement nodeTypeEl = jsonObj.get("nodetype"); 	 // get the type of the node so we can use the correct class
			if (nodeTypeEl == null) {
				throw new RuntimeException("nodetype property must be defined!"); // all JSON objects must have the property nodetype
			}
			
			String nodeType = nodeTypeEl.getAsString(); //simply casting the object as string
		    Class<? extends BasicNode> classToUse = getClassToUse(nodeType); //somehow get the Class to use based on the node type given
			return jsonDeserializationContext.deserialize(jsonElement, classToUse); // automatic desearialization.
		 } catch (Exception e) {
			throw new JsonParseException(e);
		 }
		
	}
	
	private Class<? extends BasicNode> getClassToUse(String nodeType) {

		return MyClass.valueOf(nodeType.toUpperCase()).myClass; // we use an enum to map a node type to a specific class
	}

	private enum MyClass {
		// You Only need the concrete classes, NOT the abstract ones
		BINARYOPERATOR(BinaryOperator.class),
		COMPILATIONUNIT(CompilationUnit.class),
		CONSTRUCTOR(Constructor.class),
		EXPRESSION(Expression.class),
		FIELD(Field.class),
		IF(If.class),
		LITERAL(Literal.class),
		METHOD(Method.class),
		CLASS(jsonParser.containers.MyClass.class),
		ROOT(Root.class),
		TYPEREFERENCE(TypeReference.class),
		
		// Define the remaining nodetype and corresponding classes
		;
		private Class<? extends BasicNode> myClass;

		private MyClass(Class<? extends BasicNode> myClass) {
			this.myClass = myClass;
		}
	}

}
