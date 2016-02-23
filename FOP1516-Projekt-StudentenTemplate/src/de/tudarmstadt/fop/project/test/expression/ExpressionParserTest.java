/**
 * 
 */
package de.tudarmstadt.fop.project.test.expression;

import org.junit.Assert;
import org.junit.Test;

import de.tudarmstadt.fop.project.Factory;
import de.tudarmstadt.fop.project.expression.Expression;
import de.tudarmstadt.fop.project.expression.ExpressionParser;
import de.tudarmstadt.fop.project.parser.Lexer;
import de.tudarmstadt.fop.project.parser.ParseException;
import de.tudarmstadt.fop.project.test.InstanceGenerator;

/**
 * @author Thomas Kosiewski
 * @author Veronika Kaletta 
 */
public class ExpressionParserTest extends TemplateExpressionParserTest
{

	@Test
	public void templateTestSimpleFunctionApplication2() throws ParseException{
		String input = "(f (a \"$%&/() FoP\" 2.5) y)";

		Factory factory = InstanceGenerator.instantiateFactory();
		Lexer lexer = factory.instantiateExpressionLexer(input);
		ExpressionParser parser = factory.instantiateExpressionParser(lexer);

		Expression expression = parser.getExpression();
	
		Assert.assertEquals("Expression [operator=f, operands=[ExpressionOperand(Expression [operator=a, operands=[StringOperand($%&/() FoP), DecimalOperand(2.5)]]), IdentifierOperand(y)]]",
				expression.toString());
	}

	@Test
	public void templateTestBasicSchemeParser2() throws ParseException{
		String input = "(foo \"abc\" 1.23)";

		Factory factory = InstanceGenerator.instantiateFactory();
		Lexer lexer = factory.instantiateExpressionLexer(input);
		ExpressionParser parser = factory.instantiateExpressionParser(lexer);

		Expression expression = parser.getExpression();

		Assert.assertEquals("Expression [operator=foo, operands=[StringOperand(abc), DecimalOperand(1.23)]]",
				expression.toString());
	}

}
