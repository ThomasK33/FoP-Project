/**
 * 
 */
package de.tudarmstadt.fop.project.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.tudarmstadt.fop.project.test.arithmetic.ArithmeticLexerTest;
import de.tudarmstadt.fop.project.test.arithmetic.ArithmeticParserTest;
import de.tudarmstadt.fop.project.test.arithmetic.DecimalArithmeticLexerTest;
import de.tudarmstadt.fop.project.test.arithmetic.DecimalArithmeticParserTest;
import de.tudarmstadt.fop.project.test.bracket.BracketLexerTest;
import de.tudarmstadt.fop.project.test.bracket.BracketParserTest;
import de.tudarmstadt.fop.project.test.bracket.BracketWithWsTest;
import de.tudarmstadt.fop.project.test.expression.ExpressionLexerTest;
import de.tudarmstadt.fop.project.test.expression.ExpressionParserTest;
import de.tudarmstadt.fop.project.test.soccer.parser.SoccerParserTest;

/**
 * @author Thomas Kosiewski
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
	   BracketLexerTest.class,
	   BracketParserTest.class,
	   BracketWithWsTest.class,
	   ArithmeticLexerTest.class,
	   ArithmeticParserTest.class,
	   DecimalArithmeticLexerTest.class,
	   DecimalArithmeticParserTest.class,
	   ExpressionLexerTest.class,
	   ExpressionParserTest.class,
	   SoccerParserTest.class,})
public class CustomTestAll
{

}
