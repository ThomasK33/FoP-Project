/**
 * 
 */
package de.tudarmstadt.fop.project.soccer;

import de.tudarmstadt.fop.project.soccer.controller.PassController;
import de.tudarmstadt.fop.project.soccer.controller.PassControllerConfig;
import de.tudarmstadt.fop.project.soccer.entities.team.Player;

/**
 * @author Thomas Kosiewski
 *
 */
public class PassPlayer extends Player
{

	/**
	 * Constructs a Player.
	 * 
	 * @param teamName the name of the team
	 * @param nr number of the player
	 * @param hostname client's hostname
	 * @param port the port number
	 */
	public PassPlayer(String teamName, int nr, String hostname, int port)
	{
		super(teamName, nr, hostname, port);
	}
	
	public void setConfig(PassControllerConfig pcc)
	{
		pcc.setName(this.teamName);
		this.setController(new PassController(pcc));
	}

}
