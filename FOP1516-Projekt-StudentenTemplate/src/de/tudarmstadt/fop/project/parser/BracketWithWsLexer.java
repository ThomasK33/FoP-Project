/**
 * 
 */
package de.tudarmstadt.fop.project.parser;

/**
 * @author Thomas Kosiewski
 *
 */
public class BracketWithWsLexer extends BracketLexer
{

	//	TODO: docs
	/**
	 * @param input
	 */
	public BracketWithWsLexer(String input)
	{
		super(input);
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.parser.BracketLexer#nextToken()
	 */
	@Override
	public Token nextToken() throws ParseException
	{
		while (this.la == ' ' || this.la == '\t' || this.la == '\r' || this.la == '\n')
			this.consume();
		
		Token token = super.nextToken();
		
		while (this.la == ' ' || this.la == '\t' || this.la == '\r' || this.la == '\n')
			this.consume();

		return token;
	}
}
