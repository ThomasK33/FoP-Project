/**
 * 
 */
package de.tudarmstadt.fop.project.arithmetic;

import java.math.BigDecimal;

import de.tudarmstadt.fop.project.parser.tokens.DecimalToken;

/**
 * @author Thomas Kosiewski
 *
 */
public class CustomDecimalArithmeticOperand implements ArithmeticOperand
{
	private DecimalToken token;

	/**
	 * @param token to be used
	 */
	public CustomDecimalArithmeticOperand(DecimalToken token)
	{
		this.token = token;
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.arithmetic.ArithmeticOperand#getValue()
	 */
	@Override
	public BigDecimal getValue()
	{
		if (token != null)
			return new BigDecimal(token.getText());
		else
			return null;
	}

}
