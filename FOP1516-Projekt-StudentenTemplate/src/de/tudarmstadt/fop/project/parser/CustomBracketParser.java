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
	// TODO: docs
	/**
	 * @param lexer
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
			if (token.is(RightBracketToken.class))
			{
				counter -= 1;
			}
			
			if (counter <= 0 && lexer.hasNext())
			{
				throw new ParseException("Unexpected token " + this.la);
			}
		} while(lexer.hasNext());
		
		return true;
	}

}
