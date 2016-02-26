/**
 * 
 */
package de.tudarmstadt.fop.project.arithmetic;

import de.tudarmstadt.fop.project.parser.BracketWithWsLexer;
import de.tudarmstadt.fop.project.parser.ParseException;
import de.tudarmstadt.fop.project.parser.Token;
import de.tudarmstadt.fop.project.parser.tokens.ArithmeticOperatorToken;
import de.tudarmstadt.fop.project.parser.tokens.IntegerToken;

/**
 * @author Thomas Kosiewski
 * @author Veronika Kaletta
 */
public class ArithmeticLexer extends BracketWithWsLexer
{

	/**
	 * @param input to be used 
	 */
	public ArithmeticLexer(String input)
	{
		super(input);
	}
	
	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.parser.BracketLexer#nextToken()
	 */
	@Override
	public Token nextToken() throws ParseException
	{
		Token token = null;
		
		while (this.la == ' ' || this.la == '\t' || this.la == '\r' || this.la == '\n')
			this.consume();
		
		if (this.la == '+' || this.la == '-' || this.la == '*' || this.la == '/')
		{
			ArithmeticOperatorToken.Type type = null;
			
			for (ArithmeticOperatorToken.Type t: ArithmeticOperatorToken.Type.values())
				if (t.getSymbol().equals("" + this.la))
					type = t;
			
			token = new ArithmeticOperatorToken(type);
			
			this.consume();
		}
		else if (this.la >= '0' && this.la <= '9')
		{
			String num = "";
			while (this.la >= '0' && this.la <= '9')
			{
				num += this.la;
				this.consume();
			}
			token = new IntegerToken(num);
		}
		else
			token = super.nextToken();

		return token;
	}

}
