/**
 * 
 */
package de.tudarmstadt.fop.project.soccer.controller;

import java.math.BigDecimal;

import de.tudarmstadt.fop.project.soccer.PassPlayer;
import de.tudarmstadt.fop.project.soccer.cmds.Command;
import de.tudarmstadt.fop.project.soccer.cmds.DashCommand;
import de.tudarmstadt.fop.project.soccer.cmds.KickCommand;
import de.tudarmstadt.fop.project.soccer.cmds.MoveCommand;
import de.tudarmstadt.fop.project.soccer.cmds.TurnCommand;
import de.tudarmstadt.fop.project.soccer.model.GameModelImpl;
import de.tudarmstadt.fop.project.soccer.sensor.SeeInfo;
import de.tudarmstadt.fop.project.soccer.sensor.SenseBodyInfo;
import de.tudarmstadt.fop.project.soccer.sensor.obj.Ball;
import de.tudarmstadt.fop.project.soccer.sensor.obj.PlayerInfo;
import de.tudarmstadt.fop.project.soccer.sensor.obj.SoccerObject;
import de.tudarmstadt.fop.project.soccer.sensor.obj.SoccerObjectInfo;

/**
 * @author Thomas Kosiewski
 *
 */
public class RunToBallController extends HierarchicalControllerImpl<GameModelImpl, RunToBallControllerConfig>
{
	private int lastTime = -1;
	private boolean initiated = false;
	private String name = "";

	/** Constructor for RunToBallController 
	 * @param config the configuration for given controller
	 */
	public RunToBallController(RunToBallControllerConfig config)
	{
		super(config);
		this.state = "init";
		this.name  = config.getName();
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.soccer.controller.HierarchicalControllerImpl#configure(de.tudarmstadt.fop.project.soccer.controller.ControllerConfig)
	 */
	@Override
	protected void configure(RunToBallControllerConfig config)
	{		
		this.lastTime = config.getLastTime();
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.soccer.controller.ControllerImpl#doUpdate(de.tudarmstadt.fop.project.soccer.model.GameModel)
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
			SoccerObjectInfo player = null;

			for (SoccerObjectInfo soi: si.getObjects())
			{
				SoccerObject so = soi.getSoccerObject();

				if (so instanceof Ball)
				{
					ball = soi;			
				}

				if (so instanceof PlayerInfo)
				{
					if (null != this.name && ((PlayerInfo) so).getTeamName() != null && ((PlayerInfo) so).getTeamName().equals(this.name))
						player = soi;
				}
			}

			if (null != ball)
			{
				if (this.getState().equals("walkThroughBall"))
				{
					cmd = new DashCommand(30);
					this.setState("init");
				}
				else
				if (ball.getDistance().compareTo(new BigDecimal("0.7")) <= 0)
				{
					if (null != player)
					{
						if (Math.abs(ball.getDirection()) >= 100)
						{
							cmd = new TurnCommand(Math.abs(ball.getDirection()) - 30);
							
							this.setState("walkThroughBall");
						}
						else if(player.getDistance().compareTo(new BigDecimal("20.0")) <= 0)
						{
							RequestBallControllerConfig req = new RequestBallControllerConfig();

							req.setLastTime(this.lastTime);

							this.lastTime = model.getCurrentTime();

							return new Action("init", req);
						}
						else 
							cmd = new KickCommand(3, player.getDirection());
					}
					else
					{
						cmd = new TurnCommand(10);
					}
				}
				else if (this.state.equals("turn"))
				{
					int dir = ball.getDirection();

					if (dir >= 5)
						cmd = new TurnCommand(10);
					else if (dir <= -5)
						cmd = new TurnCommand(-10);

					this.state = "dash";

				}
				else if (this.state.equals("dash") || this.state.equals("init"))
				{
					cmd = new DashCommand(25);

					if ((this.lastTime % 3) == 2)
						this.state = "turn";
					else
						this.state = "dash";
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