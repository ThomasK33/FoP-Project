/**
 * 
 */
package de.tudarmstadt.fop.project.soccer.controller;

import de.tudarmstadt.fop.project.soccer.cmds.Command;
import de.tudarmstadt.fop.project.soccer.cmds.CompositeCommand;
import de.tudarmstadt.fop.project.soccer.cmds.KickCommand;
import de.tudarmstadt.fop.project.soccer.cmds.SayCommand;
import de.tudarmstadt.fop.project.soccer.cmds.TurnCommand;
import de.tudarmstadt.fop.project.soccer.model.GameModelImpl;
import de.tudarmstadt.fop.project.soccer.sensor.HearInfo;
import de.tudarmstadt.fop.project.soccer.sensor.SeeInfo;
import de.tudarmstadt.fop.project.soccer.sensor.HearInfo.Sender;
import de.tudarmstadt.fop.project.soccer.sensor.obj.PlayerInfo;
import de.tudarmstadt.fop.project.soccer.sensor.obj.SoccerObject;
import de.tudarmstadt.fop.project.soccer.sensor.obj.SoccerObjectInfo;

/**
 * @author Thomas Kosiewski
 *
 */
public class RequestBallController extends HierarchicalControllerImpl<GameModelImpl, RequestBallControllerConfig>
{
	private int lastTime = -1;
	private String name = "";
	
	/** Constructor for RequestBallController
	 * @param config the controller's configuration
	 */
	public RequestBallController(RequestBallControllerConfig config)
	{
		super(config);
		this.lastTime = config.getLastTime();
		this.name = config.getName() + "";
		this.state = "init";
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.soccer.controller.HierarchicalControllerImpl#configure(de.tudarmstadt.fop.project.soccer.controller.ControllerConfig)
	 */
	@Override
	protected void configure(RequestBallControllerConfig config)
	{
		this.lastTime = config.getLastTime();

		if (null == this.state || this.state.equals(HierachicalController.DONE))
			this.state = "init";
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.soccer.controller.ControllerImpl#doUpdate(de.tudarmstadt.fop.project.soccer.model.GameModel)
	 */
	@Override
	public Action doUpdate(GameModelImpl model)
	{
		Command cmd = null;

		if (null != model.getLastHearInfo())
		{
			HearInfo hi = (HearInfo) model.getLastHearInfo();

			if (hi.getSender() == Sender.OUR)
			{
				if (hi.getMessage().equals("ACCEPT"))
				{
					this.setState("shot");
				}
			}
		}

		if (null != model.getLastSeeInfo() && model.getCurrentTime() != this.lastTime)
		{
			SeeInfo si = (SeeInfo) model.getLastSeeInfo();

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
			}

			if (null != player)
			{
				if (this.getState().equals("shot"))
				{
					this.lastTime = model.getCurrentTime();

//					KickCommand kc = new KickCommand((int) (player.getDistance().intValue() * 2.5) + 10, player.getDirection());
					KickCommand kc = new KickCommand(60, player.getDirection());

					SayCommand sc = new SayCommand("\"SHOOTING\"");

					CompositeCommand cc = new CompositeCommand(kc, sc);

					AcceptBallControllerConfig abcc = new AcceptBallControllerConfig(this.name);

					return new Action("init", abcc, cc);
				}

				if (this.getState().equals("init"))
				{
					cmd = new SayCommand("\"REQACCEPT\"");
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
