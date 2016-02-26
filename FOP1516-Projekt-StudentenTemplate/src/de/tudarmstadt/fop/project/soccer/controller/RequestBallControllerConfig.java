/**
 * 
 */
package de.tudarmstadt.fop.project.soccer.controller;

/**
 * @author Thomas Kosiewski
 *
 */
public class RequestBallControllerConfig extends PassControllerConfig
{
	/** Constructor for RequestBallControllerConfig
	 * @param name the player's team name
	 */
	public RequestBallControllerConfig(String name)
	{
		this(name, false);
	}
	
	public RequestBallControllerConfig()
	{
		this("", false);
	}

	/** Constructor for RequestBallControllerConfig
	 * @param name the player's team name
	 * @param b the player's active state
	 */
	public RequestBallControllerConfig(String name, boolean b)
	{
		this.name = name;
		this.active = b;
	}
}
