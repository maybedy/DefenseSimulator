package ModelAction_CEAction;

import CommonInfo.CEInfo;
import CommonInfo.DmgState;
import CommonInfo.UUID;
import CommonType.WTType;
import CommonType.WTTypeDirectParam;
import MsgC2Order.MsgDirEngOrder;
import MsgC2Order.MsgOrder;
import MsgC2Order.OrderType;
import MsgC2Report.MsgLocUpdate;
import MsgC2Report.MsgReport;
import MsgCommon.MsgDirectFire;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicActionModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class DirectEngagement extends BasicActionModel {
	
	public UUID _id;
	
	public static String _IE_OrderIn = "OrderIn";
	public static String _IE_MyInfoIn = "MyInfoIn";
	
	public static String _OE_DirectFireOut = "DirectFireOut";
	
	private static String _AWS_CurrentMission = "CurrentMission";
	
	private static String _AWS_DETECTED_ENEMY = "DetectedEnemy";
	
	private static String _CS_MYINFO = "MyInfo";
	
	private boolean _isContinuable = true;
	
	private String _AS_ACTION = "Action";
	private enum _AS{ 
		Stop, Fire
	}
	
	
	public DirectEngagement(CEInfo _myInfo) {
		String _name= "DirectEngagementAction";
		SetModelName(_name);
		
		AddInputEvent(_IE_OrderIn);
		AddInputEvent(_IE_MyInfoIn);
		
		AddOutputEvent(_OE_DirectFireOut);
		
		this._id = _myInfo._id;
		
		AddAwState(_AWS_DETECTED_ENEMY, null, true, STATETYPE_OBJECT);
		
		AddConState(_CS_MYINFO, _myInfo, true, STATETYPE_OBJECT);
		
		AddActState(_AS_ACTION, _AS.Stop, true, STATETYPE_CATEGORY);
		
		
	}

	@Override
	public boolean Act(Message msg) {
		if(this.GetActStateValue(_AS_ACTION) == _AS.Stop){
			return true;
		}else if(this.GetActStateValue(_AS_ACTION) == _AS.Fire){
			// TODO msg generating
			CEInfo _enemyInfo = (CEInfo)this.GetAWStateValue(_AWS_DETECTED_ENEMY);
			CEInfo _myInfo = (CEInfo)this.GetConStateValue(_CS_MYINFO);
			WTTypeDirectParam _myParam = (WTTypeDirectParam)_myInfo._weaponParam;
			MsgDirectFire _dirFireMsg = null;
			
			_dirFireMsg = new MsgDirectFire(_enemyInfo, _myParam);
			msg.SetValue(_OE_DirectFireOut, _dirFireMsg);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean Decide() {
		if(this.GetActStateValue(_AS_ACTION) == _AS.Stop){
			this._isContinuable = false;
			ResetContinue();
			this.UpdateActStateValue(_AS_ACTION, _AS.Fire);
			return true;
		}else if(this.GetActStateValue(_AS_ACTION) == _AS.Fire){
			MsgOrder _msgDirectEngOrder = (MsgOrder)this.GetAWStateValue(_AWS_CurrentMission);
			
			if(_msgDirectEngOrder._orderType == OrderType.STOP){
				this._isContinuable = false;
				ResetContinue();
				this.UpdateActStateValue(_AS_ACTION, _AS.Stop);
				this.UpdateAWStateValue(_AWS_DETECTED_ENEMY, null);
			}else if(_msgDirectEngOrder._orderType == OrderType.DirectEngagement){
				this._isContinuable = false;
				ResetContinue();
				this.UpdateActStateValue(_AS_ACTION, _AS.Fire);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean Perceive(Message msg) {
		if(msg.GetDstEvent() == _IE_OrderIn){
			MsgOrder _orderMsg = (MsgOrder)msg.GetValue();
			
			this.UpdateAWStateValue(_AWS_CurrentMission, _orderMsg);
			
			if(_orderMsg._orderType == OrderType.DirectEngagement){
				MsgDirEngOrder _dirOrdMsg = (MsgDirEngOrder)_orderMsg._orderMsg;
				this.UpdateAWStateValue(_AWS_DETECTED_ENEMY, _dirOrdMsg._enemyInfo);
			}else if(_orderMsg._orderType == OrderType.STOP){
				this.UpdateAWStateValue(_AWS_DETECTED_ENEMY, null);
			}
			
			return true;	
		}else if(msg.GetDstEvent() == _IE_MyInfoIn){
			MsgReport _reportMsg = (MsgReport)msg.GetValue();
			MsgLocUpdate _locUpdateMsg = (MsgLocUpdate)_reportMsg._msgValue;
			
			this.UpdateConStateValue(_CS_MYINFO, _locUpdateMsg._myInfo);
			
			makeContinue();
			return true;
		}
		
		return false;
	}

	@Override
	public double TimeAdvance() {
		this._isContinuable = true;
		if(this.GetActStateValue(_AS_ACTION) == _AS.Stop){
			return Double.POSITIVE_INFINITY;
		}else if(this.GetActStateValue(_AS_ACTION) == _AS.Fire){
			return 1;
		}
		return 0;
	}


	public void makeContinue(){
		if(this._isContinuable){
			Continue();
		}else {
			ResetContinue();
		}
	}
}
