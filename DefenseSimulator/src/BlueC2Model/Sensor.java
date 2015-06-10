package BlueC2Model;

import CEActionModel.Detection;
import Common.CEInfo;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicAgentModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class Sensor extends BasicAgentModel {
	public static String _IE_LocNoticeIn = "LocNoticeIn";
	public static String _OE_ReportOut = "ReportOut";
	
	protected static String _CS_Normal = "normal";

	public Sensor(CEInfo _myInfo) {
		String _name = "Sensor";
		SetModelName(_name);
		
		AddInputEvent(_IE_LocNoticeIn);
		AddOutputEvent(_OE_ReportOut);
		
		Detection detAction = new Detection(_myInfo);
		detAction.Activated();
		AddActionModel(detAction);
		
		AddCouplingState(_CS_Normal, true);
		
		
		AddCoupling(_CS_Normal, true, this, _IE_LocNoticeIn, detAction, Detection._IE_LocNoticeIn);
		AddCoupling(_CS_Normal, true, detAction, Detection._OE_ReportOut, this, _OE_ReportOut);
		
		
	}

	@Override
	public boolean Delta(Message arg0) {
		// TODO Auto-generated method stub
		return true;
	}

}
