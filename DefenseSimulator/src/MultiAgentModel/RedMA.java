package MultiAgentModel;

import java.util.ArrayList;

import Common.CEInfo;
import RedC2Model.RedBattalionC2;
import RedC2Model.RedCompany;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicMultiAgentModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class RedMA extends BasicMultiAgentModel {

	public static String _IE_LocNoticeIn = "LocNoticeIn";
	public static String _IE_AngleDmgIn = "AngleDmgIn";	
	public static String _IE_DirectFireIn = "DirectFireIn";
	
	public static String _OE_LocUpdateOut = "LocUpdateOut";
	public static String _OE_AngleFireOut = "AngleFireOut";
	public static String _OE_DirectFireOut = "DirectFireOut";
	
//	protected String _INT_OrderOut = "OrderOut";
//	protected String _INT_OrderIn = "OrderIn";
//	
//	protected String _INT_ReportOut = "ReportOut";
//	protected String _INT_ReportIn = "ReportIn";
//	
	protected static String _CS_Normal = "normal";
	protected static String _CS_BranchingLoc = "branchingLocation";
	protected static String _CS_BranchingDirFire = "branchingDirFire";
	protected static String _CS_BranchingAngDmg = "branchingAngDamage";
	protected static String _CS_BranchingOrder = "branchingOrder";
	
	private ArrayList<RedBattalion> _battalionList;
	
	public RedMA(CEInfo _myInfo, ArrayList<RedBattalion> _battalionList) {
		
		String _name = "RedMultiAgent";
		SetModelName(_name);
		
		/*
		 * Add input and output port
		 */
		
		AddInputEvent(_IE_AngleDmgIn);
		AddInputEvent(_IE_DirectFireIn);
		AddInputEvent(_IE_LocNoticeIn);
		
		AddOutputEvent(_OE_AngleFireOut);
		AddOutputEvent(_OE_DirectFireOut);
		AddOutputEvent(_OE_LocUpdateOut);
		
		/*
		 * Add Coupling State
		 */
		
		AddCouplingState(_CS_Normal, true);		
		AddCouplingState(_CS_BranchingAngDmg, -1);
		AddCouplingState(_CS_BranchingDirFire, -1);
		AddCouplingState(_CS_BranchingLoc, -1);
		AddCouplingState(_CS_BranchingOrder, -1);
		
		/*
		 * Add Component
		 */
		
		/*
		 * Make C2
		 */
		
		this._battalionList = _battalionList;
		
		
		for(RedBattalion _eachBattalion : _battalionList){
			_eachBattalion.Activated();
			this.addComponent(_eachBattalion);
			
			
			
			
			AddCoupling(_CS_BranchingAngDmg, i, this, _IE_AngleDmgIn, redPlatoon, _IE_AngleDmgIn);
			AddCoupling(_CS_BranchingDirFire, i ,this, _IE_DirectFireIn, redPlatoon, _IE_DirectFireIn);
			AddCoupling(_CS_BranchingLoc, i, this, _IE_LocNoticeIn, redPlatoon, _IE_LocNoticeIn);
			
			AddCoupling(_CS_Normal, true, redPlatoon, _OE_LocUpdateOut, this, _OE_LocUpdateOut);
			AddCoupling(_CS_Normal, true, redPlatoon, _OE_DirectFireOut, this, _OE_DirectFireOut);
			
			AddCoupling(_CS_BranchingOrder, i, redC2, _INT_OrderOut, redPlatoon, _INT_OrderIn);
			AddCoupling(_CS_Normal, true, redPlatoon, _INT_ReportOut, redC2, _INT_ReportIn);
			

		}
		
	}

	@Override
	public boolean Delta(Message arg0) {
		// TODO Auto-generated method stub
		
		// TODO specification coupling state
		return true;
	}

}
