/**
 * 
 */
package de.tudarmstadt.fop.project.soccer.controller;

import de.tudarmstadt.fop.project.soccer.cmds.Command;
import de.tudarmstadt.fop.project.soccer.cmds.CompositeCommand;
import de.tudarmstadt.fop.project.soccer.cmds.KickCommand;
import de.tudarmstadt.fop.project.soccer.cmds.MoveCommand;
import de.tudarmstadt.fop.project.soccer.cmds.SayCommand;
import de.tudarmstadt.fop.project.soccer.cmds.TurnCommand;
import de.tudarmstadt.fop.project.soccer.model.GameModelImpl;
import de.tudarmstadt.fop.project.soccer.sensor.HearInfo;
import de.tudarmstadt.fop.project.soccer.sensor.SeeInfo;
import de.tudarmstadt.fop.project.soccer.sensor.HearInfo.Sender;
import de.tudarmstadt.fop.project.soccer.sensor.obj.Ball;
import de.tudarmstadt.fop.project.soccer.sensor.obj.PlayerInfo;
import de.tudarmstadt.fop.project.soccer.sensor.obj.SoccerObject;
import de.tudarmstadt.fop.project.soccer.sensor.obj.SoccerObjectInfo;

/**
 * @author Thomas Kosiewski
 *
 */
public class RequestBallAcceptController extends HierarchicalControllerImpl<GameModelImpl, RequestBallAcceptControllerConfig>
{
	private int lastTime = -1;
	private String name = "";
	private boolean initiated = false;
	private boolean activePlayer = false;
	/**
	 * @param config
	 */
	public RequestBallAcceptController(RequestBallAcceptControllerConfig config)
	{
		super(config);
		this.lastTime = config.getLastTime();
		this.name = config.getName() + "";
		this.activePlayer = config.isActive();
		this.state = "init";
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.soccer.controller.HierarchicalControllerImpl#configure(de.tudarmstadt.fop.project.soccer.controller.ControllerConfig)
	 */
	@Override
	protected void configure(RequestBallAcceptControllerConfig config)
	{
		this.lastTime = config.getLastTime();
		
		if (null == this.state || this.state.equals(HierachicalController.DONE))
			this.state = "init";
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.soccer.controller.ControllerImpl#doUpdate(de.tudarmstadt.fop.project.soccer.model.GameModel)
	 */
	@Override
	public Action doUpdate(GameModelImpl model) {
		Command cmd = null;

		if (!initiated && null != model.getOwnSide())
		{			
			cmd = new MoveCommand(13, 2);

			initiated = true;

			return new Action(cmd);
		}

		if (null != model.getLastHearInfo())
		{
			HearInfo hi = (HearInfo) model.getLastHearInfo();

			if (hi.getSender() == Sender.OUR)
			{
				if (hi.getMessage().equals("ACCEPT"))
				{
					this.setState("shot");
				}
				else if (hi.getMessage().equals("REQACCEPT"))
				{
					SayCommand sc = new SayCommand("\"ACCEPT\"");

					this.setState("waitShot");

					return new Action(sc);
				}
				else if (hi.getMessage().equals("SHOOTING"))
				{
					this.setState("acceptingBall");
				}
			}
		}

		if (model.getCurrentTime() != this.lastTime)
		{
			if (null != model.getLastSeeInfo() && !this.getState().equals("waitReq") && !this.getState().equals("waitShot"))
			{
				SeeInfo si = (SeeInfo) model.getLastSeeInfo();

				SoccerObjectInfo ball = null;
				SoccerObjectInfo player = null;

				for (SoccerObjectInfo soi: si.getObjects())
				{
					SoccerObject so = soi.getSoccerObject();
					
					if (so instanceof PlayerInfo)
					{
						PlayerInfo pi = (PlayerInfo) so;
						
						if (pi.getTeamName() != null && pi.getTeamName().equals(this.name))
							player = soi;
					}

					if (so instanceof Ball)
					{
						ball = soi;
					}
				}

				if (this.getState().equals("acceptingBall") && null != ball && !this.activePlayer)
				{
					RunToBallControllerConfig run = new RunToBallControllerConfig();
					
					this.setState("init");

					run.setLastTime(this.lastTime);

					this.activePlayer = true;

					return new Action("init", run);
				}
				else if (null != player)
				{
					if (this.activePlayer)
					{
						if (this.getState().equals("shot"))
						{
							SayCommand sc = new SayCommand("\"SHOOTING\"");
							
							KickCommand kc = new KickCommand((int) (player.getDistance().intValue() * 2.5), player.getDirection());

							CompositeCommand cc = new CompositeCommand(kc, sc);

							this.setState("init");

							this.activePlayer = false;

							this.lastTime = model.getCurrentTime();
							
							return new Action(HierachicalController.DONE, cc);
						}
						
						if (this.getState().equals("init"))
						{
							cmd = new SayCommand("\"REQACCEPT\"");

							this.setState("waitRes");
						}
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
		}

		return new Action(cmd);
	}

}
