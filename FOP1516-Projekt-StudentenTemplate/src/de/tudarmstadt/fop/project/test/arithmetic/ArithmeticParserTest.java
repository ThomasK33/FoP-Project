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
public class ArithmeticParserTest
{

	@Test
	public void templateTestMultiplication() throws ParseException{
		String input = "(* 10 4)";
		
		Factory factory = InstanceGenerator.instantiateFactory();
		Lexer lexer = factory.instantiateIntegerArithmeticLexer(input);
		ArithmeticParser parser = factory.instantiateIntegerArithmeticParser(lexer);
		
		Assert.assertEquals(new BigDecimal(40), parser.getExpression().evaluate());
	}
	
	@Test
	public void templateTestSimpleExpression() throws ParseException{
		String input = "(+ 1 (* 10 4))";

		Factory factory = InstanceGenerator.instantiateFactory();
		Lexer lexer = factory.instantiateIntegerArithmeticLexer(input);
		ArithmeticParser parser = factory.instantiateIntegerArithmeticParser(lexer);
		
		Assert.assertEquals(new BigDecimal(41), parser.getExpression().evaluate());
	}

}
