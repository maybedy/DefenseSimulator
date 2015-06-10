package RedC2Model;

import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicAgentModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class RedBattalionC2 extends BasicAgentModel {
	
	public static String _IE_OrderIn = "OrderIn";
	public static String _IE_ReportIn = "ReportIn";
	
	public static String _OE_OrderOut = "OrderOut";
	public static String _OE_ReportOut = "ReportOut";

	protected static String _CS_Normal = "normal";


	public RedBattalionC2() {
		String _name = "RedC2";
		SetModelName(_name);
		
		/*
		 * Add Input and output port
		 */
		
		AddInputEvent(_IE_OrderIn);
		AddInputEvent(_IE_ReportIn);
		
		AddOutputEvent(_OE_OrderOut);
		AddOutputEvent(_OE_ReportOut);
		
		/*
		 * AddCoupling State
		 */
		
		AddCouplingState(_CS_Normal, true);
		
		/*
		 * AddComponent
		 */
		
		RedBattalionC2Action redC2Action = new RedBattalionC2Action();
		redC2Action.Activated();
		AddActionModel(redC2Action);
		
		AddCoupling(_CS_Normal, true, this, _IE_ReportIn, redC2Action, _IE_ReportIn);
		AddCoupling(_CS_Normal, true, redC2Action, _OE_OrderOut, this, _OE_OrderOut);
	}

	@Override
	public boolean Delta(Message arg0) {
		// TODO Auto-generated method stub
		return true;
	}

}
