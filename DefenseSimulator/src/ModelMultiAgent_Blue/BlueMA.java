package ModelMultiAgent_Blue;

import java.util.ArrayList;

import CommonInfo.CEInfo;
import CommonInfo.UUID;
import ModelAgent_BlueC2.BlueBattalionC2;
import ModelAgent_BlueC2.BlueCompany;
import ModelAgent_BlueC2.Sensor;
import ModelAgent_BlueC2.Shooter;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicMultiAgentModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class BlueMA extends BasicMultiAgentModel {
	
	public static String _IE_LocNoticeIn = "LocNoticeIn";
	//public static String _IE_AngleDmgIn = "AngleDmgIn";	
	public static String _IE_DirectFireIn = "DirectFireIn";
	
	public static String _OE_LocUpdateOut = "LocUpdateOut";
	public static String _OE_AngleFireOut = "AngleFireOut";
	public static String _OE_DirectFireOut = "DirectFireOut";
	
//	
//	protected String _INT_OrderOut = "OrderOut";
//	protected String _INT_OrderIn = "OrderIn";
//	
//	protected String _INT_ReportOut = "ReportOut";
//	protected String _INT_ReportIn = "ReportIn";
//	
	protected static String _CS_Normal = "normal";
	
	public UUID _modelUUID;
	
	
	private BlueBattalion _battalion;
	
	public BlueMA(CEInfo _myInfo, BlueBattalion _battalion) {
		/*
		 * Set Model Name
		 */
		
		String _name = "BlueMultiAgent";
		SetModelName(_name);
		
		this._modelUUID = _myInfo._id;
		
		/*
		 * Add input and output port
		 */
		
		AddInputEvent(_IE_DirectFireIn);
		AddInputEvent(_IE_LocNoticeIn);
		
		AddOutputEvent(_OE_AngleFireOut);
		AddOutputEvent(_OE_DirectFireOut);
		AddOutputEvent(_OE_LocUpdateOut);
		
		/*
		 * Add Coupling
		 */
		
		AddCouplingState(_CS_Normal, true);		

		/*
		 * Add Component
		 */
		this._battalion = _battalion;
		_battalion.Activated();
		addComponent(_battalion);
		
		this.AddCoupling(_CS_Normal, true, this, this._IE_DirectFireIn, _battalion, _battalion._IE_DirectFireIn);
		this.AddCoupling(_CS_Normal, true, this, this._IE_LocNoticeIn, _battalion, _battalion._IE_LocNoticeIn);
		
		this.AddCoupling(_CS_Normal, true, _battalion, _battalion._OE_AngleFireOut, this, this._OE_AngleFireOut);
		this.AddCoupling(_CS_Normal, true, _battalion, _battalion._OE_DirectFireOut, this, this._OE_DirectFireOut);
		this.AddCoupling(_CS_Normal, true, _battalion, _battalion._OE_LocUpdateOut, this, this._OE_LocUpdateOut);
	}

	@Override
	public boolean Delta(Message arg0) {
		
		return true;
	}

}
