package jsonParser.containers;

public abstract class Expression extends BasicNode {
	
	protected TypeReference type;  //Como todas as expressões vão ter um tipo associado, deixei aqui o type, que pode ser acedido p.e. pela classe Literal

}
