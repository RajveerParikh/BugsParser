package bugs;

import java.awt.Color;

import java.math.*;

import java.util.HashMap;

import tree.Tree;

/**
 * Interpreter for Bugs Program. Interprets a bug as definited by 
 * the BNF Grammer for Bugs Language
 * 
 * @author Rajveer Parikh
 * @version March 2015
 */
public class Bugs {
	HashMap<String, Double> myVariables;
	HashMap<String, Tree<Token>> myFunctions;
	double x;
	double y;
	double angle;
	String name;
	Color color;
	boolean exitLoop = false;
	int noOfLoop = 0;
	
	Bugs(){
		this.x = 0.0;
		this.y = 0.0;
		this.angle = 0.0;
		this.name = "";
		this.color = null;
		this.myVariables = new HashMap<String, Double>();
		this.myFunctions = new HashMap<String, Tree<Token>>();
	}
	
    /**
     * Store instance variables of current bug object with values 
     * @param variable The variable to be stored.
     * @param value The value to store with variable as key in the HashMap
     */
	void store(String variable, double value){
		if (variable.equals("x")){
			this.x = value;
		}
		else if (variable.equals("y")){
			this.y = value;
		}
		else if (variable.equals("angle")){
			this.angle = value;
		}
		else{
			myVariables.put(variable, value);
		}
	}
	
    /**
     * Fetch values of instance variables for an object 
     * @param variable The variable who's value is to be fetched
     */
	double fetch(String variable){
		if (variable.equals("x")){
			return this.x;
		}
		if (variable.equals("y")){
			return this.y;
		}
		if (variable.equals("angle")){
			return this.angle;
		}
		if (myVariables.get(variable) == null){
			throw new RuntimeException("No variable found");
		}
		return myVariables.get(variable);
	}
	
    /**
     * Evaluate the tree to produce the expected result based on the value of the root 
     * @param tree The tree that needs to be evaluated
     * @return result after evaluation
     */
	public double evaluate(Tree<Token> tree){
		String root = tree.getValue().value;
		if (Token.typeOf(root) == Token.Type.NUMBER) {
			return Double.parseDouble(root);
		}
		switch(root){
		case("+"):
			if (tree.getNumberOfChildren() == 1){
				return evaluate(tree.getChild(0));
			}
			else{
				return (evaluate(tree.getChild(0)) + evaluate(tree.getChild(1)));
			}
//			break;
		case("-"):
			if (tree.getNumberOfChildren() == 1){
				return evaluate(tree.getChild(0)) * -1;
			}
			else{
				return (evaluate(tree.getChild(0)) - evaluate(tree.getChild(1)));
			}
//			break;
		case("*"):
			return evaluate(tree.getChild(0)) * evaluate(tree.getChild(1));
//			break;
		case("/"):
			return evaluate(tree.getChild(0)) / evaluate(tree.getChild(1));
//			break;
		case("<"):
			double child1 = evaluate(tree.getChild(0));
			double child2 = evaluate(tree.getChild(1));
			if ((child1 - child2) > -0.001 && (child1 - child2) < 0.001){
				return 0.0;
			}
			if ((child1 - child2) > 0.001){
				return 0.0;
			}
			if ((child1 - child2) < -0.001){
				return 1.0;
			}
			break;
		case(">"):
			double child3 = evaluate(tree.getChild(0));
			double child4 = evaluate(tree.getChild(1));
			if ((child3 - child4) > -0.001 && (child3 - child4) < 0.001){
				return 0.0;
			}
			if ((child3 - child4) > 0.001){
				return 1.0;
			}
			if ((child3 - child4) < -0.001){
				return 0.0;
			}
			break;
		case(">="):
			double child5 = evaluate(tree.getChild(0));
			double child6 = evaluate(tree.getChild(1));
			if ((child5 - child6) > -0.001 && (child5 - child6) < 0.001){
				return 1.0;
			}
			if ((child5 - child6) > 0.001){
				return 1.0;
			}
			if ((child5 - child6) < -0.001){
				return 0.0;
			}
			break;
		case("<="):
			double child7 = evaluate(tree.getChild(0));
			double child8 = evaluate(tree.getChild(1));
			if ((child7 - child8) > -0.001 && (child7 - child8) < 0.001){
				return 1.0;
			}
			if ((child7 - child8) > 0.001){
				return 0.0;
			}
			if ((child7 - child8) < -0.001){
				return 1.0;
			}
			break;
		case("="):
			double child9 = evaluate(tree.getChild(0));
			double child10 = evaluate(tree.getChild(1));
			if ((child9 - child10) > -0.001 && (child9 - child10) < 0.001){
				return 1.0;
			}
			if ((child9 - child10) > 0.001){
				return 0.0;
			}
			if ((child9 - child10) < -0.001){
				return 0.0;
			}
			break;
		case("!="):
			double child11 = evaluate(tree.getChild(0));
			double child12 = evaluate(tree.getChild(1));
			if ((child11 - child12) > -0.001 && (child11 - child12) < 0.001){
				return 0.0;
			}
			if ((child11 - child12) > 0.001){
				return 1.0;
			}
			if ((child11 - child12) < -0.001){
				return 1.0;
			}
			break;
		case("case"):
			double child13 = evaluate(tree.getChild(0));
			if (child13 < -0.001 || child13 > 0.001){
				interpret(tree.getChild(1));	 
			}
			return child13;
		case("call"):
			//Skipped for this assignment
			break;			
		default:
			return fetch(root); 
		}
		return 0.00;	//TODO: Check if return value makes sense
	}
	
    /**
     * Interprets the tree to have the desired effects as defined by the BNF grammar
     * for the Bugs Language 
     * @param tree The tree that needs to be interpreted
     */
	public void interpret(Tree<Token> tree){
		String root = tree.getValue().value;
		switch(root){
		case("color"):
			String child1 = tree.getChild(0).getValue().value;
			if (child1.equals("black")){
				this.color = Color.black;
			}
			else if (child1.equals("blue")){
				this.color = Color.blue;
			}
			else if (child1.equals("cyan")){
				this.color = Color.cyan;
			}
			else if (child1.equals("darkGray")){
				this.color = Color.darkGray;
			}
			else if (child1.equals("gray")){
				this.color = Color.gray;
			}
			else if (child1.equals("green")){
				this.color = Color.green;
			}
			else if (child1.equals("lightGray")){
				this.color = Color.lightGray;
			}
			else if (child1.equals("magenta")){
				this.color = Color.magenta;
			}
			else if (child1.equals("orange")){
				this.color = Color.orange;
			}
			else if (child1.equals("pink")){
				this.color = Color.pink;
			}
			else if (child1.equals("red")){
				this.color = Color.red;
			}
			else if (child1.equals("white")){
				this.color = Color.white;
			}
			else if (child1.equals("yellow")){
				this.color = Color.yellow;
			}
			else if (child1.equals("brown")){
				this.color = new Color(165, 42, 42);
			}
			else if (child1.equals("purple")){
				this.color = new Color(160, 32, 240);
			}
			else if (child1.equals("none")){
				this.color = null;
			}
			else{
				throw new RuntimeException("color name not correct");
			}
			break;
		case("assign"):
			String child2 = tree.getChild(0).getValue().value;
			if (this.myVariables.containsKey(child2)){
				double child3 = evaluate(tree.getChild(1));
				this.myVariables.put(child2, child3);
			}
			else{
				throw new RuntimeException("No key declared in HashMap");
			}
			break;
		case("line"):
//			TODO: Not in this assignment
			break;
		case("return"):
//			TODO: Not in this assignment
			break;
		case("turnto"):
			double child4 = evaluate(tree.getChild(0));
			child4 = child4 % 360; 
			if (child4 < 0)
			{
			    child4 = 360 + child4;
			}
			this.angle = child4;
			break;
		case("turn"):
			double child5 = evaluate(tree.getChild(0));
			child5 = child5 % 360;
			if (child5 < 0)
			{
			    child5 = 360 + child5;
			}
			this.angle = (this.angle + child5) % 360;
			break;
		case("moveto"):
			double child6 = evaluate(tree.getChild(0));
			double child7 = evaluate(tree.getChild(1));
			this.x = child6;
			this.y = child7;
			break;
		case("move"):
			double child8 = evaluate(tree.getChild(0));
			double radians = this.angle * (Math.PI/180);
			this.x = this.x + (child8) * Math.cos(radians);
			this.y = this.y + (child8) * Math.sin(radians);
			break;
		case("block"):
			for (int i = 0; i < tree.getNumberOfChildren(); i++){
				interpret(tree.getChild(i));
			}
			break;
		case("initially"):
			interpret(tree.getChild(0));
			break;
		case("var"):
			for (int i = 0; i < tree.getNumberOfChildren(); i++){
				String child9 = tree.getChild(i).getValue().value; 
				store(child9, 0.0);
			}
			break;
		case("list"):
			for (int i = 0; i <tree.getNumberOfChildren(); i++){
				interpret(tree.getChild(i));
			}
			break;
		case("function"):
			this.myFunctions.put(tree.getChild(0).getValue().value, tree);
			break;
		case("switch"):
			for (int i = 0; i < tree.getNumberOfChildren(); i++){
				double child = evaluate(tree.getChild(i));
				if (child < -0.001 || child > 0.001){
					break;
				}
			}
			break;
		case("loop"):
			noOfLoop++;
			exitLoop = false;
			while (noOfLoop > 0 && exitLoop == false){  
				for (int i =  0; i< tree.getChild(0).getNumberOfChildren(); i++){ 
					interpret(tree.getChild(0).getChild(i));
					if (exitLoop == true){
						break;
					}
				}
			}
			if(noOfLoop > 0){
				exitLoop = false;
			}
			break;
		case("exit"):
			double child = evaluate(tree.getChild(0));
			if (child < -0.001 || child > 0.001){
				exitLoop = true;
				noOfLoop--;
				break;
			}
			break;
		case("Bug"):
			this.name = tree.getChild(0).getValue().value;
			for (int i = 1; i < tree.getNumberOfChildren()-1;i++){
				interpret(tree.getChild(i));
			}
			break;
		}		
	}

}
