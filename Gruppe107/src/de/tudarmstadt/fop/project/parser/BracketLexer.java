/**
 * 
 */
package de.tudarmstadt.fop.project.parser;

import de.tudarmstadt.fop.project.parser.Lexer;
import de.tudarmstadt.fop.project.parser.ParseException;
import de.tudarmstadt.fop.project.parser.Token;
import de.tudarmstadt.fop.project.parser.tokens.EofToken;
import de.tudarmstadt.fop.project.parser.tokens.LeftBracketToken;
import de.tudarmstadt.fop.project.parser.tokens.RightBracketToken;

/**
 * @author Thomas Kosiewski
 *
 */
public class BracketLexer extends Lexer 
{
	/**
	 * @param input input to be used 
	 */
	public BracketLexer(String input) 
	{
		super(input);
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.parser.Lexer#nextToken()
	 */
	@Override
	public Token nextToken() throws ParseException 
	{
		Token token = null;
		
		if (this.la == '(')
			token = new LeftBracketToken();
		else if (this.la == ')')
			token = new RightBracketToken();
		else if (this.la == Lexer.EOF)
			token = new EofToken();
		else
			throw new ParseException("Unknown token: " + this.la);
		
		this.consume();
		
		return token;
	}

}
