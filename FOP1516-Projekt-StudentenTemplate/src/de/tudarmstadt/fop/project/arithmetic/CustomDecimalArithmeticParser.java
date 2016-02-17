/**
 * 
 */
package de.tudarmstadt.fop.project.arithmetic;

import de.tudarmstadt.fop.project.parser.Lexer;
import de.tudarmstadt.fop.project.parser.ParseException;
import de.tudarmstadt.fop.project.parser.Parser;
import de.tudarmstadt.fop.project.parser.tokens.ArithmeticOperatorToken;
import de.tudarmstadt.fop.project.parser.tokens.DecimalToken;
import de.tudarmstadt.fop.project.parser.tokens.IntegerToken;
import de.tudarmstadt.fop.project.parser.tokens.LeftBracketToken;
import de.tudarmstadt.fop.project.parser.tokens.RightBracketToken;

/**
 * @author Thomas Kosiewski
 *
 */
public class CustomDecimalArithmeticParser extends Parser implements ArithmeticParser
{

	/**
	 * @param lexer
	 */
	public CustomDecimalArithmeticParser(Lexer lexer)
	{
		super(lexer);
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.arithmetic.ArithmeticParser#getExpression()
	 */
	@Override
	public ArithmeticExpression getExpression() throws ParseException
	{
		ArithmeticOperatorToken operator = null;
		ArithmeticOperand left = null;
		ArithmeticOperand right = null;

		if (this.la == null)
			this.consume();

		if (this.la.is(LeftBracketToken.class))
		{
			this.consume();
			if (this.la.is(ArithmeticOperatorToken.class))
			{
				operator = (ArithmeticOperatorToken) this.la;

				this.consume();

				if (this.la.is(LeftBracketToken.class))
				{
					left = new CustomCompoundArithmeticOperand(this.getExpression());
				}

				if (this.la.is(DecimalToken.class))
				{
					left = new CustomDecimalArithmeticOperand((DecimalToken)this.la);
				}

				this.consume();

				if (this.la.is(LeftBracketToken.class))
				{
					right = new CustomCompoundArithmeticOperand(this.getExpression());
				}

				if (this.la.is(DecimalToken.class))
				{
					right = new CustomDecimalArithmeticOperand((DecimalToken) this.la);
				}

				this.consume();

				if (this.la.isNot(RightBracketToken.class))
				{
					throw new ParseException("Expected token )");
				}
			}
		}

		return new ArithmeticExpression(operator, left, right);
	}

}
