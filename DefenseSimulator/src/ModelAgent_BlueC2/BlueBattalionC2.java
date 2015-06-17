package ModelAgent_BlueC2;

import java.util.ArrayList;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import CommonInfo.CEInfo;
import CommonInfo.UUID;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicAgentModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class BlueBattalionC2 extends BasicAgentModel {
	
	
	private static String _IE_OrderIn = "OrderIn";
	public static String _IE_ReportIn = "ReportIn";
	
	public static String _OE_OrderOut = "OrderOut";
	private static String _OE_ReportOut = "ReportOut";

	protected static String _CS_Normal = "normal";
	
	public UUID _modelUUID;

	public BlueBattalionC2(CEInfo _myInfo, ArrayList<BlueCompany> _companyList, ArrayList<Shooter> _shooterList) {
		String _name = "BlueBattalionC2";
		SetModelName(_name);
		this._modelUUID = _myInfo._id;
		
		
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
		
		BlueBattalionC2Action blueC2Action = new BlueBattalionC2Action(_myInfo, _companyList, _shooterList);
		blueC2Action.Activated();
		AddActionModel(blueC2Action);
		
		AddCoupling(_CS_Normal, true, this, _IE_ReportIn, blueC2Action, BlueBattalionC2Action._IE_ReportIn);
		AddCoupling(_CS_Normal, true, blueC2Action, BlueBattalionC2Action._OE_OrderOut, this, _OE_OrderOut);
	}

	@Override
	public boolean Delta(Message arg0) {
		// TODO Auto-generated method stub
		return true;
	}

}
