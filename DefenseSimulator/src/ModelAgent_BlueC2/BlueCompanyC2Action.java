package ModelAgent_BlueC2;

import java.util.ArrayList;

import CommonInfo.CEInfo;
import CommonInfo.UUID;
import MsgC2Order.MsgDirEngOrder;
import MsgC2Order.MsgFireOrder;
import MsgC2Order.MsgOrder;
import MsgC2Order.OrderType;
import MsgC2Report.MsgLocNotice;
import MsgC2Report.MsgLocUpdate;
import MsgC2Report.MsgReport;
import MsgC2Report.ReportType;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicActionModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class BlueCompanyC2Action extends BasicActionModel {
	public static String _IE_ReportIn = "ReportIn";
	
	public static String _OE_ReportOut = "ReportOut";
	public static String _OE_OrderOut = "OrderOut";
	
	private static String _AS_Action = "Action";
	private enum _AS{
		WAIT, PROC
	}

	private static String _CS_ShootCount = "ShootCount";
	private static String _CS_Mode = "Mode";
	private enum _MODE{
		FIRE, STOP
	}
	
	private static String _AWS_FireObject = "FireObject";
	
	private static String _AWS_WaitedOrder = "OrderList";
	private static String _AWS_WaitedReport = "ReportList";
	
	private static String _AWS_RecentReport = "RecentReport";
	
	private static String _AWS_MyInfo = "MyInfo";
	
	public UUID _modelUUID;
	public UUID _upperUUID;
	
	
	public BlueCompanyC2Action(CEInfo _myInfo) {
		String _name = "BlueCompanyC2Action";
		SetModelName(_name);
		this._modelUUID = _myInfo._id;
		/*
		 * Add Input and output port
		 */
		
		AddInputEvent(_IE_ReportIn);
		
		AddOutputEvent(_OE_OrderOut);
		
		AddOutputEvent(_OE_ReportOut);
		
		AddActState(_AS_Action, _AS.WAIT);
		

		AddConState(_CS_ShootCount, (int)0);
		
		AddAwState(_AWS_WaitedReport, new ArrayList<MsgReport>());
		AddAwState(_AWS_WaitedOrder, new ArrayList<MsgOrder>());
		AddAwState(_AWS_RecentReport, null);
		AddAwState(_AWS_FireObject, null);
	}

	@Override
	public boolean Act(Message msg) {
		this.UpdateAWStateValue(_AWS_RecentReport, null);
		
		if(this.GetActStateValue(_AS_Action) == _AS.WAIT){
			// nothing happened
			return true;
		}else if(this.GetActStateValue(_AS_Action) == _AS.PROC){
			ArrayList<MsgOrder> _orderList = (ArrayList<MsgOrder>)this.GetAWStateValue(_AWS_WaitedOrder);
			
			if(_orderList.isEmpty()){
				ArrayList<MsgReport> _reportList = (ArrayList<MsgReport>)this.GetAWStateValue(_AWS_WaitedReport);
				
				MsgReport _reportMsg = _reportList.remove(0);
				
				this.UpdateAWStateValue(_AWS_WaitedReport, _reportList);
				msg.SetValue(_OE_ReportOut, _reportMsg);
			}else {
				MsgOrder _orderMsg = _orderList.remove(0);
				
				this.UpdateAWStateValue(_AWS_WaitedOrder, _orderList);
				
				msg.SetValue(_OE_OrderOut, _orderMsg);
			}
			
			return true;
		}
		return false;
	}

	@Override
	public boolean Decide() {
		if(this.GetActStateValue(_AS_Action) == _AS.WAIT){
			if(this.GetAWStateValue(_AWS_RecentReport) == ReportType.EnemyInfo){
				this.UpdateActStateValue(_AS_Action, _AS.PROC);
			}else if(this.GetAWStateValue(_AWS_RecentReport) == ReportType.LocationChange){
				Continue();
			}
			return true;
		}else if(this.GetActStateValue(_AS_Action) == _AS.PROC){
			if(this.GetAWStateValue(_AWS_RecentReport) == ReportType.EnemyInfo){
				Continue();
			}else if(this.GetAWStateValue(_AWS_RecentReport) == ReportType.LocationChange){
				Continue();
			}else if(this.GetAWStateValue(_AWS_RecentReport) == null){
				ArrayList<MsgOrder> _orderList = (ArrayList<MsgOrder>)this.GetAWStateValue(_AWS_WaitedOrder);
				ArrayList<MsgReport> _reportList = (ArrayList<MsgReport>)this.GetAWStateValue(_AWS_WaitedReport);
				if(_orderList.isEmpty() && _reportList.isEmpty()){
					this.UpdateActStateValue(_AS_Action, _AS.WAIT);
					
				}else {
					this.UpdateActStateValue(_AS_Action, _AS.PROC);
				}
				
			}
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
				this.UpdateAWStateValue(_AWS_RecentReport, ReportType.EnemyInfo);
				
				
				MsgLocNotice _locNoticeList = (MsgLocNotice)_reportMsg._msgValue;
				MsgLocNotice _newLocNotice = new MsgLocNotice(_locNoticeList);
				MsgReport _newReport = new MsgReport(ReportType.EnemyInfo, this._modelUUID, null, _newLocNotice);
				this.addNewReport(_newReport);
				
				ArrayList<CEInfo> _detectedList = _locNoticeList.GetNearByList();
				
				if(_detectedList.isEmpty()){
					Continue();
					return true;
				}
				
				if(this.GetConStateValue(_CS_Mode) == _MODE.FIRE){
					CEInfo object = (CEInfo)this.GetAWStateValue(_AWS_FireObject);
					CEInfo _currentObj = null;

					for(CEInfo eachInfo : _detectedList){
						if(eachInfo._id.equals(object._id)){
							_currentObj = eachInfo;
							break;
						}
					}
					
					if(_currentObj == null){
						Continue();
						return true;
					}else {
						_detectedList.remove(_currentObj);
						
						if(_currentObj._HP <= 0){
							//stop fire or next fire
							///////
							if(_detectedList.size() <= 1){
								MsgOrder _newOrder = new MsgOrder(OrderType.STOP, this._modelUUID, this._modelUUID, null);
								this.UpdateAWStateValue(_AWS_FireObject, null);
								this.UpdateConStateValue(_CS_Mode, _MODE.STOP);
								this.addNewOrder(_newOrder);
								
							}else {
								CEInfo _newObject = _detectedList.remove(0);
								MsgDirEngOrder _newDirOrder =new MsgDirEngOrder(_newObject);
								MsgOrder _newOrder = new MsgOrder(OrderType.DirectEngagement, this._modelUUID, this._modelUUID, _newDirOrder);
								this.UpdateAWStateValue(_AWS_FireObject, _newObject);
								this.addNewOrder(_newOrder);
							}
							
						}else {
							//keep fire
							this.UpdateAWStateValue(_AWS_FireObject, _currentObj);	
						}
					}
					
				}else if(this.GetConStateValue(_CS_Mode)== _MODE.STOP){
					CEInfo _newObject;
					_newObject = _detectedList.remove(0);
					MsgDirEngOrder _newDirOrder =new MsgDirEngOrder(_newObject);
					MsgOrder _newOrder = new MsgOrder(OrderType.DirectEngagement, this._modelUUID, this._modelUUID, _newDirOrder);
					this.UpdateAWStateValue(_AWS_FireObject, _newObject);
					this.UpdateConStateValue(_CS_Mode, _MODE.FIRE);
					this.addNewOrder(_newOrder);
				}
				/////////////////////////FINALLY DONE
				
				
			}else if(_reportMsg._reportType == ReportType.LocationChange){
				//don't have to report upper model
				this.UpdateAWStateValue(_AWS_RecentReport, ReportType.LocationChange);
				MsgLocUpdate _locUpdate = (MsgLocUpdate)_reportMsg._msgValue;
				this.UpdateAWStateValue(_AWS_MyInfo, new CEInfo(_locUpdate._myInfo));
				
				Continue();
				
				////////done 
			}
			else if(_reportMsg._reportType == ReportType.Assessment){
				//don't have to report upper model
				this.UpdateAWStateValue(_AWS_RecentReport, ReportType.Assessment);
				MsgLocUpdate _locUpdate = (MsgLocUpdate)_reportMsg._msgValue;
				this.UpdateAWStateValue(_AWS_MyInfo, new CEInfo(_locUpdate._myInfo));
				
				Continue();
				
				///////done 
			}
			return true;
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
	
	private void addNewOrder(MsgOrder _newOrder){
		ArrayList<MsgOrder> _orderList = (ArrayList<MsgOrder>)this.GetAWStateValue(_AWS_WaitedOrder);
		_orderList.add(_newOrder);
		this.UpdateAWStateValue(_AWS_WaitedOrder, _orderList);
	}
	
	private void addNewReport(MsgReport _newReport){
		
		ArrayList<MsgReport> _reportList = (ArrayList<MsgReport>)this.GetAWStateValue(_AWS_WaitedReport);
		_reportList.add(_newReport);
		
		this.UpdateAWStateValue(_AWS_WaitedReport, _reportList);
		
		
	}
	
//	
//	
//	public ArrayList<MsgFireOrder> makeFireOrders(ArrayList<CEInfo> _enemyList){
//		ArrayList<MsgFireOrder> retList = new ArrayList<MsgFireOrder>();
//		
//		for(CEInfo _eachEnemy : _enemyList){
//			if(_eachEnemy._HP <= 0){
//				continue;
//			}
//			
//			MsgFireOrder _fireOrdMsg = new MsgFireOrder(_eachEnemy);
//			retList.add(_fireOrdMsg);
//		}
//		return retList;
//	}
//	
//	public ArrayList<MsgOrder> arrangeOrders(ArrayList<MsgFireOrder> _fireOrderList){
//		ArrayList<MsgOrder> _orderList = new ArrayList<MsgOrder>();
//		
//		int _shootCnt = (int)this.GetConStateValue(_CS_ShootCount);
//		
//		for(MsgFireOrder eachMsg : _fireOrderList){
//			_shootCnt++;
//			
//			MsgOrder _orderMsg = new MsgOrder(OrderType.AngleEngagement, _modelUUID, _modelUUID, eachMsg);
//			
//			_orderList.add(_orderMsg);
//		}
//		
//		this.UpdateConStateValue(_CS_ShootCount, _shootCnt);
//		return _orderList;
//	}


}
