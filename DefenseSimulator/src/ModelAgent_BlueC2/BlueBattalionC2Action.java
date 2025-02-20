package ModelAgent_BlueC2;

import java.util.ArrayList;

import CommonInfo.CEInfo;
import CommonInfo.UUID;
import MsgC2Order.MsgFireOrder;
import MsgC2Order.MsgOrder;
import MsgC2Order.OrderType;
import MsgC2Report.MsgLocNotice;
import MsgC2Report.MsgReport;
import MsgC2Report.ReportType;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicActionModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class BlueBattalionC2Action extends BasicActionModel {
	
	//private static String _IE_OrderIn = "OrderIn";
	public static String _IE_ReportIn = "ReportIn";
	
	public static String _OE_OrderOut = "OrderOut";
	//private static String _OE_ReportOut = "ReportOut";
	
	private static String _CS_ShootCount = "ShootCount";
	private static String _CS_NumShooter = "NumberOfShooter";
	private static String _CS_ShooterList = "ShooterList";
	private static String _CS_CompanyList = "CompanyList";
//	private static String _CS_SensorList = "SensorList";
	
	private static String _AWS_WaitedOrder = "OrderList";
	private static String _AWS_RecentReport = "RecentReport";
	
	private static String _AS_Action = "Action";
	private enum _AS{
		WAIT, PROC
	}
	
	private boolean _isContinuable = true;
	
	public UUID _modelUUID;


	public BlueBattalionC2Action(CEInfo _myInfo, ArrayList<BlueCompany> _companyList, ArrayList<Shooter> _shooterList) {
		String _name = "BlueBattalionC2Action";
		SetModelName(_name);
		
		this._modelUUID = _myInfo._id;
		
		/*
		 * Add Input and output port
		 */
		
		AddInputEvent(_IE_ReportIn);
		
		AddOutputEvent(_OE_OrderOut);
		//AddOutputEvent(_OE_ReportOut);
		
		AddActState(_AS_Action, _AS.WAIT);
		
		AddConState(_CS_ShootCount, (int)0);
		AddConState(_CS_NumShooter, (int)_shooterList.size());
		AddConState(_CS_CompanyList, _companyList);
		AddConState(_CS_ShooterList, _shooterList);
		
		AddAwState(_AWS_WaitedOrder, new ArrayList<MsgOrder>());
		AddAwState(_AWS_RecentReport, null);
	}

	@Override
	public boolean Act(Message msg) {
		
		this.UpdateAWStateValue(_AWS_RecentReport, null);
		
		if(this.GetActStateValue(_AS_Action) == _AS.WAIT){
			// nothing happened
			return true;
		}else if(this.GetActStateValue(_AS_Action) == _AS.PROC){
			ArrayList<MsgOrder> _orderList = (ArrayList<MsgOrder>)this.GetAWStateValue(_AWS_WaitedOrder);
			
			MsgOrder _orderMsg = _orderList.remove(0);
			
			this.UpdateAWStateValue(_AWS_WaitedOrder, _orderList);
			
			msg.SetValue(_OE_OrderOut, _orderMsg);
			return true;
		}
		return false;
	}

	@Override
	public boolean Decide() {
		if(this.GetActStateValue(_AS_Action) == _AS.WAIT){
			ArrayList<MsgOrder> _orderList = (ArrayList<MsgOrder>)this.GetAWStateValue(_AWS_WaitedOrder);
			if(_orderList.isEmpty()){
				//TODO Continue();
				makeContinue();
			}else {
				this._isContinuable = false;
				ResetContinue();
				this.UpdateActStateValue(_AS_Action, _AS.PROC);
			}
			return true;

		}else if(this.GetActStateValue(_AS_Action) == _AS.PROC){
			if(this.GetAWStateValue(_AWS_RecentReport) == ReportType.EnemyInfo){
				makeContinue();
			}else if(this.GetAWStateValue(_AWS_RecentReport) == ReportType.LocationChange){
				makeContinue();
			}else if(this.GetAWStateValue(_AWS_RecentReport) == null){
				ArrayList<MsgOrder> _orderList = (ArrayList<MsgOrder>)this.GetAWStateValue(_AWS_WaitedOrder);
				if(_orderList.isEmpty()){
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
		ResetContinue();
		if(msg.GetDstEvent() == _IE_ReportIn){
			//immediately process
			MsgReport _reportMsg = (MsgReport)msg.GetValue();
			
			if(_reportMsg._reportType == ReportType.EnemyInfo){
				this.UpdateAWStateValue(_AWS_RecentReport, ReportType.EnemyInfo);
				
				MsgLocNotice _locNoticeList = (MsgLocNotice)_reportMsg._msgValue;
				ArrayList<CEInfo> _detectedList = _locNoticeList.GetNearByList();
				
				ArrayList<MsgFireOrder> _angleFireMsg = this.makeFireOrders(_detectedList);
				ArrayList<MsgOrder> _newOrderList = this.arrangeOrders(_angleFireMsg);
				
				ArrayList<MsgOrder> _storedOrder = (ArrayList<MsgOrder>)this.GetAWStateValue(_AWS_WaitedOrder);
				_storedOrder.addAll(_newOrderList);
				
				this.UpdateAWStateValue(_AWS_WaitedOrder, _storedOrder);
				
			}else if(_reportMsg._reportType == ReportType.LocationChange){
				//don't have to consider
				this.UpdateAWStateValue(_AWS_RecentReport, ReportType.LocationChange);
				
				makeContinue();
			}else if(_reportMsg._reportType == ReportType.Assessment){
				//don't have to consider
				this.UpdateAWStateValue(_AWS_RecentReport, ReportType.Assessment);
				
				makeContinue();
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
	
	public ArrayList<MsgFireOrder> makeFireOrders(ArrayList<CEInfo> _enemyList){
		ArrayList<MsgFireOrder> retList = new ArrayList<MsgFireOrder>();
		
		for(CEInfo _eachEnemy : _enemyList){
			if(_eachEnemy._HP <= 0){
				continue;
			}
			
			MsgFireOrder _fireOrdMsg = new MsgFireOrder(_eachEnemy);
			retList.add(_fireOrdMsg);
		}
		return retList;
	}
	
	public ArrayList<MsgOrder> arrangeOrders(ArrayList<MsgFireOrder> _fireOrderList){
		ArrayList<MsgOrder> _orderList = new ArrayList<MsgOrder>();
		
		int _shootCnt = (int)this.GetConStateValue(_CS_ShootCount);
		int _numShooter = (int)this.GetConStateValue(_CS_NumShooter);
		ArrayList<Shooter> _shooterList = (ArrayList<Shooter>)this.GetConStateValue(_CS_ShooterList);
		
		for(MsgFireOrder eachMsg : _fireOrderList){
			UUID _shooterUUID = _shooterList.get(_shootCnt%_numShooter)._modelUUID;
			_shootCnt++;
			
			MsgOrder _orderMsg = new MsgOrder(OrderType.AngleEngagement, _modelUUID, _shooterUUID, eachMsg);
			
			_orderList.add(_orderMsg);
		}
		
		this.UpdateConStateValue(_CS_ShootCount, _shootCnt);
		return _orderList;
	}
	
	public void makeContinue(){
		if(this._isContinuable){
			Continue();
		}else {
			ResetContinue();
		}
	}

}
