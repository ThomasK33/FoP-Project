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
 *
 */
public class ArithmeticLexerTest
{

	@Test
	public void templateTestLexerValidSimpleAddition() throws ParseException{
		String input = "2 +   3";
		Factory factory = InstanceGenerator.instantiateFactory();
		Lexer lexer = factory.instantiateIntegerArithmeticLexer(input);

		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new IntegerToken(2), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new ArithmeticOperatorToken(Type.PLUS), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new IntegerToken(3), lexer.nextToken());
		
		Assert.assertFalse(lexer.hasNext());
	}

	@Test
	public void templateTestLexerValidPreOrderComplexExpression() throws ParseException{
		String input = "(- (/ 50 2) 7)";
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
		Assert.assertEquals(new IntegerToken(50), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new IntegerToken(2), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new RightBracketToken(), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new IntegerToken(7), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new RightBracketToken(), lexer.nextToken());
		
		Assert.assertFalse(lexer.hasNext());
	}

}
