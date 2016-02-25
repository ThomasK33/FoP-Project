/**
 * 
 */
package de.tudarmstadt.fop.project.soccer;

import de.tudarmstadt.fop.project.soccer.controller.ControllerImpl;
import de.tudarmstadt.fop.project.soccer.model.GameModelImpl;
import de.tudarmstadt.fop.project.soccer.sensor.SeeInfo;
import de.tudarmstadt.fop.project.soccer.sensor.obj.FieldFlag;
import de.tudarmstadt.fop.project.soccer.sensor.obj.Flag.FlagHPos;
import de.tudarmstadt.fop.project.soccer.sensor.obj.Flag.FlagVPos;
import de.tudarmstadt.fop.project.soccer.sensor.obj.SoccerObject;
import de.tudarmstadt.fop.project.soccer.sensor.obj.SoccerObjectInfo;
import de.tudarmstadt.fop.project.soccer.cmds.Command;
import de.tudarmstadt.fop.project.soccer.cmds.DashCommand;
import de.tudarmstadt.fop.project.soccer.cmds.MoveCommand;
import de.tudarmstadt.fop.project.soccer.cmds.TurnCommand;
import de.tudarmstadt.fop.project.soccer.controller.Action;

import java.math.BigDecimal;

/**
 * @author Thomas Kosiewski
 *
 */
public class CardioPlayerController extends ControllerImpl<GameModelImpl>
{
	private boolean initiated = false;
	private boolean aimed = false;
	private int lastTime = -1;

	/**
	 * @param state the controller state
	 */
	public CardioPlayerController(String state)
	{
		super(state);
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.soccer.controller.ControllerImpl#doUpdate(de.tudarmstadt.fop.project.soccer.model.GameModel)
	 */
	@Override
	public Action doUpdate(GameModelImpl model)
	{
		Command cmd = null;

		if (!initiated && null != model.getOwnSide())
		{
			cmd = new MoveCommand(-26, 0);

			initiated = true;
		}
		else if (null != model.getLastSeeInfo() && this.lastTime != model.getCurrentTime())
		{
			SeeInfo si = (SeeInfo) model.getLastSeeInfo();
			
			boolean flagFound = false;

			for (SoccerObjectInfo soi: si.getObjects())
			{
				SoccerObject so = soi.getSoccerObject();

				if (so instanceof FieldFlag)
				{
					FieldFlag flag = (FieldFlag) so;

					if (!aimed)
					{
						this.setState("dash " + flag.gethPos().toString() + " " + flag.getvPos().toString() + " 0");
						aimed = true;
					}

					String[] cmds = this.getState().split(" ");

					if (cmds.length == 4)
					{	
						if (flag.gethPos() == FlagHPos.valueOf(cmds[1].toUpperCase()) && flag.getvPos() == FlagVPos.valueOf(cmds[2].toUpperCase()))
						{
							if (soi.getDistance().compareTo(new BigDecimal("1.5")) <= 0)
							{
								cmd = new TurnCommand(30);
								
								aimed = false;
							}
							else if (cmds[0].equals("turn"))
							{
								int dir = soi.getDirection();

								if (dir >= 5)
									cmd = new TurnCommand(10);
								else if (dir <= -5)
									cmd = new TurnCommand(-10);

								String state = "dash " + cmds[1] + " " + cmds[2] + " 0";

								this.setState(state);
							}
							else if (cmds[0].equals("dash"))
							{
								cmd = new DashCommand(25);
								
								int count = Integer.parseInt(cmds[3]) + 1;
								
								String state = "";
								
								if (count >= 2)
									state = "turn " + cmds[1] + " " + cmds[2] + " 0";
								else
									state = "dash " + cmds[1] + " " + cmds[2] + " " + count;

								this.setState(state);
							}
						}
						
						flagFound  = true;
					}
				}
			}
			
			if (!flagFound)
			{
				cmd = new TurnCommand(10);
			}
			
			if (null != cmd)
			{
				lastTime = model.getCurrentTime();
			}
		}
		
		return new Action(cmd);
	}

}
