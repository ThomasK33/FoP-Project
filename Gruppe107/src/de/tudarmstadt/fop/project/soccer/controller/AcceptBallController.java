/**
 * 
 */
package de.tudarmstadt.fop.project.soccer.controller;

import java.util.Collection;
import java.util.Iterator;
import de.tudarmstadt.fop.project.soccer.cmds.Command;
import de.tudarmstadt.fop.project.soccer.cmds.MoveCommand;
import de.tudarmstadt.fop.project.soccer.cmds.SayCommand;
import de.tudarmstadt.fop.project.soccer.cmds.TurnCommand;
import de.tudarmstadt.fop.project.soccer.model.GameModelImpl;
import de.tudarmstadt.fop.project.soccer.sensor.HearInfo;
import de.tudarmstadt.fop.project.soccer.sensor.SeeInfo;
import de.tudarmstadt.fop.project.soccer.sensor.SenseBodyInfo.CollisionType;
import de.tudarmstadt.fop.project.soccer.sensor.HearInfo.Sender;
import de.tudarmstadt.fop.project.soccer.sensor.obj.Ball;
import de.tudarmstadt.fop.project.soccer.sensor.obj.SoccerObject;
import de.tudarmstadt.fop.project.soccer.sensor.obj.SoccerObjectInfo;

/**
 * @author Thomas Kosiewski
 *
 */
public class AcceptBallController extends HierarchicalControllerImpl<de.tudarmstadt.fop.project.soccer.model.GameModelImpl, AcceptBallControllerConfig>
{
	private boolean initiated = false;
	private int lastTime = -1;
	private int lastTimeWait = -1;
	private int slowdownTime = 20;

	/** Constructor for AcceptBallController 
	 * @param config configuration for AcceptBallController
	 */
	public AcceptBallController(AcceptBallControllerConfig config)
	{
		super(config);
		this.setState("init");
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.soccer.controller.HierarchicalControllerImpl#configure(de.tudarmstadt.fop.project.soccer.controller.ControllerConfig)
	 */
	@Override
	protected void configure(AcceptBallControllerConfig config)
	{
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.soccer.controller.ControllerImpl#doUpdate(de.tudarmstadt.fop.project.soccer.model.GameModel)
	 */
	@Override
	public Action doUpdate(GameModelImpl model)
	{	
		Command cmd = null;

		if (!initiated  && null != model.getOwnSide())
		{			
			cmd = new MoveCommand(13, 2);

			initiated = true;
		}

		if (null != model.getLastHearInfo())
		{
			HearInfo hi = (HearInfo) model.getLastHearInfo();

			if (hi.getSender() == Sender.OUR)
			{
				if (hi.getMessage().equals("REQACCEPT"))
				{
					SayCommand sc = new SayCommand("\"ACCEPT\"");

					return new Action(sc);
				}
				else if (hi.getMessage().equals("SHOOTING"))
				{
					this.setState("receiving");
					//					RunToBallControllerConfig run = new RunToBallControllerConfig();
					//
					//					run.setLastTime(model.getCurrentTime());
					//
					//					return new Action("init", run);
				}
			}
		}

		if (null != model.getLastSeeInfo() && this.lastTime != model.getCurrentTime() && this.getState().equals("receiving"))
		{
			SeeInfo si = (SeeInfo) model.getLastSeeInfo();

			SoccerObjectInfo ball = null;

			for (SoccerObjectInfo soi: si.getObjects())
			{
				SoccerObject so = soi.getSoccerObject();

				if (so instanceof Ball)
				{
					ball = soi;			
				}
			}

			if (null != ball)
			{
				if (this.lastTimeWait == -1)
					this.lastTimeWait = model.getCurrentTime() + this.slowdownTime;

				if (model.getCurrentTime() >= this.lastTimeWait)
				{
					RunToBallControllerConfig run = new RunToBallControllerConfig();

					run.setLastTime(model.getCurrentTime());

					this.lastTimeWait = -1;

					return new Action("init", run);
				}
			}
			else
			{
				cmd = new TurnCommand(10);
			}
		}
		
		Collection<CollisionType> collisions = model.getCollisions();
		Iterator<CollisionType> it = collisions.iterator();
		while (it.hasNext())
		{
			if (CollisionType.BALL == it.next())
			{
				this.lastTimeWait = 0;
				System.out.println("Successful pass");
			}
		}

		this.lastTime = model.getCurrentTime();

		return new Action(cmd);
	}

}
