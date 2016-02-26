/**
 * 
 */
package de.tudarmstadt.fop.project.soccer.controller;

import de.tudarmstadt.fop.project.soccer.PassPlayer;
import de.tudarmstadt.fop.project.soccer.controller.HierarchicalControllerImpl;
import de.tudarmstadt.fop.project.soccer.model.GameModelImpl;

/**
 * @author Thomas Kosiewski
 * @author Veronika Kaletta
 */
public class PassController extends HierarchicalControllerImpl<GameModelImpl, PassControllerConfig>
{

	/** Constructor for PassController
	 * @param config configuration used to control the behavior of given controller 
	 */
	public PassController(PassControllerConfig config, PassPlayer passPlayer)
	{
		super(config);
		
		RunToBallController rtbc = new RunToBallController(new RunToBallControllerConfig(config.getName()));
		RequestBallController rbc = new RequestBallController(new RequestBallControllerConfig(config.getName()));
		AcceptBallController abc = new AcceptBallController(new AcceptBallControllerConfig(config.getName()));
		
		if (config.isActive())
		{
			this.setActive(rtbc);
		}
		else
		{			
			this.setActive(abc);
		}
		
		this.addController(rtbc);
		this.addController(rbc);
		this.addController(abc);
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.soccer.controller.HierarchicalControllerImpl#configure(de.tudarmstadt.fop.project.soccer.controller.ControllerConfig)
	 */
	@Override
	protected void configure(PassControllerConfig config)
	{		
	}

}
