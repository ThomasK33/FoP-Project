/**
 * 
 */
package de.tudarmstadt.fop.project.soccer.praser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.tudarmstadt.fop.project.expression.CustomExpressionParser;
import de.tudarmstadt.fop.project.expression.DecimalOperand;
import de.tudarmstadt.fop.project.expression.Expression;
import de.tudarmstadt.fop.project.expression.ExpressionOperand;
import de.tudarmstadt.fop.project.expression.IdentifierOperand;
import de.tudarmstadt.fop.project.expression.Operand;
import de.tudarmstadt.fop.project.expression.Operator;
import de.tudarmstadt.fop.project.expression.StringOperand;
import de.tudarmstadt.fop.project.parser.Lexer;
import de.tudarmstadt.fop.project.parser.ParseException;
import de.tudarmstadt.fop.project.soccer.parser.SoccerParser;
import de.tudarmstadt.fop.project.soccer.sensor.SeeInfo;
import de.tudarmstadt.fop.project.soccer.sensor.SenseBodyInfo;
import de.tudarmstadt.fop.project.soccer.sensor.SenseBodyInfo.ArmInfo;
import de.tudarmstadt.fop.project.soccer.sensor.SenseBodyInfo.Card;
import de.tudarmstadt.fop.project.soccer.sensor.SenseBodyInfo.CollisionType;
import de.tudarmstadt.fop.project.soccer.sensor.SenseBodyInfo.FocusInfo;
import de.tudarmstadt.fop.project.soccer.sensor.SenseBodyInfo.FoulInfo;
import de.tudarmstadt.fop.project.soccer.sensor.SenseBodyInfo.TackleInfo;
import de.tudarmstadt.fop.project.soccer.sensor.SenseBodyInfo.Team;
import de.tudarmstadt.fop.project.soccer.sensor.SenseBodyInfo.ViewIntensity;
import de.tudarmstadt.fop.project.soccer.sensor.SenseBodyInfo.ViewMode;
import de.tudarmstadt.fop.project.soccer.sensor.SensorInformation;
import de.tudarmstadt.fop.project.soccer.sensor.HearInfo;
import de.tudarmstadt.fop.project.soccer.sensor.HearInfo.Sender;
import de.tudarmstadt.fop.project.soccer.sensor.InitInfo.Side;
import de.tudarmstadt.fop.project.soccer.sensor.obj.Ball;
import de.tudarmstadt.fop.project.soccer.sensor.obj.FieldFlag;
import de.tudarmstadt.fop.project.soccer.sensor.obj.Flag;
import de.tudarmstadt.fop.project.soccer.sensor.obj.Flag.FlagHPos;
import de.tudarmstadt.fop.project.soccer.sensor.obj.Flag.FlagVPos;
import de.tudarmstadt.fop.project.soccer.sensor.obj.Goal;
import de.tudarmstadt.fop.project.soccer.sensor.obj.GoalFlag;
import de.tudarmstadt.fop.project.soccer.sensor.obj.Line;
import de.tudarmstadt.fop.project.soccer.sensor.obj.Line.LinePosition;
import de.tudarmstadt.fop.project.soccer.sensor.obj.PenaltyFlag;
import de.tudarmstadt.fop.project.soccer.sensor.obj.PlayerInfo;
import de.tudarmstadt.fop.project.soccer.sensor.obj.SoccerObject;
import de.tudarmstadt.fop.project.soccer.sensor.obj.SoccerObjectInfo;
import de.tudarmstadt.fop.project.soccer.sensor.obj.VirtualFlag;
import de.tudarmstadt.fop.project.soccer.sensor.obj.VirtualFlagBottom;
import de.tudarmstadt.fop.project.soccer.sensor.obj.VirtualFlagLeft;
import de.tudarmstadt.fop.project.soccer.sensor.obj.VirtualFlagRight;
import de.tudarmstadt.fop.project.soccer.sensor.obj.VirtualFlagTop;

/**
 * @author Thomas Kosiewski
 *
 */
public class CustomSoccerParser extends CustomExpressionParser implements SoccerParser
{

	// TODO: docs
	/**
	 * @param lexer
	 */
	public CustomSoccerParser(Lexer lexer)
	{
		super(lexer);
	}

	/* (non-Javadoc)
	 * @see de.tudarmstadt.fop.project.soccer.parser.SoccerParser#extractSensorInformation()
	 */
	@Override
	public SensorInformation extractSensorInformation() throws ParseException
	{
		SensorInformation si = null;
		Expression expr = this.getExpression();

		if (expr.getOperator().getIdentifier().equals("see"))
		{
			int pos = 0;

			List<Operand> operandsList = expr.getOperands();
			Operand operand = operandsList.get(pos);

			if (is(operand, DecimalOperand.class))
			{
				int time = ((DecimalOperand) operand).asInt();
				ArrayList<SoccerObjectInfo> lst = new ArrayList<SoccerObjectInfo>();

				pos += 1;

				while (pos < operandsList.size())
				{
					Operand objInfo = operandsList.get(pos);

					if (is(objInfo, ExpressionOperand.class))
					{
						Operator objName = ((ExpressionOperand) objInfo).getValue().getOperator();
						List<Operand> opList = ((ExpressionOperand) objInfo).getValue().getOperands();

						if (objName.isExpression())
						{
							Expression expression = objName.getExpression();
							String id = expression.getOperator().getIdentifier();
							List<Operand> expOpLst = expression.getOperands();

							SoccerObject so = null;

							if (id.equals("p"))
							{
								PlayerInfo player = new PlayerInfo();

								// TODO: look for is kicking flag in NEWS_rcssserver

								for (Operand op: expOpLst)
								{
									// Team name
									if (op.getClass().isAssignableFrom(StringOperand.class))
										player.setTeamName(((StringOperand) op).getValue());
									// Player Number
									else if (op.getClass().isAssignableFrom(DecimalOperand.class))
										player.setNumber(((DecimalOperand) op).asInt());
									// Goalie
									else if (op.getClass().isAssignableFrom(IdentifierOperand.class))
										player.setGoalie(((IdentifierOperand) op).getValue().equals("true") ? true : false);
								}

								so = player;
							}
							else if (id.equals("b"))
							{
								so = new Ball();
							}
							else if (id.equals("g"))
							{
								Operand arg = expOpLst.get(0);
								if (is(arg, IdentifierOperand.class))
								{
									if (((IdentifierOperand) arg).getValue().equals("l"))
										so = new Goal(Side.LEFT);
									else
										so = new Goal(Side.RIGHT);
								}
							}
							else if (id.equals("f"))
							{
								if (expOpLst.size() == 3)
								{
									// 3rd arg is number --> Virtual Flag
									if (is(expOpLst.get(2), DecimalOperand.class))
									{
										VirtualFlag vf = null;

										int distance = ((DecimalOperand) expOpLst.get(2)).asInt();

										// TODO: make this clear for every case
										if (!is(expOpLst.get(0), IdentifierOperand.class) || !is(expOpLst.get(1), IdentifierOperand.class))
											throw new ParseException("Expexted flag identifier, instead found this " + expOpLst.get(0).toString() + " " + expOpLst.get(1).toString());

										String arg1 = ((IdentifierOperand) expOpLst.get(0)).getValue();
										String arg2 = ((IdentifierOperand) expOpLst.get(1)).getValue();

										if (arg1.equals("t") || arg1.equals("b"))
										{
											FlagHPos hPos = null;

											if (arg2.equals("r"))
												hPos = FlagHPos.RIGHT;
											else if (arg2.equals("l"))
												hPos = FlagHPos.LEFT;

											if (arg1.equals("t"))
												vf = new VirtualFlagTop(hPos, distance);
											else if (arg1.equals("b"))
												vf = new VirtualFlagBottom(hPos, distance);
										}
										else if (arg1.equals("l") || arg1.equals("r"))
										{
											FlagVPos vPos = null;

											if (arg2.equals("t"))
												vPos = FlagVPos.TOP;
											else if (arg2.equals("b"))
												vPos = FlagVPos.BOTTOM;

											if (arg1.equals("l"))
												vf = new VirtualFlagLeft(vPos, distance);
											else if (arg1.equals("r"))
												vf = new VirtualFlagRight(vPos, distance);
										}

										so = vf;
									}
									// either penalty or goal flag
									else
									{
										// TODO: make this clear for every case
										if (!is(expOpLst.get(0), IdentifierOperand.class) || !is(expOpLst.get(1), IdentifierOperand.class) || !is(expOpLst.get(2), IdentifierOperand.class))
											throw new ParseException("Expexted flag identifier, instead found this " + expOpLst.get(0).toString() + " " + expOpLst.get(1).toString() + " " + expOpLst.get(2).toString());


										String arg1 = ((IdentifierOperand) expOpLst.get(0)).getValue();
										String arg2 = ((IdentifierOperand) expOpLst.get(1)).getValue();
										String arg3 = ((IdentifierOperand) expOpLst.get(2)).getValue();

										FlagHPos hPos = null;

										if (arg2.equals("r"))
											hPos = FlagHPos.RIGHT;
										else if (arg2.equals("l"))
											hPos = FlagHPos.LEFT;
										else
											hPos = FlagHPos.CENTER;

										FlagVPos vPos = null;

										if (arg3.equals("t"))
											vPos = FlagVPos.TOP;
										else if (arg3.equals("b"))
											vPos = FlagVPos.BOTTOM;
										else
											vPos = FlagVPos.CENTER;

										if (arg1.equals("p"))
											// penalty flag
											so = new PenaltyFlag(hPos, vPos);
										else if (arg1.equals("g"))
											// goal flag
											so = new GoalFlag(hPos, vPos);
									}
								}
								else if (expOpLst.size() == 2)
								{
									if (!is(expOpLst.get(0), IdentifierOperand.class))
										throw new ParseException("Expected a flag identifier, instead found this " + expOpLst.get(0).toString());

									String arg1 = ((IdentifierOperand) expOpLst.get(0)).getValue();

									if (is(expOpLst.get(1), DecimalOperand.class))
									{
										int distance = ((DecimalOperand) expOpLst.get(1)).asInt();

										if (arg1.equals("r"))
											so = new VirtualFlagRight(null, distance);
										else if (arg1.equals("l"))
											so = new VirtualFlagLeft(null, distance);
										else if (arg1.equals("t"))
											so = new VirtualFlagTop(null, distance);
										else if (arg1.equals("b"))
											so = new VirtualFlagBottom(null, distance);
									}
									else
									{
										if (!is(expOpLst.get(1), IdentifierOperand.class))
											throw new ParseException("Expected a flag identifier, instead found this " + expOpLst.get(1).toString());

										String arg2 = ((IdentifierOperand) expOpLst.get(1)).getValue();

										FlagVPos vPos = null;

										if (arg2.equals("t"))
											vPos = FlagVPos.TOP;
										else if (arg2.equals("b"))
											vPos = FlagVPos.BOTTOM;

										if (arg1.equals("l"))
											so = new FieldFlag(FlagHPos.LEFT, vPos);
										else if (arg1.equals("c"))
											so = new FieldFlag(FlagHPos.CENTER, vPos);
										else if (arg1.equals("r"))
											so = new FieldFlag(FlagHPos.RIGHT, vPos);
									}
								}
								else
								{
									if (!is(expOpLst.get(0), IdentifierOperand.class))
										throw new ParseException("Expected a flag identifier, instead found this " + expOpLst.get(0).toString());

									String arg1 = ((IdentifierOperand) expOpLst.get(0)).getValue();

									if (arg1.equals("c"))
										so = new Flag(FlagHPos.CENTER, FlagVPos.CENTER);
								}
							}
							else if (id.equals("l"))
							{
								Operand arg = expOpLst.get(0);
								if (is(arg, IdentifierOperand.class))
								{
									String side = ((IdentifierOperand) arg).getValue();
									if (side.equals("l"))
										so = new Line(LinePosition.LEFT);
									else if (side.equals("r"))
										so = new Line(LinePosition.RIGHT);
									else if (side.equals("t"))
										so = new Line(LinePosition.TOP);
									else if (side.equals("b"))
										so = new Line(LinePosition.BOTTOM);
								}
							}
							else if (id.equals("P"))
							{
								// TODO: check for updates: https://moodle.informatik.tu-darmstadt.de/mod/forum/discuss.php?d=40838
								// unspecified player
							}
							else if (id.equals("B")) 
							{
								// unspecified ball
							}
							else if (id.equals("G"))
							{
								so = new Goal();
							}
							else if (id.equals("F"))
							{
								so = new Flag();
							}
							else
								throw new ParseException("Expected value for ObjName, insted found this " + id);

							SoccerObjectInfo soi = new SoccerObjectInfo();
							soi.setSoccerObject(so);

							if (opList.size() == 1)
							{
								if (is(opList.get(0), DecimalOperand.class) && ((DecimalOperand) opList.get(0)).asInt() >= -180 && ((DecimalOperand) opList.get(0)).asInt() <= 180)
									soi.setDirection(((DecimalOperand) opList.get(0)).asInt());
								else
									throw new ParseException("Expected decimal between -180 to 180 for direction, insted found this " + opList.get(0).toString());
							}
							if (opList.size() >= 2)
							{
								if (is(opList.get(0), DecimalOperand.class) && ((DecimalOperand) opList.get(0)).asDecimal().compareTo(BigDecimal.ZERO) >= 0)
								{
									soi.setDistance(((DecimalOperand) opList.get(0)).asDecimal());

									if (is(opList.get(1), DecimalOperand.class) && ((DecimalOperand) opList.get(1)).asInt() >= -180 && ((DecimalOperand) opList.get(1)).asInt() <= 180)
										soi.setDirection(((DecimalOperand) opList.get(1)).asInt());
									else
										throw new ParseException("Expected decimal between -180 to 180 for direction, insted found this " + opList.get(0).toString());
								}
								else
									throw new ParseException("Expected positive decimal for distance, insted found this " + opList.get(0).toString());
							}
							if (opList.size() >= 4)
							{
								if (is(opList.get(2), DecimalOperand.class))
								{
									soi.setDistChange(((DecimalOperand) opList.get(2)).asDecimal());

									if (is(opList.get(3), DecimalOperand.class))
									{
										soi.setDirChange(((DecimalOperand) opList.get(3)).asDecimal());
									}
									else
										throw new ParseException("Expected a decimal for DirChange, instead found this " + opList.get(3).toString());
								}
								else
									throw new ParseException("Expected a decimal for DistChange, instead found this " + opList.get(2).toString());
							}
							if (opList.size() >= 6)
							{
								if (is(opList.get(4), DecimalOperand.class))
								{
									soi.setBodyFacingDir(((DecimalOperand) opList.get(4)).asInt());

									if (is(opList.get(5), DecimalOperand.class))
									{
										soi.setHeadFacingDir(((DecimalOperand) opList.get(5)).asInt());
									}
									else
										throw new ParseException("Expected decimal for BodyFancingDir, instead found this " + opList.get(5).toString());
								}
								else
									throw new ParseException("Expected decimal for BodyFancingDir, instead found this " + opList.get(4).toString());
							}

							lst.add(soi);
						}
						else
							throw new ParseException("Expected an expression for ObjName, instead found this " + objInfo.toString());
					}
					else
						throw new ParseException("Expected an expression for ObjInfo, instead found this " + objInfo.toString());

					pos += 1;
				}

				si = new SeeInfo(time, lst);
			}
			else
				throw new ParseException("Expected an integer for time, instead found this " + operand.toString());
		}
		else if (expr.getOperator().getIdentifier().equals("hear"))
		{
			List<Operand> operandsList = expr.getOperands();

			HearInfo hi = new HearInfo();

			// TODO: merge both cases into one
			if (operandsList.size() == 3)
			{
				if (is(operandsList.get(0), DecimalOperand.class))
				{
					DecimalOperand DO = (DecimalOperand) operandsList.get(0);
					hi.setTime(DO.asInt());

					if (is(operandsList.get(1), IdentifierOperand.class))
					{
						IdentifierOperand IO = (IdentifierOperand) operandsList.get(1);
						String sender = IO.getValue();

						if (sender.equals("online_coach_left"))
							hi.setSender(Sender.COACH_L);
						else if (sender.equals("online_coach_right"))
							hi.setSender(Sender.COACH_R);
						else if (sender.equals("coach"))
							hi.setSender(Sender.COACH);
						else if (sender.equals("referee"))
							hi.setSender(Sender.REFEREE);
						else if (sender.equals("our"))
							hi.setSender(Sender.OUR);
						else if (sender.equals("opp"))
							hi.setSender(Sender.OPP);
						else if (sender.equals("self"))
							hi.setSender(Sender.SELF);

						if (is(operandsList.get(2), StringOperand.class))
						{
							StringOperand SO = (StringOperand) operandsList.get(2);
							String message = SO.getValue();

							hi.setMessage(message);
						}
						else
							throw new ParseException("Expected string for message, instead found this " + operandsList.get(2).toString());
					}
					else
						throw new ParseException("Expected identifier for sender, indead found this " + operandsList.get(1).toString());
				}
				else
					throw new ParseException("Expected decimal for time, indead found this " + operandsList.get(0).toString());
			}
			else if (operandsList.size() > 3)
			{
				if (is(operandsList.get(0), DecimalOperand.class))
				{
					DecimalOperand DO = (DecimalOperand) operandsList.get(0);
					hi.setTime(DO.asInt());

					if (is(operandsList.get(1), DecimalOperand.class))
					{
						DecimalOperand direction = (DecimalOperand) operandsList.get(1);
						hi.setDirection(direction.asInt());

						if (is(operandsList.get(2), IdentifierOperand.class))
						{
							IdentifierOperand IO = (IdentifierOperand) operandsList.get(2);
							String sender = IO.getValue();

							if (sender.equals("online_coach_left"))
								hi.setSender(Sender.COACH_L);
							else if (sender.equals("online_coach_right"))
								hi.setSender(Sender.COACH_R);
							else if (sender.equals("coach"))
								hi.setSender(Sender.COACH);
							else if (sender.equals("referee"))
								hi.setSender(Sender.REFEREE);
							else if (sender.equals("our"))
								hi.setSender(Sender.OUR);
							else if (sender.equals("opp"))
								hi.setSender(Sender.OPP);
							else if (sender.equals("self"))
								hi.setSender(Sender.SELF);

							if (is(operandsList.get(3), DecimalOperand.class))
							{
								DecimalOperand playerNumber = (DecimalOperand) operandsList.get(3);
								hi.setNr(playerNumber.asInt());

								if (is(operandsList.get(4), StringOperand.class))
								{
									StringOperand SO = (StringOperand) operandsList.get(4);
									String message = SO.getValue();

									hi.setMessage(message);
								}
								else
									throw new ParseException("Expected string for message, instead found this " + operandsList.get(4).toString());
							}
							else
								throw new ParseException("Expected decimal for player number, instead found this " + operandsList.get(3).toString());

						}
						else
							throw new ParseException("Expected identifier for sender, indead found this " + operandsList.get(2).toString());
					}
					else
						throw new ParseException("Expected decimal for direction, indead found this " + operandsList.get(1).toString());
				}
				else
					throw new ParseException("Expected decimal for time, indead found this " + operandsList.get(0).toString());
			}

			si = hi;
		}
		else if (expr.getOperator().getIdentifier().equals("sense_body"))
		{
			// TODO: implement this
			List<Operand> operandsList = expr.getOperands();
			SenseBodyInfo sbi = new SenseBodyInfo();

			boolean timeSet = false;

			for (Operand op: operandsList)
			{
				if (is(op, DecimalOperand.class) && !timeSet)
				{
					DecimalOperand t = (DecimalOperand) op;
					sbi.setTime(t.asInt());
					timeSet = true;
				}

				else if (is(op, DecimalOperand.class) && timeSet)
					throw new ParseException("Expected an expression as time is already set, instead found this " + op.toString());				

				else if (is(op, ExpressionOperand.class))
				{
					Operator operator = ((ExpressionOperand) op).getValue().getOperator();
					List<Operand> argLst = ((ExpressionOperand) op).getValue().getOperands();

					if (operator.isPrimitive())
					{
						String id = operator.getIdentifier();

						if (id.equals("view_mode"))
						{
							if (argLst.size() < 2)
								throw new ParseException("Expected two args for" + id + ", instead found this " + argLst.toString());
							if (!is(argLst.get(0), IdentifierOperand.class) || !is(argLst.get(1), IdentifierOperand.class))
								throw new ParseException("Expected both operands for view_mode to be identifier, instead found this " + argLst.get(0).toString() + " " + argLst.get(1).toString());

							String intensity = ((IdentifierOperand)argLst.get(0)).getValue();
							String viewmode = ((IdentifierOperand)argLst.get(1)).getValue();

							ViewMode  viewMode =  ViewMode.valueOf(viewmode.toUpperCase());
							ViewIntensity viewIntensity = ViewIntensity.valueOf(intensity.toUpperCase());

							sbi.setViewMode(viewMode);
							sbi.setViewIntensity(viewIntensity);
						}
						else if (id.equals("stamina"))
						{
							if (argLst.size() < 2)
								throw new ParseException("Expected at least two args for" + id + ", instead found this " + argLst.toString());

							if (!is(argLst.get(0), DecimalOperand.class))
								throw new ParseException("Expected first argument for " + id + " to be a decimal, instead found this " + argLst.get(0).toString());
							if (!is(argLst.get(1), DecimalOperand.class))
								throw new ParseException("Expected second argument for " + id + " to be a decimal, instead found this " + argLst.get(1).toString());
							if (argLst.size() == 3 && !is(argLst.get(2), DecimalOperand.class))
								throw new ParseException("Expected third argument for " + id + " to be a decimal, instead found this " + argLst.get(1).toString());

							BigDecimal stamina = ((DecimalOperand)argLst.get(0)).asDecimal();
							BigDecimal effort =  ((DecimalOperand)argLst.get(1)).asDecimal();

							sbi.setStamina(stamina);
							sbi.setEffort(effort);

							if (argLst.size() == 3)
							{
								BigDecimal capacity = ((DecimalOperand) argLst.get(2)).asDecimal();

								sbi.setCapacity(capacity);
							}
						}
						else if (id.equals("speed"))
						{
							if (argLst.size() < 2)
								throw new ParseException("Expected at least two args for" + id + ", instead found this " + argLst.toString());

							if (!is(argLst.get(0), DecimalOperand.class))
								throw new ParseException("Expected first argument for " + id + " to be a decimal, instead found this " + argLst.get(0).toString());
							if (!is(argLst.get(1), DecimalOperand.class))
								throw new ParseException("Expected second argument for " + id + " to be a decimal, instead found this " + argLst.get(1).toString());

							BigDecimal speed = ((DecimalOperand)argLst.get(0)).asDecimal();
							BigDecimal angle =  ((DecimalOperand)argLst.get(1)).asDecimal();

							sbi.setSpeed(speed);
							// TODO: find what to do with angle
						}
						else if (id.equals("head_angle") || id.equals("kick") || id.equals("dash") || id.equals("turn") || id.equals("say") || id.equals("turn_neck") || id.equals("catch") || id.equals("move") || id.equals("change_view"))
						{
							if (argLst.size() < 1)
								throw new ParseException("Expected at least one args for" + id + ", instead found this " + argLst.toString());

							if (!is(argLst.get(0), DecimalOperand.class))
								throw new ParseException("Expected first argument for " + id + " to be a decimal, instead found this " + argLst.get(0).toString());

							int arg0 = ((DecimalOperand)argLst.get(0)).asInt();

							if (id.equals("head_angle"))
								sbi.setHeadAngle(arg0);
							else if (id.equals("kick"))
								sbi.setKickCount(arg0);
							else if (id.equals("dash"))
								sbi.setDashCount(arg0);
							else if (id.equals("turn"))
								sbi.setTurnCount(arg0);
							else if (id.equals("say"))
								sbi.setSayCount(arg0);
							else if (id.equals("turn_neck"))
								sbi.setTurnNeckCount(arg0);
							else if (id.equals("catch"))
								sbi.setCatchCount(arg0);
							else if (id.equals("move"))
								sbi.setMoveCount(arg0);
							else if (id.equals("change_view"))
								sbi.setChangeViewCount(arg0);
						}
						else if (id.equals("arm") || id.equals("focus") || id.equals("tackle"))
						{
							int lastItemIndex = argLst.size() - 1;

							if (is(argLst.get(lastItemIndex), ExpressionOperand.class))
							{
								Operator countOperand = ((ExpressionOperand) argLst.get(lastItemIndex)).getValue().getOperator();
								List<Operand> countLst = ((ExpressionOperand) argLst.get(lastItemIndex)).getValue().getOperands();

								if (countOperand.getIdentifier().equals("count") && is(countLst.get(0), DecimalOperand.class))
								{
									int count = ((DecimalOperand) countLst.get(0)).asInt();

									if (id.equals("arm"))
									{
										if (argLst.size() < 4)
											throw new ParseException("Expected argument count to be at least four for arm, instead found only " + argLst.size());

										if (!is(argLst.get(0), ExpressionOperand.class) || !is(argLst.get(1), ExpressionOperand.class) || !is(argLst.get(2), ExpressionOperand.class))
											throw new ParseException("Expected all arguments to be expressions");

										int[] iarr = new int[3];
										String[] sarr = new String[]{"movable", "expires"};

										for (int i = 0; i < sarr.length; i++)
										{
											Expression expression = ((ExpressionOperand) argLst.get(i)).getValue();

											if (expression.getOperator().getIdentifier().equals(sarr[i]) && is(expression.getOperands().get(0), DecimalOperand.class))
											{
												iarr[i] = ((DecimalOperand) expression.getOperands().get(0)).asInt();
											}
											else
												throw new ParseException("Expected an expression for movable with decimal count");
										}

										Expression targetOperand = ((ExpressionOperand) argLst.get(2)).getValue();

										int movable = iarr[0];
										int expires = iarr[1];
										int target = 0;
										int dir = 0;

										if (targetOperand.getOperator().getIdentifier().equals("target") && is(targetOperand.getOperands().get(0), DecimalOperand.class) && is(targetOperand.getOperands().get(1), DecimalOperand.class))
										{
											target = ((DecimalOperand) targetOperand.getOperands().get(0)).asInt();
											dir = ((DecimalOperand) targetOperand.getOperands().get(1)).asInt();
										}
										else
											throw new ParseException("Expected an expression with decimals as arguments");



										ArmInfo armInfo = new ArmInfo();

										armInfo.setMovable(movable);
										armInfo.setExpires(expires);
										armInfo.setDir(dir);
										armInfo.setCount(count);

										sbi.setArmInfo(armInfo);

										FocusInfo focusInfo = new FocusInfo();

										focusInfo.setTarget(target);

										sbi.setFocusInfo(focusInfo);
									}
									else if (id.equals("focus"))
									{
										Expression targetOperand = ((ExpressionOperand) argLst.get(0)).getValue();

										if (targetOperand.getOperator().getIdentifier().equals("target"))
										{
											List<Operand> args = targetOperand.getOperands();

											FocusInfo focusInfo = new FocusInfo();
											focusInfo.setCount(count);

											if (args.size() == 2)
											{
												String arg0 = ((IdentifierOperand) args.get(0)).getValue();
												int target = ((DecimalOperand) args.get(1)).asInt();

												focusInfo.setTeam(Team.valueOf(arg0.toUpperCase()));
												focusInfo.setTarget(target);
											}
											else if (args.size() == 1)
											{
												focusInfo.setTarget(0);
											}
											else
												throw new ParseException("Expected at least one argument for target, instead found none");

											sbi.setFocusInfo(focusInfo);
										}
										else
											throw new ParseException("Expected an expression with decimals as arguments");
									}
									else // id.equals("tackle")
									{
										TackleInfo tackleInfo = new TackleInfo();

										tackleInfo.setCount(count);

										Expression expiresOperand = ((ExpressionOperand) argLst.get(0)).getValue();

										if (expiresOperand.getOperator().getIdentifier().equals("expires") && is(expiresOperand.getOperands().get(0), DecimalOperand.class))
										{
											int expires = ((DecimalOperand) expiresOperand.getOperands().get(0)).asInt();

											tackleInfo.setExpires(expires);
										}
										else
											throw new ParseException("Expected an expression for movable with decimal count");

										sbi.setTackleInfo(tackleInfo);
									}
								}
								else
									throw new ParseException("Expected last expression to be count, instead found this " + countOperand.toString());
							}
							else
								throw new ParseException("Expected last argument to be an expression for count, instead found this " + argLst.get(lastItemIndex).toString());
						}
						else if (id.equals("collision"))
						{
							// Pattern: (collision (ball) (player))

							Set<CollisionType> set = new HashSet<CollisionType>();

							for (Operand o: argLst)
							{
								if (is(o, IdentifierOperand.class))
								{
									set.add(CollisionType.NONE);
									break;
								}
								else if (is(o, ExpressionOperand.class))
								{
									String collisionType = ((ExpressionOperand) o).getValue().getOperator().getIdentifier();

									set.add(CollisionType.valueOf(collisionType.toUpperCase()));
								}
								else
									throw new ParseException("Unexpected operand found " + o.toString());
							}

							sbi.setCollisions(set);
						}
						else if (id.equals("foul"))
						{
							// Pattern: (foul (charged CYCLE) (card {none|yellow|red})))

							FoulInfo foulInfo = new FoulInfo();

							for (Operand o: argLst)
							{
								if (is(o, ExpressionOperand.class))
								{
									Expression exp = ((ExpressionOperand) o).getValue();
									
									if (exp.getOperator().getIdentifier().equals("charged"))
									{
										if (exp.getOperands().size() >= 1 && is(exp.getOperands().get(0), DecimalOperand.class))
										{
											int charged = ((DecimalOperand) exp.getOperands().get(0)).asInt();
											
											foulInfo.setCharged(charged);
										}
									}
									else if (exp.getOperator().getIdentifier().equals("card"))
									{
										if (exp.getOperands().size() >= 1 && is(exp.getOperands().get(0), IdentifierOperand.class))
										{
											String cardType = ((IdentifierOperand) exp.getOperands().get(0)).getValue();
											
											Card card = Card.valueOf(cardType.toUpperCase());
											
											foulInfo.setCard(card);
										}
									}
									else
										throw new ParseException("Unknown operator used: " + exp.getOperator().getIdentifier());
								}
								else
									throw new ParseException("Unexpected operand found " + o.toString());
							}

							sbi.setFoulInfo(foulInfo);
						}
					}
					else
						throw new ParseException("Expected an identifier, instead found this " + operator.toString());
				}

				else
					throw new ParseException("Unexpected operand, found this " + op.toString());
			}
			si = sbi;
		}
		return si;
	}

	// TODO: docs
	/**
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	private boolean is(Object obj1, Class<?> cls)
	{
		return obj1.getClass().isAssignableFrom(cls);
	}

}
