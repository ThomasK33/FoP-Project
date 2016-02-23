/**
 * 
 */
package de.tudarmstadt.fop.project.arithmetic;

import java.math.BigDecimal;

import de.tudarmstadt.fop.project.arithmetic.ArithmeticOperand;
import de.tudarmstadt.fop.project.parser.tokens.IntegerToken;

/**
 * @author Thomas Kosiewski
 *
 */
public class CustomArithmeticOperand implements ArithmeticOperand
{
	private IntegerToken token;

	// TODO: docs
	/**
	 * @param token 
	 */
	public CustomArithmeticOperand(IntegerToken token)
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
