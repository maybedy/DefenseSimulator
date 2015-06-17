package ModelAgent_RedC2;

import java.util.ArrayList;

import CommonInfo.CEInfo;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicAgentModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class RedBattalionC2 extends BasicAgentModel {
	
	public static String _IE_ReportIn = "ReportIn";
	
	public static String _OE_OrderOut = "OrderOut";

	protected static String _CS_Normal = "normal";


	public RedBattalionC2(CEInfo _myInfo, ArrayList<RedCompany> _companyList) {
		String _name = "RedC2";
		SetModelName(_name);
		
		/*
		 * Add Input and output port
		 */
		
		AddInputEvent(_IE_ReportIn);
		
		AddOutputEvent(_OE_OrderOut);
		
		/*
		 * AddCoupling State
		 */
		
		AddCouplingState(_CS_Normal, true);
		
		/*
		 * AddComponent
		 */
		
		RedBattalionC2Action redC2Action = new RedBattalionC2Action(_myInfo, _companyList);
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
