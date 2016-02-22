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

	// TODO: docs
	/**
	 * @param state
	 */
	public DribblerController(String state) {
		super(state);
	}
	
	private boolean initiated = false;

	int counter = 0;
	
	String cmdToCome = "dash";

	// TODO: docs
	// TODO: initialization problem
	/**
	 * @param model
	 * @return
	 */
	@Override
	public Action doUpdate(GameModelImpl model) {
		
		Command cmd = null;

		if (!initiated)
		{
				cmd = new MoveCommand(-20, 20);

			initiated = true;
		} 
		else if (null != model.getLastSeeInfo())
		{
			SeeInfo si = (SeeInfo) model.getLastSeeInfo();
			
			int ballFound = 0;

			for (SoccerObjectInfo soi: si.getObjects())
			{
				SoccerObject so = soi.getSoccerObject();

				if (so instanceof Ball)
				{
					ballFound++;
					
					if (soi.getDistance().compareTo(new BigDecimal("1.0")) <= 0)
					{
						if(soi.getSoccerObject() instanceof Ball){

							int counter3 = 0;
							
							for (SoccerObjectInfo soi2: si.getObjects())
							{
								SoccerObject so2 = soi2.getSoccerObject();

								if (so2 instanceof Goal){
									
									Goal goal = (Goal) so2;
									
									if(goal.getPosition() == Side.RIGHT){
										counter3++;
										
										if(soi2.getDistance().compareTo(new BigDecimal("30.0")) <= 0)
											cmd = new KickCommand(100, soi2.getDirection());
										
										else cmd = new KickCommand(3, soi2.getDirection());
									}		
								 }
							}
							
							if(counter3==0){
								cmd = new DashCommand(40);
							}
						} else {
							cmd = new TurnCommand(10);
						}
					}
					else if (cmdToCome == "turn")
					{
						int dir = soi.getDirection();

						if (dir >= 5)
							cmd = new TurnCommand(10);
						else if (dir <= -5)
							cmd = new TurnCommand(-10);

						cmdToCome = "dash";

					}
					else if (cmdToCome == "dash")
					{
						cmd = new DashCommand(25);
						
						counter++;
						
						if (counter >= 2)
						{	cmdToCome = "turn";
						    counter = 0;
						}
						else
							cmdToCome = "dash";

					}				
				}
			}
			
			if(ballFound==0){
				cmd = new TurnCommand(10);
			}
		}			
		return new Action(cmd);
	}			

}
