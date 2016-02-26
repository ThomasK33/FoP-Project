/**
 * 
 */
package de.tudarmstadt.fop.project.soccer.controller;

import de.tudarmstadt.fop.project.soccer.controller.ControllerConfig;

/**
 * @author Thomas Kosiewski
 * @author Veronika Kaletta
 */
public class PassControllerConfig implements ControllerConfig
{
	/**
	 * Constructor for PassControllerConfig
	 */
	public PassControllerConfig()
	{
		this(false, "");
	}
	
	/** Constructor for PassControllerConfig
	 * @param active the player's active state
	 */
	public PassControllerConfig(boolean active)
	{
		this(active, "");
	}
	
	/** Constructor for PassControllerConfig
	 * @param active the player's active state
	 * @param name the player's team name
	 */
	public PassControllerConfig(boolean active, String name)
	{
		this.setActive(active);
		this.setName(name);
	}
	
	/** Constructor for PassControllerConfig
	 * @param name the player's team name
	 */
	public PassControllerConfig(String name)
	{
		this(false, name);
	}

	protected int lastTime = -1;
	protected boolean initiated = false;
	protected boolean active = false;
	protected String name = "";
	
	/** State whether the player is active
	 * @return true - if player is active
	 * 			false - if player is passive
	 */
	public boolean isActive()
	{
		return active;
	}

	/** Sets the player state
	 * @param boolean value whether player shall act active
	 */
	public void setActive(boolean active)
	{
		this.active = active;
	}

	/** Gets last ingame time stamp
	 * @return the last time stamp ingame
	 */
	public int getLastTime()
	{
		return lastTime;
	}

	/** Sets the ingame time stamp
	 * @param lastTime the lastTime to be set
	 */
	public void setLastTime(int lastTime)
	{
		this.lastTime = lastTime;
	}

	/** Retrieves initiation state of corresponding controller
	 * @return true - if initiated
	 * 			false - if not initiated
	 */
	public boolean isInitiated()
	{
		return initiated;
	}

	/** Sets initiation state of corresponding controller 
	 * @param initiated the initiated to set
	 */
	public void setInitiated(boolean initiated)
	{
		this.initiated = initiated;
	}

	/**	Gets the player's team name
	 * @return the team name
	 */
	public String getName()
	{
		return name;
	}

	/** Sets the player's team name
	 * @param the name to be set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
}