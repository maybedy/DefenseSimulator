package SimMain;

import java.io.InputStream;

import CommonInfo.CEInfo;
import CommonInfo.UUID;
import CommonInfo.UUID.UUIDSideType;
import EnvMultiEnv.MultiEnvModel;
import ModelMultiAgent_Blue.BlueMA;
import ModelMultiAgent_Red.RedMA;
import MsgC2Report.MsgReport;
import MsgCommon.MsgAngleDmg;
import MsgCommon.MsgDirectFire;
import ScenarioParsing.AgentFactory;
import ScenarioParsing.DefenseSimulatorScenarioProvider;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicAgentBasedModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;
import edu.kaist.seslab.ldef.parser.scenario.ParameterGroup;
import edu.kaist.seslab.ldef.parser.scenario.Scenario;
import edu.kaist.seslab.ldef.parser.scenario.ScenarioParser;

public class OutmostABM extends BasicAgentBasedModel {

	protected String _CS_Normal = "normal";

	@Override
	public boolean Delta(Message msg) {
		return true;
	}

	@Override
	public void createModels() {
		
		Scenario sce = this.getScenario();
		
		/*
		 * parsing CSV file phase
		 */
		
		AgentFactory _agentFactory = new AgentFactory(sce);
		_agentFactory.generatingAgent();
		
		
		// TODO 
		/*
		 * need csv file name from initializeScenario()
		 * think it how to code
		 */
		
		/*
		 * creating Model phase 
		 */
		SetModelName("OutmostABM");
		
		RedMA red = _agentFactory._redMAM;
		BlueMA blue = _agentFactory._blueMAM;
		
		MultiEnvModel multiEnv = new MultiEnvModel(_agentFactory._listOfAgent_B, _agentFactory._listOfAgent_R);
		
		red.Activated();
		blue.Activated();
		AddMultiAgentModel(red);
		AddMultiAgentModel(blue);
		
		addMultiEnvModel(multiEnv);
		
		AddCouplingState(_CS_Normal, true);
		
		AddCoupling(_CS_Normal, true, red, red._OE_LocUpdateOut, multiEnv, multiEnv._IE_LocUpdateIn);
		AddCoupling(_CS_Normal, true, blue, blue._OE_LocUpdateOut, multiEnv, multiEnv._IE_LocUpdateIn);
		AddCoupling(_CS_Normal, true, blue, blue._OE_AngleFireOut, multiEnv, multiEnv._IE_AngleFireIn);
		
		AddCoupling(_CS_Normal, true, red, red._OE_DirectFireOut, blue, blue._IE_DirectFireIn);
		AddCoupling(_CS_Normal, true, blue, blue._OE_DirectFireOut, red, red._IE_DirectFireIn);
		
		AddCoupling(_CS_Normal, true, multiEnv, multiEnv._OE_LocNoticeOutB, blue, blue._IE_LocNoticeIn);
		AddCoupling(_CS_Normal, true, multiEnv, multiEnv._OE_AngleDmgOutR, red, red._IE_AngleDmgIn);
		AddCoupling(_CS_Normal, true, multiEnv, multiEnv._OE_LocNoticeOutR, red, red._IE_LocNoticeIn);
	}

	@Override
	public void initializeScenario() {
		ClassLoader loader = getClass().getClassLoader();

		InputStream input = loader.getResourceAsStream("input_param.csv");
		
		DefenseSimulatorScenarioProvider sceProvider = new DefenseSimulatorScenarioProvider();
		
		try{
			Scenario sce= sceProvider.getScenario();
			ScenarioParser.parseCSVFile(input, sce);
			this.setScenario(sce);
			
		} catch (Exception e){
			e.printStackTrace();
		}

		
	}

}
