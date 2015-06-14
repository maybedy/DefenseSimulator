package ModelMultiAgent_Blue;

import java.util.ArrayList;

import CommonInfo.CEInfo;
import CommonInfo.UUID;
import ModelAgent_BlueC2.BlueBattalionC2;
import ModelAgent_BlueC2.BlueCompany;
import ModelAgent_BlueC2.Sensor;
import ModelAgent_BlueC2.Shooter;
import MsgC2Order.MsgOrder;
import MsgC2Report.MsgLocNotice;
import MsgCommon.MsgDirectFire;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicMultiAgentModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class BlueBattalion extends BasicMultiAgentModel {
	//component is battalionC2, blueCompany, sensor, shooter
	

	public static String _IE_LocNoticeIn = "LocNoticeIn";
	public static String _IE_DirectFireIn = "DirectFireIn";
	
	public static String _OE_AngleFireOut = "AngleFireOut";
	public static String _OE_DirectFireOut = "DirectFireOut";
	public static String _OE_LocUpdateOut = "LocUpdateOut";
	
	private static String _CS_Normal = "Normal";
	private static String _CS_MsgBranch = "Branch";
	
	public UUID _modelUUID;
	
	private BlueBattalionC2 _C2Agent;
	private ArrayList<BlueCompany> _CompanyList;
	private ArrayList<Sensor> _SensorList;
	private ArrayList<Shooter> _ShooterList;
	
	
	public BlueBattalion(CEInfo _myInfo, BlueBattalionC2 _C2Agent, ArrayList<BlueCompany> _CompanyList, ArrayList<Sensor> _SensorList, ArrayList<Shooter> _ShooterList) {
		
		String _name = "BlueBattalion";
		SetModelName(_name);
		this._modelUUID = _myInfo._id;
		
		AddInputEvent(_IE_DirectFireIn);
		AddInputEvent(_IE_LocNoticeIn);
		
		AddOutputEvent(_OE_AngleFireOut);
		AddOutputEvent(_OE_DirectFireOut);
		AddOutputEvent(_OE_LocUpdateOut);
		
		this.AddCouplingState(_CS_Normal, true);
		
		
		this._C2Agent = _C2Agent;
		_C2Agent.Activated();
		this.addComponent(_C2Agent);
		
		this._SensorList = _SensorList;
		for(Sensor _eachSensor: _SensorList)
		{
			_eachSensor.Activated();
			this.addComponent(_eachSensor);
			
			this.AddCoupling(_CS_MsgBranch, _eachSensor._modelUUID.getUniqID_ABM(), this, this._IE_LocNoticeIn, _eachSensor, _eachSensor._IE_LocNoticeIn);
			
			this.AddCoupling(_CS_Normal, true, _eachSensor, _eachSensor._OE_ReportOut, _C2Agent, _C2Agent._IE_ReportIn);
		}
		
		this._ShooterList = _ShooterList;
		for(Shooter _eachShooter : _ShooterList){
			_eachShooter.Activated();
			this.addComponent(_eachShooter);
			
			this.AddCoupling(_CS_MsgBranch, _eachShooter._modelUUID.getUniqID_Batt(), _C2Agent, _C2Agent._OE_OrderOut, _eachShooter, _eachShooter._IE_OrderIn);
			
			this.AddCoupling(_CS_Normal, true, _eachShooter, _eachShooter._OE_AngleFireOut, this, this._OE_AngleFireOut);
		}

		this._CompanyList = _CompanyList;
		for(BlueCompany _eachCompany : _CompanyList){
			
			_eachCompany.Activated();
			this.addComponent(_eachCompany);
			
			this.AddCoupling(_CS_MsgBranch, _eachCompany._modelUUID.getUniqID_Batt(), this, this._IE_DirectFireIn, _eachCompany, _eachCompany._IE_DirectFireIn);
			this.AddCoupling(_CS_MsgBranch, _eachCompany._modelUUID.getUniqID_Batt(), this, this._IE_LocNoticeIn, _eachCompany, _IE_LocNoticeIn);
			
			this.AddCoupling(_CS_Normal, true, _eachCompany, _eachCompany._OE_DirectFireOut, this, this._OE_DirectFireOut);
			this.AddCoupling(_CS_Normal, true, _eachCompany, _eachCompany._OE_LocUpdateOut, this, this._OE_LocUpdateOut);
			this.AddCoupling(_CS_Normal, true, _eachCompany, _eachCompany._OE_ReportOut, _C2Agent,_C2Agent._IE_ReportIn);
			
		}
		
		
	}

	@Override
	public boolean Delta(Message msg) {
		// TODO Auto-generated method stub
		if(msg.GetDstEvent() == _C2Agent._OE_OrderOut){
			MsgOrder _orderMsg = (MsgOrder)msg.GetValue();
			
			this.updateCouplingState(_CS_MsgBranch, _orderMsg._destUUID.getUniqID_Batt(), true);
			return true;
		}
		else if(msg.GetDstEvent() == _IE_DirectFireIn){
			MsgDirectFire _directFireMsg = (MsgDirectFire)msg.GetValue();
			
			this.updateCouplingState(_CS_MsgBranch, _directFireMsg._destUUID.getUniqID_Batt(), true);
			
			return true;
			
		}else if(msg.GetDstEvent() == _IE_LocNoticeIn){
			MsgLocNotice _locNoticeMsg = (MsgLocNotice)msg.GetValue();
			
			this.updateCouplingState(_CS_MsgBranch, _locNoticeMsg._destUUID.getUniqID_Batt(), true);
			return true;
		}else {
			this.updateCouplingState(_CS_MsgBranch, -1, true);
			
			return true;
		}
		
	}

}
