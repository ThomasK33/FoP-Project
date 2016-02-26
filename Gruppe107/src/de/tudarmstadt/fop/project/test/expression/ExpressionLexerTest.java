/**
 * 
 */
package de.tudarmstadt.fop.project.test.expression;

import org.junit.Assert;
import org.junit.Test;

import de.tudarmstadt.fop.project.Factory;
import de.tudarmstadt.fop.project.parser.Lexer;
import de.tudarmstadt.fop.project.parser.ParseException;
import de.tudarmstadt.fop.project.parser.tokens.DecimalToken;
import de.tudarmstadt.fop.project.parser.tokens.IdentifierToken;
import de.tudarmstadt.fop.project.parser.tokens.LeftBracketToken;
import de.tudarmstadt.fop.project.parser.tokens.RightBracketToken;
import de.tudarmstadt.fop.project.parser.tokens.StringToken;
import de.tudarmstadt.fop.project.test.InstanceGenerator;

/**
 * @author Thomas Kosiewski
 * @author Veronika Kaletta
 */
public class ExpressionLexerTest extends TemplateExpressionLexerTest
{

	@Test
	public void templateTestBasicSchemeLexer2() throws ParseException{
		String input = "(qwertz \"asdf\" 13.37 (h4x0r l33t))";
		Factory factory = InstanceGenerator.instantiateFactory();
		Lexer lexer = factory.instantiateExpressionLexer(input);

		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new LeftBracketToken(), lexer.nextToken());
		
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new IdentifierToken("qwertz"), lexer.nextToken());
		
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new StringToken("asdf"), lexer.nextToken());
		
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new DecimalToken("13.37"), lexer.nextToken());
		
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new LeftBracketToken(), lexer.nextToken());
		
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new IdentifierToken("h4x0r"), lexer.nextToken());
		
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new IdentifierToken("l33t"), lexer.nextToken());
		
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new RightBracketToken(), lexer.nextToken());
		
		Assert.assertTrue(lexer.hasNext());
		Assert.assertEquals(new RightBracketToken(), lexer.nextToken());
		
		Assert.assertFalse(lexer.hasNext());
	}

}
