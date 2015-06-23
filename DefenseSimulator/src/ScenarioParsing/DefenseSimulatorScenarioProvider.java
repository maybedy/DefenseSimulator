package ScenarioParsing;

import edu.kaist.seslab.ldef.parser.scenario.Parameter;
import edu.kaist.seslab.ldef.parser.scenario.ParameterGroup;
import edu.kaist.seslab.ldef.parser.scenario.Scenario;
import edu.kaist.seslab.ldef.parser.scenario.ScenarioProvider;

public class DefenseSimulatorScenarioProvider implements ScenarioProvider {

	public static String _CEInfo_Agent[] = { "BlueBattalionC2",
		"BlueCompanyC2",
		"BlueSensor",
		"BlueShooter",
		"RedBattalionC2",
		"RedCompanyC2"
		};
	
	public static String _CEInfo_Params[] = {"HP",
		"Movable",
		"maxSpeed",
		"detectable",
		"detectRange",
		"dengageable",
		"weaponType"
	};
	
	public static String _WT_Angle_Side[]= {
		"BlueAngle"
	};
	
	public static String _WT_Angle_Params[] = {
		"vertical_error",
		"horizontal_error",
		"casualty_radius",
		"time_for_flight"
	};
	
	public static String _WT_Direct_Side[]= {
		"BlueDirect",
		"RedDirect"
	};
	
	public static String _WT_Direct_Params[] ={
		
	};


	@Override
	public Scenario getScenario() throws Exception {
		
		
		
		Scenario sce = new Scenario("DefenseSimulator Scenario");
		ParameterGroup group = sce.getParams();
		
				
		//////////////////////////////////////////////////////////////////////////
		/*
		* For General Information
		*/
		//////////////////////////////////////////////////////////////////////////
		ParameterGroup generalInfoGroup = new ParameterGroup(group, "General Information");
		group.addParameterGroup(generalInfoGroup);
		
		ParameterGroup simRegGroup = new ParameterGroup(generalInfoGroup, "Simulation Region");
		ParameterGroup simParGroup = new ParameterGroup(generalInfoGroup, "Simulation Params");
		generalInfoGroup.addParameterGroup(simRegGroup);
		generalInfoGroup.addParameterGroup(simParGroup);
		

		simRegGroup.addParameter(new Parameter(simRegGroup, Parameter.TYPE_DOUBLE, "Start_Lat"));
		simRegGroup.addParameter(new Parameter(simRegGroup, Parameter.TYPE_DOUBLE, "Start_Lon"));
		simRegGroup.addParameter(new Parameter(simRegGroup, Parameter.TYPE_DOUBLE, "End_Lat"));
		simRegGroup.addParameter(new Parameter(simRegGroup, Parameter.TYPE_DOUBLE, "End_Lon"));
		
		
		simParGroup.addParameter(new Parameter(simParGroup, Parameter.TYPE_STRING, "Simulation Endtime"));
		simParGroup.addParameter(new Parameter(simParGroup, Parameter.TYPE_STRING, "OSM file"));
		simParGroup.addParameter(new Parameter(simParGroup, Parameter.TYPE_STRING, "AgentLocFile"));
		
		

		//////////////////////////////////////////////////////////////////////////
		/*
		 * For CEInfo
		 */
		//////////////////////////////////////////////////////////////////////////
		
		ParameterGroup CEInfoGroup = new ParameterGroup(group, "CEInfo");
		group.addParameterGroup(CEInfoGroup);
		
		for(int i = 0; i< _CEInfo_Agent.length;i++) {
			ParameterGroup CEInfo_AgentGroup = new ParameterGroup(CEInfoGroup, _CEInfo_Agent[i]);
			CEInfoGroup.addParameterGroup(CEInfo_AgentGroup);
			
			for(int j = 0;j < _CEInfo_Params.length;i++){
				CEInfo_AgentGroup.addParameter(new Parameter(CEInfo_AgentGroup, Parameter.TYPE_STRING, _CEInfo_Params[j]));
			}
			
		}
		

		//////////////////////////////////////////////////////////////////////////
		/*
		 * For WTType
		 */
		//////////////////////////////////////////////////////////////////////////
		
		ParameterGroup WTTypeParamGroup = new ParameterGroup(group, "WTTypeParam");
		group.addParameterGroup(WTTypeParamGroup);
		
		for(int i = 0; i< _WT_Angle_Side.length;i++) {
			ParameterGroup WTAngleSideGroup = new ParameterGroup(WTTypeParamGroup, _WT_Angle_Side[i]);
			WTTypeParamGroup.addParameterGroup(WTAngleSideGroup);
			
			for(int j = 0;j < _WT_Angle_Params.length;i++){
				WTAngleSideGroup.addParameter(new Parameter(WTAngleSideGroup, Parameter.TYPE_STRING, _WT_Angle_Params[j]));
			}
			
		}
		
		for(int i = 0; i< _WT_Direct_Side.length;i++) {
			ParameterGroup WTDirectSideGroup = new ParameterGroup(WTTypeParamGroup, _WT_Direct_Side[i]);
			WTTypeParamGroup.addParameterGroup(WTDirectSideGroup);
			
			for(int j = 0;j < _WT_Direct_Params.length;i++){
				WTDirectSideGroup.addParameter(new Parameter(WTDirectSideGroup, Parameter.TYPE_STRING, _WT_Direct_Params[j]));
			}
			
		}
		
		return null;
	}

}
