package patternsGrammar;

public class BetterNode extends SimpleNode {

	public BetterNode(Parser p, int i) {
		super(p, i);
	}

	public BetterNode(int i) {
		super(i);
	}

	@Override
	public String toString() {
		String res = super.toString();
		if(this.jjtGetValue() != null)
			res += "(" + this.jjtGetValue().toString() + ")";
		return res;
	}
	
	public String getType() {
		return super.toString();
	}

	
}
