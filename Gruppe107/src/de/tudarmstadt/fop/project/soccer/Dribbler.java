/**
 * 
 */
package de.tudarmstadt.fop.project.soccer;

import de.tudarmstadt.fop.project.soccer.entities.team.Player;

/**
 * @author Thomas Kosiewski
 *
 */
public class Dribbler extends Player
{
	/**
	 * @param teamName the name of the team 
	 * @param nr the number of the player 
	 * @param hostname the client's hostname 
	 * @param port the port number 
	 */
	public Dribbler(String teamName, int nr, String hostname, int port)
	{
		super(teamName, nr, hostname, port);
		this.setController(new DribblerController("init"));
	}
}
