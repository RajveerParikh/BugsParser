package bugs;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;

/**
 * This class consists of a number of methods that "recognize" strings
 * composed of Tokens that follow the indicated grammar rules for each
 * method.
 * <p>Each method may have one of three outcomes:
 * <ul>
 *   <li>The method may succeed, returning <code>true</code> and
 *      consuming the tokens that make up that particular nonterminal.</li>
 *   <li>The method may fail, returning <code>false</code> and not
 *       consuming any tokens.</li>
 *   <li>(Some methods only) The method may determine that an
 *       unrecoverable error has occurred and throw a
 *       <code>SyntaxException</code></li>.
 * </ul>
 * @author David Matuszek
 * @author Rajveer Parikh
 * @version February 2015
 */
public class Recognizer {
    StreamTokenizer tokenizer = null;
    int lineNumber;
    
    /**
     * Constructs a Recognizer for the given string.
     * @param text The string to be recognized.
     */
    public Recognizer(String text) {
        Reader reader = new StringReader(text);
        tokenizer = new StreamTokenizer(reader);
        tokenizer.parseNumbers();
        tokenizer.eolIsSignificant(true);
        tokenizer.slashStarComments(true);
        tokenizer.slashSlashComments(true);
        tokenizer.lowerCaseMode(false);
        tokenizer.ordinaryChars(33, 47);
        tokenizer.ordinaryChars(58, 64);
        tokenizer.ordinaryChars(91, 96);
        tokenizer.ordinaryChars(123, 126);
        tokenizer.quoteChar('\"');
        lineNumber = 1;
    }

    /**
     * Tries to recognize an &lt;action&gt; statement.
     * <pre>&ltaction&gt ::= &ltmove action&gt | &ltmoveto action&gt
     * | &ltturn action&gt| &ltturnto action&gt| &ltline action&gt)</pre>
     * @return <code>true</code> if an &lt;action&gt; is recognized.
     */
  
    public boolean isAction(){
    	if (isMoveAction()){
    		return true;
    	}
    	if (isMoveToAction()){
    		return true;
    	}
    	if (isTurnAction()){
    		return true;
    	}
    	if (isTurnToAction()){
    		return true;
    	}
    	if (isLineAction()){
    		return true;
    	}
    	return false;
    }
    

    /**
     * Tries to check for &lt;allbugs code&gt; .
     * <pre>&ltallbugs code&gt ::= "Allbugs"  "{" &lteol&gt { &ltvar declaration&gt } { &ltfunction definition&gt } "}" &lteol&gt</pre>
     * A <code>SyntaxException</code> will be thrown if &lt;keyword&gt; Allbugs
     * is present but not followed by a '{', if '{' is not followed
     * by &lt;eol&gt, if &lt;eol&gt is not followed by '}' and if
     * '}' is not followed by &lt;eol&gt;.
     * @return <code>true</code> if &ltallbugs code&gt is recognized.
     */
    
    public boolean isAllbugsCode(){
    	if (!keyword("Allbugs")){
    		return false;
    	}
    	if (!symbol("{")){
    		error("No '{' after keyword");
    	}
    	if (!isEol()){
    		error("No EOL after '}'");
    	}
    	while(isVarDeclaration()){
    		
    	}
    	while(isFunctionDefinition()){
    		
    	}
    	if(!symbol("}")){
    		error("Missing '}'");
    	}
    	if (!isEol()){
    		error("No EOL after '}'");
    	}
    	return true;
    }
    

    /**
     * Tries to check if it is an &lt;assignment statement&gt;
     * <pre>&ltassignment statement&gt ::= &ltvariable&gt "=" &ltexpression&gt &lteol&gt</pre>
     * A <code>SyntaxException</code> will be thrown if &lt;variable&gt;
     * is present but not followed by an '=', if '=' is not followed
     * by an &lt;expression&gt, and if &lt;expression&gt is not followed by &lt;eol&gt;.
     * @return <code>true</code> if an &lt;assignment statement&gt; is recognized.
     */
    
    public boolean isAssignmentStatement(){
    	if (!(isVariable())){
    		return false;
    	}
    	if (!(symbol("="))){
    		error("No '=' after variable");
    	}
    	if (!(isExpression())){
    		error("No expression after '='");
    	}
    	if (!(isEol())){
    		error("No EOL after expression");
    	}
    	return true;
    }
    

    /**
     * Tries to check if it is a &lt;block&gt;
     * <pre>&ltblock&gt ::= "{" &lteol&gt { &ltcommand&gt }  "}" &lteol&gt</pre>
     * A <code>SyntaxException</code> will be thrown if '{' symbol
     * is present but not followed by an &lt;eol&gt;, if '{' is not closed
     * by an '}', and if '}' is not followed by &lt;eol&gt;.
     * @return <code>true</code> if a &ltblock&gt is recognized.
     */
    
    public boolean isBlock(){
    	if (!symbol("{")){
    		return false;
    	}
    	if (!(isEol())){
    		error("No EOL after '{'");
    	}
    	while (isCommand()){
    	}
    	if (!(symbol("}"))){
    		error("No '}' after commands");
    	}
    	if (!(isEol())){
    		error("No EOL after '}'");
    	}
    	return true;
    }
    
    /**
     * Tries to check if it is a &lt;bug definition&gt;
     * <pre>&ltbug definition&gt ::= "Bug" &ltname&gt "{" &lteol&gt { &ltvar declaration&gt } [ &ltinitialization block&gt ] &ltcommand&gt { &ltcommand&gt } { &ltfunction definition&gt } "}" &lteol&gt</pre>
     * A <code>SyntaxException</code> will be thrown if &ltkeyword&gt "Bug"
     * is present but not followed by an &lt;name&gt;, if &lt;name&gt; is not followed by
     * '{', if '{' is not followed by &lt;eol&gt;, if &lt;eol&gt; is not followed by '{', 
     * if '{' is not followed by &lt;command&gt;, if &lt;command&gt; is not followed by '}'
     * and if '}' is not followed by &lt;eol&gt;.
     * @return <code>true</code> if a &ltbug definition&gt is recognized.
     */
    public boolean isBugDefinition(){
    	if (!keyword("Bug")){
    		return false;
    	}
    	if (!name()){
    		error("No name after keyword");
    	}
    	if (!symbol("{")){
    		error ("No '{' after name");
    	}
    	if (!isEol()){
    		error("No EOL after '{'");
    	}
    	while(isVarDeclaration()){
    		
    	}
    	if (isInitializationBlock()){
    		
    	}
    	if (!isCommand()){
    		error("No command where needed");
    	}
    	while(isCommand()){
    		
    	}
    	while(isFunctionDefinition()){
    		
    	}
    	if (!symbol("}")){
    		error("Missing '}'");
    	}
    	if (!isEol()){
    		error("No EOL following '}'");
    	}
    	return true;
    }
    
    /**
     * Tries to check if it is a &lt;color statement&gt;
     * <pre>&ltcolor statement&gt ::= "color" &ltKEYWORD&gt &lteol&gt</pre>
     * A <code>SyntaxException</code> will be thrown if &lt;keyword&gt "color"
     * is present but not followed by another &lt;keyword&gt;, if &lt;keyword&gt; 
     * is not followed by &lt;eol&gt;.
     * @return <code>true</code> if a &ltcolor statement&gt is recognized.
     */
    
    public boolean isColorStatement(){
    	if (!(keyword("color"))){
    		return false;
    	}
    	if (!(nextTokenMatches(Token.Type.KEYWORD))){
    		error ("No keyword after keyword 'color'");
    	}
    	if (!(isEol())){
    		error("No EOL after keyword");
    	}
    	return true;
    }
    
    /**
     * Tries to check if it is a &lt;command&gt; statement
     * <pre>&ltcommand&gt ::= &ltaction&gt | &ltstatement&gt</pre>
     * @return <code>true</code> if a &ltcommand&gt statement is recognized.
     */
    
    public boolean isCommand(){
    	if (isAction()){
    		return true;
    	}
    	if (isStatement()){
    		return true;
    	}
    	return false;
    }
    
    /**
     * Tries to check for &lt;comparator&gt;
     * <pre>&ltcomparator&gt ::= "&lt" | "&lt=" | "=" | "!="| "&gt=" | "&gt"</pre>
     * @return <code>true</code> if a &ltcomparator&gt is recognized.
     */
    
    public boolean isComparator(){
    	if (symbol("!")){
    		if (!symbol("=")){
    			error("No '=' after '!'");
    		}
    		else{
    			return true;
    		}
    	}
    	
    	if (symbol("=")){
    		return true;
    	}
    	if (symbol("<") || symbol (">")){
    		if (symbol("=")){
    			return true;
    		}
    		return true;
    	}
    	return false;
    }
    

    /**
     * Tries to check if it is a &lt;do statement&gt; 
     * <pre>&ltdo statement&gt ::= "do" &ltvariable&gt [ &ltparameter list&gt ] &lteol&gt</pre>
     * A <code>SyntaxException</code> will be thrown if &lt;keyword&gt "do"
     * is present but not followed by a &lt;variable&gt;, and if &lt;variable&gt; 
     * is not followed by &lt;eol&gt;.
     * @return <code>true</code> if a &ltdo statement&gt is recognized.
     */
    
    public boolean isDoStatement(){
    	if (!(keyword("do"))){
    		return false;
    	}
    	if (!(isVariable())){
    		error("No variable after keyword");
    	}
    	if (isParameterList()){
    		
    	}
    	if (!isEol()){
    		error("No EOL After variable or parameter list");
    	}
    	return true;    	
    }
    
    
    /**
     * Tries to check if it is an &lt;exit if statement&gt; 
     * <pre>&ltexit if statement&gt ::= "exit" "if" &ltexpression&gt &lteol&gt</pre>
     * A <code>SyntaxException</code> will be thrown if &lt;keyword&gt "exit"
     * is present but not followed by &lt;keyword&gt "if", if &lt;keyword&gt "if" 
     * is not followed by &lt;expression&gt if &lt;expression&gt is not 
     * followed by &lt;eol&gt;.
     * @return <code>true</code> if a &ltexit if statement&gt is recognized.
     */
    
    public boolean isExitIfStatement(){
    	if (!(keyword("exit"))){
    		return false;
    	}
    	if (!(keyword("if"))){
    		error("No if after exit");
    	}
    	if (!(isExpression())){
    		error("No expression after exit if");
    	}
    	if (!(isEol())){
    		error("No EOL after expression");
    	}
    	return true;
    }
    
    /**
     * Tries to check if it is a &lt;function call&gt; 
     * <pre>&ltfunction call&gt ::= &ltNAME&gt &ltparameter list&gt</pre>
     * A <code>SyntaxException</code> will be thrown if &lt;name&gt
     * is present but not followed by &lt;parameter list&gt
     * @return <code>true</code> if a &ltfunction call&gt is recognized.
     */
    
    public boolean isFunctionCall(){
    	if (!name()){
    		return false;
    	}
    	if (!isParameterList()){
    		error("No Parameter List after name");
    	}
    	return true;
    }
    
    /**
     * Tries to check if it is a &lt;function definition&gt; 
     * <pre>&ltfunction definition&gt ::= "define" &ltNAME&gt 
     * [ "using" &ltvariable&gt { "," &ltvariable&gt }  ] &ltblock&gt</pre>
     * A <code>SyntaxException</code> will be thrown if &lt;keyword&gt "define"
     * is present but not followed by &lt;name&gt and if it is not ended with
     * &lt;block&gt
     * @return <code>true</code> if a &ltfunction Definition&gt is recognized.
     */
    
    public boolean isFunctionDefinition(){
    	if (!keyword("define")){
    		return false;
    	}
    	if (!name()){
    		error("No Name followed by keyword");
    	}
    	if (keyword("using")){
    		if (!isVariable()){
    			error("No Variable after keyword");
    		}
    		while(symbol(",")){
    			if (!isVariable()){
    				error("No Variable after ','");
    			}
    		}
    	}
    	if (!isBlock()){
    		error("No Block at end of function definition");
    	}
    	return true;
    }
        
    /**
     * Tries to check if it is an &lt;initialization block&gt; 
     * <pre>&ltinitialization block&gt ::= "initially" &ltblock&gt</pre>
     * A <code>SyntaxException</code> will be thrown if &lt;keyword&gt "initially"
     * is present but not followed by &lt;block&gt 
     * @return <code>true</code> if an &lt initialization block&gt is recognized.
     */
    
    public boolean isInitializationBlock(){
    	if (!keyword("initially")){
    		return false;
    	}
    	if (!isBlock()){
    		error("No Block followed by keyword");
    	}
    	return true;
    }
    
    
    /**
     * Tries to check if it is a &lt;line action&gt; 
     * <pre>&ltline action&gt ::= "line" &ltexpression&gt ","&ltexpression&gt 
     * ","&ltexpression&gt "," &ltexpression&gt &lteol&gt</pre>
     * A <code>SyntaxException</code> will be thrown if &lt;keyword&gt "line"
     * is present but not followed by 4 &lt;expression&gt,each separated by a ','. 
     * If the 4 &lt;expression&gt are not followed by &lt;eol&gt   
     * @return <code>true</code> if a &ltline action&gt is recognized.
     */
    public boolean isLineAction(){
    	if (!keyword("line")){
    		return false;
    	}
    	if (!isExpression()){
    		error("No expression after keyword");
    	}
    	if (!symbol(",")){
    		error("No ',' after expression");
    	}
    	if (!isExpression()){
    		error("No expression after ','");
    	}
    	if (!symbol(",")){
    		error("No ',' after expression");
    	}
    	if (!isExpression()){
    		error("No expression after ','");
    	}
    	if (!symbol(",")){
    		error("No ',' after expression");
    	}
    	if (!isExpression()){
    		error("No expression after ','");
    	}
    	if (!isEol()){
    		error("No EOL after expression");
    	}
    	return true;
    }
    
    
    /**
     * Tries to check if it is a &lt;loop statement&gt; 
     * <pre>&ltloop statement&gt ::= "loop" &ltblock&gt</pre>
     * A <code>SyntaxException</code> will be thrown if &lt;keyword&gt "loop"
     * is present but not followed by a &lt;block&gt   
     * @return <code>true</code> if a &ltloop statement&gt is recognized.
     */
    
    public boolean isLoopStatement(){
    	if (!(keyword("loop"))){
    		return false;
    	}
    	if (!(isBlock())){
    		error("No block after keyword");
    	}
    	return true;
    }
    
    /**
     * Tries to check if it is a &lt;move action&gt; 
     * <pre>&ltmove action&gt ::= "move" &ltexpression&gt &lteol&gt</pre>
     * A <code>SyntaxException</code> will be thrown if &lt;keyword&gt "move"
     * is present but not followed by an &lt;expression&gt and &lt;eol&gt    
     * @return <code>true</code> if a &ltmove action&gt is recognized.
     */
    public boolean isMoveAction(){
    	if (!keyword("move")){
    		return false;
    	}
    	if (!isExpression()){
    		error("No expression after keyword");
    		}
    	if (!isEol()){
    		error("No EOL after expression");
    	}
    	return true;
    }

    
    /**
     * Tries to check if it is a &lt;moveto action&gt; 
     * <pre>&ltmoveto action&gt ::= "moveto" &ltexpression&gt "," &ltexpression&gt &lteol&gt</pre>
     * A <code>SyntaxException</code> will be thrown if &lt;keyword&gt "moveto"
     * is present but not followed by 2 &lt;expression&gt separated by ',' 
     * and &lt;eol&gt    
     * @return <code>true</code> if a &ltmoveto action&gt is recognized.
     */
    
    public boolean isMoveToAction(){
    	if (!keyword("moveto")){
    		return false;
    	}
    	if (!(isExpression())){
    		error("No expression after keyword");
    	}
    	if (!symbol(",")){
    		error("No ',' after expression");
    	}
    	if (!isExpression()){
    		error("No expression after ','");
    	}
    	if (!isEol()){
    		error("No EOL after expression");
    	}
    	return true;
    }
    

    /**
     * Tries to check if it is a &lt;program&gt; 
     * <pre>&ltprogram&gt ::= [ &ltallbugs code> ] &ltbug definition&gt { &ltbug definition&gt }</pre>
     * A <code>SyntaxException</code> will be thrown if &lt;AllbugsCode&gt
     * is present but not followed by &lt;BugDefinition&gt
     * @return <code>true</code> if a &ltprogram&gt is recognized.
     */
    
    public boolean isProgram(){
    	while (isEol()){
    		
    	}
    	if (isAllbugsCode()){
    		if (!isBugDefinition()){
    			error ("No bug Definition after all bugs code");
    		}
    		while (isBugDefinition()){
    			
    		}
    		return true;
    	}
    	if (!isBugDefinition()){
    		return false;
    	}
    	while (isBugDefinition()){
    		
    	}
    	return true;
    }
    
    /**
     * Tries to check if it is a &lt;return statement&gt; 
     * <pre>&ltreturn statement&gt ::= "return" &ltexpression&gt &lteol&gt</pre>
     * A <code>SyntaxException</code> will be thrown if &lt;keyword&gt "return"
     * is present but not followed by &lt;expression&gt and &lt;eol&gt 
     * @return <code>true</code> if a &ltreturn statement&gt is recognized.
     */
    
    public boolean isReturnStatement(){
    	if (!(keyword("return"))){
    		return false;
    	}
    	if (!(isExpression())){
    		error("No expression after keyword");
    	}
    	if (!(isEol())){
    		error("No EOL after expression");
    	}
    	return true;
    }
    
    /**
     * Tries to check if it is a &lt;statement&gt; 
     * <pre>&ltstatement&gt ::= &ltassignment statement&gt | &ltloop statement&gt | &ltexit if statement&gt
     * | &ltswitch statement&gt | &ltreturn statement&gt | &ltdo statement&gt | &ltcolor statement&gt</pre>
     * @return <code>true</code> if a &ltstatement&gt is recognized.
     */
    
    public boolean isStatement(){
    	if (isAssignmentStatement()){
    		return true;
    	}
    	if (isLoopStatement()){
    		return true;
    	}
    	if (isExitIfStatement()){
    		return true;
    	}
    	if (isSwitchStatement()){
    		return true;
    	}
    	if (isReturnStatement()){
    		return true;
    	}
    	if (isDoStatement()){
    		return true;
    	}
    	if (isColorStatement()){
    		return true;
    	}
    	return false;
    }
    
    /**
     * Tries to check if it is a &lt;switch statement&gt; 
     * <pre>&ltswitch statement&gt ::= "switch" "{" &lteol&gt 
     * { "case" &ltexpression&gt &lteol&gt { &ltcommand&gt } } "}" &lteol&gt</pre>
     * A <code>SyntaxException</code> will be thrown if &lt;keyword&gt "switch"
     * is present but not followed by '{', closed with '}' and &lt;eol&gt 
     * @return <code>true</code> if a &ltswitch statement&gt is recognized.
     */
    
    public boolean isSwitchStatement(){
    	if (!(keyword("switch"))){
    		return false;
    	}
    	if (!(symbol("{"))){
    		error("No '{' followed by keyword");
    	}
    	if (!(isEol())){
    		error("No EOL after '{'");
    	}
    	while(keyword("case")){
    		if (!(isExpression())){
    			error("No expression for case");
    		}
    		if (!(isEol())){
    			error("No EOL after expression");
    		}
    		while (isCommand()){
    		}
    	}
    		if (!(symbol("}"))){
    			error("No '}' after cases");
    		}
    		if (!(isEol())){
    			error("No EOL After '}'");
    		}
    	return true;
    }

    /**
     * Tries to check if it is a &lt;turn action&gt; 
     * <pre>&ltturn action&gt ::= "turn" &ltexpression&gt &lteol&gt</pre>
     * A <code>SyntaxException</code> will be thrown if &lt;keyword&gt "turn"
     * is present but not followed by &lt;expression&gt and &lt;eol&gt 
     * @return <code>true</code> if a &ltturn action&gt is recognized.
     */
    
    public boolean isTurnAction(){
    	if (!keyword("turn")){
    		return false;
    	}
    	if (!isExpression()){
    		error("No expression after keyword");
    	}
    	if (!isEol()){
    		error("No EOL after expression");
    	}
    	return true;
    }
    
    /**
     * Tries to check if it is a &lt;turnto action&gt; 
     * <pre>&ltturnto action&gt ::= "turnto" &ltexpression&gt &lteol&gt</pre>
     * A <code>SyntaxException</code> will be thrown if &lt;keyword&gt "turnto"
     * is present but not followed by &lt;expression&gt and &lt;eol&gt 
     * @return <code>true</code> if a &ltturnto action&gt is recognized.
     */
    
    public boolean isTurnToAction(){
    	if (!keyword("turnto")){
    		return false;
    	}
    	if (!isExpression()){
    		error("No expression after keyword");
    	}
    	if (!isEol()){
    		error("No EOL after expression");
    	}
    	return true;    	
    }
    
    /**
     * Tries to check if it is a &lt;var declaration&gt; 
     * <pre>&ltvar declaration&gt ::= "var" &ltNAME&gt { "," &ltNAME&gt } &lteol&gt</pre>
     * A <code>SyntaxException</code> will be thrown if &lt;keyword&gt "var"
     * is present but not followed by &lt;name&gt and &lt;eol&gt 
     * @return <code>true</code> if a &ltvar declaration&gt is recognized.
     */
    public boolean isVarDeclaration(){
    	if (!keyword("var")){
    		return false;
    	}
    	if (!name()){
    		error("No name followed by keyword");
    	}
    	while(symbol(",")){
    		if (!name()){
    			error("No name followed by ','");
    		}
    	}
    	if (!isEol()){
    		error("No EOL after after name");
    	}
    	return true;
    }

    /**
     * Tries to check if it is an &lt;expression&gt; 
     * <pre>&ltexpression&gt ::= &ltarithmetic expression&gt {  &ltcomparator&gt &ltarithmetic expression&gt }</pre>
     * A <code>SyntaxException</code> will be thrown if there is a
     * &lt;comparator&gt but it is not followed by &lt;arithmetic expression&gt 
     * @return <code>true</code> if an &ltexpression&gt is recognized.
     */
    
    public boolean isExpression() {
    	if (!(isArithmeticExpression())){
    		return false;
    	}
    	while (isComparator()){
    		if (!(isArithmeticExpression())){
    			error("No Arithemetic expression after comparator");
    		}
    	}
    	return true;
    }
    


    /**
     * Tries to build an &lt;expression&gt; on the global stack.
     * <pre>&lt;expression&gt; ::= &lt;term&gt; { &lt;add_operator&gt; &lt;expression&gt; }</pre>
     * A <code>SyntaxException</code> will be thrown if the add_operator
     * is present but not followed by a valid &lt;expression&gt;.
     * @return <code>true</code> if an expression is recognized.
     */
    public boolean isArithmeticExpression() {
    	boolean startsWithUnary = symbol("+") || symbol("-"); // will be used later
        if (!isTerm())
            return false;
        while (isAddOperator()) {
            if (!isTerm()) error("Error in expression after '+' or '-'");
        }
        return true;
    }

    /**
     * Tries to recognize a &lt;term&gt;.
     * <pre>&lt;term&gt; ::= &lt;factor&gt; { &lt;multiply_operator&gt; &lt;term&gt;}</pre>
     * A <code>SyntaxException</code> will be thrown if the multiply_operator
     * is present but not followed by a valid &lt;term&gt;.
     * @return <code>true</code> if a term is recognized.
     */
    public boolean isTerm() {
        if (!isFactor()) return false;
        while (isMultiplyOperator()) {
            if (!isTerm()) error("No term after '*' or '/'");
        }
        return true;
    }

    /**
     * Tries to recognize a &lt;factor&gt;.
     * <pre>&lt;factor&gt; ::= [ &lt;add operator&gt; ] &lt;unsigned factor&gt;</pre>
     * @return <code>true</code> if a factor is parsed.
     */
    public boolean isFactor() {
        if(symbol("+") || symbol("-")) {
            if (isUnsignedFactor()) {
                return true;
            }
            error("No factor following unary plus or minus");
            return false; // Can't ever get here
        }
        return isUnsignedFactor();
    }

    /**
     * Tries to recognize an &lt;unsigned factor&gt;.
     * <pre>&lt;factor&gt; ::= &lt;name&gt; "." &lt;name&gt;
     *           | &lt;name&gt; "(" &lt;parameter list&gt; ")"
     *           | &lt;name&gt;
     *           | &lt;number&gt;
     *           | "(" &lt;expression&gt; ")"</pre>
     * A <code>SyntaxException</code> will be thrown if the opening
     * parenthesis is present but not followed by a valid
     * &lt;expression&gt; and a closing parenthesis.
     * @return <code>true</code> if a factor is recognized.
     */
    public boolean isUnsignedFactor() {
        if (isVariable()) {
            if (symbol(".")) {              // reference to another Bug
                if (name()) return true;
                error("Incorrect use of dot notation");
            }
            else if (isParameterList()) return true; // function call
            else return true;                        // just a variable
        }
        if (number()) return true;
        if (symbol("(")) {
            if (!isExpression()) error("Error in parenthesized expression");
            if (!symbol(")")) error("Unclosed parenthetical expression");
            return true;
       }
       return false;
    }

    /**
     * Tries to recognize a &lt;parameter list&gt;.
     * <pre>&ltparameter list&gt; ::= "(" [ &lt;expression&gt; { "," &lt;expression&gt; } ] ")"
     * @return <code>true</code> if a parameter list is recognized.
     */
    public boolean isParameterList() {
        if (!symbol("(")) return false;
        if (isExpression()) {
            while (symbol(",")) {
                if (!isExpression()) error("No expression after ','");
            }
        }
        if (!symbol(")")) error("Parameter list doesn't end with ')'");
        return true;
    }

    /**
     * Tries to recognize an &lt;add_operator&gt;.
     * <pre>&lt;add_operator&gt; ::= "+" | "-"</pre>
     * @return <code>true</code> if an addop is recognized.
     */
    public boolean isAddOperator() {
        return symbol("+") || symbol("-");
    }

    /**
     * Tries to recognize a &lt;multiply_operator&gt;.
     * <pre>&lt;multiply_operator&gt; ::= "*" | "/"</pre>
     * @return <code>true</code> if a multiply_operator is recognized.
     */
    public boolean isMultiplyOperator() {
        return symbol("*") || symbol("/");
    }

    /**
     * Tries to recognize a &lt;variable&gt;.
     * <pre>&lt;variable&gt; ::= &lt;NAME&gt;</pre>
     * @return <code>true</code> if a variable is recognized.
     */
    public boolean isVariable() {
        return name();
    }
    
    /**
     * Tries to recognize a &lt;EOL&gt;.
     * <pre>&lt;eol&gt; ::= &lt;EOL&gt; { &lt;EOL&gt;}</pre>
     * @return <code>true</code> if a variable is EOL.
     */    
    public boolean isEol(){
    	if (!eol()){
    		return false;
    	}
    	while (eol()){	
    		}
    	return true;
    }

//----- Private "helper" methods

    /**
   * Tests whether the next token is a number. If it is, the token
   * is consumed, otherwise it is not.
   *
   * @return <code>true</code> if the next token is a number.
   */
      private boolean number() {
        return nextTokenMatches(Token.Type.NUMBER);
    }
      
      /**
       * Tests whether the next token is the end of line. If it is, the token
       * is consumed, otherwise it is not.
       *
       * @return <code>true</code> if the next token is the end of line.
       */
      private boolean eol(){
    	  return nextTokenMatches(Token.Type.EOL);
      }

    /**
     * Tests whether the next token is a name. If it is, the token
     * is consumed, otherwise it is not.
     *
     * @return <code>true</code> if the next token is a name.
     */
    private boolean name() {
        return nextTokenMatches(Token.Type.NAME);
    }

    /**
     * Tests whether the next token is the expected name. If it is, the token
     * is consumed, otherwise it is not.
     *
     * @param expectedName The String value of the expected next token.
     * @return <code>true</code> if the next token is a name with the expected value.
     */
    private boolean name(String expectedName) {
        return nextTokenMatches(Token.Type.NAME, expectedName);
    }

    /**
     * Tests whether the next token is the expected keyword. If it is, the token
     * is moved to the stack, otherwise it is not.
     *
     * @param expectedKeyword The String value of the expected next token.
     * @return <code>true</code> if the next token is a keyword with the expected value.
     */
    private boolean keyword(String expectedKeyword) {
        return nextTokenMatches(Token.Type.KEYWORD, expectedKeyword);
    }

    /**
     * Tests whether the next token is the expected symbol. If it is,
     * the token is consumed, otherwise it is not.
     *
     * @param expectedSymbol The String value of the token we expect
     *    to encounter next.
     * @return <code>true</code> if the next token is the expected symbol.
     */
    boolean symbol(String expectedSymbol) {
        return nextTokenMatches(Token.Type.SYMBOL, expectedSymbol);
    }

    /**
     * Tests whether the next token has the expected type. If it does,
     * the token is consumed, otherwise it is not. This method would
     * normally be used only when the token's value is not relevant.
     *
     * @param type The expected type of the next token.
     * @return <code>true</code> if the next token has the expected type.
     */
    boolean nextTokenMatches(Token.Type type) {
        Token t = nextToken();
        if (t.type == type) return true;
        pushBack();
        return false;
    }

    /**
     * Tests whether the next token has the expected type and value.
     * If it does, the token is consumed, otherwise it is not. This
     * method would normally be used when the token's value is
     * important.
     *
     * @param type The expected type of the next token.
     * @param value The expected value of the next token; must
     *              not be <code>null</code>.
     * @return <code>true</code> if the next token has the expected type.
     */
    boolean nextTokenMatches(Token.Type type, String value) {
        Token t = nextToken();
        if (type == t.type && value.equals(t.value)) return true;
        pushBack();
        return false;
    }

    /**
     * Returns the next Token.
     * @return The next Token.
     */
    Token nextToken() {
        int code;
        try { code = tokenizer.nextToken(); }
        catch (IOException e) { throw new Error(e); } // Should never happen
        switch (code) {
            case StreamTokenizer.TT_WORD:
                if (Token.KEYWORDS.contains(tokenizer.sval)) {
                    return new Token(Token.Type.KEYWORD, tokenizer.sval);
                }
                return new Token(Token.Type.NAME, tokenizer.sval);
            case StreamTokenizer.TT_NUMBER:
                return new Token(Token.Type.NUMBER, tokenizer.nval + "");
            case StreamTokenizer.TT_EOL:
                return new Token(Token.Type.EOL, "\n");
            case StreamTokenizer.TT_EOF:
                return new Token(Token.Type.EOF, "EOF");
            default:
                return new Token(Token.Type.SYMBOL, ((char) code) + "");
        }
    }

    /**
     * Returns the most recent Token to the tokenizer.
     */
    void pushBack() {
        tokenizer.pushBack();
    }

    /**
     * Utility routine to throw a <code>SyntaxException</code> with the
     * given message.
     * @param message The text to put in the <code>SyntaxException</code>.
     */
    private void error(String message) {
        throw new SyntaxException("Line " + lineNumber + ": " + message);
    }
}