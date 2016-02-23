/**
 * 
 */
package de.tudarmstadt.fop.project.arithmetic;

import java.math.BigDecimal;

import de.tudarmstadt.fop.project.arithmetic.ArithmeticExpression;
import de.tudarmstadt.fop.project.arithmetic.ArithmeticOperand;

/**
 * @author Thomas Kosiewski
 *
 */
public class CustomCompoundArithmeticOperand implements ArithmeticOperand
{
	ArithmeticExpression expression;

	// TODO: docs
	/**
	 * @param expression 
	 */
	public CustomCompoundArithmeticOperand(ArithmeticExpression expression)
	{
		this.expression = expression;
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.arithmetic.ArithmeticOperand#getValue()
	 */
	@Override
	public BigDecimal getValue()
	{
		if (this.expression != null)
			return this.expression.evaluate();
		else
			return null;
	}

}
