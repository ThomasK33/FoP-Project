/**
 * 
 */
package de.tudarmstadt.fop.project.test.arithmetic;

import org.junit.Assert;
import org.junit.Test;

import de.tudarmstadt.fop.project.Factory;
import de.tudarmstadt.fop.project.parser.Lexer;
import de.tudarmstadt.fop.project.parser.ParseException;
import de.tudarmstadt.fop.project.parser.tokens.ArithmeticOperatorToken;
import de.tudarmstadt.fop.project.parser.tokens.DecimalToken;
import de.tudarmstadt.fop.project.parser.tokens.LeftBracketToken;
import de.tudarmstadt.fop.project.parser.tokens.RightBracketToken;
import de.tudarmstadt.fop.project.parser.tokens.ArithmeticOperatorToken.Type;
import de.tudarmstadt.fop.project.test.InstanceGenerator;

/**
 * @author Thomas Kosiewski
 * @author Veronika Kaletta
 */
public class DecimalArithmeticLexerTest extends TemplateDecimalArithmeticLexerTest
{

	@Test
	public void templateTestLexerValidSimpleMultiply2() throws ParseException{
		String input = "(5.5 * 2)";
		Factory factory = InstanceGenerator.instantiateFactory();
		Lexer lexer = factory.instantiateDecimalArithmeticLexer(input);
		
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new LeftBracketToken(), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new DecimalToken("5.5"), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new ArithmeticOperatorToken(Type.TIMES), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new DecimalToken("2"), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new RightBracketToken(), lexer.nextToken());
		
		Assert.assertFalse(lexer.hasNext());
	}
	
	@Test
	public void templateTestLexerValidSimpleSubtraktion2() throws ParseException{
		String input = "-10 - 12";
		Factory factory = InstanceGenerator.instantiateFactory();
		Lexer lexer = factory.instantiateDecimalArithmeticLexer(input);
		
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new DecimalToken("-10"), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new ArithmeticOperatorToken(Type.MINUS), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new DecimalToken(12), lexer.nextToken());
		
		Assert.assertFalse(lexer.hasNext());
	}

}
