/**
 * 
 */
package de.tudarmstadt.fop.project.soccer;

import java.io.IOException;

import de.tudarmstadt.fop.project.soccer.controller.PassControllerConfig;
import de.tudarmstadt.fop.project.soccer.entities.team.Player;
import de.tudarmstadt.fop.project.soccer.entities.team.TwoPlayerTeam;

/**
 * @author Thomas Kosiewski
 * @author Veronika Kaletta
 */
public class PassMain extends TwoPlayerTeam
{

	/**
	 * Constructs a PassMain with a given name and default hostname and ports.
	 * 
	 * @param name the team name
	 */
	public PassMain(String name)
	{
		super(name);
	}

	/**
	 * Constructs PassMain.
	 * 
	 * @param name team name
	 * @param hostname the hostname 
	 * @param playerPort the player port
	 * @param coachPort the coach port
	 */
	public PassMain(String name, String hostname, int playerPort, int coachPort)
	{
		super(name, hostname, playerPort, coachPort);
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.soccer.entities.team.TwoPlayerTeam#initPlayer(java.lang.String, int)
	 */
	@Override
	protected Player initPlayer(String hostname, int port)
	{
		PassPlayer pp = new PassPlayer(this.getName(), 1, hostname, port);
		pp.setConfig(new PassControllerConfig(true, this.getName()));
		
		return pp;
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.soccer.entities.team.TwoPlayerTeam#initPartner(java.lang.String, int)
	 */
	@Override
	protected Player initPartner(String hostname, int port)
	{
		PassPlayer pp = new PassPlayer(this.getName(), 1, hostname, port);
		pp.setConfig(new PassControllerConfig(false, this.getName()));
		
		return pp;
	}
	
	public static void main(String[] args)
	{
		PassMain pm = new PassMain("Test");
		
		try
		{
			pm.connect();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
