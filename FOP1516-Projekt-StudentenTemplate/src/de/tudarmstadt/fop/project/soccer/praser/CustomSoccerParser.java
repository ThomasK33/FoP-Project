/**
 * 
 */
package de.tudarmstadt.fop.project.soccer.praser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
							else if (id.equals("P") || id.equals("B") || id.equals("G") || id.equals("F"))
							{
								// TODO: write each case into it's own if case and create blank objects of said instance
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
