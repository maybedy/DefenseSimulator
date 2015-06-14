package MultiAgentModel;

import java.util.ArrayList;

import CommonInfo.CEInfo;
import RedC2Model.RedBattalionC2;
import RedC2Model.RedCompany;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicMultiAgentModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class RedBattalion extends BasicMultiAgentModel {

	public static String _IE_LocNoticeIn = "LocNoticeIn";
	public static String _IE_DirectFireIn = "DirectFireIn";
	
	public static String _OE_AngleFireOut = "AngleFireOut";
	public static String _OE_DirectFireOut = "DirectFireOut";
	
	
	//component is red battalionC2, and red company
	
	private RedBattalionC2 _C2Agent;
	private ArrayList<RedCompany> _CompanyList;
	
	
	public RedBattalion(CEInfo _myInfo, RedBattalionC2 _C2Agent, ArrayList<RedCompany> _CompanyList) {
		
		String _name = "RedBattalion";
		SetModelName(_name);
		
		AddInputEvent(_IE_DirectFireIn);
		AddInputEvent(_IE_LocNoticeIn);
		
		AddOutputEvent(_OE_AngleFireOut);
		AddOutputEvent(_OE_DirectFireOut);

		
	}

	@Override
	public boolean Delta(Message arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
