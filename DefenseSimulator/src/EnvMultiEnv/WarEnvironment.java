package EnvMultiEnv;

import java.util.ArrayList;

import CommonInfo.CEInfo;
import EnvElem.DmgManager;
import EnvElem.LocManager;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicEnvModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class WarEnvironment extends BasicEnvModel {
	
	public String _IE_LocUpdateIn = "LocUpdateIn";
	public String _IE_AngleFireIn = "AngleFireIn";
	
	public String _OE_LocNoticeOut = "LocNoticeOut";
	public String _OE_AngleDmgOut = "AngleDmgOut";

	private String _CS_Normal = "normal";

	public WarEnvironment(ArrayList<CEInfo> _listOfAgents_B, ArrayList<CEInfo> _listOfAgents_R) {
		String name = "WarEnvModel";
		SetModelName(name);

		AddInputEvent(_IE_AngleFireIn);
		AddInputEvent(_IE_LocUpdateIn);
		
		AddOutputEvent(_OE_AngleDmgOut);
		AddOutputEvent(_OE_LocNoticeOut);
		
		AddCouplingState(_CS_Normal, true);
		
		LocManager locMgr = new LocManager(_listOfAgents_B, _listOfAgents_R);
		DmgManager dmgMgr = new DmgManager();
		
		AddCoupling(_CS_Normal, true, this, this._IE_AngleFireIn, dmgMgr, dmgMgr._IE_AngFire);
		AddCoupling(_CS_Normal, true, this, this._IE_LocUpdateIn, locMgr, locMgr._IE_LocUpdate);
		
		AddCoupling(_CS_Normal, true, dmgMgr, dmgMgr._OE_AngDmg, this, this._OE_AngleDmgOut);
		AddCoupling(_CS_Normal, true, locMgr, locMgr._OE_LocNotice, this, this._OE_LocNoticeOut);
		
		//TODO add internal changes
		AddCoupling(_CS_Normal, true, locMgr, locMgr._OE_LocUpdate, dmgMgr, dmgMgr._IE_LocUpdate);
		
		
	}

	@Override
	public boolean Delta(Message arg0) {
		// TODO Auto-generated method stub
		return true;
	}

}
