package ModelAgent_BlueC2;

import CommonInfo.CEInfo;
import CommonInfo.UUID;
import ModelAction_CEAction.DamageAssessment;
import ModelAction_CEAction.Detection;
import ModelAction_CEAction.DirectEngagement;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicAgentModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class BlueCompany extends BasicAgentModel {
	
	public static String _IE_LocNoticeIn = "LocNoticeIn";
	public static String _IE_DirectFireIn = "DirectFireIn";
	
	public static String _OE_DirectFireOut = "DirectFireOut";
	public static String _OE_ReportOut = "ReportOut";
	public static String _OE_LocUpdateOut = "LocUpdateOut";
	
	public UUID _modelUUID;
	
	protected static String _CS_Normal = "normal";

	public BlueCompany(CEInfo _myInfo) {
		String _name = "BlueCompany";
		SetModelName(_name);
		this._modelUUID = _myInfo._id;
		
		/*
		 * Add Input and Output port
		 */
		AddInputEvent(_IE_DirectFireIn);
		AddInputEvent(_IE_LocNoticeIn);
		
		AddOutputEvent(_OE_DirectFireOut);
		AddOutputEvent(_OE_ReportOut);
		AddOutputEvent(_OE_LocUpdateOut);
		
		
		
		BlueCompanyC2Action bPC2 = new BlueCompanyC2Action(_myInfo);
		
		DamageAssessment dmgAss = new DamageAssessment(_myInfo);
		Detection detAction = new Detection(_myInfo);
		DirectEngagement engment = new DirectEngagement(_myInfo);
		
		bPC2.Activated();
		dmgAss.Activated();
		detAction.Activated();
		engment.Activated();
		
		AddCouplingState(_CS_Normal, true);
		
		AddCoupling(_CS_Normal, true, this, _IE_LocNoticeIn, detAction, detAction._IE_LocNoticeIn);
		AddCoupling(_CS_Normal, true, this, _IE_DirectFireIn, dmgAss, dmgAss._IE_DirectFireIn);
		
		AddCoupling(_CS_Normal, true, bPC2, bPC2._OE_ReportOut, this, _OE_ReportOut);
		AddCoupling(_CS_Normal, true, engment, engment._OE_DirectFireOut, this, _OE_DirectFireOut);
		
		AddCoupling(_CS_Normal, true, detAction, detAction._OE_ReportOut, bPC2, bPC2._IE_ReportIn);
		
		AddCoupling(_CS_Normal, true, bPC2, bPC2._OE_OrderOut, engment, engment._IE_OrderIn);
		
			
		AddCoupling(_CS_Normal, true, dmgAss, dmgAss._OE_AssessOut, bPC2, bPC2._IE_ReportIn);
		AddCoupling(_CS_Normal, true, dmgAss, dmgAss._OE_AssessOut, this, this._OE_LocUpdateOut);
		AddCoupling(_CS_Normal, true, dmgAss, dmgAss._OE_AssessOut, detAction, detAction._IE_LocUpdate);
		AddCoupling(_CS_Normal, true, dmgAss, dmgAss._OE_AssessOut, engment, engment._IE_MyInfoIn);
		AddCoupling(_CS_Normal, true, dmgAss, dmgAss._OE_AssessOut, this, this._OE_LocUpdateOut);		
		
	}

	@Override
	public boolean Delta(Message arg0) {
		// TODO Auto-generated method stub
		return true;
	}

}
