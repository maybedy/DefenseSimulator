package ModelAction_CEAction;

import java.util.ArrayList;

import CommonInfo.CEInfo;
import MsgC2Report.MsgLocNotice;
import MsgC2Report.MsgLocUpdate;
import MsgC2Report.MsgReport;
import MsgC2Report.ReportType;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicActionModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class Detection extends BasicActionModel {

	public static String _IE_LocNoticeIn = "LocNoticeIn";
	public static String _IE_LocUpdate = "MyInfoIn";
	public static String _OE_ReportOut = "ReportOut";
	
	private static String _AS_Action = "Action";
	
	private static String _AWS_DetectedList = "DetectedList";
	
	private static String _CS_MyInfo = "MyInfo";
	
	private boolean _isContinuable = true;
	
	private enum _ActState{
		Stop, Report
	}
	
	public CommonInfo.UUID _modelID;
	
	public Detection(CEInfo _myInfo) {
		// TODO Auto-generated constructor stub
		String _name = "DetectionAction";
		SetModelName(_name);
		
		_modelID = _myInfo._id;
		
		/*
		 * Add Input and Output Port
		 */
		AddInputEvent(_IE_LocNoticeIn);
		AddInputEvent(_IE_LocUpdate);

		AddOutputEvent(_OE_ReportOut);
		
		AddActState(_AS_Action, _ActState.Stop);
		
		AddConState(_CS_MyInfo, _myInfo);
		
		AddAwState(_AWS_DetectedList, null);
		
		
	}

	@Override
	public boolean Act(Message msg) {
		if(this.GetActStateValue(_AS_Action) == _ActState.Stop){
			//nothing to do
			return true;
		}else if(this.GetActStateValue(_AS_Action) == _ActState.Report){
			MsgLocNotice _detectedAgents = (MsgLocNotice)this.GetAWStateValue(_AWS_DetectedList);
			MsgReport _reportMsg = new MsgReport(ReportType.EnemyInfo, this._modelID, null, _detectedAgents);
			//TODO dest UUID???
			msg.SetValue(_OE_ReportOut, _reportMsg);
			return true;
		}
		return false;
	}

	@Override
	public boolean Decide() {
		if(this.GetActStateValue(_AS_Action) == _ActState.Stop){
			this.UpdateActStateValue(_AS_Action, _ActState.Report);
			return true;
		}else if(this.GetActStateValue(_AS_Action) == _ActState.Report){
			this.UpdateActStateValue(_AS_Action, _ActState.Stop);
			return true;
		}
		return false;
	}

	@Override
	public boolean Perceive(Message msg) {
		if(msg.GetDstEvent() == _IE_LocNoticeIn){// from env, info of nearby agents
			
			System.out.println("Detection activated - " + this._modelID.getString());
			MsgReport _reportMsg = (MsgReport)msg.GetValue();
			MsgLocNotice _locNotice = (MsgLocNotice)_reportMsg._msgValue;
			
			this.UpdateAWStateValue(_AWS_DetectedList, _locNotice);
			
			return true;
		}else if(msg.GetDstEvent() == _IE_LocUpdate){
			MsgReport _reportMsg = (MsgReport)msg.GetValue();
			MsgLocUpdate _locMsg = (MsgLocUpdate)_reportMsg._msgValue;
			
			this.UpdateConStateValue(_CS_MyInfo, _locMsg._myInfo);
			Continue();
			return true;
		}
		return false;
	}

	@Override
	public double TimeAdvance() {
		this._isContinuable = true;
		if(this.GetActStateValue(_AS_Action) == _ActState.Stop){
			return Double.POSITIVE_INFINITY;
		}else if(this.GetActStateValue(_AS_Action) == _ActState.Report){
			return 0;
		}
		return 0;
	}

}
