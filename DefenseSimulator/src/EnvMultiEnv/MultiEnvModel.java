package EnvMultiEnv;

import java.util.ArrayList;

import CommonInfo.CEInfo;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicMultiEnvModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class MultiEnvModel extends BasicMultiEnvModel {
	
	public static String _IE_LocUpdateIn = "LocUpdateIn";
	public static String _IE_AngleFireIn = "AngleFireIn";
	
	public static String _OE_LocNoticeOutB = "LocNoticeOut_B";
	public static String _OE_AngleDmgOutB = "AngleDmgOut_B";
	
	public static String _OE_LocNoticeOutR = "LocNoticeOut_R";
	public static String _OE_AngleDmgOutR = "AngleDmgOut_R";
	
	private String _CS_Normal = "normal";

	public MultiEnvModel(ArrayList<CEInfo> _listOfAgents_B, ArrayList<CEInfo> _listOfAgents_R) {
		String name = "MultiEnvModel";
		SetModelName(name);
		
		AddInputEvent(_IE_AngleFireIn);
		AddInputEvent(_IE_LocUpdateIn);
		
		AddOutputEvent(_OE_AngleDmgOutB);
		AddOutputEvent(_OE_AngleDmgOutR);
		AddOutputEvent(_OE_LocNoticeOutB);
		AddOutputEvent(_OE_LocNoticeOutR);
		
		AddCouplingState(_CS_Normal, true);
		
		WarEnvironment warEnv = new WarEnvironment(_listOfAgents_B, _listOfAgents_R);
		warEnv.Activated();
		this.addComponent(warEnv);
		
		AddCoupling(_CS_Normal, true, this, this._IE_AngleFireIn, warEnv, warEnv._IE_AngleFireIn);
		AddCoupling(_CS_Normal, true, this, this._IE_LocUpdateIn, warEnv, warEnv._IE_LocUpdateIn);
		
		AddCoupling(_CS_Normal, true, warEnv, warEnv._OE_AngleDmgOutB, this, this._OE_AngleDmgOutB);
		AddCoupling(_CS_Normal, true, warEnv, warEnv._OE_LocNoticeOutB, this, this._OE_LocNoticeOutB);

		AddCoupling(_CS_Normal, true, warEnv, warEnv._OE_AngleDmgOutR, this, this._OE_AngleDmgOutR);
		AddCoupling(_CS_Normal, true, warEnv, warEnv._OE_LocNoticeOutR, this, this._OE_LocNoticeOutR);
		
	
	}

	@Override
	public boolean Delta(Message msg) {
		return true;
	}

}
