/**
 * 
 */
package de.tudarmstadt.fop.project.parser;

import de.tudarmstadt.fop.project.parser.Lexer;
import de.tudarmstadt.fop.project.bracket.BracketParser;
import de.tudarmstadt.fop.project.parser.ParseException;
import de.tudarmstadt.fop.project.parser.CustomBracketParser;
import de.tudarmstadt.fop.project.parser.Token;
import de.tudarmstadt.fop.project.parser.tokens.LeftBracketToken;
import de.tudarmstadt.fop.project.parser.tokens.RightBracketToken;

/**
 * @author Thomas Kosiewski
 *
 */
public class CustomBracketParser extends Parser implements BracketParser
{	
	
	/**
	 * @param lexer String to be lexed 
	 */
	public CustomBracketParser(Lexer lexer)
	{
		super(lexer);
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.bracket.BracketParser#isCorrectlyNested()
	 */
	@Override
	public boolean isCorrectlyNested() throws ParseException
	{	
		boolean correct = true;
		int counter = 0;
		Token token = null;
		do
		{
			this.consume();
			token = this.la;
			
			if (token.is(LeftBracketToken.class))
			{
				counter += 1;
			}
			else if (token.is(RightBracketToken.class))
			{
				counter -= 1;
			}
			
			else
			{
				throw new ParseException("Unexpected token " + this.la);
			}
			
			if (counter < 0 && lexer.hasNext())
				correct = false;
		} while(lexer.hasNext() && correct);
		
		return counter == 0 && correct;
	}

}
