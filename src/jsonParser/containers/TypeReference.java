package jsonParser.containers;

import com.google.gson.annotations.SerializedName;

public class TypeReference extends Reference {
	
	@SerializedName("package") 	//usem esta anotação quando a propriedade no JSON é uma palavra reservada. aqui para não dar erro no Java digo que a minha variável
	protected String _package;	//chama-se "_package" MAS a propriedade que ele deve usar é mesmo "package"
	protected String name;
	
}
