/**
 * 
 */
package de.tudarmstadt.fop.project.soccer.controller;

import de.tudarmstadt.fop.project.soccer.controller.ControllerConfig;

/**
 * @author Thomas Kosiewski
 *
 */
public class PassControllerConfig implements ControllerConfig
{
	// TODO: docs	
	/**
	 * 
	 */
	public PassControllerConfig()
	{
	}
	
	/** Constructor for PassControllerConfig
	 * @param active the players active state
	 */
	public PassControllerConfig(boolean active)
	{
		this.setActive(active);
	}
	
	/**
	 * @param b
	 * @param name
	 */
	public PassControllerConfig(boolean active, String name)
	{
		this.setActive(active);
		this.setName(name);
	}
	
	public PassControllerConfig(String name)
	{
		this.setName(name);
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

	/**
	 * @return the lastTime
	 */
	public int getLastTime()
	{
		return lastTime;
	}

	/**
	 * @param lastTime the lastTime to set
	 */
	public void setLastTime(int lastTime)
	{
		this.lastTime = lastTime;
	}

	/**
	 * @return the initiated
	 */
	public boolean isInitiated()
	{
		return initiated;
	}

	/**
	 * @param initiated the initiated to set
	 */
	public void setInitiated(boolean initiated)
	{
		this.initiated = initiated;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
}