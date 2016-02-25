/**
 * 
 */
package de.tudarmstadt.fop.project.soccer.controller;

import de.tudarmstadt.fop.project.soccer.controller.HierarchicalControllerImpl;
import de.tudarmstadt.fop.project.soccer.model.GameModelImpl;

/**
 * @author Thomas Kosiewski
 *
 */
public class PassController extends HierarchicalControllerImpl<GameModelImpl, PassControllerConfig>
{

	/** Constructor for PassController
	 * @param config configuration used to control the behavior of given controller 
	 */
	public PassController(PassControllerConfig config)
	{
		super(config);
		// TODO Auto-generated constructor stub
		
		if (config.isActive())
		{
			this.setActive(new RunToBallController(new RunToBallControllerConfig(config.getName())));
			
			this.addController(this.active);
			this.addController(new RequestBallAcceptController(new RequestBallAcceptControllerConfig(config.getName(), true)));
			this.addController(new RunToBallController(new RunToBallControllerConfig(config.getName())));
		}
		else
		{			
			this.setActive(new RequestBallAcceptController(new RequestBallAcceptControllerConfig(config.getName(), false)));
			
			this.addController(this.active);
			this.addController(new RunToBallController(new RunToBallControllerConfig(config.getName())));
			this.addController(new RequestBallAcceptController(new RequestBallAcceptControllerConfig(config.getName(), false)));
		}
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.soccer.controller.HierarchicalControllerImpl#configure(de.tudarmstadt.fop.project.soccer.controller.ControllerConfig)
	 */
	@Override
	protected void configure(PassControllerConfig config)
	{		
	}

}
