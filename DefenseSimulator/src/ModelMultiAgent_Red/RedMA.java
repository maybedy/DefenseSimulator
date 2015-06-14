package ModelMultiAgent_Red;

import java.util.ArrayList;

import CommonInfo.CEInfo;
import CommonInfo.UUID;
import ModelAgent_RedC2.RedBattalionC2;
import ModelAgent_RedC2.RedCompany;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicMultiAgentModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class RedMA extends BasicMultiAgentModel {

	public static String _IE_LocNoticeIn = "LocNoticeIn";
	public static String _IE_AngleDmgIn = "AngleDmgIn";	
	public static String _IE_DirectFireIn = "DirectFireIn";
	
	public static String _OE_LocUpdateOut = "LocUpdateOut";
	public static String _OE_DirectFireOut = "DirectFireOut";

	protected static String _CS_Normal = "Normal";
	private static String _CS_MsgBranch = "Branch";
	
	public UUID _modelUUID;
	
	private ArrayList<RedBattalion> _battalionList;
	
	public RedMA(CEInfo _myInfo, ArrayList<RedBattalion> _battalionList) {
		
		String _name = "RedMultiAgent";
		SetModelName(_name);
		this._modelUUID = _myInfo._id;
		
		/*
		 * Add input and output port
		 */
		
		AddInputEvent(_IE_AngleDmgIn);
		AddInputEvent(_IE_DirectFireIn);
		AddInputEvent(_IE_LocNoticeIn);

		AddOutputEvent(_OE_DirectFireOut);
		AddOutputEvent(_OE_LocUpdateOut);
		
		/*
		 * Add Coupling State
		 */
		
		AddCouplingState(_CS_Normal, true);
		AddCouplingState(_CS_MsgBranch, -1);
		
		/*
		 * Add Component
		 */
		this._battalionList = _battalionList;
		
		for(RedBattalion _eachBattalion : _battalionList){
			_eachBattalion.Activated();
			this.addComponent(_eachBattalion);
			
			

		}
		
	}

	@Override
	public boolean Delta(Message arg0) {
		// TODO Auto-generated method stub
		
		// TODO specification coupling state
		return true;
	}

}
