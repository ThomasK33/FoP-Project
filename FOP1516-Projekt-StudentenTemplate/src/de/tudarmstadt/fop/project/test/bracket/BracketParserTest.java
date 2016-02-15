/**
 * 
 */
package de.tudarmstadt.fop.project.test.bracket;

import org.junit.Assert;
import org.junit.Test;

import de.tudarmstadt.fop.project.Factory;
import de.tudarmstadt.fop.project.bracket.BracketParser;
import de.tudarmstadt.fop.project.parser.Lexer;
import de.tudarmstadt.fop.project.parser.ParseException;
import de.tudarmstadt.fop.project.test.InstanceGenerator;

/**
 * @author Thomas Kosiewski
 *
 */
public class BracketParserTest
{

	@Test
	public void templateTestBracketParseValidBracketExpression() throws ParseException{
		String input = "((()))";

		Factory factory = InstanceGenerator.instantiateFactory();
		Lexer lexer = factory.instantiateBracketLexer(input);
		BracketParser parser = factory.instantiateBracketParser(lexer);
		
		Assert.assertTrue(parser.isCorrectlyNested());
	}
	
	@Test(expected=ParseException.class)
	public void templateTestBracketParseInvalidBracketNumber() throws ParseException{
		String input = "((())))))";

		Factory factory = InstanceGenerator.instantiateFactory();
		Lexer lexer = factory.instantiateBracketLexer(input);
		BracketParser parser = factory.instantiateBracketParser(lexer);

		Assert.assertFalse(parser.isCorrectlyNested());
	}
	
	@Test(expected=ParseException.class)
	public void templateTestBracketParseInvalidNotOneExpression() throws ParseException{
		String input = "(())()";

		Factory factory = InstanceGenerator.instantiateFactory();
		Lexer lexer = factory.instantiateBracketLexer(input);
		BracketParser parser = factory.instantiateBracketParser(lexer);
		
		Assert.assertTrue(parser.isCorrectlyNested());
	}

}
