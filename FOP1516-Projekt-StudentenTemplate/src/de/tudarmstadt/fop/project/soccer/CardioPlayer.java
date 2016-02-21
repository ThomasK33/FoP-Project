/**
 * 
 */
package de.tudarmstadt.fop.project.soccer;

import de.tudarmstadt.fop.project.soccer.entities.team.Player;

/**
 * @author Thomas Kosiewski
 *
 */
public class CardioPlayer extends Player
{
	// TODO: docs
	/**
	 * @param teamName
	 * @param nr
	 * @param hostname
	 * @param port
	 */
	public CardioPlayer(String teamName, int nr, String hostname, int port)
	{
		super(teamName, nr, hostname, port);
		this.setController(new CardioPlayerController("init"));
	}
}
