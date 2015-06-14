package ModelAgent_RedC2;

import CommonInfo.CEInfo;
import CommonInfo.UUID;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicAgentModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class RedCompany extends BasicAgentModel {
	public static String _IE_LocNoticeIn = "LocNoticeIn";
	public static String _IE_AngleDmgIn = "AngleDmgIn";
	public static String _IE_DirectFireIn = "DirectFireIn";
	public static String _IE_OrderIn = "OrderIn";
	
	public static String _OE_DirectFireOut = "DirectFireOut";
	public static String _OE_LocUpdateOut = "LocUpdateOut";
	public static String _OE_ReportOut = "ReportOut";
	
	public UUID _modelUUID;

	public RedCompany(CEInfo _myInfo) {
		
		this._modelUUID = _myInfo._id;
	}

	@Override
	public boolean Delta(Message arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
