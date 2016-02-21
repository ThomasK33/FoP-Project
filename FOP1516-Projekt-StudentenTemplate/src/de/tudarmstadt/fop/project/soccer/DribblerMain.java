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
public class DribblerMain extends SinglePlayerTeam
{

	/**
	 * @param name
	 */
	public DribblerMain(String name)
	{
		super(name);
	}

	/**
	 * @param name
	 * @param hostname
	 * @param playerPort
	 * @param coachPort
	 */
	public DribblerMain(String name, String hostname, int playerPort, int coachPort)
	{
		super(name, hostname, playerPort, coachPort);
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.soccer.entities.team.SinglePlayerTeam#initPlayer(java.lang.String, int)
	 */
	@Override
	protected Player initPlayer(String hostname, int port)
	{
		return new Dribbler(this.getName(), 1, hostname, port);
	}
	
	public static void main(String[] args)
	{
		DribblerMain dm = new DribblerMain("Test");
		
		try
		{
			dm.connect();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
