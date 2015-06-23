package ScenarioParsing;

import java.util.ArrayList;

import edu.kaist.seslab.ldef.parser.scenario.ParameterGroup;
import edu.kaist.seslab.ldef.parser.scenario.Scenario;
import CommonInfo.CEInfo;
import ModelMultiAgent_Blue.BlueMA;
import ModelMultiAgent_Red.RedMA;

public class AgentFactory {
	
	public ArrayList<CEInfo> _listOfAgent_B;
	public ArrayList<CEInfo> _listOfAgent_R;
	public RedMA _redMAM;
	public BlueMA _blueMAM;

	public AgentFactory(Scenario sce) {
		ParameterGroup rootGroup = sce.getParams();
		ParameterGroup generalInfo = rootGroup.getParameterGroup("General Information");
		ParameterGroup simParam = generalInfo.getParameterGroup("Simulation Params");
		String _AgentLocFileName = (String)simParam.getParameterValueOfFirstHit("AgentLocFile");
		
		ParameterGroup CEInfoGroup = rootGroup.getParameterGroup("CEInfo");
		ParameterGroup WTTypeParamGroup = rootGroup.getParameterGroup("WTTypeParam");
		
		
	}
	
	
	public void generatingAgent(){
		
	}
	
	

}
