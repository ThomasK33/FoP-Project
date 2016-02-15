/**
 * 
 */
package de.tudarmstadt.fop.project;

import de.tudarmstadt.fop.project.arithmetic.ArithmeticExpression;
import de.tudarmstadt.fop.project.arithmetic.ArithmeticLexer;
import de.tudarmstadt.fop.project.arithmetic.ArithmeticOperand;
import de.tudarmstadt.fop.project.arithmetic.ArithmeticParser;
import de.tudarmstadt.fop.project.arithmetic.CustomArithmeticOperand;
import de.tudarmstadt.fop.project.arithmetic.CustomArithmeticParser;
import de.tudarmstadt.fop.project.arithmetic.CustomCompoundArithmeticOperand;
import de.tudarmstadt.fop.project.bracket.BracketParser;
import de.tudarmstadt.fop.project.expression.ExpressionParser;
import de.tudarmstadt.fop.project.parser.BracketLexer;
import de.tudarmstadt.fop.project.parser.BracketWithWsLexer;
import de.tudarmstadt.fop.project.parser.CustomBracketParser;
import de.tudarmstadt.fop.project.parser.Lexer;
import de.tudarmstadt.fop.project.parser.tokens.DecimalToken;
import de.tudarmstadt.fop.project.parser.tokens.IntegerToken;
import de.tudarmstadt.fop.project.soccer.parser.SoccerParser;

/**
 * @author Thomas Kosiewski
 *
 */
public class FactoryIM implements Factory
{

	/**
	 * 
	 */
	public FactoryIM()
	{
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.Factory#instantiateBracketLexer(java.lang.String)
	 */
	@Override
	public Lexer instantiateBracketLexer(String input)
	{
		return new BracketLexer(input);
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.Factory#instantiateBracketParser(de.tudarmstadt.fop.project.parser.Lexer)
	 */
	@Override
	public BracketParser instantiateBracketParser(Lexer lexer)
	{
		return new CustomBracketParser(lexer);
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.Factory#instantiateBracketWithWsLexer(java.lang.String)
	 */
	@Override
	public Lexer instantiateBracketWithWsLexer(String input)
	{
		return new BracketWithWsLexer(input);
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.Factory#instantiateIntegerArithmeticLexer(java.lang.String)
	 */
	@Override
	public Lexer instantiateIntegerArithmeticLexer(String input)
	{
		return new ArithmeticLexer(input);
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.Factory#instantiateIntegerOperand(de.tudarmstadt.fop.project.parser.tokens.IntegerToken)
	 */
	@Override
	public ArithmeticOperand instantiateIntegerOperand(IntegerToken token)
	{
		return new CustomArithmeticOperand(token);
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.Factory#instantiateCompositeOperand(de.tudarmstadt.fop.project.arithmetic.ArithmeticExpression)
	 */
	@Override
	public ArithmeticOperand instantiateCompositeOperand(ArithmeticExpression expr)
	{
		return new CustomCompoundArithmeticOperand(expr);
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.Factory#instantiateIntegerArithmeticParser(de.tudarmstadt.fop.project.parser.Lexer)
	 */
	@Override
	public ArithmeticParser instantiateIntegerArithmeticParser(Lexer lexer)
	{
		return new CustomArithmeticParser(lexer);
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.Factory#instantiateDecimalOperand(de.tudarmstadt.fop.project.parser.tokens.DecimalToken)
	 */
	@Override
	public ArithmeticOperand instantiateDecimalOperand(DecimalToken token)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.Factory#instantiateDecimalArithmeticLexer(java.lang.String)
	 */
	@Override
	public Lexer instantiateDecimalArithmeticLexer(String input)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.Factory#instantiateDecimalArithmeticParser(de.tudarmstadt.fop.project.parser.Lexer)
	 */
	@Override
	public ArithmeticParser instantiateDecimalArithmeticParser(Lexer lexer)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.Factory#instantiateExpressionLexer(java.lang.String)
	 */
	@Override
	public Lexer instantiateExpressionLexer(String input)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.Factory#instantiateExpressionParser(de.tudarmstadt.fop.project.parser.Lexer)
	 */
	@Override
	public ExpressionParser instantiateExpressionParser(Lexer lexer)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.Factory#instantiateSoccerParser(de.tudarmstadt.fop.project.parser.Lexer)
	 */
	@Override
	public SoccerParser instantiateSoccerParser(Lexer lexer)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
