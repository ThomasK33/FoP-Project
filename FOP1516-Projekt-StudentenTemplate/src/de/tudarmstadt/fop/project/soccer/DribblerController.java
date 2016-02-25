/**
 * 
 */
package de.tudarmstadt.fop.project.soccer;

import java.math.BigDecimal;

import de.tudarmstadt.fop.project.soccer.cmds.Command;
import de.tudarmstadt.fop.project.soccer.cmds.DashCommand;
import de.tudarmstadt.fop.project.soccer.cmds.KickCommand;
import de.tudarmstadt.fop.project.soccer.cmds.MoveCommand;
import de.tudarmstadt.fop.project.soccer.cmds.TurnCommand;
import de.tudarmstadt.fop.project.soccer.controller.Action;
import de.tudarmstadt.fop.project.soccer.controller.ControllerImpl;
import de.tudarmstadt.fop.project.soccer.model.GameModelImpl;
import de.tudarmstadt.fop.project.soccer.sensor.SeeInfo;
import de.tudarmstadt.fop.project.soccer.sensor.obj.SoccerObject;
import de.tudarmstadt.fop.project.soccer.sensor.obj.SoccerObjectInfo;
import de.tudarmstadt.fop.project.soccer.sensor.obj.Ball;
import de.tudarmstadt.fop.project.soccer.sensor.InitInfo.Side;
import de.tudarmstadt.fop.project.soccer.sensor.obj.Goal;

/**
 * @author Thomas Kosiewski, Alexander Mainka, Livia Neubauer
 *
 */
public class DribblerController extends ControllerImpl<GameModelImpl> {
	
	private boolean initiated = false;
	private int counter = 0;
	private String cmdToCome = "dash";
	private int lastTime = -1;
	
	/**
	 * @param state the controller state
	 */
	public DribblerController(String state) {
		super(state);
	}

	/**
	 * Come up with a new {@link Action} (in particular: new controller state
	 * and Command to issue) based on the given game model.
	 * 
	 * @param model the game model
	 * @return the next controller action
	 */
	@Override
	public Action doUpdate(GameModelImpl model) {

		Command cmd = null;

		if (!initiated && null != model.getOwnSide())
		{
			cmd = new MoveCommand(-26, 0);

			initiated = true;
		}
		else if (null != model.getLastSeeInfo() && model.getCurrentTime() != this.lastTime)
		{
			SeeInfo si = (SeeInfo) model.getLastSeeInfo();

			SoccerObjectInfo ball = null;
			SoccerObjectInfo goal = null;

			for (SoccerObjectInfo soi: si.getObjects())
			{
				SoccerObject so = soi.getSoccerObject();

				if (so instanceof Ball)
				{
					ball = soi;			
				}
				else if (so instanceof Goal)
				{
					// Shoot onto the right if you're left
					// Shoot onto the left if you're right
					if (((Goal) so).getPosition() == Side.RIGHT)
						goal = soi;
				}
			}

			if (null != ball)
			{
				if (ball.getDistance().compareTo(new BigDecimal("0.7")) <= 0)
				{
					if (null != goal)
					{
						if(goal.getDistance().compareTo(new BigDecimal("30.0")) <= 0)
							cmd = new KickCommand(100, goal.getDirection());

						else cmd = new KickCommand(3, goal.getDirection());
					}
					else
					{
						cmd = new DashCommand(40);
					}
				}
				else if (cmdToCome.equals("turn"))
				{
					int dir = ball.getDirection();

					if (dir >= 5)
						cmd = new TurnCommand(10);
					else if (dir <= -5)
						cmd = new TurnCommand(-10);

					cmdToCome = "dash";

				}
				else if (cmdToCome.equals("dash"))
				{
					cmd = new DashCommand(25);

					counter++;

					if (counter >= 2)
					{	
						cmdToCome = "turn";
						counter = 0;
					}
					else
						cmdToCome = "dash";

				}	
			}
			else
			{
				cmd = new TurnCommand(10);
			}
			
			if (null != cmd)
			{
				this.lastTime = model.getCurrentTime();
			}
		}			
		
		return new Action(cmd);
	}			

}
