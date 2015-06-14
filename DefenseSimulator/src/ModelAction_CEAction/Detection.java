package ModelAction_CEAction;

import java.util.ArrayList;

import CommonInfo.CEInfo;
import MsgC2Report.MsgLocNotice;
import MsgC2Report.MsgLocUpdate;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicActionModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class Detection extends BasicActionModel {

	public static String _IE_LocNoticeIn = "LocNoticeIn";
	public static String _IE_LocUpdate = "LocUpdate";
	public static String _OE_ReportOut = "ReportOut";
	
	private static String _AS_Action = "Action";
	
	private static String _AWS_DetectedList = "DetectedList";
	
	private static String _CS_MyInfo = "MyInfo";
	
	private boolean _isContinuable = true;
	
	private enum _ActState{
		Stop, Report
	}
	
	public Detection(CEInfo _myInfo) {
		// TODO Auto-generated constructor stub
		String _name = "DetectionAction";
		SetModelName(_name);
		
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
			
			msg.SetValue(_OE_ReportOut, _detectedAgents);
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
			MsgLocNotice _locNotice = (MsgLocNotice)msg.GetValue();
			this.UpdateConStateValue(_AWS_DetectedList, _locNotice);

			return true;
		}else if(msg.GetDstEvent() == _IE_LocUpdate){
			MsgLocUpdate _locUpdate= (MsgLocUpdate)msg.GetValue();
			this.UpdateConStateValue(_CS_MyInfo, _locUpdate._myInfo);
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
