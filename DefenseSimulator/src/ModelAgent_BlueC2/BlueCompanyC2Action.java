package ModelAgent_BlueC2;

import java.util.ArrayList;

import CommonInfo.CEInfo;
import CommonInfo.UUID;
import MsgC2Order.MsgDirEngOrder;
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
	private boolean _isContinuable = true;
	
	
	public UUID _modelUUID;
	
	
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
		AddConState(_CS_Mode, _MODE.STOP);

		AddConState(_CS_ShootCount, (int)0);
		
		AddAwState(_AWS_WaitedReport, new ArrayList<MsgReport>());
		AddAwState(_AWS_WaitedOrder, new ArrayList<MsgOrder>());
		AddAwState(_AWS_RecentReport, null);
		AddAwState(_AWS_FireObject, null);
		
		AddAwState(_AWS_MyInfo, _myInfo);
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
		CEInfo _myInfo = (CEInfo) this.GetAWStateValue(_AWS_MyInfo);
		if(_myInfo._HP <= 0 ){
			this._isContinuable = false;
			ResetContinue();
			this.UpdateActStateValue(_AS_Action, _AS.WAIT);
			return true;
		}
		if(this.GetActStateValue(_AS_Action) == _AS.WAIT){
			if(this.GetAWStateValue(_AWS_RecentReport) == ReportType.EnemyInfo){
				this._isContinuable = false;
				ResetContinue();
				this.UpdateActStateValue(_AS_Action, _AS.PROC);
			}
			else if(this.GetAWStateValue(_AWS_RecentReport) == ReportType.LocationChange){
				makeContinue();
			}
			else if(this.GetAWStateValue(_AWS_RecentReport) == ReportType.Assessment){
				makeContinue();
			}
			return true;
		}else if(this.GetActStateValue(_AS_Action) == _AS.PROC){
			if(this.GetAWStateValue(_AWS_RecentReport) == ReportType.EnemyInfo){
				makeContinue();
			}else if(this.GetAWStateValue(_AWS_RecentReport) == ReportType.LocationChange){
				makeContinue();
			}
			else if(this.GetAWStateValue(_AWS_RecentReport) == ReportType.Assessment){
				makeContinue();
			}
			else if(this.GetAWStateValue(_AWS_RecentReport) == null){
				ArrayList<MsgOrder> _orderList = (ArrayList<MsgOrder>)this.GetAWStateValue(_AWS_WaitedOrder);
				ArrayList<MsgReport> _reportList = (ArrayList<MsgReport>)this.GetAWStateValue(_AWS_WaitedReport);
				if(_orderList.isEmpty() && _reportList.isEmpty()){
					this._isContinuable = false;
					ResetContinue();
					this.UpdateActStateValue(_AS_Action, _AS.WAIT);
					
				}else {
					this._isContinuable = false;
					ResetContinue();
					this.UpdateActStateValue(_AS_Action, _AS.PROC);
				}
				
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean Perceive(Message msg) {
		CEInfo _myInfo = (CEInfo) this.GetAWStateValue(_AWS_MyInfo);
		if(_myInfo._HP <= 0 ){
			Continue();
			return true;
		}
		if(msg.GetDstEvent() == _IE_ReportIn){
			//immediately process
			MsgReport _reportMsg = (MsgReport)msg.GetValue();
			ResetContinue();
			if(_reportMsg._reportType == ReportType.EnemyInfo){
				this.UpdateAWStateValue(_AWS_RecentReport, ReportType.EnemyInfo);
				
				MsgLocNotice _locNoticeList = (MsgLocNotice)_reportMsg._msgValue;
				MsgLocNotice _newLocNotice = new MsgLocNotice(_locNoticeList);
				MsgReport _newReport = new MsgReport(ReportType.EnemyInfo, this._modelUUID, null, _newLocNotice);
				this.addNewReport(_newReport);
				
				ArrayList<CEInfo> _detectedList = _locNoticeList.GetNearByList();
				
				if(_detectedList.isEmpty()){
					makeContinue();
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
						makeContinue();
						return true;
					}else {
						_detectedList.remove(_currentObj);
						
						if(_currentObj._HP <= 0){
							//stop fire or next fire
							///////
							CEInfo _newObject = null;
							if(_detectedList.size() <= 0){
								
							}else {
								for(CEInfo eachInfo : _detectedList){
									if(eachInfo._HP > 0){
										_newObject = eachInfo;
										break;
									}
								}
							}
							
							if(_newObject == null){

								MsgOrder _newOrder = new MsgOrder(OrderType.STOP, this._modelUUID, this._modelUUID, null);
								this.UpdateAWStateValue(_AWS_FireObject, null);
								this.UpdateConStateValue(_CS_Mode, _MODE.STOP);
								this.addNewOrder(_newOrder);
							}
							else {
								MsgDirEngOrder _newDirOrder =new MsgDirEngOrder(_newObject);
								MsgOrder _newOrder = new MsgOrder(OrderType.DirectEngagement, this._modelUUID, this._modelUUID, _newDirOrder);
								this.UpdateAWStateValue(_AWS_FireObject, _newObject);
								this.UpdateConStateValue(_CS_Mode, _MODE.FIRE);
								this.addNewOrder(_newOrder);
							}
						}else {
							//keep fire
							this.UpdateAWStateValue(_AWS_FireObject, _currentObj);	
						}
					}
					
				}else if(this.GetConStateValue(_CS_Mode)== _MODE.STOP){
					
					CEInfo _newObject = null;
					for(CEInfo eachInfo : _detectedList){
						if(eachInfo._HP > 0){
							_newObject = eachInfo;
							break;
						}
					}
					if(_newObject == null){
						makeContinue();
					}else {
						MsgDirEngOrder _newDirOrder =new MsgDirEngOrder(_newObject);
						MsgOrder _newOrder = new MsgOrder(OrderType.DirectEngagement, this._modelUUID, this._modelUUID, _newDirOrder);
						this.UpdateAWStateValue(_AWS_FireObject, _newObject);
						this.UpdateConStateValue(_CS_Mode, _MODE.FIRE);
						this.addNewOrder(_newOrder);
					}
				}
				/////////////////////////FINALLY DONE
				
				
			}else if(_reportMsg._reportType == ReportType.LocationChange){
				ResetContinue();
				//don't have to report upper model
				this.UpdateAWStateValue(_AWS_RecentReport, ReportType.LocationChange);
				MsgLocUpdate _locUpdate = (MsgLocUpdate)_reportMsg._msgValue;
				this.UpdateAWStateValue(_AWS_MyInfo, new CEInfo(_locUpdate._myInfo));
				if(_locUpdate._myInfo._HP <= 0){
					
				}else {
					makeContinue();	
				}
				////////done 
			}
			else if(_reportMsg._reportType == ReportType.Assessment){
				ResetContinue();
				//don't have to report upper model
				this.UpdateAWStateValue(_AWS_RecentReport, ReportType.Assessment);
				MsgLocUpdate _locUpdate = (MsgLocUpdate)_reportMsg._msgValue;
				this.UpdateAWStateValue(_AWS_MyInfo, new CEInfo(_locUpdate._myInfo));
				if(_locUpdate._myInfo._HP <= 0){
					
				}else {
					makeContinue();	
				}
				///////done 
			}
			return true;
		}
		return false;
	}

	@Override
	public double TimeAdvance() {
		this._isContinuable = true;
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
	

	public void makeContinue(){
		if(this._isContinuable){
			Continue();
		}else {
			ResetContinue();
		}
	}
	
}
