package bugs;

import static org.junit.Assert.*;

import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;


/**
 * Test class for Bugs recognizer.
 * @author David Matuszek
 */
public class RecognizerTest {
    
    Recognizer r0, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11;
    
    /**
     * Constructor for RecognizerTest.
     */
    public RecognizerTest() {
        r0 = new Recognizer("2 + 2");
        r1 = new Recognizer("");
    }


    @Before
    public void setUp() throws Exception {
        r0 = new Recognizer("");
        r1 = new Recognizer("250");
        r2 = new Recognizer("hello");
        r3 = new Recognizer("(xyz + 3)");
        r4 = new Recognizer("12 * 5 - 3 * 4 / 6 + 8");
        r5 = new Recognizer("12 * ((5 - 3) * 4) / 6 + (8)");
        r6 = new Recognizer("17 +");
        r7 = new Recognizer("22 *");
        r8 = new Recognizer("#");
    }

    @Test
    public void testRecognizer() {
        r0 = new Recognizer("");
        r1 = new Recognizer("2 + 2");
    }
    
    @Test
    public void testisComparator(){
    	r0 = new Recognizer("= <= > < text >= != 12");
    	r1 = new Recognizer("=");
    	r2 = new Recognizer("!=");
    	r3 = new Recognizer(">");
    	r4 = new Recognizer(">=");
    	r5 = new Recognizer("<=");
    	r6 = new Recognizer("<");
    	r7 = new Recognizer("text");
    	r8 = new Recognizer("12");
    	assertTrue(r0.isComparator());
    	assertTrue(r0.isComparator());
    	assertTrue(r0.isComparator());
    	assertTrue(r0.isComparator());
    	assertFalse(r0.isComparator());
    	r0.nextToken();
    	assertTrue(r0.isComparator());
    	assertTrue(r0.isComparator());
    	assertFalse(r0.isComparator());
    	assertTrue(r1.isComparator());
    	assertTrue(r2.isComparator());
    	assertTrue(r3.isComparator());
    	assertTrue(r4.isComparator());
    	assertTrue(r5.isComparator());
    	assertTrue(r6.isComparator());
    	assertFalse(r7.isComparator());
    	assertFalse(r8.isComparator());
    }
    
    @Test
    public void testIsEol(){
    	r0 = new Recognizer("\n \n \n \n");
    	r1 = new Recognizer("1");
    	r2 = new Recognizer("\n");
    	assertTrue(r0.isEol());
    	assertFalse(r1.isEol());
    	assertTrue(r2.isEol());
    }
    
    @Test
    //<expression> ::= <arithmetic expression> {  <comparator> <arithmetic expression> }
    public void testIsExpression(){
    	r0 = new Recognizer("");
    	r1 = new Recognizer("250 + hello");
    	r2 = new Recognizer("hello < xyz");
    	r3 = new Recognizer("hello >=");
    	
    	assertFalse(r0.isExpression());
    	assertTrue(r1.isExpression());
    	assertTrue(r2.isExpression());       
    	
    	try {
            assertFalse(r3.isExpression());
            fail();
        }
        catch (SyntaxException e) {
        }
    }


    @Test
    public void testIsArithmeticExpression() {
        assertTrue(r1.isArithmeticExpression());
        assertTrue(r2.isArithmeticExpression());
        assertTrue(r3.isArithmeticExpression());
        assertTrue(r4.isArithmeticExpression());
        assertTrue(r5.isArithmeticExpression());

        assertFalse(r0.isArithmeticExpression());
        assertFalse(r8.isArithmeticExpression());

        try {
            assertFalse(r6.isArithmeticExpression());
            fail();
        }
        catch (SyntaxException e) {
        }
        try {
            assertFalse(r7.isArithmeticExpression());
            fail();
        }
        catch (SyntaxException e) {
        }
    }
    
    @Test
    public void testIsArithmeticExpressionWithUnaryMinus() {
        assertTrue(new Recognizer("-5").isArithmeticExpression());
        assertTrue(new Recognizer("12+(-5*10)").isArithmeticExpression());
        assertTrue(new Recognizer("+5").isArithmeticExpression());
        assertTrue(new Recognizer("12+(+5*10)").isArithmeticExpression());
    }

    @Test
    public void testIsTerm() {
        assertFalse(r0.isTerm()); // ""
        
        assertTrue(r1.isTerm()); // "250"
        
        assertTrue(r2.isTerm()); // "hello"
        
        assertTrue(r3.isTerm()); // "(xyz + 3)"
        followedBy(r3, "");
        
        assertTrue(r4.isTerm());  // "12 * 5 - 3 * 4 / 6 + 8"
        assertEquals(new Token(Token.Type.SYMBOL, "-"), r4.nextToken());
        assertTrue(r4.isTerm());
        followedBy(r4, "+ 8");

        assertTrue(r5.isTerm());  // "12 * ((5 - 3) * 4) / 6 + (8)"
        assertEquals(new Token(Token.Type.SYMBOL, "+"), r5.nextToken());
        assertTrue(r5.isTerm());
        followedBy(r5, "");
    }

    @Test
    public void testIsFactor() {
        assertTrue(r1.isFactor());
        assertTrue(r2.isFactor());
        assertTrue(r3.isFactor());
        assertTrue(r4.isFactor()); followedBy(r4, "* 5 - 3 * 4 / 6 + 8");
        assertTrue(r5.isFactor()); followedBy(r5, "* ((5");
        assertTrue(r6.isFactor()); followedBy(r6, "+");
        assertTrue(r7.isFactor()); followedBy(r7, "*");

        assertFalse(r0.isFactor());
        assertFalse(r8.isFactor()); followedBy(r8, "#");

        Recognizer r = new Recognizer("foo()");
        assertTrue(r.isFactor());
        r = new Recognizer("bar(5, abc, 2+3)+");
        assertTrue(r.isFactor()); followedBy(r, "+");

        r = new Recognizer("foo.bar$");
        assertTrue(r.isFactor()); followedBy(r, "$");
        
        r = new Recognizer("123.123");
        assertEquals(new Token(Token.Type.NUMBER, "123.123"), r.nextToken());
        
        r = new Recognizer("5");
        assertEquals(new Token(Token.Type.NUMBER, "5.0"), r.nextToken());
    }
    
    @Test
    // <function call> ::= <NAME> <parameter list>
    
    public void testIsFunctionCall(){
    	r0 = new Recognizer("asdf123 (250 + 30)");
    	r1 = new Recognizer("asdf");
    	r2 = new Recognizer("");
    	
    	assertTrue(r0.isFunctionCall());
    	assertFalse(r2.isFunctionCall());
    	try {
            assertFalse(r1.isFunctionCall());
            fail();
        }
        catch (SyntaxException e) {
        }
    	
    }
    
    @Test
    //<function definition> ::= "define" <NAME> [ "using" <variable> { "," <variable> }  ] <block>
    
    public void testIsFunctionDefinition(){
    	r0 = new Recognizer("");
    	r1 = new Recognizer("define");
    	r2 = new Recognizer("define test");
    	r3 = new Recognizer("define test {\n}\n");
    	r4 = new Recognizer("define test using");
    	r5 = new Recognizer("define test using ab {\n}\n");
    	r6 = new Recognizer("define test using ab,");
    	r7 = new Recognizer("define test using ab, cd {\n}\n");
    	assertFalse(r0.isFunctionDefinition());
    	try {
            assertFalse(r1.isFunctionDefinition());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r2.isFunctionDefinition());
            fail();
        }
        catch (SyntaxException e) {
        }
    	assertTrue(r3.isFunctionDefinition());
    	try {
            assertFalse(r4.isFunctionDefinition());
            fail();
        }
        catch (SyntaxException e) {
        }
    	assertTrue(r5.isFunctionDefinition());
    	try {
            assertFalse(r6.isFunctionDefinition());
            fail();
        }
        catch (SyntaxException e) {
        }
    	assertTrue(r7.isFunctionDefinition());
    }
    
    @Test
    //<block> ::= "{" <eol> { <command> }  "}" <eol>
    
    public void testIsBlock(){
    	r0 = new Recognizer("a");
    	r1 = new Recognizer("{ a");
    	r2 = new Recognizer("{ \n } \n");
    	r3 = new Recognizer("{ \n move 250 + 3 \n } \n");
    	assertFalse(r0.isBlock());
    	try {
            assertFalse(r1.isBlock());
            fail();
        }
        catch (SyntaxException e) {
        }
    	assertTrue(r2.isBlock());
    	assertTrue(r3.isBlock());
    	
    }
    
    @Test
    //<move action> ::= "move" <expression> <eol>

    public void testIsMoveAction(){
    	r0 = new Recognizer("");
    	r1 = new Recognizer("move");
    	r2 = new Recognizer("move 250 + 3");
    	r3 = new Recognizer("move 250 + 3 \n");
    	assertFalse(r0.isMoveAction());
    	try {
            assertFalse(r1.isMoveAction());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r2.isMoveAction());
            fail();
        }
        catch (SyntaxException e) {
        }
    	assertTrue(r3.isMoveAction());	
    }
    
    @Test
//    <moveto action> ::= "moveto" <expression> "," <expression> <eol>
    
    public void testIsMoveToAction(){
    	r0 = new Recognizer("");
    	r1 = new Recognizer("moveto");
    	r2 = new Recognizer("moveto 250 + 3");
    	r3 = new Recognizer("moveto 250 + 3 \n");
    	r4 = new Recognizer("moveto 250 + 3 , 430 < 6");
    	r5 = new Recognizer("moveto 250 + 3 , 430 < 6 \n");
    	
    	assertFalse(r0.isMoveToAction());
    	try {
            assertFalse(r1.isMoveToAction());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r2.isMoveToAction());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r3.isMoveToAction());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r4.isMoveToAction());
            fail();
        }
        catch (SyntaxException e) {
        }
    	assertTrue(r5.isMoveToAction());
    }
    
    @Test
//    <turn action> ::= "turn" <expression> <eol>

    public void testIsTurnAction(){
    	r0 = new Recognizer("");
    	r1 = new Recognizer("turn");
    	r2 = new Recognizer("turn 250 + 3");
    	r3 = new Recognizer("turn 250 + 3 \n");
    	assertFalse(r0.isMoveAction());
    	try {
            assertFalse(r1.isTurnAction());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r2.isTurnAction());
            fail();
        }
        catch (SyntaxException e) {
        }
    	assertTrue(r3.isTurnAction());	
    }
    
    @Test
//    <turnto action> ::= "turnto" <expression> <eol>
    
    public void testIsTurnToAction(){
    	r0 = new Recognizer("");
    	r1 = new Recognizer("turnto");
    	r2 = new Recognizer("turnto 250 + 3");
    	r3 = new Recognizer("turnto 250 + 3 \n");
    	assertFalse(r0.isMoveAction());
    	try {
            assertFalse(r1.isTurnToAction());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r2.isTurnToAction());
            fail();
        }
        catch (SyntaxException e) {
        }
    	assertTrue(r3.isTurnToAction());
    }
    
    @Test
//    <line action> ::= "line" <expression> ","<expression> ","<expression> "," <expression> <eol>

    public void testIsLineAction(){
    	r0 = new Recognizer("");
    	r1 = new Recognizer("line");
    	r2 = new Recognizer("line 250 + 3");
    	r3 = new Recognizer("line 250 + 3 ,");
    	r4 = new Recognizer("line 250 + 3 , 50 < 25");
    	r5 = new Recognizer("line 250 + 3 , 50 < 25");
    	r6 = new Recognizer("line 250 + 3 , 50 < 25 ,");
    	r7 = new Recognizer("line 250 + 3 , 50 < 25 , 13 >= 10");
    	r8 = new Recognizer("line 250 + 3 , 50 < 25 , 13 >= 10 ,");
    	r9 = new Recognizer("line 250 + 3 , 50 < 25 , 13 >= 10 , 9 + 6");
    	r10 = new Recognizer("line 250 + 3 , 50 < 25 , 13 >= 10 , 9 + 6 \n");
    	assertFalse(r0.isLineAction());
    	try {
            assertFalse(r1.isLineAction());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r2.isLineAction());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r3.isLineAction());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r4.isLineAction());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r5.isLineAction());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r6.isLineAction());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r7.isLineAction());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r8.isLineAction());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r9.isLineAction());
            fail();
        }
        catch (SyntaxException e) {
        }
    	assertTrue(r10.isLineAction());
    	
    }
    
    @Test
//    <assignment statement> ::= <variable> "=" <expression> <eol>
    
    public void testIsAssignmentStatement(){
    	r0 = new Recognizer("");
    	r1 = new Recognizer("abc");
    	r2 = new Recognizer("abc =");
    	r3 = new Recognizer("abc = 250 - 99");
    	r4 = new Recognizer("abc = 250 - 99 \n");
    	assertFalse(r0.isAssignmentStatement());
    	try {
            assertFalse(r1.isAssignmentStatement());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r2.isAssignmentStatement());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r3.isAssignmentStatement());
            fail();
        }
        catch (SyntaxException e) {
        }
    	assertTrue(r4.isAssignmentStatement());
    }
    
    @Test
//    <loop statement> ::= "loop" <block>

    public void testisLoopStatement(){
    	r0 = new Recognizer("");
    	r1 = new Recognizer("loop");
    	r2 = new Recognizer("loop { \n move 250 + 3 \n } \n");
    	assertFalse(r0.isLoopStatement());
    	try {
            assertFalse(r1.isLoopStatement());
            fail();
        }
        catch (SyntaxException e) {
        }
    	assertTrue(r2.isLoopStatement());
    }
    
    @Test
//    <exit if statement> ::= "exit" "if" <expression> <eol>

    public void testIsExitIfStatement(){
    	r0 = new Recognizer("");
    	r1 = new Recognizer("exit");
    	r2 = new Recognizer("exit if");
    	r3 = new Recognizer("exit if 2 + 3");
    	r4 = new Recognizer("exit if 2 + 3 \n");
    	assertFalse(r0.isExitIfStatement());
    	try {
            assertFalse(r1.isExitIfStatement());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r2.isExitIfStatement());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r3.isExitIfStatement());
            fail();
        }
        catch (SyntaxException e) {
        }
    	assertTrue(r4.isExitIfStatement());
    }
    
    @Test
//    <switch statement> ::= "switch" "{" <eol> { "case" <expression> <eol> { <command> } } "}" <eol>

    public void testIsSwitchStatement(){
    	r11 = new Recognizer("");
    	r0 = new Recognizer("switch");
    	r1 = new Recognizer("switch {");
    	r2 = new Recognizer("switch { \n");
    	r3 = new Recognizer("switch { \n }");
    	r4 = new Recognizer("switch { \n } \n");
    	r5 = new Recognizer("switch { \n case");
    	r6 = new Recognizer("switch { \n case 2 + 3");
    	r7 = new Recognizer("switch { \n case 2 + 3 \n");
    	r8 = new Recognizer("switch { \n case 2 + 3 \n }");
    	r9 = new Recognizer("switch { \n case 2 + 3 \n } \n");
    	r10 = new Recognizer("switch { \n case 2 + 3 \n move 2 + 3 \n} \n");
    	assertFalse(r11.isSwitchStatement());
    	assertTrue(r4.isSwitchStatement());
    	assertTrue(r9.isSwitchStatement());
    	assertTrue(r10.isSwitchStatement());
    	try {
            assertFalse(r0.isSwitchStatement());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r1.isSwitchStatement());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r2.isSwitchStatement());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r3.isSwitchStatement());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r5.isSwitchStatement());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r6.isSwitchStatement());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r7.isSwitchStatement());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r8.isSwitchStatement());
            fail();
        }
        catch (SyntaxException e) {
        }
    }
    
    @Test
//    <return statement> ::= "return" <expression> <eol>

    public void testIsReturnStatement(){
    	r0 = new Recognizer("");
    	r1 = new Recognizer("return");
    	r2 = new Recognizer("return 2 + 3");
    	r3 = new Recognizer("return 2 + 3 \n");
    	assertFalse(r0.isReturnStatement());
    	try {
            assertFalse(r1.isReturnStatement());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r2.isReturnStatement());
            fail();
        }
        catch (SyntaxException e) {
        }
    	assertTrue(r3.isReturnStatement());
    }
    
    @Test
//    <do statement> ::= "do" <variable> [ <parameter list> ] <eol>

    public void isDoStatement(){
    	r0 = new Recognizer("");
    	r1 = new Recognizer("do");
    	r2 = new Recognizer("do abc");
    	r3 = new Recognizer("do abc \n");//
    	r4 = new Recognizer("do abc ()");
    	r5 = new Recognizer("do abc () \n");
    	assertFalse(r0.isDoStatement());
    	try {
            assertFalse(r1.isDoStatement());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r2.isDoStatement());
            fail();
        }
        catch (SyntaxException e) {
        }
    	assertTrue(r3.isDoStatement());
    	try {
            assertFalse(r4.isDoStatement());
            fail();
        }
        catch (SyntaxException e) {
        }
    	assertTrue(r5.isDoStatement());
    }
    
    @Test
//    <color statement> ::= "color" <KEYWORD> <eol>

    public void testIsColorStatement(){
    	r0 = new Recognizer("");
    	r1 = new Recognizer("color");
    	r2 = new Recognizer("color turn");
    	r3 = new Recognizer("color turn \n");
    	assertFalse(r0.isColorStatement());
    	try {
            assertFalse(r1.isColorStatement());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r2.isColorStatement());
            fail();
        }
        catch (SyntaxException e) {
        }
    	assertTrue(r3.isColorStatement());
    }
    
    @Test
//    <action> ::= <move action> | <moveto action> | <turn action> | <turnto action> | <line action>
    
    public void testIsAction(){
    	r0 = new Recognizer("move 250 + 3 \n");
    	r1 = new Recognizer("moveto 250 + 3 , 430 < 6 \n");
    	r2 = new Recognizer("turn 250 + 3 \n");
    	r3 = new Recognizer("turnto 250 + 3 \n");
    	r4 = new Recognizer("line 250 + 3 , 50 < 25 , 13 >= 10 , 9 + 6 \n");
    	r5 = new Recognizer("");
    	assertTrue(r0.isAction());
    	assertTrue(r1.isAction());
    	assertTrue(r2.isAction());
    	assertTrue(r3.isAction());
    	assertTrue(r4.isAction());
    	assertFalse(r5.isAction());
    }
    
    @Test
//    <statement> ::= <assignment statement> | <loop statement> | <exit if statement> | <switch statement> | <return statement> | <do statement> | <color statement>
    public void isStatement(){
    	r0 = new Recognizer("abc = 250 - 99 \n");
    	r1 = new Recognizer("loop { \n move 250 + 3 \n } \n");
    	r2 = new Recognizer("exit if 2 + 3 \n");
    	r3 = new Recognizer("switch { \n } \n");
    	r4 = new Recognizer("return 2 + 3 \n");
    	r5 = new Recognizer("do abc () \n");
    	r6 = new Recognizer("color turn \n");
    	r7 = new Recognizer("");
    	assertTrue(r0.isStatement());
    	assertTrue(r1.isStatement());
    	assertTrue(r2.isStatement());
    	assertTrue(r3.isStatement());
    	assertTrue(r4.isStatement());
    	assertTrue(r5.isStatement());
    	assertTrue(r6.isStatement());
    	assertFalse(r7.isStatement());
    }
    
    @Test
//    <command> ::= <action> | <statement>
    
    public void testIsCommand(){
    	r0 = new Recognizer("turnto 250 + 3 \n");
    	r1 = new Recognizer("color turn \n");
    	r2 = new Recognizer("");
    	assertTrue(r0.isCommand());
    	assertTrue(r1.isCommand());
    	assertFalse(r2.isCommand());
    }
    
    @Test
//    <var declaration> ::= "var" <NAME> { "," <NAME> } <eol>                                      

    public void testIsVarDeclaration(){
    	r0 = new Recognizer("");
    	r1 = new Recognizer("var");
    	r2 = new Recognizer("var abc");
    	r3 = new Recognizer("var abc \n");
    	r4 = new Recognizer("var abc ,");
    	r5 = new Recognizer("var abc , def");
    	r6 = new Recognizer("var abc, def \n");
    	assertFalse(r0.isVarDeclaration());
    	try {
            assertFalse(r1.isVarDeclaration());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r2.isVarDeclaration());
            fail();
        }
        catch (SyntaxException e) {
        }
    	assertTrue(r3.isVarDeclaration());
    	try {
            assertFalse(r4.isVarDeclaration());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r5.isVarDeclaration());
            fail();
        }
        catch (SyntaxException e) {
        }
    	assertTrue(r6.isVarDeclaration());
    }
    
    @Test
//    <initialization block> ::= "initially" <block>

    public void testIsInitializationBlock(){
    	r0 = new Recognizer("");
    	r1 = new Recognizer("initially");
    	r2 = new Recognizer("initially { \n } \n");
    	assertFalse(r0.isInitializationBlock());
    	try {
            assertFalse(r1.isInitializationBlock());
            fail();
        }
        catch (SyntaxException e) {
        }
    	assertTrue(r2.isInitializationBlock());
    }
    
    @Test
//    <bug definition> ::= "Bug" <name> "{" <eol>
//    { <var declaration> }
//    [ <initialization block> ]
//    <command>
//    { <command> }
//    { <function definition> }
//"}" <eol>

    public void testIsBugDefinition(){
    	r0 = new Recognizer("");
    	r1 = new Recognizer("Bug");
    	r2 = new Recognizer("Bug test");
    	r3 = new Recognizer("Bug test {");
    	r4 = new Recognizer("Bug test { \n");
    	r5 = new Recognizer("Bug test { \n turnto 250 + 3");
    	r6 = new Recognizer("Bug test { \n turnto 250 + 3 }");
    	r7 = new Recognizer("Bug test { \n turnto 250 + 3 \n } \n");//
    	r8 = new Recognizer("Bug test { \n var abc, def \n turnto 250 + 3 \n } \n");//
    	r9 = new Recognizer("Bug test { \n initially { \n } \n turnto 250 + 3 \n } \n");//
    	r10 = new Recognizer("Bug test { \n turnto 250 + 3 \n color turn \n} \n");//
    	r11 = new Recognizer("Bug test { \n turnto 250 + 3 \n define test {\n}\n} \n");//
    	
    	assertFalse(r0.isBugDefinition());
    	assertTrue(r7.isBugDefinition());
    	assertTrue(r8.isBugDefinition());
    	assertTrue(r9.isBugDefinition());
    	assertTrue(r10.isBugDefinition());
    	assertTrue(r11.isBugDefinition());
    	try {
            assertFalse(r1.isBugDefinition());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r2.isBugDefinition());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r3.isBugDefinition());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r4.isBugDefinition());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r5.isBugDefinition());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r6.isBugDefinition());
            fail();
        }
        catch (SyntaxException e) {
        }
    }
    
    @Test
//    <allbugs code> ::= "Allbugs"  "{" <eol> { <var declaration> } { <function definition> } "}" <eol>

    public void testIsAllBugsCode(){
    	r0 = new Recognizer("");
    	r1 = new Recognizer("Allbugs");
    	r2 = new Recognizer("Allbugs {");
    	r3 = new Recognizer("Allbugs { \n");
    	r4 = new Recognizer("Allbugs { \n }");
    	r5 = new Recognizer("Allbugs { \n } \n");//
    	r6 = new Recognizer("Allbugs { \n var abc \n } \n");//
    	r7 = new Recognizer("Allbugs { \n define test {\n}\n } \n");//
    	r8 = new Recognizer("Allbugs { \n var abc \n define test {\n}\n } \n");//
    	
    	assertFalse(r0.isAllbugsCode());
    	assertTrue(r5.isAllbugsCode());
    	assertTrue(r6.isAllbugsCode());
    	assertTrue(r7.isAllbugsCode());
    	assertTrue(r8.isAllbugsCode());
    	try {
            assertFalse(r1.isAllbugsCode());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r2.isAllbugsCode());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r3.isAllbugsCode());
            fail();
        }
        catch (SyntaxException e) {
        }
    	try {
            assertFalse(r4.isAllbugsCode());
            fail();
        }
        catch (SyntaxException e) {
        }
    }
    
    @Test
//    <program> ::= [ <allbugs code> ] <bug definition> { <bug definition> }
    
    public void testIsProgram(){
    	r0 = new Recognizer("");
    	r1 = new Recognizer("Allbugs { \n } \n");
    	r2 = new Recognizer("Allbugs { \n } \n Bug test { \n turnto 250 + 3 \n } \n"); //
    	r3 = new Recognizer("Allbugs { \n } \n Bug test { \n turnto 250 + 3 \n } \n Bug test { \n var abc, def \n turnto 250 + 3 \n } \n");//
    	r4 = new Recognizer("Bug test { \n turnto 250 + 3 \n } \n Bug test { \n var abc, def \n turnto 250 + 3 \n } \n Bug test { \n turnto 250 + 3 \n color turn \n} \n");//
    	assertFalse(r0.isProgram());
    	try {
            assertFalse(r1.isProgram());
            fail();
        }
        catch (SyntaxException e) {
        }
    	assertTrue(r2.isProgram());
    	assertTrue(r3.isProgram());
    	assertTrue(r4.isProgram());  	
    }
    
    @Test
    public void testIsParameterList() {
        Recognizer r = new Recognizer("() $");
        assertTrue(r.isParameterList()); followedBy(r, "$");
        r = new Recognizer("(5) $");
        assertTrue(r.isParameterList()); followedBy(r, "$");
        r = new Recognizer("(bar, x+3) $");
        assertTrue(r.isParameterList()); followedBy(r, "$");
    }

    @Test
    public void testIsAddOperator() {
        Recognizer r = new Recognizer("+ - $");
        assertTrue(r.isAddOperator());
        assertTrue(r.isAddOperator());
        assertFalse(r.isAddOperator());
        followedBy(r, "$");
    }

    @Test
    public void testIsMultiplyOperator() {
        Recognizer r = new Recognizer("* / $");
        assertTrue(r.isMultiplyOperator());
        assertTrue(r.isMultiplyOperator());
        assertFalse(r.isMultiplyOperator());
        followedBy(r, "$");
    }

    @Test
    public void testIsVariable() {
        Recognizer r = new Recognizer("foo 23 bar +");
        assertTrue(r.isVariable());
        
        assertFalse(r.isVariable());
        assertTrue(r.isFactor());
        
        assertTrue(r.isVariable());
        
        assertFalse(r.isVariable());
        assertTrue(r.isAddOperator());
    }

    @Test
    public void testSymbol() {
        Recognizer r = new Recognizer("++");
        assertEquals(new Token(Token.Type.SYMBOL, "+"), r.nextToken());
    }

    @Test
    public void testNextTokenMatchesType() {
        Recognizer r = new Recognizer("++abc");
        assertTrue(r.nextTokenMatches(Token.Type.SYMBOL));
        assertFalse(r.nextTokenMatches(Token.Type.NAME));
        assertTrue(r.nextTokenMatches(Token.Type.SYMBOL));
        assertTrue(r.nextTokenMatches(Token.Type.NAME));
    }

    @Test
    public void testNextTokenMatchesTypeString() {
        Recognizer r = new Recognizer("+abc+");
        assertTrue(r.nextTokenMatches(Token.Type.SYMBOL, "+"));
        assertTrue(r.nextTokenMatches(Token.Type.NAME, "abc"));
        assertFalse(r.nextTokenMatches(Token.Type.SYMBOL, "*"));
        assertTrue(r.nextTokenMatches(Token.Type.SYMBOL, "+"));
    }

    @Test
    public void testNextToken() {
        // NAME, KEYWORD, NUMBER, SYMBOL, EOL, EOF };
        Recognizer r = new Recognizer("abc move 25 *\n");
        assertEquals(new Token(Token.Type.NAME, "abc"), r.nextToken());
        assertEquals(new Token(Token.Type.KEYWORD, "move"), r.nextToken());
        assertEquals(new Token(Token.Type.NUMBER, "25.0"), r.nextToken());
        assertEquals(new Token(Token.Type.SYMBOL, "*"), r.nextToken());
        assertEquals(new Token(Token.Type.EOL, "\n"), r.nextToken());
        assertEquals(new Token(Token.Type.EOF, "EOF"), r.nextToken());
        
        r = new Recognizer("foo.bar 123.456");
        assertEquals(new Token(Token.Type.NAME, "foo"), r.nextToken());
        assertEquals(new Token(Token.Type.SYMBOL, "."), r.nextToken());
        assertEquals(new Token(Token.Type.NAME, "bar"), r.nextToken());
        assertEquals(new Token(Token.Type.NUMBER, "123.456"), r.nextToken());
    }

    @Test
    public void testPushBack() {
        Recognizer r = new Recognizer("abc 25");
        assertEquals(new Token(Token.Type.NAME, "abc"), r.nextToken());
        r.pushBack();
        assertEquals(new Token(Token.Type.NAME, "abc"), r.nextToken());
        assertEquals(new Token(Token.Type.NUMBER, "25.0"), r.nextToken());
    }
    
//  ----- "Helper" methods

    /**
     * This method is given a String containing some or all of the
     * tokens that should yet be returned by the Tokenizer, and tests
     * whether the Tokenizer in fact has those Tokens. To succeed,
     * everything in the given String must still be in the Tokenizer,
     * but there may be additional (untested) Tokens to be returned.
     * This method is primarily to test whether rejected Tokens are
     * pushed back appropriately.
     * 
     * @param recognizer The Recognizer whose Tokenizer is to be tested.
     * @param expectedTokens The Tokens we expect to get from the Tokenizer.
     */
    private void followedBy(Recognizer recognizer, String expectedTokens) {
        int expectedType;
        int actualType;
        StreamTokenizer actual = recognizer.tokenizer;

        Reader reader = new StringReader(expectedTokens);
        StreamTokenizer expected = new StreamTokenizer(reader);
        expected.ordinaryChar('-');
        expected.ordinaryChar('/');

        try {
            while (true) {
                expectedType = expected.nextToken();
                if (expectedType == StreamTokenizer.TT_EOF) break;
                actualType = actual.nextToken();
                assertEquals(expectedType, actualType);
                if (actualType == StreamTokenizer.TT_WORD) {
                    assertEquals(expected.sval, actual.sval);
                }
                else if (actualType == StreamTokenizer.TT_NUMBER) {
                    assertEquals(expected.nval, actual.nval, 0.001);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

