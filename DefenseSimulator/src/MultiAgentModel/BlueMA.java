package MultiAgentModel;

import java.util.ArrayList;

import BlueC2Model.BlueBattalionC2;
import BlueC2Model.BlueCompany;
import BlueC2Model.Sensor;
import BlueC2Model.Shooter;
import Common.CEInfo;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicMultiAgentModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class BlueMA extends BasicMultiAgentModel {
	
	public static String _IE_LocNoticeIn = "LocNoticeIn";
	public static String _IE_AngleDmgIn = "AngleDmgIn";	
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
	protected static String _CS_BranchingLoc = "branchingLocation";
	protected static String _CS_BranchingDirFire = "branchingDirFire";
	protected static String _CS_BranchingAngFire = "branchingAngFire";
	protected static String _CS_BranchingOrder = "branchingOrder";
	
	private BlueBattalion _battalion;
	
	public BlueMA(CEInfo _myInfo, BlueBattalion _battalion) {
		/*
		 * Set Model Name
		 */
		
		String _name = "BlueMultiAgent";
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
		 * Add Coupling
		 */
		
		AddCouplingState(_CS_Normal, true);		
		AddCouplingState(_CS_BranchingAngFire, -1);
		AddCouplingState(_CS_BranchingDirFire, -1);
		AddCouplingState(_CS_BranchingLoc, -1);
		AddCouplingState(_CS_BranchingOrder, -1);
		/*
		 * Add Component
		 */
		
		/*
		 * Make C2
		 */
			
		this._battalion = _battalion;
		
		_battalion.Activated();
		
		addComponent(_battalion);
		
//		
//		BluePlatoon bluePlatoon = new BluePlatoon();
//		Sensor sensor = new Sensor();
//		Shooter shooter = new Shooter();
//		
//		bluePlatoon.Activated();
//		sensor.Activated();
//		shooter.Activated();
//
//		addComponent(bluePlatoon);
//		addComponent(sensor);
//		addComponent(shooter);
		

		// TODO Use parameters
		// TODO make coupling using id and number of agents
		/*
		 * Make platoons
		 */
		int n = 1;
		for(int i = 1;i < n;i++){
			BlueCompany bluePlatoon = new BlueCompany();
			
			AddCoupling(_CS_BranchingDirFire, i, this, _IE_DirectFireIn, bluePlatoon, _IE_DirectFireIn);
			AddCoupling(_CS_BranchingLoc, i, this, _IE_LocNoticeIn, bluePlatoon, _IE_LocNoticeIn);
			
			AddCoupling(_CS_Normal, true, bluePlatoon, _OE_DirectFireOut, this, _OE_DirectFireOut);
			AddCoupling(_CS_Normal, true, bluePlatoon, _OE_LocUpdateOut, this, _OE_LocUpdateOut);
			
			AddCoupling(_CS_Normal, true, bluePlatoon, _INT_ReportOut, blueC2, _INT_ReportIn);
			AddCoupling(_CS_BranchingOrder, i, blueC2, _INT_OrderOut, bluePlatoon, _INT_OrderIn);
			
		}
		
		/*
		 * make sensors
		 */
		
		n = 1;
		for(int i = 1;i < n;i++){
			Sensor sensor = new Sensor();
			
			AddCoupling(_CS_BranchingLoc, i, this, _IE_LocNoticeIn, sensor, _IE_LocNoticeIn);
			
			AddCoupling(_CS_Normal, true, sensor, _INT_ReportOut, blueC2, _INT_ReportIn);
		}
		
		/*
		 * make shooters
		 */
		n = 1;
		for(int i = 1;i < n;i++){
			Shooter shooter = new Shooter();
			
			AddCoupling(_CS_Normal, true, blueC2, _INT_OrderOut, shooter, _INT_OrderIn);
			
			AddCoupling(_CS_Normal, true, shooter, _OE_AngleFireOut, this, _OE_AngleFireOut);
		}
		
	}

	@Override
	public boolean Delta(Message arg0) {
		
		// TODO changing coupling structure
		return false;
	}

}
