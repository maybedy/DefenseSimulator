package ModelAgent_RedC2;

import java.util.ArrayList;

import CommonInfo.CEInfo;
import CommonInfo.UUID;
import CommonInfo.XY;
import CommonPathFinder.PathFinder;
import MsgC2Order.MsgMoveOrder;
import MsgC2Order.MsgOrder;
import MsgC2Order.OrderType;
import MsgC2Report.MsgLocNotice;
import MsgC2Report.MsgLocUpdate;
import MsgC2Report.MsgReport;
import MsgC2Report.ReportType;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicActionModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class RedCompanyC2Action extends BasicActionModel {

	public static String _IE_ReportIn = "ReportIn";
	public static String _IE_OrderIn = "OrderIn";
	
//	public static String _OE_ReportOut = "ReportOut";
	public static String _OE_OrderOut = "OrderOut";
	
	
	
	private static String _AS_Action = "Action";
	private enum _AS{
		WAIT, PROC
	}
	
	
	private static String _CS_SpreadOut = "Spread"; // true or false; initially false
	private static String _CS_ShootCount = "ShootCount";
	private static String _CS_Mode = "Mode";
	private enum _MODE{
		FIRE, MOVE
	}
	
	private static String _AWS_FireObject = "FireObject";
	private static String _AWS_WaitedOrder = "OrderList";
	private static String _AWS_RecentReport = "RecentReport";
	
	private static String _AWS_MyInfo = "MyInfo";
	
	public UUID _modelUUID;
	
	public RedCompanyC2Action(CEInfo _myInfo) {
		String _name = "RedCompanyC2Action";
		SetModelName(_name);
		
		this._modelUUID = _myInfo._id;
		/*
		 * Add Input and output port
		 */
		
		AddInputEvent(_IE_ReportIn);
		AddInputEvent(_IE_OrderIn);
		
		AddOutputEvent(_OE_OrderOut);
//		AddOutputEvent(_OE_ReportOut);
		
		AddActState(_AS_Action, _AS.WAIT);
		AddConState(_CS_SpreadOut, false);
		AddConState(_CS_Mode, _MODE.MOVE);
		
		AddConState(_CS_ShootCount, (int)0);
	
		AddAwState(_AWS_WaitedOrder, new ArrayList<MsgOrder>());
		AddAwState(_AWS_RecentReport, null);
		AddAwState(_AWS_FireObject, null);
	}

	@Override
	public boolean Act(Message arg0) {
		if(this.GetActStateValue(_AS_Action) == _AS.WAIT){
			return true;
		}else if(this.GetActStateValue(_AS_Action) == _AS.PROC){
			return true;
		}
		return false;
	}

	@Override
	public boolean Decide() {
		if(this.GetActStateValue(_AS_Action) == _AS.WAIT){
			this.UpdateActStateValue(_AS_Action, _AS.PROC);
			return true;
		}else if(this.GetActStateValue(_AS_Action) == _AS.PROC){
			this.UpdateActStateValue(_AS_Action, _AS.WAIT);
			return true;
		}
		return false;
	}

	@Override
	public boolean Perceive(Message msg) {
		if(msg.GetDstEvent() == _IE_ReportIn){
			//immediately process
			MsgReport _reportMsg = (MsgReport)msg.GetValue();
			
			if(_reportMsg._reportType == ReportType.EnemyInfo){
				MsgLocNotice _locNotice = (MsgLocNotice)_reportMsg._msgValue;
				ArrayList<CEInfo> objectList = _locNotice._nearbyList;
				if(this.GetConStateValue(_CS_Mode) == _MODE.FIRE){
					//find objective enemy
					CEInfo object = (CEInfo)this.GetAWStateValue(_AWS_FireObject);
					CEInfo _currentObj;
					
					for(CEInfo eachInfo : objectList){
						if(eachInfo._id.equals(object._id)){
							_currentObj = eachInfo;
							break;
						}
					}

					if(_currentObj._HP <= 0){
						//stop fire or next fire
						///////
						if(objectList.size() > 1){
							MsgOrder _newOrder = new MsgOrder(OrderType.STOP, this._modelUUID, this._modelUUID, null);
							this.UpdateAWStateValue(_AWS_FireObject, null);	
						}else {
							
							MsgOrder _newOrder = new MsgOrder(OrderType.DirectEngagement, this._modelUUID, this._modelUUID, );
							this.UpdateAWStateValue(_AWS_FireObject, );	
						}
						
					}else {
						//keep fire
						this.UpdateAWStateValue(_AWS_FireObject, eachInfo);	
					}
				}else if(this.GetConStateValue(_CS_Mode)== _MODE.MOVE){
					// make new  object
					
					CEInfo object = objectList.get(0);
					
					
				}
				
			}else if(_reportMsg._reportType == ReportType.LocationChange){
				MsgLocUpdate _locUpdate = (MsgLocUpdate)_reportMsg._msgValue;
				CEInfo _newInfo =_locUpdate._myInfo;
				this.UpdateAWStateValue(_AWS_MyInfo, _newInfo);
				
			}else if(_reportMsg._reportType == ReportType.Assessment){
				MsgLocUpdate _locUpdate = (MsgLocUpdate)_reportMsg._msgValue;
				CEInfo _newInfo =_locUpdate._myInfo;
				this.UpdateAWStateValue(_AWS_MyInfo, _newInfo);
				
				Continue();
				return true;
				//TODO if dead?
			}
			
			if(this.GetActStateValue(_AS_Action) == _AS.WAIT){
				return true;
			}else if(this.GetActStateValue(_AS_Action) == _AS.PROC){
				return true;
			}
		}else if(msg.GetDstEvent() == _IE_OrderIn){
			MsgOrder _orderMsg = (MsgOrder)msg.GetValue();
			
			if(_orderMsg._orderType == OrderType.Move){
				this.UpdateAWStateValue(_AWS_RecentReport, OrderType.Move);
				
				MsgMoveOrder _moveOrder = (MsgMoveOrder)_orderMsg._orderMsg;
				MsgOrder _newOrder = new MsgOrder(OrderType.Move, this._modelUUID, this._modelUUID, _moveOrder);
				ArrayList<MsgOrder> _waitedOrder = (ArrayList<MsgOrder>)this.GetAWStateValue(_AWS_WaitedOrder);
				_waitedOrder.add(_newOrder);
				this.UpdateAWStateValue(_AWS_WaitedOrder, _waitedOrder);
				
				
				return true;
			}else if(_orderMsg._orderType == OrderType.SpreadOut){
				
				this.UpdateConStateValue(_CS_SpreadOut, true);
				this.UpdateAWStateValue(_AWS_RecentReport, OrderType.SpreadOut);				
				
				if((boolean)_orderMsg._orderMsg == true){
					//emergency status, automatically activate
				}else {
					// not emergency status
					this.UpdateConStateValue(_CS_Mode, _MODE.MOVE);
					// make move order
					PathFinder.calculatePath();
					ArrayList<XY> _path = PathFinder.getPath(...);
					MsgMoveOrder _moveOrder = new MsgMoveOrder(_path);
					MsgOrder _newOrder = new MsgOrder(OrderType.Move, this._modelUUID, this._modelUUID, _moveOrder);
					ArrayList<MsgOrder> _waitedOrder = (ArrayList<MsgOrder>)this.GetAWStateValue(_AWS_WaitedOrder);
					_waitedOrder.add(_newOrder);
					this.UpdateAWStateValue(_AWS_WaitedOrder, _waitedOrder);
					
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public double TimeAdvance() {
		if(this.GetActStateValue(_AS_Action) == _AS.WAIT){
			return Double.POSITIVE_INFINITY;
		}else if(this.GetActStateValue(_AS_Action) == _AS.PROC){
			return 0;
		}
		return 0;
	}

}
