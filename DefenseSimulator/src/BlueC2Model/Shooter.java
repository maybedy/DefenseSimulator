package BlueC2Model;

import CEActionModel.AngleEngagement;
import Common.CEInfo;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicAgentModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class Shooter extends BasicAgentModel {
	
	public static String _IE_OrderIn = "OrderIn";
	public static String _OE_AngleFireOut = "AngleFireOut";
	
	
	protected static String _CS_Normal = "normal";

	public Shooter(CEInfo _myInfo) {
		String _name = "Shooter";
		SetModelName(_name);
		
		
		AddInputEvent(_IE_OrderIn);
		AddOutputEvent(_OE_AngleFireOut);
	
		AngleEngagement engAction = new AngleEngagement(_myInfo);
		
		engAction.Activated();
		AddActionModel(engAction);
		
		AddCouplingState(_CS_Normal, true);
		
		AddCoupling(_CS_Normal, true, this, _IE_OrderIn, engAction, AngleEngagement._IE_OrderIn);
		
		AddCoupling(_CS_Normal, true, engAction, AngleEngagement._OE_AngleFireOut, this, _OE_AngleFireOut);
	}

	@Override
	public boolean Delta(Message arg0) {
		// TODO Auto-generated method stub
		return true;
	}

}
