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
import de.tudarmstadt.fop.project.parser.tokens.IntegerToken;
import de.tudarmstadt.fop.project.parser.tokens.LeftBracketToken;
import de.tudarmstadt.fop.project.parser.tokens.RightBracketToken;
import de.tudarmstadt.fop.project.parser.tokens.ArithmeticOperatorToken.Type;
import de.tudarmstadt.fop.project.test.InstanceGenerator;

/**
 * @author Thomas Kosiewski
 * @author Veronika Kaletta
 */
public class ArithmeticLexerTest extends TemplateIntegerArithmeticLexerTest
{

	@Test
	public void templateTestLexerValidSimpleAddition2() throws ParseException{
		String input = "16 + 3";
		Factory factory = InstanceGenerator.instantiateFactory();
		Lexer lexer = factory.instantiateIntegerArithmeticLexer(input);

		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new IntegerToken(16), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new ArithmeticOperatorToken(Type.PLUS), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new IntegerToken(3), lexer.nextToken());
		
		Assert.assertFalse(lexer.hasNext());
	}

	@Test
	public void templateTestLexerValidPreOrderComplexExpression2() throws ParseException{
		String input = "(- (/ 90 3) 7)";
		Factory factory = InstanceGenerator.instantiateFactory();
		Lexer lexer = factory.instantiateIntegerArithmeticLexer(input);

		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new LeftBracketToken(), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new ArithmeticOperatorToken(Type.MINUS), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new LeftBracketToken(), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new ArithmeticOperatorToken(Type.DIVISION), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new IntegerToken(90), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new IntegerToken(3), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new RightBracketToken(), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new IntegerToken(7), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new RightBracketToken(), lexer.nextToken());
		
		Assert.assertFalse(lexer.hasNext());
	}

}
