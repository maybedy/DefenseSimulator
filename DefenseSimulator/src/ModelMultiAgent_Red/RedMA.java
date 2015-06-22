package ModelMultiAgent_Red;

import java.util.ArrayList;

import CommonInfo.CEInfo;
import CommonInfo.UUID;
import ModelAgent_RedC2.RedBattalionC2;
import ModelAgent_RedC2.RedCompany;
import MsgC2Report.MsgLocNotice;
import MsgC2Report.MsgReport;
import MsgCommon.MsgAngleDmg;
import MsgCommon.MsgDirectFire;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicMultiAgentModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class RedMA extends BasicMultiAgentModel {

	public static String _IE_LocNoticeIn = "LocNoticeIn";
	public static String _IE_AngleDmgIn = "AngleDmgIn";	
	public static String _IE_DirectFireIn = "DirectFireIn";
	
	public static String _OE_LocUpdateOut = "LocUpdateOut";
	public static String _OE_DirectFireOut = "DirectFireOut";

	protected static String _CS_Normal = "Normal";
	private static String _CS_MsgBranch = "Branch";
	
	public UUID _modelUUID;
	
	private ArrayList<RedBattalion> _battalionList;
	
	public RedMA(CEInfo _myInfo, ArrayList<RedBattalion> _battalionList) {
		
		String _name = "RedMultiAgent";
		SetModelName(_name);
		this._modelUUID = _myInfo._id;
		
		/*
		 * Add input and output port
		 */
		
		AddInputEvent(_IE_AngleDmgIn);
		AddInputEvent(_IE_DirectFireIn);
		AddInputEvent(_IE_LocNoticeIn);

		AddOutputEvent(_OE_DirectFireOut);
		AddOutputEvent(_OE_LocUpdateOut);
		
		/*
		 * Add Coupling State
		 */
		
		AddCouplingState(_CS_Normal, true);
		AddCouplingState(_CS_MsgBranch, -1);
		
		/*
		 * Add Component
		 */
		this._battalionList = _battalionList;
		
		for(RedBattalion _eachBattalion : _battalionList){
			_eachBattalion.Activated();
			this.addComponent(_eachBattalion);
			
			UUID _batUUID = _eachBattalion._modelUUID;
			
			this.AddCoupling(_CS_MsgBranch, _batUUID.getUniqID_MAM(), this, this._IE_AngleDmgIn, _eachBattalion, _eachBattalion._IE_AngleDmgIn);
			this.AddCoupling(_CS_MsgBranch, _batUUID.getUniqID_MAM(), this, this._IE_DirectFireIn, _eachBattalion, _eachBattalion._IE_DirectFireIn);
			this.AddCoupling(_CS_MsgBranch, _batUUID.getUniqID_MAM(), this, this._IE_LocNoticeIn, _eachBattalion, _eachBattalion._IE_LocNoticeIn);

			this.AddCoupling(_CS_Normal, true, _eachBattalion, _eachBattalion._OE_DirectFireOut, this, this._OE_DirectFireOut);
			this.AddCoupling(_CS_Normal, true, _eachBattalion, _eachBattalion._OE_LocUpdateOut, this, this._OE_LocUpdateOut);
		}
		
	}

	@Override
	public boolean Delta(Message msg) {
		if(msg.GetDstEvent() == this._IE_AngleDmgIn){
			MsgAngleDmg _angleDmgMsg = (MsgAngleDmg)msg.GetValue();
			
			
			this.updateCouplingState(_CS_MsgBranch, _angleDmgMsg._destUUID.getUniqID_MAM(), true);
			
			return true;
		}else if(msg.GetDstEvent() == this._IE_DirectFireIn){
			MsgDirectFire _dirFireMsg = (MsgDirectFire)msg.GetValue();
			CEInfo _targetInfo = _dirFireMsg._target;
			UUID _targetID= _targetInfo._id;
			
			this.updateCouplingState(_CS_MsgBranch, _targetID.getUniqID_MAM(), true);
			return true;
		}else if(msg.GetDstEvent() == this._IE_LocNoticeIn){
			MsgReport _reportMsg = (MsgReport)msg.GetValue();
			MsgLocNotice _locNotMsg = (MsgLocNotice)_reportMsg._msgValue;
			
			this.updateCouplingState(_CS_MsgBranch, _reportMsg._destUUID.getUniqID_MAM(), true);
			return true;
		}else {
			this.updateCouplingState(_CS_MsgBranch, -1, true);
			return true;
		}
	}

}
