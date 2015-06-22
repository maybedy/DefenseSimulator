package ModelAction_CEAction;

import java.util.ArrayList;

import CommonInfo.CEInfo;
import CommonInfo.XY;
import CommonType.WTTypeAngleParam;
import MsgC2Order.MsgFireOrder;
import MsgC2Order.MsgOrder;
import MsgC2Report.MsgLocUpdate;
import MsgC2Report.MsgReport;
import MsgCommon.MsgAngleFire;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicActionModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class AngleEngagement extends BasicActionModel {
	
	public static String _IE_OrderIn = "OrderIn";
	public static String _IE_MyInfoIn = "MyInfoIn";
	
	public static String _OE_AngleFireOut = "AngleFireOut";
	
	//private static String _AWS_DETECTED_ENEMY = "DetectedEnemy";
	
	//private static String _AWS_ORDERTYPE = "OrderType";
	//private static String _AWS_ASSESSMENT = "Assessment";
	
	private static String _AWS_Q_AngleOrder = "AngleFireOrder";
	
	
	private static String _CS_MYINFO = "MyInfo";
		
	
	private String _AS_ACTION = "Action";
	private enum _AS{ 
		Stop, Fire
	}
	

	public AngleEngagement(CEInfo _myInfo) {
		String _name= "AngleEngagementAction";
		SetModelName(_name);
		
		AddInputEvent(_IE_OrderIn);
		AddInputEvent(_IE_MyInfoIn);
		AddOutputEvent(_OE_AngleFireOut);
		
		AddAwState(_AWS_Q_AngleOrder, new ArrayList<MsgFireOrder>());
		
		AddConState(_CS_MYINFO, _myInfo, true, STATETYPE_OBJECT);
		
		AddActState(_AS_ACTION, _AS.Stop, true, STATETYPE_CATEGORY);
		
	}

	@Override
	public boolean Act(Message msg) {
		if(this.GetActStateValue(_AS_ACTION) == _AS.Stop){
			return true;
		}else if(this.GetActStateValue(_AS_ACTION) == _AS.Fire){
			// TODO msg generating
			ArrayList<MsgFireOrder> orderList = (ArrayList<MsgFireOrder>)this.GetAWStateValue(_AWS_Q_AngleOrder);
			
			if(orderList.isEmpty()){
				// TODO maybe error
				System.out.println("empty list");
			}else {
				MsgFireOrder _orderFireMsg = orderList.remove(0);
				
				this.UpdateAWStateValue(_AWS_Q_AngleOrder, orderList);
				CEInfo _enemy = _orderFireMsg._enemyInfo;
				XY _impactLoc = _enemy._myLoc;
 				
				CEInfo _myInfo = (CEInfo)this.GetConStateValue(_CS_MYINFO);
				WTTypeAngleParam _myAngleParam = (WTTypeAngleParam)_myInfo._weaponParam;
				//TODO Make new MsgAngleFire
				MsgAngleFire _newAngleFire = new MsgAngleFire(_impactLoc, _myAngleParam);
				msg.SetValue(_OE_AngleFireOut, _newAngleFire);
			}
			
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean Decide() {
		if(this.GetActStateValue(_AS_ACTION) == _AS.Stop){
			this.UpdateActStateValue(_AS_ACTION, _AS.Fire);
			return true;
		}else if(this.GetActStateValue(_AS_ACTION) == _AS.Fire){
			ArrayList<MsgFireOrder> orderList = (ArrayList<MsgFireOrder>)this.GetAWStateValue(_AWS_Q_AngleOrder);
			if(orderList.isEmpty()){
				this.UpdateActStateValue(_AS_ACTION, _AS.Stop);
			}else {
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
			MsgFireOrder _fireOrdMsg = (MsgFireOrder)_orderMsg._orderMsg;
			ArrayList<MsgFireOrder> orderList = (ArrayList<MsgFireOrder>)this.GetAWStateValue(_AWS_Q_AngleOrder);
			
			orderList.add(_fireOrdMsg);
			this.UpdateAWStateValue(_AWS_Q_AngleOrder, orderList);
			
			if(this.GetActStateValue(_AS_ACTION) == _AS.Stop){
				
			}else if(this.GetActStateValue(_AS_ACTION)== _AS.Fire){
				Continue();
			}
			return true;	
		}else if(msg.GetDstEvent() == _IE_MyInfoIn){
			//won't be happened
			MsgReport _reportMsg = (MsgReport)msg.GetValue();
			MsgLocUpdate _locMsg = (MsgLocUpdate)_reportMsg._msgValue;
			
			this.UpdateConStateValue(_CS_MYINFO, _locMsg._myInfo);
			Continue();
			return true;
		}
		return false;
	}

	@Override
	public double TimeAdvance() {
		if(this.GetActStateValue(_AS_ACTION) == _AS.Stop){
			return Double.POSITIVE_INFINITY;
		}else if(this.GetActStateValue(_AS_ACTION) == _AS.Fire){
			return 0;
		}
		return 0;
	}

}
