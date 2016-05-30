package bugs;
import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

import tree.Tree;
	
public class BugsTest {
	Bugs bugs = new Bugs();
	@Test
	public void testConstructor(){
		assertTrue(bugs.x == 0);
		assertTrue(bugs.y == 0);
		assertTrue(bugs.angle == 0.0);
		assertTrue(bugs.name.equals(""));
		assertTrue(bugs.color == null);
		try {
            assertFalse(bugs.myVariables.containsKey("pet"));
        }
        catch (RuntimeException e) {
        }
		bugs.store("x", 1.0);
		bugs.store("y", 1.0);
		bugs.store("angle", 90.0);
		bugs.store("dog", 9.0);
		assertTrue(bugs.fetch("x") == 1.0);
		assertTrue(bugs.fetch("y") == 1.0);
		assertTrue(bugs.fetch("angle") == 90.0);
		assertTrue(bugs.myVariables.containsKey("dog"));
		assertTrue(bugs.myVariables.containsValue(9.0));
		assertTrue(bugs.fetch("dog") == 9.0);
	}
	
	@Test
	public void testNumber(){
		Parser parser = new Parser("3");
		assertTrue(parser.isExpression());
		assertEquals(3.0, bugs.evaluate(parser.stack.pop()), 0.001);
	}
	@Test
	public void testPlusOperator(){
		Parser parser = new Parser("3.0 + 6.0");
		Parser parser1 = new Parser("+ 3.0");
		assertTrue(parser.isExpression());
		assertEquals(9.0, bugs.evaluate(parser.stack.pop()), 0.001);
		assertTrue(parser1.isExpression());
		assertEquals(3.0, bugs.evaluate(parser1.stack.pop()), 0.001);
	}
	@Test
	public void testMinusOperator(){
		Parser parser = new Parser("6.0 - 3.0");
		Parser parser1 = new Parser("- 3.0");
		assertTrue(parser.isExpression());
		assertEquals(3.0, bugs.evaluate(parser.stack.pop()), 0.001);
		assertTrue(parser1.isExpression());
		assertEquals(-3.0, bugs.evaluate(parser1.stack.pop()), 0.001);
	}
	@Test
	public void testMultiplyOperator(){
		Parser parser = new Parser ("3 * 4");
		Parser parser1 = new Parser ("3.001 * 1");
		assertTrue(parser.isExpression());
		assertEquals(12.0, bugs.evaluate(parser.stack.pop()), 0.001);
		assertTrue(parser1.isExpression());
		assertEquals(3.0, bugs.evaluate(parser1.stack.pop()), 0.001);
	}
	@Test
	public void testDivideOperator(){
		Parser parser = new Parser ("9 / 3");
		Parser parser1 = new Parser ("12.001 / 3");
		assertTrue(parser.isExpression());
		assertEquals(3.0, bugs.evaluate(parser.stack.pop()), 0.001);
		assertTrue(parser1.isExpression());
		assertEquals(4.0, bugs.evaluate(parser1.stack.pop()), 0.001);
	}
	@Test
	public void testLessThanOperator(){
		Parser parser = new Parser ("4 < 2");
		Parser parser1 = new Parser ("2 < 4");
		Parser parser2 = new Parser ("3.999 < 4");
		Parser parser3 = new Parser ("3.998 < 4");
		assertTrue(parser.isExpression());
		assertEquals(0.0, bugs.evaluate(parser.stack.pop()), 0.001);
		assertTrue(parser1.isExpression());
		assertEquals(1.0, bugs.evaluate(parser1.stack.pop()), 0.001);
		assertTrue(parser2.isExpression());
		assertEquals(0.0, bugs.evaluate(parser2.stack.pop()), 0.001);
		assertTrue(parser3.isExpression());
		assertEquals(1.0, bugs.evaluate(parser3.stack.pop()), 0.001);
	}
	@Test
	public void testGreaterThanOperator(){
		Parser parser = new Parser ("4 > 2");
		Parser parser1 = new Parser ("2 > 4");
		Parser parser2 = new Parser ("4 > 4");
		Parser parser3 = new Parser ("2.001 > 2");
		Parser parser4 = new Parser ("2.002 > 2");
		assertTrue(parser.isExpression());
		assertEquals(1.0, bugs.evaluate(parser.stack.pop()), 0.001);
		assertTrue(parser1.isExpression());
		assertEquals(0.0, bugs.evaluate(parser1.stack.pop()), 0.001);
		assertTrue(parser2.isExpression());
		assertEquals(0.0, bugs.evaluate(parser2.stack.pop()), 0.001);
		assertTrue(parser3.isExpression());
		assertEquals(0.0, bugs.evaluate(parser3.stack.pop()), 0.001);
		assertTrue(parser4.isExpression());
		assertEquals(1.0, bugs.evaluate(parser4.stack.pop()), 0.001);
	}
	@Test
	public void testGreaterThanEqualsOperator(){
		Parser parser = new Parser ("4 >= 2");
		Parser parser1 = new Parser ("2 >= 4");
		Parser parser2 = new Parser ("4 >= 4");
		Parser parser3 = new Parser ("2.001 >= 2");
		Parser parser4 = new Parser ("2.002 >= 2");
		assertTrue(parser.isExpression());
		assertEquals(1.0, bugs.evaluate(parser.stack.pop()), 0.001);
		assertTrue(parser1.isExpression());
		assertEquals(0.0, bugs.evaluate(parser1.stack.pop()), 0.001);
		assertTrue(parser2.isExpression());
		assertEquals(1.0, bugs.evaluate(parser2.stack.pop()), 0.001);
		assertTrue(parser3.isExpression());
		assertEquals(1.0, bugs.evaluate(parser3.stack.pop()), 0.001);
		assertTrue(parser4.isExpression());
		assertEquals(1.0, bugs.evaluate(parser4.stack.pop()), 0.001);
	}
	@Test
	public void testLessThanEqualsOperator(){
		Parser parser = new Parser ("4 <= 2");
		Parser parser1 = new Parser ("2 <= 4");
		Parser parser2 = new Parser ("4 <= 4");
		Parser parser3 = new Parser ("2.001 <= 2");
		Parser parser4 = new Parser ("2.002 <= 2");
		Parser parser5 = new Parser ("1.999 <= 2");
		Parser parser6 = new Parser ("1.998 <= 2");
		assertTrue(parser.isExpression());
		assertEquals(0.0, bugs.evaluate(parser.stack.pop()), 0.001);
		assertTrue(parser1.isExpression());
		assertEquals(1.0, bugs.evaluate(parser1.stack.pop()), 0.001);
		assertTrue(parser2.isExpression());
		assertEquals(1.0, bugs.evaluate(parser2.stack.pop()), 0.001);
		assertTrue(parser3.isExpression());
		assertEquals(1.0, bugs.evaluate(parser3.stack.pop()), 0.001);
		assertTrue(parser4.isExpression());
		assertEquals(0.0, bugs.evaluate(parser4.stack.pop()), 0.001);
		assertTrue(parser5.isExpression());
		assertEquals(1.0, bugs.evaluate(parser5.stack.pop()), 0.001);
		assertTrue(parser6.isExpression());
		assertEquals(1.0, bugs.evaluate(parser6.stack.pop()), 0.001);
	}
	@Test
	public void testEqualsOperator(){
		Parser parser = new Parser ("4 = 2");
		Parser parser1 = new Parser ("2.001 = 2");
		Parser parser2 = new Parser ("1.999 = 2");
		Parser parser3 = new Parser ("2 = 2");
		assertTrue(parser.isExpression());
		assertEquals(0.0, bugs.evaluate(parser.stack.pop()), 0.001);
		assertTrue(parser1.isExpression());
		assertEquals(1.0, bugs.evaluate(parser1.stack.pop()), 0.001);
		assertTrue(parser2.isExpression());
		assertEquals(1.0, bugs.evaluate(parser2.stack.pop()), 0.001);
		assertTrue(parser3.isExpression());
		assertEquals(1.0, bugs.evaluate(parser3.stack.pop()), 0.001);
	}
	@Test
	public void testNotEqualsOperator(){
		Parser parser = new Parser ("4 != 2");
		Parser parser1 = new Parser ("2.001 != 2");
		Parser parser2 = new Parser ("1.999 != 2");
		Parser parser3 = new Parser ("2 != 2");
		assertTrue(parser.isExpression());
		assertEquals(1.0, bugs.evaluate(parser.stack.pop()), 0.001);
		assertTrue(parser1.isExpression());
		assertEquals(0.0, bugs.evaluate(parser1.stack.pop()), 0.001);
		assertTrue(parser2.isExpression());
		assertEquals(0.0, bugs.evaluate(parser2.stack.pop()), 0.001);
		assertTrue(parser3.isExpression());
		assertEquals(0.0, bugs.evaluate(parser3.stack.pop()), 0.001);
	}

	@Test
	public void testVariables(){
		bugs.myVariables.put("a", 3.0);
		bugs.store("b", 9.0);
		Parser parser = new Parser("a");
		assertTrue(parser.isExpression());
		assertEquals(3.0, bugs.evaluate(parser.stack.peek()), 0.001);
		parser = new Parser("b");
		assertTrue(parser.isExpression());
		assertEquals(9.0, bugs.evaluate(parser.stack.peek()), 0.001);
	}
	@Test
	public void testColor(){
		assertTrue(bugs.color == null);
		Parser parser1 = new Parser("color blue \n");
    	assertTrue(parser1.isColorStatement());
    	bugs.interpret(parser1.stack.pop());
    	assertTrue(bugs.color == Color.blue);
    	parser1 = new Parser("color purple \n");
    	assertTrue(parser1.isColorStatement());
    	bugs.interpret(parser1.stack.pop());
    	Color expected = new Color(160, 32, 240);
    	assertEquals(bugs.color, expected);
    	parser1 = new Parser("color switch \n");
    	assertTrue(parser1.isColorStatement());
    	try {
            bugs.interpret(parser1.stack.pop());
        }
        catch (RuntimeException e) {
        }
    	parser1 = new Parser("color none \n");
    	assertTrue(parser1.isColorStatement());
    	bugs.interpret(parser1.stack.pop());
    	assertTrue(bugs.color == null);
	}
	@Test
	public void testAssign(){
		Parser parser = new Parser("test = 12 \n");
		assertTrue(parser.isAssignmentStatement());
		try{
			bugs.interpret(parser.stack.peek());
		}
		catch(RuntimeException e){
			
		}
		bugs.myVariables.put("test", 0.0);
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.myVariables.containsKey("test"));
		assertTrue(bugs.myVariables.get("test") == 12.0);
		parser = new Parser("test = 12 + 9 <= 6+20 \n");
		assertTrue(parser.isAssignmentStatement());
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.myVariables.containsKey("test"));
		assertTrue(bugs.myVariables.get("test") == 1.0);
	}
	@Test
	public void testTurnTo(){
		Parser parser = new Parser("turnto 80 \n");
		assertTrue(parser.isTurnToAction());
		assertTrue(bugs.angle == 0.0);
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.angle == 80.0);
		parser = new Parser("turnto -90 \n");
		assertTrue(parser.isTurnToAction());
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.angle == 270.0);
		parser = new Parser("turnto -280 \n");
		assertTrue(parser.isTurnToAction());
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.angle == 80.0);
		parser = new Parser("turnto -360 \n");
		assertTrue(parser.isTurnToAction());
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.angle == 0.0);
		parser = new Parser("turnto 60 \n");
		assertTrue(parser.isTurnToAction());
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.angle == 60.0);
		parser = new Parser("turnto 390 \n");
		assertTrue(parser.isTurnToAction());
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.angle == 30.0);
	}
	@Test
	public void testTurn(){
		assertTrue(bugs.angle == 0.0);
		Parser parser = new Parser("turn 90 \n");
		assertTrue(parser.isTurnAction());
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.angle == 90.0);
		parser = new Parser("turn 60 \n");
		assertTrue(parser.isTurnAction());
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.angle == 150.0);
		parser = new Parser("turn -90 \n");
		assertTrue(parser.isTurnAction());
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.angle == 60.0);
		parser = new Parser("turn 330 \n");
		assertTrue(parser.isTurnAction());
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.angle == 30.0);
	}
	@Test
	public void testMoveTo(){
		Parser parser = new Parser("moveto 12 + 2 , 6+3 \n");
		assertTrue(parser.isMoveToAction());
		assertTrue(bugs.x == 0.0);
		assertTrue(bugs.y == 0.0);
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.x == 14.0);
		assertTrue(bugs.y == 9.0);
	}
	@Test
	public void testMove(){
		Parser parser = new Parser("move 13+6 \n");
		assertTrue(parser.isMoveAction());
		assertTrue(bugs.x == 0);
		assertTrue(bugs.y == 0);
		assertTrue(bugs.angle == 0);
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.x == 19.0);
		assertTrue(bugs.y == 0.0);
		parser = new Parser("turn 60 \n");
		assertTrue(parser.isTurnAction());
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.angle == 60.0);
		parser = new Parser("move 5 \n");
		assertTrue(parser.isMoveAction());
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.x == 21.5);
		assertTrue(bugs.y == 4.330127018922193);
		assertTrue(bugs.angle == 60);
		parser = new Parser("turn -90 \n");
		assertTrue(parser.isTurnAction());
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.angle == 330.0);
		parser = new Parser("move 5 \n");
		assertTrue(parser.isMoveAction());
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.x == 25.83012701892219);
		assertTrue(bugs.y == 1.8301270189221905);
		assertTrue(bugs.angle == 330.0);
	}
	@Test
	public void testBlock(){
		Parser parser = new Parser("{ \n move 9 \n move 6+4 \n } \n");
		assertTrue(parser.isBlock());
		assertTrue(bugs.x == 0);
		assertTrue(bugs.y == 0);
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.x == 19.0);
		assertTrue(bugs.y == 0.0);
		bugs.angle = 180.0;
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.x == 0.0);
		assertTrue(bugs.y == 2.326828918379971E-15);
	}
	@Test
	public void testInitially(){
		Parser parser = new Parser ("initially {\n move 5 \n moveto 9 , 13 \n} \n");
		assertTrue(parser.isInitializationBlock());
		assertTrue(bugs.x == 0.0);
		assertTrue(bugs.y == 0.0);
		bugs.angle = 90.0;
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.x == 9.0);
		assertTrue(bugs.y == 13.0);
		parser = new Parser("initially {\n move 14 \n} \n");
		assertTrue(parser.isInitializationBlock());
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.x == 9.0);
		assertTrue(bugs.y == 27.0);
	}
	@Test
	public void testVar(){
		Parser parser = new Parser("var hello1, hello2, hello3 \n");
		assertTrue(parser.isVarDeclaration());
		try {
            assertFalse(bugs.myVariables.containsKey("hello1"));
        }
        catch (RuntimeException e) {
        }
		try {
            assertFalse(bugs.myVariables.containsKey("hello2"));
        }
        catch (RuntimeException e) {
        }
		try {
            assertFalse(bugs.myVariables.containsKey("hello3"));
        }
        catch (RuntimeException e) {
        }
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.myVariables.containsKey("hello1"));
		assertTrue(bugs.myVariables.containsKey("hello2"));
		assertTrue(bugs.myVariables.containsKey("hello3"));
		assertTrue(0.0 == bugs.myVariables.get("hello1"));
		assertTrue(0.0 == bugs.myVariables.get("hello2"));
		assertTrue(0.0 == bugs.myVariables.get("hello3"));
	}
	@Test
	public void testFunction(){
		Parser parser = new Parser("define abc {\n move a+b \n} \n");
		assertTrue(parser.isFunctionDefinition());
		try {
            assertFalse(bugs.myFunctions.containsKey("abc"));
        }
        catch (RuntimeException e) {
        }
		bugs.interpret(parser.stack.peek());
		assertTrue(bugs.myFunctions.containsKey("abc"));
		assertEquals(parser.stack.peek(), bugs.myFunctions.get("abc"));
	}
	@Test
	public void testLoop(){
		bugs.myVariables.put("one", 0.0);
		bugs.myVariables.put("two", 0.0);
		Parser parser = new Parser("loop {\n move 100\n exit if one = 4\n loop {\n exit if two = 2\n move 1\n two = two + 1\n}\n one = one + 1\n}\n");
		assertTrue(parser.isLoopStatement());
		
		bugs.interpret(parser.stack.pop());
		assertEquals(502.0, bugs.fetch("x"), 0.001);
	}
	@Test
	public void testBugs(){
		Parser parser = new Parser("Bug test { \n var abc, def \n initially { \n move 9 \n} \n move 12 \n moveto 13, 15 \n define test3 { \n moveto 16, 13 \n} \n} \n");
		assertTrue(parser.isBugDefinition());
		assertFalse(bugs.myVariables.containsKey("abc"));
		assertFalse(bugs.myVariables.containsKey("def"));
		assertTrue(bugs.x == 0.0);
		assertTrue(bugs.y == 0.0);
		bugs.interpret(parser.stack.peek());
		assertEquals(bugs.name, "test");
		assertTrue(bugs.myVariables.containsKey("abc"));
		assertTrue(bugs.myVariables.containsKey("def"));
		assertTrue(0.0 == bugs.myVariables.get("abc"));
		assertTrue(0.0 == bugs.myVariables.get("def"));
		assertTrue(bugs.x == 13.0);
		assertTrue(bugs.y == 15.0);
		assertFalse(bugs.x == 16.0);
		assertFalse(bugs.y == 13.0);
	}
	@Test
	public void testSwitch(){
		Parser parser = new Parser("switch { \n case 8+3 \n turn 60 \n } \n");
		assertTrue(parser.isSwitchStatement());
		assertTrue(bugs.angle == 0.0);
		bugs.interpret(parser.stack.peek());
		assertEquals(parser.stack.peek().getChild(0).getValue().value, "case");
		assertTrue(bugs.evaluate(parser.stack.peek().getChild(0).getChild(0)) == 11.0);
		assertTrue(bugs.angle == 60.0);
		
	}
}
