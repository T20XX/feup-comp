package patternFinder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.MethodRefParameter;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.WildcardType;

import patternParser.BasicNode;
import patternParser.LocalVariableDeclarationStatement;
import patternParser.Statement;
import patternParser.TypeType;
import patternParser.VariableDeclarator;
import patternParser.VariableDeclaratorId;

public class MyASTVisitor extends ASTVisitor {

	private BasicNode nodeToFind;
	private ASTNode correspondingNode;
	private boolean found;
	private int position;
	private static HashMap<String,String> currentPatterns = new HashMap<String,String>();

	public MyASTVisitor(ASTNode nodeToSearch, BasicNode nodeToFind) {
		super();

		if(nodeToFind.getType() == BasicNode.Type.Statement)
		{
			this.nodeToFind = nodeToFind.getFirstChild();
		}
		else{
			this.nodeToFind = nodeToFind;
		}


		nodeToSearch.accept(this);
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {

		//System.out.println("Visited VariableDeclarationStatement");

		this.found = false;

		if(this.nodeToFind.getType() == BasicNode.Type.LocalVariableDeclarationStatement){
			
			TypeType typeType = (TypeType)((LocalVariableDeclarationStatement) nodeToFind).getTypeType();
			VariableDeclarator variableDeclarator = (VariableDeclarator)((LocalVariableDeclarationStatement) nodeToFind).getVariableDeclarator();

			
			
			MyASTVisitor variableDeclaratorVisitor = new MyASTVisitor((VariableDeclarationFragment) node.fragments().get(0), variableDeclarator);

			this.found = (typeType == null);
			if(!found){
				MyASTVisitor typeTypeVisitor = new MyASTVisitor(node.getType(), typeType.getTypeNode());
				this.found = typeTypeVisitor.isFound();
			}
			this.found &= variableDeclaratorVisitor.isFound();

			this.correspondingNode = node;

			if(found)
			{
				System.out.println("Found match on position: " + node.getStartPosition());
				System.out.println("Node: " + node);
			}
		}	

		return false;
	}


	public boolean visit(PrimitiveType node)
	{
		//System.out.println("Visited PrimitiveType");

		this.found = false;

		if(this.nodeToFind.getType() == BasicNode.Type.PrimitiveType){

			if (node.getPrimitiveTypeCode().toString().equalsIgnoreCase(nodeToFind.getValue())){
				this.found = true;
			}
		}

		return false;
	}

	public boolean visit(VariableDeclarationFragment node)
	{
		this.found = false;

		if(this.nodeToFind.getType() == BasicNode.Type.VariableDeclarator && (((patternParser.VariableDeclarator) nodeToFind).getOperator() == null||((patternParser.VariableDeclarator) nodeToFind).getOperator().toString().equals("="))){

			String leftOperand = ((patternParser.VariableDeclarator) nodeToFind).getLeftOperand();
			String rightOperand = ((patternParser.VariableDeclarator) nodeToFind).getRightOperand();
			//PATTERNS
			if(((patternParser.VariableDeclarator) nodeToFind).leftOperandIsPattern()){
				System.out.println("PATTERN: " + leftOperand);
				if(currentPatterns.get(leftOperand) == null){
					System.out.println("AINDA NAO HAVIA NENHUM: " + leftOperand + "->" + node.getName().toString());
					currentPatterns.put(leftOperand, node.getName().toString());
					leftOperand = node.getName().toString();
				}else{
					System.out.println("JA HAVIA E ERA: " + leftOperand + "->" + currentPatterns.get(leftOperand));
					leftOperand = currentPatterns.get(leftOperand);
				}
			}
			if(rightOperand != null && node.getInitializer() != null){
			if(((patternParser.VariableDeclarator) nodeToFind).rightOperandIsPattern()){
				System.out.println("PATTERN: " + rightOperand);
				if(currentPatterns.get(rightOperand) == null){
					System.out.println("AINDA NAO HAVIA NENHUM: " + rightOperand + "->" + node.getInitializer().toString());
					currentPatterns.put(rightOperand, node.getInitializer().toString());
					rightOperand = node.getInitializer().toString();
				}else{
					System.out.println("JA HAVIA E ERA: " + rightOperand + "->" + currentPatterns.get(rightOperand));
					rightOperand = currentPatterns.get(rightOperand);
				}
			}


			if(node.getName().toString().equals(leftOperand) &&
					node.getInitializer().toString().equals(rightOperand)){
				this.found = true;
			}
		}else if (rightOperand == null && node.getInitializer() == null){
			if(node.getName().toString().equals(leftOperand)){
				this.found = true;
			}
		}
			this.correspondingNode = node;

			if(found)
			{
				System.out.println("Found match on position: " + node.getStartPosition());
				System.out.println("Node: " + node);
			}
		}	
		return false;	
	}




	@Override
	public boolean visit(AnnotationTypeDeclaration node) {

		return false;
	}

	@Override
	public boolean visit(AnnotationTypeMemberDeclaration node) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean visit(AnonymousClassDeclaration node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(ArrayAccess node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(ArrayCreation node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(ArrayInitializer node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(ArrayType node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(AssertStatement node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(Assignment node) {
		this.found = false;

		if(this.nodeToFind.getType() == BasicNode.Type.VariableDeclarator){

			Assignment.Operator operator = Assignment.Operator.toOperator(((patternParser.VariableDeclarator) nodeToFind).getOperator());
			String leftOperand = ((patternParser.VariableDeclarator) nodeToFind).getLeftOperand();
			String rightOperand = ((patternParser.VariableDeclarator) nodeToFind).getRightOperand();
			System.out.println("ASSIGNMENT: " + leftOperand + operator + rightOperand);
			//PATTERNS
			if(((patternParser.VariableDeclarator) nodeToFind).leftOperandIsPattern()){
				System.out.println("PATTERN: " + leftOperand);
				if(currentPatterns.get(leftOperand) == null){
					System.out.println("AINDA NAO HAVIA NENHUM: " + leftOperand + "->" + node.getLeftHandSide().toString());
					currentPatterns.put(leftOperand, node.getLeftHandSide().toString());
					leftOperand = node.getLeftHandSide().toString();
				}else{
					System.out.println("JA HAVIA E ERA: " + leftOperand + "->" + currentPatterns.get(leftOperand));
					leftOperand = currentPatterns.get(leftOperand);
				}
			}
			if(((patternParser.VariableDeclarator) nodeToFind).rightOperandIsPattern()){
				System.out.println("PATTERN: " + rightOperand);
				if(currentPatterns.get(rightOperand) == null){
					System.out.println("AINDA NAO HAVIA NENHUM: " + rightOperand + "->" + node.getRightHandSide().toString());
					currentPatterns.put(rightOperand, node.getRightHandSide().toString());
					rightOperand = node.getRightHandSide().toString();
				}else{
					System.out.println("JA HAVIA E ERA: " + rightOperand + "->" + currentPatterns.get(rightOperand));
					rightOperand = currentPatterns.get(rightOperand);
				}
			}


			if(node.getLeftHandSide().toString().equals(leftOperand) &&
					node.getOperator()== operator &&
					node.getRightHandSide().toString().equals(rightOperand)){
				this.found = true;
			}
			this.correspondingNode = node;

			if(found)
			{
				System.out.println("Found assignment match on position: " + node.getStartPosition());
				System.out.println("Node: " + node);
			}
		}	
		return false;	}

	@Override
	public boolean visit(Block node) {
		this.found = false;

		if(this.nodeToFind.getType() == BasicNode.Type.Block){

			List<BasicNode> patternStatements = ((patternParser.Block) nodeToFind).getChildren();
			List<ASTNode> astStatements = node.statements();

			if(patternStatements.size() == astStatements.size()){
				this.found=true;
				for(int i = 0; i <patternStatements.size(); i++){
					MyASTVisitor tmpVisitor = new MyASTVisitor(astStatements.get(i), patternStatements.get(i));
					if(!tmpVisitor.isFound()){
						this.found=false;
						break;
					}
				}

				this.correspondingNode = node;

				if(found)
				{
					System.out.println("Found match on position: " + node.getStartPosition());
					System.out.println("Node: " + node);
				}
			}

		}	
		return false;	}

	@Override
	public boolean visit(BlockComment node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(BooleanLiteral node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(BreakStatement node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(CastExpression node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(CatchClause node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(CharacterLiteral node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(ClassInstanceCreation node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(CompilationUnit node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(ConditionalExpression node) {

		return false;
	}

	@Override
	public boolean visit(ConstructorInvocation node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(ContinueStatement node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(DoStatement node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(EmptyStatement node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(EnhancedForStatement node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(EnumConstantDeclaration node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(EnumDeclaration node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(ExpressionStatement node) {
		MyASTVisitor tmpVisitor = new MyASTVisitor(node.getExpression(), this.nodeToFind.getFirstChild());
		
		this.found = tmpVisitor.isFound();
		

		this.correspondingNode = node;

		return false;	}

	@Override
	public boolean visit(FieldAccess node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(FieldDeclaration node) {

		this.found = false;

		if(this.nodeToFind.getType() == BasicNode.Type.LocalVariableDeclarationStatement){

			TypeType typeType = (TypeType)((LocalVariableDeclarationStatement) nodeToFind).getTypeType();
			VariableDeclarator variableDeclarator = (VariableDeclarator)((LocalVariableDeclarationStatement) nodeToFind).getVariableDeclarator();

			MyASTVisitor variableDeclaratorVisitor = new MyASTVisitor((VariableDeclarationFragment) node.fragments().get(0), variableDeclarator);

			this.found = (typeType == null);
			if(!found){
				MyASTVisitor typeTypeVisitor = new MyASTVisitor(node.getType(), typeType.getTypeNode());
				this.found = typeTypeVisitor.isFound();
			}
			this.found &= variableDeclaratorVisitor.isFound();

			this.correspondingNode = node;

			if(found)
			{
				System.out.println("Found match on position: " + node.getStartPosition());
				System.out.println("Node: " + node);
			}
		}	

		return false;	

	}

	@Override
	public boolean visit(ForStatement node) {
		this.found = false;

		if(this.nodeToFind.getType() == BasicNode.Type.ForStatement){

			BasicNode forInit = nodeToFind.getChildren().get(0);
			BasicNode forExpression = nodeToFind.getChildren().get(1);
			BasicNode forUpdater = nodeToFind.getChildren().get(2);
			BasicNode forBody = nodeToFind.getChildren().get(3);

			MyASTVisitor initVisitor = new MyASTVisitor((ASTNode) node.initializers().get(0), forInit);
			MyASTVisitor expressionVisitor = new MyASTVisitor((ASTNode) node.getExpression(), forExpression);
			MyASTVisitor updaterVisitor = new MyASTVisitor((ASTNode) node.updaters().get(0), forUpdater);
			MyASTVisitor bodyVisitor = new MyASTVisitor((ASTNode) node.getBody(), forBody);

			this.found = initVisitor.isFound() && expressionVisitor.isFound() && updaterVisitor.isFound() && bodyVisitor.isFound();
			//this.found = initVisitor.isFound() && expressionVisitor.isFound() && bodyVisitor.isFound();

			this.correspondingNode = node;

			if(found)
			{
				System.out.println("Found forstatement match on position: " + node.getStartPosition());
				System.out.println("Node: " + node);
			}
		}	

		return false;	}

	@Override
	public boolean visit(IfStatement node) {
		this.found = false;

		if(this.nodeToFind.getType() == BasicNode.Type.IfStatement){

			BasicNode thenStatement = nodeToFind.getChildren().get(1);

			MyASTVisitor expressionVisitor = new MyASTVisitor(node.getExpression(), nodeToFind.getFirstChild());
			MyASTVisitor thenVisitor = new MyASTVisitor(node.getThenStatement(), thenStatement);
			if(nodeToFind.getChildren().size()> 2 && node.getElseStatement() != null){
				BasicNode elseStatement = nodeToFind.getChildren().get(2);
				MyASTVisitor elseVisitor = new MyASTVisitor(node.getElseStatement(), elseStatement);
				this.found = expressionVisitor.isFound() && thenVisitor.isFound() && elseVisitor.isFound();
			}else if(nodeToFind.getChildren().size() <= 2 && node.getElseStatement() == null){
				this.found = expressionVisitor.isFound() && thenVisitor.isFound();
			}


			this.correspondingNode = node;

			if(found)
			{
				System.out.println("Found ifstatement match on position: " + node.getStartPosition());
				System.out.println("Node: " + node);
			}
		}	

		return false;	}

	@Override
	public boolean visit(ImportDeclaration node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(InfixExpression node) {
		this.found = false;

		if(this.nodeToFind.getType() == BasicNode.Type.ConditionalExpression){

			InfixExpression.Operator operator = InfixExpression.Operator.toOperator(((patternParser.ConditionalExpression) nodeToFind).getOperator());
			String leftOperand = ((patternParser.ConditionalExpression) nodeToFind).getLeftOperand();
			String rightOperand = ((patternParser.ConditionalExpression) nodeToFind).getRightOperand();

			//PATTERNS
			if(((patternParser.ConditionalExpression) nodeToFind).leftOperandIsPattern()){
				System.out.println("PATTERN: " + leftOperand);
				if(currentPatterns.get(leftOperand) == null){
					System.out.println("AINDA NAO HAVIA NENHUM: " + leftOperand + "->" + node.getLeftOperand().toString());
					currentPatterns.put(leftOperand, node.getLeftOperand().toString());
					this.found = true;
				}else{
					System.out.println("JA HAVIA E ERA: " + leftOperand + "->" + currentPatterns.get(leftOperand));
					leftOperand = currentPatterns.get(leftOperand);
				}
			}
			if(((patternParser.ConditionalExpression) nodeToFind).rightOperandIsPattern()){
				System.out.println("PATTERN: " + rightOperand);
				if(currentPatterns.get(rightOperand) == null){
					System.out.println("AINDA NAO HAVIA NENHUM: " + rightOperand + "->" + node.getRightOperand().toString());
					currentPatterns.put(rightOperand, node.getRightOperand().toString());
					this.found = true;
				}else{
					System.out.println("JA HAVIA E ERA: " + rightOperand + "->" + currentPatterns.get(rightOperand));
					rightOperand = currentPatterns.get(rightOperand);
				}
			}


			if(node.getLeftOperand().toString().equals(leftOperand) &&
					node.getOperator()== operator &&
					node.getRightOperand().toString().equals(rightOperand)){
				this.found = true;
				this.correspondingNode = node;
			}

			if(found)
			{
				System.out.println("Found conditionalExpression match on position: " + node.getStartPosition());
				System.out.println("Node: " + node);
			}
		}	
		return false;	}

	@Override
	public boolean visit(Initializer node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(InstanceofExpression node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(Javadoc node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(LabeledStatement node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(LineComment node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(MarkerAnnotation node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(MemberRef node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(MemberValuePair node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(MethodDeclaration node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(MethodInvocation node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(MethodRef node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(MethodRefParameter node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(Modifier node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(NormalAnnotation node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(NullLiteral node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(NumberLiteral node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(PackageDeclaration node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(ParameterizedType node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(ParenthesizedExpression node) {
		return false;	}

	@Override
	public boolean visit(PostfixExpression node) {
		this.found = false;

		if(this.nodeToFind.getType() == BasicNode.Type.VariableDeclarator){

			PostfixExpression.Operator operator = PostfixExpression.Operator.toOperator(((patternParser.VariableDeclarator) nodeToFind).getValue());
			String operand = ((patternParser.VariableDeclarator) nodeToFind).getLeftOperand();

			//PATTERNS
			if(((patternParser.VariableDeclarator) nodeToFind).leftOperandIsPattern()){
				System.out.println("PATTERN: " + operand);
				if(currentPatterns.get(operand) == null){
					System.out.println("AINDA NAO HAVIA NENHUM: " + operand + "->" + node.getOperand().toString());
					currentPatterns.put(operand, node.getOperand().toString());
					this.found = true;
				}else{
					System.out.println("JA HAVIA E ERA: " + operand + "->" + currentPatterns.get(operand));
					operand = currentPatterns.get(operand);
				}
			}
			System.out.println("JAVA OPERAND: " + node.getOperand().toString());
			System.out.println("JAVA OPERATOR: " + node.getOperator());
			System.out.println("PATTERN OPERAND: " + operand);
			System.out.println("PATTERN OPERATOR: " + operator);


			if(node.getOperand().toString().equals(operand) &&
					node.getOperator()== operator){
				this.found = true;
				this.correspondingNode = node;
			}

			if(found)
			{
				System.out.println("Found conditionalExpression match on position: " + node.getStartPosition());
				System.out.println("Node: " + node);
			}
		}	
		return false;	}

	@Override
	public boolean visit(PrefixExpression node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(QualifiedName node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(QualifiedType node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(ReturnStatement node) {
		this.found = false;

		if(this.nodeToFind.getType() == BasicNode.Type.ReturnStatement){

			String returnValue = ((patternParser.ReturnStatement) nodeToFind).getReturnValue();

			System.out.println("RETURN JAVA: " + node.getExpression().toString());
			System.out.println("RETURN PATTERN: " + returnValue);

			if(((patternParser.ReturnStatement) nodeToFind).returnValueIsPattern()){
				System.out.println("PATTERN: " + returnValue);
				if(currentPatterns.get(returnValue) == null){
					System.out.println("AINDA NAO HAVIA NENHUM: " + returnValue + "->" + node.getExpression().toString());
					currentPatterns.put(returnValue, node.getExpression().toString());
					this.found = true;
				}else{
					System.out.println("JA HAVIA E ERA: " + returnValue + "->" + currentPatterns.get(returnValue));
					this.found = node.getExpression().toString().equals(currentPatterns.get(returnValue));
				}
			}else{
				this.found = node.getExpression().toString().equals(returnValue);
			}

			this.correspondingNode = node;

			if(found)
			{
				System.out.println("Found match on position: " + node.getStartPosition());
				System.out.println("Node: " + node);
			}
		}	
		return false;	}

	@Override
	public boolean visit(SimpleName node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(SimpleType node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(SingleMemberAnnotation node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(SingleVariableDeclaration node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(StringLiteral node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(SuperConstructorInvocation node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(SuperFieldAccess node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(SuperMethodInvocation node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(SwitchCase node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(SwitchStatement node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(SynchronizedStatement node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(TagElement node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(TextElement node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(ThisExpression node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(ThrowStatement node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(TryStatement node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(TypeDeclaration node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(TypeDeclarationStatement node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(TypeLiteral node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(TypeParameter node) {
		// TODO Auto-generated method stub
		return false;	}

	@Override
	public boolean visit(VariableDeclarationExpression node) {
		this.found = false;

		if(this.nodeToFind.getType() == BasicNode.Type.LocalVariableDeclarationStatement){

			TypeType typeType = (TypeType)((LocalVariableDeclarationStatement) nodeToFind).getTypeType();
			VariableDeclarator variableDeclarator = (VariableDeclarator)((LocalVariableDeclarationStatement) nodeToFind).getVariableDeclarator();

			MyASTVisitor variableDeclaratorVisitor = new MyASTVisitor((VariableDeclarationFragment) node.fragments().get(0), variableDeclarator);

			this.found = (typeType == null);
			if(!found){
				MyASTVisitor typeTypeVisitor = new MyASTVisitor(node.getType(), typeType.getTypeNode());
				this.found = typeTypeVisitor.isFound();
			}
			this.found &= variableDeclaratorVisitor.isFound();

			this.correspondingNode = node;

			if(found)
			{
				System.out.println("Found match on position: " + node.getStartPosition());
				System.out.println("Node: " + node);
			}
		}	

		return false;	}

	@Override
	public boolean visit(WhileStatement node) {
		this.found = false;

		if(this.nodeToFind.getType() == BasicNode.Type.WhileStatement){

			MyASTVisitor expressionVisitor = new MyASTVisitor(node.getExpression(), nodeToFind.getFirstChild());
			MyASTVisitor bodyVisitor = new MyASTVisitor(node.getBody(), ((patternParser.WhileStatement) nodeToFind).getBody());

			this.found = expressionVisitor.isFound() && bodyVisitor.isFound();

			this.correspondingNode = node;

			if(found)
			{
				System.out.println("Found ifstatement match on position: " + node.getStartPosition());
				System.out.println("Node: " + node);
			}
		}	

		return false;	}

	@Override
	public boolean visit(WildcardType node) {
		// TODO Auto-generated method stub
		return false;	}

	public BasicNode getNodeToFind() {
		return nodeToFind;
	}

	public ASTNode getCorrespondingNode() {
		return correspondingNode;
	}

	public boolean isFound() {
		return found;
	}

	public static void clearCurrentPatterns() {
		currentPatterns = new HashMap<String, String>();
	}

}
