/**
 * 
 */
package de.tudarmstadt.fop.project.test.soccer.parser;

import org.junit.Assert;
import org.junit.Test;

import de.tudarmstadt.fop.project.Factory;
import de.tudarmstadt.fop.project.parser.Lexer;
import de.tudarmstadt.fop.project.parser.ParseException;
import de.tudarmstadt.fop.project.soccer.parser.SoccerParser;
import de.tudarmstadt.fop.project.soccer.sensor.SensorInformation;
import de.tudarmstadt.fop.project.test.InstanceGenerator;

/**
 * @author Thomas Kosiewski
 *
 */
public class SoccerParserTest extends TemplateSoccerParserTest
{
	
	@Test
	public void templateTestSeeFlagInformation2() throws ParseException {
		String input = "(see 0 ((f c t) 6.7 27 0 0) ((f r t) 58.6 3) ((f g r b) 73 37) ((g r) 69.4 32) ((f g r t) 66 27) ((f p r c) 55.7 41) ((f p r t) 45.2 22) ((f t 0) 6.3 -18 0 0) ((f t r 10) 16.1 -7 0 0) ((f t r 20) 26 -4 0 0) ((f t r 30) 36.2 -3) ((f t r 40) 46.1 -2) ((f t r 50) 56.3 -2) ((f r 0) 73.7 30) ((f r t 10) 68.7 23) ((f r t 20) 66 15) ((f r t 30) 64.1 6) ((f r b 10) 79 37) ((f r b 20) 85.6 42))";
		
		Factory factory = InstanceGenerator.instantiateFactory();
		Lexer lexer = factory.instantiateExpressionLexer(input);
		SoccerParser parser = factory.instantiateSoccerParser(lexer);
		
		SensorInformation info = parser.extractSensorInformation();

		Assert.assertEquals("See [time=0, objectInfos=[SoccerObjectInfo [soccerObject=FieldFlag [hPos=CENTER, vPos=TOP], distance=6.7, direction=27, distChange=0, dirChange=0, headFacingDir=0, bodyFacingDir=0], SoccerObjectInfo [soccerObject=FieldFlag [hPos=RIGHT, vPos=TOP], distance=58.6, direction=3, distChange=null, dirChange=null, headFacingDir=0, bodyFacingDir=0], SoccerObjectInfo [soccerObject=GoalFlag [hPos=RIGHT, vPos=BOTTOM], distance=73, direction=37, distChange=null, dirChange=null, headFacingDir=0, bodyFacingDir=0], SoccerObjectInfo [soccerObject=Goal [position=RIGHT], distance=69.4, direction=32, distChange=null, dirChange=null, headFacingDir=0, bodyFacingDir=0], SoccerObjectInfo [soccerObject=GoalFlag [hPos=RIGHT, vPos=TOP], distance=66, direction=27, distChange=null, dirChange=null, headFacingDir=0, bodyFacingDir=0], SoccerObjectInfo [soccerObject=PenaltyFlag [hPos=RIGHT, vPos=CENTER], distance=55.7, direction=41, distChange=null, dirChange=null, headFacingDir=0, bodyFacingDir=0], SoccerObjectInfo [soccerObject=PenaltyFlag [hPos=RIGHT, vPos=TOP], distance=45.2, direction=22, distChange=null, dirChange=null, headFacingDir=0, bodyFacingDir=0], SoccerObjectInfo [soccerObject=VirtualFlag [distance=0, hPos=null, vPos=TOP], distance=6.3, direction=-18, distChange=0, dirChange=0, headFacingDir=0, bodyFacingDir=0], SoccerObjectInfo [soccerObject=VirtualFlag [distance=10, hPos=RIGHT, vPos=TOP], distance=16.1, direction=-7, distChange=0, dirChange=0, headFacingDir=0, bodyFacingDir=0], SoccerObjectInfo [soccerObject=VirtualFlag [distance=20, hPos=RIGHT, vPos=TOP], distance=26, direction=-4, distChange=0, dirChange=0, headFacingDir=0, bodyFacingDir=0], SoccerObjectInfo [soccerObject=VirtualFlag [distance=30, hPos=RIGHT, vPos=TOP], distance=36.2, direction=-3, distChange=null, dirChange=null, headFacingDir=0, bodyFacingDir=0], SoccerObjectInfo [soccerObject=VirtualFlag [distance=40, hPos=RIGHT, vPos=TOP], distance=46.1, direction=-2, distChange=null, dirChange=null, headFacingDir=0, bodyFacingDir=0], SoccerObjectInfo [soccerObject=VirtualFlag [distance=50, hPos=RIGHT, vPos=TOP], distance=56.3, direction=-2, distChange=null, dirChange=null, headFacingDir=0, bodyFacingDir=0], SoccerObjectInfo [soccerObject=VirtualFlag [distance=0, hPos=RIGHT, vPos=null], distance=73.7, direction=30, distChange=null, dirChange=null, headFacingDir=0, bodyFacingDir=0], SoccerObjectInfo [soccerObject=VirtualFlag [distance=10, hPos=RIGHT, vPos=TOP], distance=68.7, direction=23, distChange=null, dirChange=null, headFacingDir=0, bodyFacingDir=0], SoccerObjectInfo [soccerObject=VirtualFlag [distance=20, hPos=RIGHT, vPos=TOP], distance=66, direction=15, distChange=null, dirChange=null, headFacingDir=0, bodyFacingDir=0], SoccerObjectInfo [soccerObject=VirtualFlag [distance=30, hPos=RIGHT, vPos=TOP], distance=64.1, direction=6, distChange=null, dirChange=null, headFacingDir=0, bodyFacingDir=0], SoccerObjectInfo [soccerObject=VirtualFlag [distance=10, hPos=RIGHT, vPos=BOTTOM], distance=79, direction=37, distChange=null, dirChange=null, headFacingDir=0, bodyFacingDir=0], SoccerObjectInfo [soccerObject=VirtualFlag [distance=20, hPos=RIGHT, vPos=BOTTOM], distance=85.6, direction=42, distChange=null, dirChange=null, headFacingDir=0, bodyFacingDir=0]]]",
				info.toString());
	}
	
	@Test
	public void templateTestHearInformation2() throws ParseException {
		String input = "(hear 32 -89 our 1 \"some\")";
		
		Factory factory = InstanceGenerator.instantiateFactory();
		Lexer lexer = factory.instantiateExpressionLexer(input);
		SoccerParser parser = factory.instantiateSoccerParser(lexer);
		
		SensorInformation info = parser.extractSensorInformation();
		
		Assert.assertEquals("HearInfo [time=32, sender=OUR, direction=-89, nr=1, message=some]", 
				info.toString());
	}
	
	@Test
	public void templateTestSenseBodyInformation2() throws ParseException{
		String input = "(sense_body 0 (view_mode high normal) (stamina 8000 1 130600) (speed 0 0) (head_angle 0) (kick 0) (dash 0) (turn 0) (say 0) (turn_neck 0) (catch 0) (move 0) (change_view 0) (arm (movable 0) (expires 0) (target 0 0) (count 0)) (focus (target none) (count 0)) (tackle (expires 0) (count 0)) (collision none) (foul  (charged 0) (card none)))";
		
		Factory factory = InstanceGenerator.instantiateFactory();
		Lexer lexer = factory.instantiateExpressionLexer(input);
		SoccerParser parser = factory.instantiateSoccerParser(lexer);
		
		SensorInformation info = parser.extractSensorInformation();

		Assert.assertEquals("SenseBodyInfo [time=0, viewIntensity=HIGH, viewMode=NORMAL, stamina=8000, effort=1, capacity=130600, speed=0, speedDir=0, headAngle=0, kickCount=0, dashCount=0, turnCount=0, sayCount=0, turnNeckCount=0, catchCount=0, moveCount=0, changeViewCount=0, armInfo=ArmInfo [movable=0, expires=0, dist=0, dir=0, count=0], tackleInfo=TackleInfo [expires=0, count=0], collisions=[NONE], foulInfo=FoulInfo [charged=0, card=NONE], focusInfo=FocusInfo [target=0, team=null, count=0]]",
				info.toString());
	}

}
