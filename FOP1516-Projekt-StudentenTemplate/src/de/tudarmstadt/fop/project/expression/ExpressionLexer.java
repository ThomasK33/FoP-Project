/**
 * 
 */
package de.tudarmstadt.fop.project.expression;

import de.tudarmstadt.fop.project.arithmetic.DecimalArithmeticLexer;
import de.tudarmstadt.fop.project.parser.ParseException;
import de.tudarmstadt.fop.project.parser.Token;
import de.tudarmstadt.fop.project.parser.tokens.IdentifierToken;
import de.tudarmstadt.fop.project.parser.tokens.StringToken;

/**
 * @author Thomas Kosiewski
 *
 */
public class ExpressionLexer extends DecimalArithmeticLexer
{
	/**
	 * @param input input to be used 
	 */
	public ExpressionLexer(String input)
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


		// See Ascii table
		if (this.la >= 'A' && this.la <= 'z')
		{
			String string = "" + this.la;

			this.consume();

			while (((this.la >= 'A' && this.la <= 'z') || (this.la >= '0' || this.la <= '9') || this.la == '_') && 
					this.la != ' ' && 
					this.la != ')'
					)
			{
				string += this.la;
				this.consume();
			}

			token = new IdentifierToken(string);
		}
		else if (this.la == '"')
		{
			String string = "";
			
			this.consume();
			
			// while (this.la >= 32 && this.la <= 126) 
			while (this.la != '"' && (this.la >= ' ' && this.la <= '~'))
			{
				string += this.la;
				this.consume();
			}
			
			if (this.la == '"')
				this.consume();
			
			token = new StringToken(string);
		}
		else
			token = super.nextToken();

		return token;
	}
}
