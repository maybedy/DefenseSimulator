package ModelAgent_RedC2;

import CommonInfo.CEInfo;
import CommonInfo.UUID;
import ModelAction_CEAction.DamageAssessment;
import ModelAction_CEAction.Detection;
import ModelAction_CEAction.DirectEngagement;
import ModelAction_CEAction.Movement;
import MsgC2Order.MsgOrder;
import MsgC2Order.OrderType;
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
	
	private static String _CS_Normal = "Normal";
	private static String _CS_MsgBranch = "Branch";
	
	private enum MsgBranch {
		None, MoveOrder, FireOrder;
	}
	
	public UUID _modelUUID;

	public RedCompany(CEInfo _myInfo) {
		String _name = "RedCompany";
		SetModelName(_name);
		
		this._modelUUID = _myInfo._id;
		
		RedCompanyC2Action rCC2 = new RedCompanyC2Action(_myInfo);
		
		DamageAssessment dmgAss = new DamageAssessment(_myInfo);
		Detection detAction = new Detection(_myInfo);
		DirectEngagement engment = new DirectEngagement(_myInfo);
		Movement mvment = new Movement(_myInfo);
		
		rCC2.Activated();
		
		dmgAss.Activated();
		detAction.Activated();
		engment.Activated();
		mvment.Activated();
		
		AddInputEvent(_IE_LocNoticeIn);
		AddInputEvent(_IE_AngleDmgIn);
		AddInputEvent(_IE_DirectFireIn);
		AddInputEvent(_IE_OrderIn);
		
		AddOutputEvent(_OE_DirectFireOut);
		AddOutputEvent(_OE_LocUpdateOut);
		AddOutputEvent(_OE_ReportOut);
		
		AddCouplingState(_CS_Normal, true);
		AddCouplingState(_CS_MsgBranch, MsgBranch.None);
		
		AddCoupling(_CS_Normal, true, this, this._IE_LocNoticeIn, detAction, detAction._IE_LocNoticeIn);
		AddCoupling(_CS_Normal, true, this, _IE_DirectFireIn, dmgAss, dmgAss._IE_DirectFireIn);
		AddCoupling(_CS_Normal, true, this, _IE_AngleDmgIn, dmgAss, dmgAss._IE_AngleDmgIn);
		AddCoupling(_CS_Normal, true, this, this._IE_OrderIn, rCC2, rCC2._IE_OrderIn);
		
		AddCoupling(_CS_Normal, true, engment, engment._OE_DirectFireOut, this, _OE_DirectFireOut);
		AddCoupling(_CS_Normal, true, detAction, detAction._OE_ReportOut, rCC2, rCC2._IE_ReportIn);
			
		AddCoupling(_CS_Normal, true, dmgAss, dmgAss._OE_AssessOut, rCC2, rCC2._IE_ReportIn);
		AddCoupling(_CS_Normal, true, dmgAss, dmgAss._OE_AssessOut, this, this._OE_LocUpdateOut);
		AddCoupling(_CS_Normal, true, dmgAss, dmgAss._OE_AssessOut, detAction, detAction._IE_LocUpdate);
		AddCoupling(_CS_Normal, true, dmgAss, dmgAss._OE_AssessOut, engment, engment._IE_MyInfoIn);
		AddCoupling(_CS_Normal, true, dmgAss, dmgAss._OE_AssessOut, this, this._OE_LocUpdateOut);
		AddCoupling(_CS_Normal, true, dmgAss, dmgAss._OE_AssessOut, mvment, mvment._IE_MyInfo);
		
		AddCoupling(_CS_Normal, true, mvment, mvment._OE_LocUpdateOut, rCC2, rCC2._IE_ReportIn);
		AddCoupling(_CS_Normal, true, mvment, mvment._OE_LocUpdateOut, this, this._OE_LocUpdateOut);
		AddCoupling(_CS_Normal, true, mvment, mvment._OE_LocUpdateOut, detAction, detAction._IE_LocUpdate);
		AddCoupling(_CS_Normal, true, mvment, mvment._OE_LocUpdateOut, engment, engment._IE_MyInfoIn);
		AddCoupling(_CS_Normal, true, mvment, mvment._OE_LocUpdateOut, this, this._OE_LocUpdateOut);
		AddCoupling(_CS_Normal, true, mvment, mvment._OE_LocUpdateOut, dmgAss, dmgAss._IE_MyInfoIn);

		AddCoupling(_CS_Normal, true, rCC2, rCC2._OE_ReportOut, this, _OE_ReportOut);
		/*
		 * TODO branching order
		 */
		AddCoupling(_CS_MsgBranch, MsgBranch.FireOrder, rCC2, rCC2._OE_OrderOut, engment, engment._IE_OrderIn);
		AddCoupling(_CS_MsgBranch, MsgBranch.MoveOrder, rCC2, rCC2._OE_OrderOut, mvment, mvment._IE_OrderIn);
		
	}

	@Override
	public boolean Delta(Message msg) {
		
		if(msg.GetDstEvent() == RedCompanyC2Action._OE_OrderOut){
			MsgOrder _orderMsg = (MsgOrder)msg.GetValue();
			
			if(_orderMsg._orderType == OrderType.DirectEngagement){
				this.updateCouplingState(_CS_MsgBranch, MsgBranch.FireOrder, true);
			}else if(_orderMsg._orderType == OrderType.Move){
				this.updateCouplingState(_CS_MsgBranch, MsgBranch.MoveOrder, true);
			}else if(_orderMsg._orderType == OrderType.STOP){
				this.updateCouplingState(_CS_MsgBranch, MsgBranch.FireOrder, true);
				this.updateCouplingState(_CS_MsgBranch, MsgBranch.MoveOrder, true);
			}
			else {
				// TODO error case
				System.out.println("Not available order in RedCompany");
				return false;
			}
			
			return true;
			
		}else {
			this.updateCouplingState(_CS_MsgBranch, MsgBranch.None, true);
			
			return true;
		}
		
	}

}
