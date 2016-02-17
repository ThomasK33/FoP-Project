/**
 * 
 */
package de.tudarmstadt.fop.project.expression;

import de.tudarmstadt.fop.project.arithmetic.CustomDecimalArithmeticOperand;
import de.tudarmstadt.fop.project.parser.Lexer;
import de.tudarmstadt.fop.project.parser.ParseException;
import de.tudarmstadt.fop.project.parser.Parser;
import de.tudarmstadt.fop.project.parser.tokens.DecimalToken;
import de.tudarmstadt.fop.project.parser.tokens.IdentifierToken;
import de.tudarmstadt.fop.project.parser.tokens.LeftBracketToken;
import de.tudarmstadt.fop.project.parser.tokens.RightBracketToken;
import de.tudarmstadt.fop.project.parser.tokens.StringToken;

/**
 * @author Thomas Kosiewski
 *
 */
public class CustomExpressionParser extends Parser implements ExpressionParser
{

	// TODO: docs
	/**
	 * @param lexer
	 */
	public CustomExpressionParser(Lexer lexer)
	{
		super(lexer);
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.expression.ExpressionParser#getExpression()
	 */
	@Override
	public Expression getExpression() throws ParseException
	{
		Expression expr = null;

		if (this.la == null)
			this.consume();

		if (this.la.is(LeftBracketToken.class))
		{
			this.consume();

			if (this.la.is(IdentifierToken.class))
				expr = new Expression((IdentifierToken) this.la);
			else if (this.la.is(LeftBracketToken.class))
				expr = new Expression(this.getExpression());
			
			if (expr == null)
				throw new ParseException("Unexpected token. Expected either IdentifierToken or new expression but found this instead " + this.la.toString());
			
			this.consume();
			
			// TODO: check this, seems very dirty too me. at least it works
			
			while (this.lexer.hasNext() && this.la.isNot(RightBracketToken.class))
			{
				
				
				// PrimitiveOperand
				if (this.la.is(DecimalToken.class))
//					expr.addOperand(new PrimitiveOperand<DecimalToken>(new DecimalToken(this.la.getText())){});
					expr.addOperand(new DecimalOperand((DecimalToken) this.la));
				else if (this.la.is(IdentifierToken.class))
//					expr.addOperand(new PrimitiveOperand<IdentifierToken>(new IdentifierToken(this.la.getText())){});
					expr.addOperand(new IdentifierOperand((IdentifierToken) this.la));
				else if (this.la.is(StringToken.class))
//					expr.addOperand(new PrimitiveOperand<StringToken>(new StringToken(this.la.getText())){});
					expr.addOperand(new StringOperand((StringToken) this.la));
				
				// expressionOperand
				else if (this.la.is(LeftBracketToken.class))
					expr.addOperand(new ExpressionOperand(this.getExpression()));
				
				// Not acceptable operand 
				else
					throw new ParseException("Unexpected token " + this.la.toString());
				
				
				this.consume();
			}
			
			
			if (this.la.isNot(RightBracketToken.class))
				throw new ParseException("Expected expression to be finished, instead found this " + this.la.toString());
			
		}

		return expr;
	}

}
