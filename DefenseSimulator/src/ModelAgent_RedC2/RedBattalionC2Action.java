package ModelAgent_RedC2;

import java.util.ArrayList;
import java.util.HashMap;

import CommonInfo.CEInfo;
import CommonInfo.UUID;
import CommonInfo.XY;
import CommonMap.GridInfo;
import ModelAgent_BlueC2.BlueCompany;
import MsgC2Order.MsgOrder;
import MsgC2Order.OrderType;
import MsgC2Report.MsgLocUpdate;
import MsgC2Report.MsgReport;
import MsgC2Report.ReportType;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicActionModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class RedBattalionC2Action extends BasicActionModel {
	
	
	private static String _IE_OrderIn = "OrderIn";
	public static String _IE_ReportIn = "ReportIn";
	
	public static String _OE_OrderOut = "OrderOut";
	public static String _OE_ReportOut = "ReportOut";
	
	
	private static String _CS_BSpreadOut = "SpreadOutStatus"; // Map<index, boolean>
	private static String _CS_CompanyList = "CompanyList"; // ArrayList<BlueCompany>
	
	
	private static String _AWS_RecentReport = "RecentReport"; // ReportType
	private static String _AWS_WaitedOrder = "OrderList"; // ArrayList<MsgOrder>
	
	private static String _AS_Action = "Action";
	private enum _AS{
		WAIT, PROC
	}
	
	private static String _CS_STATUS = "Status";
	private enum _STATUS {
		ACTIVATE, SPREADOUT
	}
	
	
	public UUID _myUUID;

	public RedBattalionC2Action(CEInfo myInfo, ArrayList<BlueCompany> _companyList) {
		String _name = "RedBattalionC2Action";
		SetModelName(_name);
		
		this._myUUID = myInfo._id;
		/*
		 * Add Input and output port
		 */
		
		//AddInputEvent(_IE_OrderIn);
		AddInputEvent(_IE_ReportIn);
		
		AddOutputEvent(_OE_OrderOut);
		AddOutputEvent(_OE_ReportOut);

		AddActState(_AS_Action, _AS.WAIT);
		
		AddConState(_CS_STATUS, _STATUS.ACTIVATE);
		
		HashMap<Integer, Boolean> _spreadOutStatus = new HashMap<Integer, Boolean>();
		for(BlueCompany eachCompany : _companyList){
			UUID _comInfo = eachCompany._modelUUID;
			Integer uniqID = new Integer(_comInfo.getUniqID_Batt());
			_spreadOutStatus.put(uniqID, true);
		}
		AddConState(_CS_BSpreadOut, _spreadOutStatus);
		
		AddConState(_CS_CompanyList, _companyList);
		
		
		AddAwState(_AWS_WaitedOrder, new ArrayList<MsgOrder>());
		AddAwState(_AWS_RecentReport, null);
	}

	@Override
	public boolean Act(Message msg) {
		this.UpdateAWStateValue(_AWS_RecentReport, null);
		
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
	
	private boolean checkSpreadOut(UUID _companyInfo)
	{
		HashMap<Integer, Boolean> _spreadOutStatus = (HashMap<Integer, Boolean>)this.GetConStateValue(_CS_BSpreadOut);
		
		Integer uniqID = _companyInfo.getUniqID_Batt();
		
		if(_spreadOutStatus.get(uniqID)){
			return true;
		}else {
			return false;
		}
	}
	
	private boolean updateCompanyInfo(CEInfo _comInfo){
		ArrayList<CEInfo> _companyList = (ArrayList<CEInfo>)this.GetConStateValue(_CS_CompanyList);
		CEInfo _modifiedInfo = null;
		
		for(CEInfo eachInfo : _companyList){
			if(eachInfo._id.equals(_comInfo._id)){
				_modifiedInfo = eachInfo;
				break;
			}
		}
		
		if(_modifiedInfo == null){
			return false;
		}
		
		_companyList.remove(_modifiedInfo);
		_companyList.add(_comInfo);
		
		return true;
	
	}

	@Override
	public boolean Perceive(Message msg) {
		if(msg.GetDstEvent() == _IE_ReportIn){
			//immediately process
			MsgReport _reportMsg = (MsgReport)msg.GetValue();
			UUID srcUUID = _reportMsg._srcUUID;
			
			if(_reportMsg._reportType == ReportType.EnemyInfo){
				//detected status 
				this.UpdateAWStateValue(_AWS_RecentReport, ReportType.EnemyInfo);
				if(this.checkSpreadOut(srcUUID)){
					// TODO continue;
					Continue();
					return true;
				}else {
					MsgOrder _orderMsg =new MsgOrder(OrderType.SpreadOut, this._myUUID, srcUUID, null);
					ArrayList<MsgOrder> _waitedOrder = (ArrayList<MsgOrder>)this.GetAWStateValue(_AWS_WaitedOrder);
					_waitedOrder.add(_orderMsg);
					this.UpdateAWStateValue(_AWS_WaitedOrder, _waitedOrder);
					if(this.GetActStateValue(_AS_Action) == _AS.WAIT){
						return true;
					}else if(this.GetActStateValue(_AS_Action) == _AS.PROC){
						Continue();
						return true;
					}
				}
				
			}else if(_reportMsg._reportType == ReportType.LocationChange){
				this.UpdateAWStateValue(_AWS_RecentReport, ReportType.LocationChange);
				
				MsgLocUpdate _locUpdateMsg = (MsgLocUpdate)_reportMsg._msgValue;
				CEInfo _companyInfo = _locUpdateMsg._myInfo;
				this.updateCompanyInfo(_companyInfo);
				if(this.checkSpreadOut(srcUUID)){
					// TODO continue;
					Continue();
					return true;
				}else {
					
					GridInfo _currentGrid = _companyInfo._currentGrid;
					XY _currentLoc = _companyInfo._myLoc;
					if(_currentGrid._mainLoc.equalsWithError(_currentLoc)){
						if(){
							MsgOrder _orderMsg =new MsgOrder(OrderType.SpreadOut, this._myUUID, srcUUID, null);
							ArrayList<MsgOrder> _waitedOrder = (ArrayList<MsgOrder>)this.GetAWStateValue(_AWS_WaitedOrder);
							_waitedOrder.add(_orderMsg);
							this.UpdateAWStateValue(_AWS_WaitedOrder, _waitedOrder);
						}else {
							
						}
					}else {
						
					}
					
					if(this.GetActStateValue(_AS_Action) == _AS.WAIT){
						return true;
					}else if(this.GetActStateValue(_AS_Action) == _AS.PROC){
						Continue();
						return true;
					}
				}

				
			}else if(_reportMsg._reportType == ReportType.Assessment){
				this.UpdateAWStateValue(_AWS_RecentReport, ReportType.Assessment);
				
				
				MsgLocUpdate _locUpdateMsg = (MsgLocUpdate)_reportMsg._msgValue;
				CEInfo _companyInfo = _locUpdateMsg._myInfo;
				this.updateCompanyInfo(_companyInfo);

				
				if(this.checkSpreadOut(srcUUID)){
					// TODO continue;
					Continue();
					return true;
				}else {
					MsgOrder _orderMsg =new MsgOrder(OrderType.SpreadOut, this._myUUID, srcUUID, null);
					ArrayList<MsgOrder> _waitedOrder = (ArrayList<MsgOrder>)this.GetAWStateValue(_AWS_WaitedOrder);
					_waitedOrder.add(_orderMsg);
					this.UpdateAWStateValue(_AWS_WaitedOrder, _waitedOrder);
					if(this.GetActStateValue(_AS_Action) == _AS.WAIT){
						return true;
					}else if(this.GetActStateValue(_AS_Action) == _AS.PROC){
						Continue();
						return true;
					}
				}
			}
		}else if(msg.GetDstEvent() == _IE_OrderIn){
			// TODO will not happened
			MsgOrder _orderMsg = (MsgOrder)msg.GetValue();
			
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
