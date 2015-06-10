package MultiAgentModel;

import java.util.ArrayList;

import BlueC2Model.BlueBattalionC2;
import BlueC2Model.BlueCompany;
import BlueC2Model.Sensor;
import BlueC2Model.Shooter;
import Common.CEInfo;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicMultiAgentModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class BlueBattalion extends BasicMultiAgentModel {
	//component is battalionC2, blueCompany, sensor, shooter
	

	public static String _IE_LocNoticeIn = "LocNoticeIn";
	public static String _IE_DirectFireIn = "DirectFireIn";
	
	public static String _OE_AngleFireOut = "AngleFireOut";
	public static String _OE_DirectFireOut = "DirectFireOut";
	
	private BlueBattalionC2 _C2Agent;
	private ArrayList<BlueCompany> _CompanyList;
	private ArrayList<Sensor> _SensorList;
	private ArrayList<Shooter> _ShooterList;
	
	
	public BlueBattalion(CEInfo _myInfo, BlueBattalionC2 _C2Agent, ArrayList<BlueCompany> _CompanyList, ArrayList<Sensor> _SensorList, ArrayList<Shooter> _ShooterList) {
		
		String _name = "BlueBattalion";
		SetModelName(_name);
		
		AddInputEvent(_IE_DirectFireIn);
		AddInputEvent(_IE_LocNoticeIn);
		
		AddOutputEvent(_OE_AngleFireOut);
		AddOutputEvent(_OE_DirectFireOut);
		
		
		
		
		
	}

	@Override
	public boolean Delta(Message arg0) {
		// TODO Auto-generated method stub
		this.
		return false;
	}

}
