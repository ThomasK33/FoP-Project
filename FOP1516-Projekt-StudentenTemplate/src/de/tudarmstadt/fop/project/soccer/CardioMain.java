/**
 * 
 */
package de.tudarmstadt.fop.project.soccer;

import java.io.IOException;

import de.tudarmstadt.fop.project.soccer.entities.team.Player;
import de.tudarmstadt.fop.project.soccer.entities.team.SinglePlayerTeam;

/**
 * @author Thomas Kosiewski
 *
 */
public class CardioMain extends SinglePlayerTeam
{

	/**
	 * @param name
	 */
	public CardioMain(String name)
	{
		super(name);
	}

	/**
	 * @param name
	 * @param hostname
	 * @param playerPort
	 * @param coachPort
	 */
	public CardioMain(String name, String hostname, int playerPort, int coachPort)
	{
		super(name, hostname, playerPort, coachPort);
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.soccer.entities.team.SinglePlayerTeam#initPlayer(java.lang.String, int)
	 */
	@Override
	protected Player initPlayer(String hostname, int port)
	{
		CardioPlayer cp = new CardioPlayer(this.name, 1, hostname, port);
		return cp;
	}
	
	public static void main(String[] args)
	{
		CardioMain cm = new CardioMain("Test");
		
		try
		{
			cm.connect();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
