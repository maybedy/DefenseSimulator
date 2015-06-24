package EnvMultiEnv;

import java.util.ArrayList;

import CommonInfo.CEInfo;
import EnvElem.DmgManager;
import EnvElem.DummyModel;
import EnvElem.LocManager;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicEnvModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class WarEnvironment extends BasicEnvModel {
	
	public static String _IE_LocUpdateIn = "LocUpdateIn";
	public static String _IE_AngleFireIn = "AngleFireIn";

	public static String _OE_LocNoticeOutB = "LocNoticeOut_B";
	public static String _OE_AngleDmgOutB = "AngleDmgOut_B";
	
	public static String _OE_LocNoticeOutR = "LocNoticeOut_R";
	public static String _OE_AngleDmgOutR = "AngleDmgOut_R";
	
	private String _CS_Normal = "normal";

	public WarEnvironment(ArrayList<CEInfo> _listOfAgents_B, ArrayList<CEInfo> _listOfAgents_R) {
		String name = "WarEnvModel";
		SetModelName(name);

		AddInputEvent(_IE_AngleFireIn);
		AddInputEvent(_IE_LocUpdateIn);
		
		AddOutputEvent(_OE_AngleDmgOutB);
		AddOutputEvent(_OE_LocNoticeOutB);
		
		AddOutputEvent(_OE_AngleDmgOutR);
		AddOutputEvent(_OE_LocNoticeOutR);
		
		AddCouplingState(_CS_Normal, true);
		
		DummyModel _dummy = new DummyModel();
		LocManager locMgr = new LocManager(_listOfAgents_B, _listOfAgents_R);
		DmgManager dmgMgr = new DmgManager();
		locMgr.Activated();
		dmgMgr.Activated();
		this.AddEnvElement("LocationManager", locMgr);
		this.AddEnvElement("DamageManager", dmgMgr);
		
		AddCoupling(_CS_Normal, true, this, this._IE_AngleFireIn, dmgMgr, dmgMgr._IE_AngFire);
		AddCoupling(_CS_Normal, true, this, this._IE_LocUpdateIn, locMgr, locMgr._IE_LocUpdate);
		
		AddCoupling(_CS_Normal, true, dmgMgr, dmgMgr._OE_AngleDmgOutB, this, this._OE_AngleDmgOutB);
		AddCoupling(_CS_Normal, true, locMgr, locMgr._OE_LocNoticeOutB, this, this._OE_LocNoticeOutB);
		
		AddCoupling(_CS_Normal, true, dmgMgr, dmgMgr._OE_AngleDmgOutR, this, this._OE_AngleDmgOutR);
		AddCoupling(_CS_Normal, true, locMgr, locMgr._OE_LocNoticeOutR, this, this._OE_LocNoticeOutR);
		
		//TODO add internal changes
		AddCoupling(_CS_Normal, true, locMgr, locMgr._OE_LocUpdate, dmgMgr, dmgMgr._IE_LocUpdate);
		
		AddCoupling(_CS_Normal, true, locMgr, locMgr._OE_dummy, _dummy, _dummy._IE_input);
		
	}

	@Override
	public boolean Delta(Message arg0) {
		// TODO Auto-generated method stub
		return true;
	}

}
