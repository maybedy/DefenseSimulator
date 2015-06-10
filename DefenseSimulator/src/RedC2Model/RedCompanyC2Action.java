package RedC2Model;

import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicActionModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class RedCompanyC2Action extends BasicActionModel {

	public static String _IE_LocNoticeIn = "LocNoticeIn";
	public static String _IE_LocUpdateIn = "LocUpdateIn";
	public static String _IE_OrderIn = "OrderIn";
	public static String _IE_AssessIn = "AssessmentIn";
	
	public static String _OE_ReportOut = "ReportOut";
	public static String _OE_OrderOut = "OrderOut";
	
	private static String _AS_Action = "Action";
	private enum _AS{
		WAIT, PROC
	}

	
	public RedCompanyC2Action() {
		String _name = "BlueCompanyC2Action";
		SetModelName(_name);
		
		/*
		 * Add Input and output port
		 */
		
		
		AddInputEvent(_IE_OrderIn);
		AddInputEvent(_IE_LocNoticeIn);
		AddInputEvent(_IE_AssessIn);
		AddInputEvent(_IE_LocUpdateIn);
		
		AddOutputEvent(_OE_OrderOut);
		AddOutputEvent(_OE_ReportOut);
		
		AddActState(_AS_Action, _AS.WAIT);
		
		
	}

	@Override
	public boolean Act(Message arg0) {
		if(this.GetActStateValue(_AS_Action) == _AS.WAIT){
			return true;
		}else if(this.GetActStateValue(_AS_Action) == _AS.PROC){
			return true;
		}
		return false;
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
	public boolean Perceive(Message arg0) {
		if(this.GetActStateValue(_AS_Action) == _AS.WAIT){
			return true;
		}else if(this.GetActStateValue(_AS_Action) == _AS.PROC){
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

}
