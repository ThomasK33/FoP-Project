/**
 * 
 */
package de.tudarmstadt.fop.project.arithmetic;

import de.tudarmstadt.fop.project.parser.Lexer;
import de.tudarmstadt.fop.project.parser.LookaheadLexer;
import de.tudarmstadt.fop.project.parser.ParseException;
import de.tudarmstadt.fop.project.parser.Token;
import de.tudarmstadt.fop.project.parser.tokens.ArithmeticOperatorToken;
import de.tudarmstadt.fop.project.parser.tokens.DecimalToken;
import de.tudarmstadt.fop.project.parser.tokens.IntegerToken;

/**
 * @author Thomas Kosiewski
 *
 */
public class DecimalArithmeticLexer extends ArithmeticLexer implements LookaheadLexer
{

	// TODO: docs
	/**
	 * @param input
	 */
	public DecimalArithmeticLexer(String input)
	{
		super(input);
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.parser.LookaheadLexer#lookahead(int)
	 */
	@Override
	public Character lookahead(int characters)
	{
		if ((pos + characters) < this.input.length())
			return this.input.charAt(pos + characters);
		else
			return '\0';
	}
	
	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.arithmetic.ArithmeticLexer#nextToken()
	 */
	@Override
	public Token nextToken() throws ParseException
	{
		Token token = null;
		
		while (this.la == ' ' || this.la == '\t' || this.la == '\r' || this.la == '\n')
			this.consume();
		
		if (this.la == '-' && (this.lookahead(1) >= '0' || this.lookahead(1) <= '9'))
		{
			String num = "-";
			this.consume();
			while ((this.la >= '0' && this.la <= '9') || (this.la == '.' && (this.lookahead(1) >= '0' || this.lookahead(1) <= '9')))
			{
				num += this.la;
				this.consume();
			}
			
			if (num.equals("-"))
				token = new ArithmeticOperatorToken(ArithmeticOperatorToken.Type.MINUS);
			else
				token = new DecimalToken(num);
		}
		else if (this.la >= '0' && this.la <= '9')
		{
			String num = "";
			while ((this.la >= '0' && this.la <= '9') || (this.la == '.' && (this.lookahead(1) >= '0' || this.lookahead(1) <= '9')))
			{
				num += this.la;
				this.consume();
			}
			token = new DecimalToken(num);
		}
		else
			token = super.nextToken();
		
		return token;
	}

}
