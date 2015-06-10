package Sim.main;

import java.io.InputStream;

import MultiAgentModel.BlueMA;
import MultiAgentModel.RedMA;
import MultiEnv.MultiEnvModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicAgentBasedModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class OutmostABM extends BasicAgentBasedModel {
	
	protected String _INT_LocUpdateIn = "LocUpdateIn";
	protected String _INT_LocUpdateOut = "LocUpdateOut";
	
	protected String _INT_LocNoticeIn = "LocNoticeIn";
	protected String _INT_LocNoticeOut = "LocNoticeOut";
	
	protected String _INT_AngleFireOut = "AngleFireOut";
	protected String _INT_AngleFireIn = "AngleFireIn";
	
	protected String _INT_AngleDmgOut = "AngleDmgOut";
	protected String _INT_AngleDmgIn = "AngleDmgIn";
	
	protected String _INT_DirectFireOut = "DirectFireOut";
	protected String _INT_DirectFireIn = "DirectFireIn";
	
	protected String _CS_Normal = "normal";
	
	

	public OutmostABM() {
	}

	@Override
	public boolean Delta(Message arg0) {
		return true;
	}

	@Override
	public void createModels() {
		
		/*
		 * parsing CSV file phase
		 */
		
		
		// TODO 
		/*
		 * need csv file name from initializeScenario()
		 * think it how to code
		 */
		
		
		
		
		
		/*
		 * creating Model phase 
		 */
		SetModelName("OutmostABM");
		
		RedMA red = new RedMA();
		BlueMA blue = new BlueMA();
		
		MultiEnvModel multiEnv = new MultiEnvModel();
		
		AddMultiAgentModel(red);
		AddMultiAgentModel(blue);
		
		addMultiEnvModel(multiEnv);
		
		AddCouplingState(_CS_Normal, true);
		
		AddCoupling(_CS_Normal, true, red, _INT_DirectFireOut, blue, _INT_DirectFireIn);
		AddCoupling(_CS_Normal, true, blue, _INT_DirectFireOut, red, _INT_DirectFireIn);
		
		
		AddCoupling(_CS_Normal, true, red, _INT_LocUpdateOut, multiEnv, _INT_LocUpdateIn);
		AddCoupling(_CS_Normal, true, red, _INT_AngleFireOut, multiEnv, _INT_AngleFireIn);
		
		AddCoupling(_CS_Normal, true, blue, _INT_LocUpdateOut, multiEnv, _INT_LocUpdateIn);
		AddCoupling(_CS_Normal, true, blue, _INT_AngleFireOut, multiEnv, _INT_AngleFireIn);
		
		AddCoupling(_CS_Normal, true, multiEnv, _INT_LocNoticeOut, blue, _INT_LocNoticeIn);
		AddCoupling(_CS_Normal, true, multiEnv, _INT_LocNoticeOut, red, _INT_LocNoticeIn);
		AddCoupling(_CS_Normal, true, multiEnv, _INT_AngleDmgOut, blue, _INT_AngleDmgIn);
		AddCoupling(_CS_Normal, true, multiEnv, _INT_AngleDmgOut, red, _INT_AngleDmgIn);
	}

	@Override
	public void initializeScenario() {
		ClassLoader loader = getClass().getClassLoader();

		InputStream input = loader.getResourceAsStream("input_param.csv");
		
		try{
			
			
		} catch (Exception e){
			e.printStackTrace();
		}

		
	}

}
