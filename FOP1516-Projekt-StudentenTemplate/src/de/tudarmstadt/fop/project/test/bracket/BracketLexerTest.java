/**
 * 
 */
package de.tudarmstadt.fop.project.test.bracket;

import org.junit.Assert;
import org.junit.Test;

import de.tudarmstadt.fop.project.Factory;
import de.tudarmstadt.fop.project.parser.Lexer;
import de.tudarmstadt.fop.project.parser.ParseException;
import de.tudarmstadt.fop.project.parser.tokens.EofToken;
import de.tudarmstadt.fop.project.parser.tokens.LeftBracketToken;
import de.tudarmstadt.fop.project.parser.tokens.RightBracketToken;
import de.tudarmstadt.fop.project.test.InstanceGenerator;

/**
 * @author Thomas Kosiewski
 *
 */
public class BracketLexerTest
{
	@Test
	public void templateTestLexerValidBracketsOnly() throws ParseException{
		String input = "()()";
		Factory factory = InstanceGenerator.instantiateFactory();
		Lexer lexer = factory.instantiateBracketLexer(input);

		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new LeftBracketToken(), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new RightBracketToken(), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new LeftBracketToken(), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new RightBracketToken(), lexer.nextToken());

		Assert.assertFalse(lexer.hasNext());
		
		Assert.assertEquals(new EofToken(), lexer.nextToken());
		Assert.assertEquals(new EofToken(), lexer.nextToken());
	}

	@Test(expected=ParseException.class)
	public void templateTestLexerInvalidLetter() throws ParseException{
		String input = "()$()";
		Factory factory = InstanceGenerator.instantiateFactory();
		Lexer lexer = factory.instantiateBracketLexer(input);

		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new LeftBracketToken(), lexer.nextToken());
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new RightBracketToken(), lexer.nextToken());

		// should throw exception
		lexer.nextToken();
	}	

}