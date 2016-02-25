/**
 * 
 */
package de.tudarmstadt.fop.project.soccer.controller;

/**
 * @author Thomas Kosiewski
 *
 */
public class RequestBallAcceptControllerConfig extends PassControllerConfig
{
	// TODO: docs
	/**
	 * @param name
	 */
	public RequestBallAcceptControllerConfig(String name)
	{
		this.name = name;
	}
	
	public RequestBallAcceptControllerConfig()
	{
	}

	/**
	 * @param name
	 * @param b
	 */
	public RequestBallAcceptControllerConfig(String name, boolean b)
	{
		this.name = name;
		this.active = b;
	}
}
