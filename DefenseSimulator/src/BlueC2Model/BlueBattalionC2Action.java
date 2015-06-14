package BlueC2Model;

import C2OrderMsgs.MsgOrder;
import C2ReportMsgs.MsgReport;
import C2ReportMsgs.ReportType;
import CommonInfo.CEInfo;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicActionModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class BlueBattalionC2Action extends BasicActionModel {
	
	private static String _IE_OrderIn = "OrderIn";
	public static String _IE_ReportIn = "ReportIn";
	
	public static String _OE_OrderOut = "OrderOut";
	private static String _OE_ReportOut = "ReportOut";
	
	private static String _AS_Action = "Action";
	private enum _AS{
		WAIT, PROC
	}


	public BlueBattalionC2Action(CEInfo _myInfo) {
		String _name = "BlueBattalionC2Action";
		SetModelName(_name);
		
		/*
		 * Add Input and output port
		 */
		
		AddInputEvent(_IE_OrderIn);
		AddInputEvent(_IE_ReportIn);
		
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
			
		}else if(_reportMsg._reportType == ReportType.MyInfo){
			
		}
		if(this.GetActStateValue(_AS_Action) == _AS.WAIT){
			return true;
		}else if(this.GetActStateValue(_AS_Action) == _AS.PROC){
			return true;
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
