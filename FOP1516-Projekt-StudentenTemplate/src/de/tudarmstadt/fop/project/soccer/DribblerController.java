/**
 * 
 */
package de.tudarmstadt.fop.project.soccer;

import de.tudarmstadt.fop.project.soccer.controller.Action;
import de.tudarmstadt.fop.project.soccer.controller.ControllerImpl;
import de.tudarmstadt.fop.project.soccer.model.GameModelImpl;

/**
 * @author Thomas Kosiewski
 *
 */
public class DibblerController extends ControllerImpl<GameModelImpl>
{

	// TODO: docs
	/**
	 * @param state
	 */
	public DibblerController(String state)
	{
		super(state);
	}
	
	// TODO: docs
	// TODO: implement the controller for a dribbling player
	/**
	 * @param model
	 * @return
	 */
	public Action doUpdate(GameModelImpl model) {
		return new Action();
	}

}
