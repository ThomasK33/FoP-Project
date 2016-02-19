/**
 * 
 */
package de.tudarmstadt.fop.project.test.arithmetic;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import de.tudarmstadt.fop.project.Factory;
import de.tudarmstadt.fop.project.arithmetic.ArithmeticParser;
import de.tudarmstadt.fop.project.parser.Lexer;
import de.tudarmstadt.fop.project.parser.ParseException;
import de.tudarmstadt.fop.project.test.InstanceGenerator;

/**
 * @author Thomas Kosiewski
 *
 */
public class DecimalArithmeticParserTest extends TemplateDecimalArithmeticParserTest
{

	@Test
	public void templateTestSimpleExpressionNegativeNumber2() throws ParseException{
		String input = "(* -10 -2)";
		
		Factory factory = InstanceGenerator.instantiateFactory();
		Lexer lexer = factory.instantiateDecimalArithmeticLexer(input);
		ArithmeticParser parser = factory.instantiateDecimalArithmeticParser(lexer);
		
		Assert.assertEquals(new BigDecimal(20), parser.getExpression().evaluate());
	}
	
	@Test
	public void templateTestSimpleExpressionNegativeAndFloatingPoint2() throws ParseException{
		String input = "(- -5 (* 20.5  -1))";
		Factory factory = InstanceGenerator.instantiateFactory();
		Lexer lexer = factory.instantiateDecimalArithmeticLexer(input);
		ArithmeticParser parser = factory.instantiateDecimalArithmeticParser(lexer);
		
		Assert.assertEquals(new BigDecimal(15.5), parser.getExpression().evaluate());
	}

}
