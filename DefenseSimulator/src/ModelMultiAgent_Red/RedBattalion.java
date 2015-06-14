package ModelMultiAgent_Red;

import java.util.ArrayList;

import CommonInfo.CEInfo;
import CommonInfo.UUID;
import ModelAgent_RedC2.RedBattalionC2;
import ModelAgent_RedC2.RedCompany;
import MsgC2Order.MsgOrder;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicMultiAgentModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class RedBattalion extends BasicMultiAgentModel {

	public static String _IE_LocNoticeIn = "LocNoticeIn";
	public static String _IE_AngleDmgIn = "AngleDmgIn";	
	public static String _IE_DirectFireIn = "DirectFireIn";
	
	public static String _OE_LocUpdateOut = "LocUpdateOut";
	public static String _OE_DirectFireOut = "DirectFireOut";
	//component is red battalionC2, and red company
	
	private RedBattalionC2 _C2Agent;
	private ArrayList<RedCompany> _CompanyList;
	
	
	private static String _CS_Normal = "Normal";
	private static String _CS_MsgBranch = "Branch";
	
	public UUID _modelUUID;  
	
	public RedBattalion(CEInfo _myInfo, RedBattalionC2 _C2Agent, ArrayList<RedCompany> _CompanyList) {
		
		String _name = "RedBattalion";
		SetModelName(_name);
		this._modelUUID = _myInfo._id;
		
		AddInputEvent(_IE_DirectFireIn);
		AddInputEvent(_IE_LocNoticeIn);
		AddInputEvent(_IE_AngleDmgIn);
		
		AddOutputEvent(_OE_DirectFireOut);
		AddOutputEvent(_OE_LocUpdateOut);
		
		this.AddCouplingState(_CS_Normal, true);
		this.AddCouplingState(_CS_MsgBranch, -1);
		
		this._C2Agent = _C2Agent;
		_C2Agent.Activated();
		this.addComponent(_C2Agent);
		
		
		
		this._CompanyList = _CompanyList;
		for(RedCompany _eachCompany : _CompanyList){
			_eachCompany.Activated();
			this.addComponent(_eachCompany);
			
			this.AddCoupling(_CS_Normal, true, this, this._IE_AngleDmgIn, _eachCompany, _eachCompany._IE_AngleDmgIn);
			this.AddCoupling(_CS_Normal, true, this, this._IE_DirectFireIn, _eachCompany, _eachCompany._IE_DirectFireIn);
			this.AddCoupling(_CS_Normal, true, this, this._IE_LocNoticeIn, _eachCompany, _eachCompany._IE_LocNoticeIn);
			
			this.AddCoupling(_CS_Normal,  true,  _eachCompany, _eachCompany._OE_DirectFireOut, this, this._OE_DirectFireOut);
			this.AddCoupling(_CS_Normal, true, _eachCompany, _eachCompany._OE_LocUpdateOut, this, this._OE_LocUpdateOut);
			
			this.AddCoupling(_CS_Normal, true, _eachCompany, _eachCompany._OE_ReportOut, _C2Agent, _C2Agent._IE_ReportIn);
			
			this.AddCoupling(_CS_MsgBranch, _eachCompany._modelUUID.getUniqID_Batt(), _C2Agent, _C2Agent._OE_OrderOut, _eachCompany, _eachCompany._IE_OrderIn);
			
		}
		
	}

	@Override
	public boolean Delta(Message msg) {
		if(msg.GetDstEvent() == RedCompany._IE_OrderIn){
			MsgOrder _orderMsg = (MsgOrder)msg.GetValue();
			
			this.updateCouplingState(_CS_MsgBranch, _orderMsg._destUUID.getUniqID_Batt(), true);
			return true;
		}else {
			this.updateCouplingState(_CS_MsgBranch, -1, true);
			return true;
		}
	}

}
