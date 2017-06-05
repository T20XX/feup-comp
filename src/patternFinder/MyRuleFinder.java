package patternFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;

import patternParser.BasicNode;
import patternParser.Rule;
import utils.Position;

public class MyRuleFinder {

	private CompilationUnit rootCu;
	private Rule rule;
	private ArrayList<BasicNode> rulePatterns;
	private Map<BasicNode, MyASTVisitor> correspondencies;
	private ArrayList<Position> correspondenciesPositions;

	private boolean ruleFulfiflled;
	private int nodeCounter = 0;

	public MyRuleFinder(CompilationUnit rootCu, Rule rule) {

		this.rootCu = rootCu;
		this.rule = rule;

		this.rulePatterns = rule.getChildren();

		this.correspondencies = new HashMap<>();
		this.correspondenciesPositions = new ArrayList<>();

		this.ruleFulfiflled = true;
	}

	public void search(ASTNode parentNode){
		List<ASTNode> nodes = getASTNodeChildren(parentNode);
		int astNodeCounter = 0;
		ArrayList<Position> tmpPositions = new ArrayList<Position>();
		// Search on every node of the java code
		for(int i = 0; i < getASTNodeChildren(parentNode).size(); i++){

			System.out.println("Node: " + nodes.get(i));
			System.out.println("Node type: " + nodes.get(i).getNodeType());

			// Search for all the pattern nodes
			//for(BasicNode pattern : this.rulePatterns){

			MyASTVisitor myASTVisitor = new MyASTVisitor(nodes.get(i), this.rulePatterns.get(nodeCounter));

			if(myASTVisitor.isFound()){
				if(nodeCounter == 0){
					astNodeCounter = i;
				}
				this.correspondencies.put(this.rulePatterns.get(nodeCounter), myASTVisitor);

				int beginPos = myASTVisitor.getCorrespondingNode().getStartPosition();
				int endPos = beginPos + myASTVisitor.getCorrespondingNode().getLength();

				Position pos = new Position(beginPos, endPos);

				tmpPositions.add(pos);
				nodeCounter++;
				if(this.rulePatterns.size() == nodeCounter){
					for(Position p : tmpPositions){
						this.correspondenciesPositions.add(p);
					}
					tmpPositions = new ArrayList<Position>();
					MyASTVisitor.clearCurrentPatterns();
					nodeCounter = 0;
					i = astNodeCounter;
				}
			}else if(nodeCounter == 0 || (nodes.size() - i <= this.rulePatterns.size() - nodeCounter)){
				nodeCounter = 0;
				tmpPositions = new ArrayList<Position>();
				MyASTVisitor.clearCurrentPatterns();
				this.ruleFulfiflled = false;
				
				search(nodes.get(i));
			}else if(nodeCounter >= 1){
				nodeCounter = 0;
				tmpPositions = new ArrayList<Position>();
				MyASTVisitor.clearCurrentPatterns();
				this.ruleFulfiflled = false;
				i = astNodeCounter;
			}

			System.out.println("COUNTER VAI A " + nodeCounter);


			//}


		}	

	}

	public boolean verifySameParent(){

		ASTNode matchingNode = null;

		for(MyASTVisitor successfulVisitor: this.correspondencies.values()){

			if(matchingNode == null)
				matchingNode = successfulVisitor.getCorrespondingNode().getParent();

			if(successfulVisitor.getCorrespondingNode().getParent() != matchingNode)
			{
				return false;
			}
		}

		return true;
	}

	public boolean verifySameOrder(){

		int currentPosition = -1;

		for(BasicNode pattern : this.rulePatterns){

			int correspondencePosition = this.correspondencies.get(pattern).getCorrespondingNode().getStartPosition();

			// The position must always be higher than the previous, obeying the order of the list
			if(correspondencePosition > currentPosition)
			{
				currentPosition = correspondencePosition;
			}
			else
			{
				return false;
			}

		}
		return true;
	}

	@SuppressWarnings("rawtypes")
	public static List<ASTNode> getASTNodeChildren(ASTNode node) {
		List<ASTNode> children = new ArrayList<ASTNode>();

		List list = node.structuralPropertiesForType();

		for (int i = 0; i < list.size(); i++) {

			Object child = node.getStructuralProperty((StructuralPropertyDescriptor)list.get(i));

			if (child instanceof ASTNode) {
				children.add((ASTNode) child);
			}
			else if(child instanceof List){

				for(int j = 0; j < ((List) child).size(); j++){

					children.add((ASTNode)(((List) child).get(j)));

				}

			}

		}
		return children;
	}

	public ArrayList<Position> getCorrespondenciesPositions() {
		return correspondenciesPositions;
	}

	public CompilationUnit getRootCu() {
		return rootCu;
	}

	public Rule getRule() {
		return rule;
	}

	public ArrayList<BasicNode> getRulePatterns() {
		return rulePatterns;
	}

	public Map<BasicNode, MyASTVisitor> getCorrespondencies() {
		return correspondencies;
	}

	public boolean isRuleFulfiflled() {
		return ruleFulfiflled;
	}



}
