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
	 * @param name the name of the team 
	 * 
	 */
	public CardioMain(String name)
	{
		super(name);
	}

	/**
	 * @param name the name of the team 
	 * @param hostname the name of the host 
	 * @param playerPort the player port 
	 * @param coachPort the coach port 
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
		return new CardioPlayer(this.name, 1, hostname, port);
	}
	
	public static void main(String[] args)
	{
		CardioMain cm = new CardioMain("Test");
		
		try
		{
			cm.connect();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
