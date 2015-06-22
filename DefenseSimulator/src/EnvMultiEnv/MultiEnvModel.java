package EnvMultiEnv;

import java.util.ArrayList;

import CommonInfo.CEInfo;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicMultiEnvModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class MultiEnvModel extends BasicMultiEnvModel {
	
	public String _IE_LocUpdateIn = "LocUpdateIn";
	public String _IE_AngleFireIn = "AngleFireIn";
	
	public String _OE_LocNoticeOut = "LocNoticeOut";
	public String _OE_AngleDmgOut = "AngleDmgOut";
	
	private String _CS_Normal = "normal";

	public MultiEnvModel(ArrayList<CEInfo> _listOfAgents_B, ArrayList<CEInfo> _listOfAgents_R) {
		String name = "MultiEnvModel";
		SetModelName(name);
		
		AddInputEvent(_IE_AngleFireIn);
		AddInputEvent(_IE_LocUpdateIn);
		
		AddOutputEvent(_OE_AngleDmgOut);
		AddOutputEvent(_OE_LocNoticeOut);
		
		AddCouplingState(_CS_Normal, true);
		
		WarEnvironment warEnv = new WarEnvironment(_listOfAgents_B, _listOfAgents_R);
		
		AddCoupling(_CS_Normal, true, this, this._IE_AngleFireIn, warEnv, warEnv._IE_AngleFireIn);
		AddCoupling(_CS_Normal, true, this, this._IE_LocUpdateIn, warEnv, warEnv._IE_LocUpdateIn);
		
		AddCoupling(_CS_Normal, true, warEnv, warEnv._OE_AngleDmgOut, this, this._OE_AngleDmgOut);
		AddCoupling(_CS_Normal, true, warEnv, warEnv._OE_LocNoticeOut, this, this._OE_LocNoticeOut);
		
	
	}

	@Override
	public boolean Delta(Message arg0) {
		// TODO Auto-generated method stub
		return true;
	}

}
