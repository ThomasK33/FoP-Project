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
	// TODO: docs
	/**
	 * @param name
	 */
	public RequestBallControllerConfig(String name)
	{
		this.name = name;
	}
	
	public RequestBallControllerConfig()
	{
	}

	/**
	 * @param name
	 * @param b
	 */
	public RequestBallControllerConfig(String name, boolean b)
	{
		this.name = name;
		this.active = b;
	}
}
