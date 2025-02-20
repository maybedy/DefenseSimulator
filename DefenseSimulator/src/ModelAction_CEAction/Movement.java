package ModelAction_CEAction;

import CommonInfo.CEInfo;
import CommonInfo.Path;
import CommonInfo.UUID;
import CommonInfo.XY;
import CommonMap.GridInfo;
import MsgC2Order.MsgMoveOrder;
import MsgC2Order.MsgOrder;
import MsgC2Order.OrderType;
import MsgC2Report.MsgLocUpdate;
import MsgC2Report.MsgReport;
import MsgC2Report.ReportType;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicActionModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class Movement extends BasicActionModel {
	public static String _IE_OrderIn = "OrderIn";
	public static String _IE_MyInfo = "MyInfoIn";
	public static String _OE_LocUpdateOut = "LocUpdateOut";
	
	private static String _CS_CurrentLoc = "CurrentLocation"; // XY
	private static String _CS_MyInfo = "MyInfo";  // CEInfo
	private static String _CS_CurrentDirection = "CurrentDirection"; // double
	private static String _CS_CurrentObjective = "CurrentObjective"; // XY
	private static String _CS_CurrentSpeed = "CurrentSpeed";
	 
	private static String _AWS_CurrentPath = "CurrentPath"; // ArrayList<XY> 
	
	private static String _AS_Action = "Action";
	private boolean _isContinuable = true;
	
	public UUID _modelUUID;
	
	private enum _AS{
		Stop, Move
	}
	
	private double _PARAM_MaxSpeed;
	
	public Movement(CEInfo _myInfo) {
		String _name = "MovementAction";
		SetModelName(_name);
		
		this._modelUUID = _myInfo._id;
		this._PARAM_MaxSpeed = _myInfo._maxSpeed;
		
		/*
		 * Add Input and Output Port
		 */
		AddInputEvent(_IE_OrderIn);
		AddInputEvent(_IE_MyInfo);
		AddOutputEvent(_OE_LocUpdateOut);
		
		AddConState(_CS_MyInfo, _myInfo);
		AddConState(_CS_CurrentLoc, _myInfo._myLoc);
		//AddConState(_CS_Movable, _myInfo._movable);
		
		AddConState(_CS_CurrentDirection, null);
		AddConState(_CS_CurrentObjective, null);
		AddConState(_CS_CurrentSpeed, 0);
		
		AddAwState(_AWS_CurrentPath, null);
		
		AddActState(_AS_Action, _AS.Stop);
	}
	
	private XY UpdateMyLoc(XY currentLoc, double direction, double speed, XY _currCheckpoint) {
		// TODO update new loc
		
		if(currentLoc.equalsWithError(_currCheckpoint)){
			return _currCheckpoint;
		}
		if(speed * 1000/3600 > currentLoc.distance(_currCheckpoint)){
			return _currCheckpoint;
		}else {
			
		}
		XY ret = currentLoc.calEndPointObj(_currCheckpoint, speed*1000/3600);	
		
		System.out.println(_modelUUID.getString() + " - " + ret.x + ", " + ret.y);
		
		return ret;
	}
	
	private boolean isArrive(XY currentLoc, XY dest){
		if(currentLoc.equalsWithError(dest)){
			return true;
		}
		return false;
	}

	@Override
	public boolean Act(Message msg) {
		if(this.GetActStateValue(_AS_Action) == _AS.Stop){
			//  nothing to do
			return true;
		}else if(this.GetActStateValue(_AS_Action) == _AS.Move){
			GridInfo _currCheckpoint = (GridInfo)this.GetConStateValue(_CS_CurrentObjective);
			double _currDirection = (double)this.GetConStateValue(_CS_CurrentDirection);
			XY _currLoc = (XY)this.GetConStateValue(_CS_CurrentLoc);
			double _currSpeed = (double)this.GetConStateValue(_CS_CurrentSpeed);
			CEInfo _currInfo = (CEInfo)this.GetConStateValue(_CS_MyInfo);
			
			XY _newLoc = this.UpdateMyLoc(_currLoc, _currDirection, _currSpeed, _currCheckpoint._mainLoc);
			this.UpdateConStateValue(_CS_CurrentLoc, _newLoc);
			
			CEInfo _newInfo = new CEInfo(_currInfo);
			_newInfo._myLoc = _newLoc;
			
			if(_currCheckpoint.isInThisGrid(_newLoc) && _newInfo._currentGrid._gridIndex != _currCheckpoint._gridIndex){
				//TODO make calculating speed in the Grid
				this.updateSpeed(_currCheckpoint);
				_newInfo._currentGrid = _currCheckpoint;
				
			}
			
			if(this.isArrive(_newLoc, _currCheckpoint._mainLoc)){
				Path _pathList = (Path)this.GetAWStateValue(_AWS_CurrentPath);
				
				if(_pathList.isEmpty()){
					this.UpdateConStateValue(_CS_CurrentDirection, null);
					this.UpdateConStateValue(_CS_CurrentObjective, null);
					this.UpdateAWStateValue(_AWS_CurrentPath, null);
					
				}else {
					_currCheckpoint = _pathList.removeCurrentObject();
					this.updateDirection(_currCheckpoint);
					this.UpdateConStateValue(_CS_CurrentObjective, _currCheckpoint);
					this.UpdateAWStateValue(_AWS_CurrentPath, _pathList);
				}
				
			}
			
			this.UpdateConStateValue(_CS_MyInfo, _newInfo);
			MsgLocUpdate _locUpdateMsg = new MsgLocUpdate(_newInfo);
			MsgReport _reportMsg = new MsgReport(ReportType.LocationChange, this._modelUUID, null, _locUpdateMsg);
			msg.SetValue(_OE_LocUpdateOut, _reportMsg);
			
			return true;
		}
		return false;
	}

	@Override
	public boolean Decide() {
		CEInfo _myInfo = (CEInfo) this.GetConStateValue(_CS_MyInfo);
		if(_myInfo._HP <= 0 ){
			this._isContinuable = false;
			ResetContinue();
			this.UpdateActStateValue(_AS_Action, _AS.Stop);
			return true;
		}
		if(this.GetActStateValue(_AS_Action) == _AS.Stop){
			this._isContinuable = false;
			ResetContinue();
			this.UpdateActStateValue(_AS_Action, _AS.Move);
			return true;
		}else if(this.GetActStateValue(_AS_Action) == _AS.Move){
			Path _currentPath = (Path)this.GetAWStateValue(_AWS_CurrentPath);
			this._isContinuable = false;
			ResetContinue();
			if(_currentPath == null || _currentPath.isEmpty()){
				this.UpdateActStateValue(_AS_Action, _AS.Stop);	
			}else {
				this.UpdateActStateValue(_AS_Action, _AS.Move);
			}
			return true;
		}

		return false;
	}

	@Override
	public boolean Perceive(Message msg) {
		CEInfo _myInfo = (CEInfo) this.GetConStateValue(_CS_MyInfo);
		if(_myInfo._HP <= 0 ){
			Continue();
			return true;
		}
		if(msg.GetDstEvent() == _IE_MyInfo){
			MsgReport _reportMsg = (MsgReport)msg.GetValue();
			MsgLocUpdate _myInfoMsg = (MsgLocUpdate)_reportMsg._msgValue;
			this.UpdateConStateValue(_CS_MyInfo, _myInfoMsg._myInfo);
			if(_myInfoMsg._myInfo._HP <= 0){
				
			}else {
				makeContinue();	
			}
			return true;
		}else if(msg.GetDstEvent() == _IE_OrderIn){
			MsgOrder _orderMsg = (MsgOrder)msg.GetValue();
			if(_orderMsg._orderType == OrderType.STOP){
				
				this.UpdateAWStateValue(_AWS_CurrentPath, null);
				ResetContinue();
				return true;
				
			}else if(_orderMsg._orderType == OrderType.Move){
				
				MsgMoveOrder _moveOrderMsg = (MsgMoveOrder)_orderMsg._orderMsg;
				Path _pathList = _moveOrderMsg.getPath();
				GridInfo _currCheckpoint = _pathList.removeCurrentObject();
				
				ResetContinue();
				this.UpdateAWStateValue(_AWS_CurrentPath, _pathList);
				this.updateSpeed(_currCheckpoint);
				this.updateDirection(_currCheckpoint);
				
				return true;
			}
			
		}
		return false;
	}
//	
//	private boolean isInNewGrid(){
//		XY _currentXY = (XY)this.GetConStateValue(_CS_CurrentLoc);
//		Path _currentPath = (Path) this.GetAWStateValue(_AWS_CurrentPath);
//		GridInfo _newObjGrid = _currentPath.removeCurrentObject();
//		
//		if(_newObjGrid.isInThisGrid(_currentXY)){
//			return true;
//		}
//		return false;
//	}
//	
	private void updateSpeed(GridInfo _currentObjGrid){
		double _currSpeed = _currentObjGrid.getSpeedInThisGrid(this._PARAM_MaxSpeed);
		this.UpdateConStateValue(_CS_CurrentSpeed, _currSpeed);
	}
	
	private void updateDirection(GridInfo _newObjGrid){
		XY _currLoc = (XY)this.GetConStateValue(_CS_CurrentLoc);
		double _currDirection = _currLoc.calBearing(_newObjGrid._mainLoc);
		
		this.UpdateConStateValue(_CS_CurrentDirection, _currDirection);
		this.UpdateConStateValue(_CS_CurrentObjective, _newObjGrid);
		
		
	}

	@Override
	public double TimeAdvance() {
		this._isContinuable = true;
		if(this.GetActStateValue(_AS_Action) == _AS.Stop){
			return Double.POSITIVE_INFINITY;
		}else if(this.GetActStateValue(_AS_Action) == _AS.Move){
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
