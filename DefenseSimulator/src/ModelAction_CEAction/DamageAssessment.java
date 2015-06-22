package ModelAction_CEAction;

import java.util.ArrayList;

import CommonInfo.CEInfo;
import CommonInfo.UUID;
import MsgC2Report.MsgLocUpdate;
import MsgC2Report.MsgReport;
import MsgC2Report.ReportType;
import MsgCommon.MsgAngleDmg;
import MsgCommon.MsgDirectFire;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicActionModel;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class DamageAssessment extends BasicActionModel {
	public static String _IE_AngleDmgIn = "AngleDmgIn";	
	public static String _IE_DirectFireIn = "DirectFireIn";
	public static String _IE_MyInfoIn = "MyInfoIn";
	
	public static String _OE_AssessOut = "AssessmentOut";
	
	private static String _AWS_DirectDamageQueue = "DirectDamageQueue";
	private static String _AWS_AngleDamageQueue = "AngleDamageQueue";
	
	private static String _CS_MyHP = "HP";
	private static String _CS_MyInfo = "MyInfo";
	
	private boolean _isContinuable = true;
	
	private static String _AS_Action = "Action";
	
	public UUID _modelUUID;
	
	private enum _ActState {
		Stop, Calculate
	}

	public DamageAssessment(CEInfo _myInfo) {
		String _name = "DamageAssessmentAction";
		SetModelName(_name);
		
		this._modelUUID = _myInfo._id;
		
		AddInputEvent(_IE_AngleDmgIn);
		AddInputEvent(_IE_DirectFireIn);
		
		AddInputEvent(_IE_MyInfoIn);
		
		
		AddOutputEvent(_OE_AssessOut);
		
		AddConState(_AWS_DirectDamageQueue, new ArrayList<MsgDirectFire>());
		AddConState(_AWS_AngleDamageQueue, new ArrayList<MsgAngleDmg>());
		
		
		AddConState(_CS_MyHP, _myInfo._HP, true, STATETYPE_NUMERIC);
		AddConState(_CS_MyInfo, _myInfo);
		
		AddActState(_AS_Action, _ActState.Stop, true, STATETYPE_CATEGORY);
		
	}

	@Override
	public boolean Act(Message msg) {
		if(this.GetActStateValue(_AS_Action) == _ActState.Stop){
			
			// no need to make msg
			return true;
		}else if(this.GetActStateValue(_AS_Action) == _ActState.Calculate){
			
			CEInfo _myInfo = (CEInfo)this.GetConStateValue(_CS_MyInfo);
			double _newHP = _myInfo._HP; 
			
			ArrayList<MsgDirectFire> _directFireList = (ArrayList<MsgDirectFire>)this.GetAWStateValue(_AWS_DirectDamageQueue);
			ArrayList<MsgAngleDmg> _angleFireList = (ArrayList<MsgAngleDmg>)this.GetAWStateValue(_AWS_AngleDamageQueue);
			
			if(_directFireList.isEmpty()){
				//nothing happened
			}else {
				for(MsgDirectFire _eachMsg : _directFireList){
					_newHP = _myInfo.applyAssessment(_eachMsg);					
				}
				_directFireList.clear();
				this.UpdateAWStateValue(_AWS_DirectDamageQueue, _directFireList);
			}
			
			if(_angleFireList.isEmpty()){
				//nothing
			}else {
				for(MsgAngleDmg _eachMsg : _angleFireList){
					_newHP = _myInfo.applyAssessment(_eachMsg);					
				}
				_angleFireList.clear();
				this.UpdateAWStateValue(_AWS_AngleDamageQueue, _angleFireList);
			}
			MsgLocUpdate _myInfoMsg = new MsgLocUpdate(_myInfo);
			MsgReport _reportMsg = new MsgReport(ReportType.Assessment, this._modelUUID, null, _myInfoMsg);
			
			this.UpdateConStateValue(_CS_MyHP, _newHP);
			this.UpdateConStateValue(_CS_MyInfo, _myInfo);
			
			msg.SetValue(_OE_AssessOut, _reportMsg);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean Decide() {
		if(this.GetActStateValue(_AS_Action) == _ActState.Stop){
			this._isContinuable = false;
			ResetContinue();
			this.UpdateActStateValue(_AS_Action, _ActState.Calculate);
			return true;
		}else if(this.GetActStateValue(_AS_Action) == _ActState.Calculate){
			// Both Q are empty
			ArrayList<MsgAngleDmg> _angleDmgList = (ArrayList<MsgAngleDmg>)this.GetAWStateValue(_AWS_AngleDamageQueue);
			ArrayList<MsgDirectFire> _directDmgList = (ArrayList<MsgDirectFire>)this.GetAWStateValue(_AWS_DirectDamageQueue);
			
			if(_angleDmgList.isEmpty() && _directDmgList.isEmpty()){
				// Both Q are empty
				this._isContinuable = false;
				ResetContinue();
				this.UpdateActStateValue(_AS_Action, _ActState.Stop);	
			}else {
				// Otherwise
				this._isContinuable = false;
				ResetContinue();
				this.UpdateActStateValue(_AS_Action, _ActState.Calculate);	
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean Perceive(Message msg) {
		
		if(msg.GetDstEvent() == _IE_AngleDmgIn){
			ArrayList<MsgAngleDmg> _angleDmgList = (ArrayList<MsgAngleDmg>)this.GetAWStateValue(_AWS_AngleDamageQueue);
			MsgAngleDmg _angleDmgMsg = (MsgAngleDmg)msg.GetValue();
			_angleDmgList.add(_angleDmgMsg);
			
			this.UpdateAWStateValue(_AWS_AngleDamageQueue, _angleDmgList);
			
			
			if(this.GetActStateValue(_AS_Action) == _ActState.Stop){
			}else if(this.GetActStateValue(_AS_Action) == _ActState.Calculate){
				this.makeContinue();
			}
			return true;
		}else if(msg.GetDstEvent() == _IE_DirectFireIn){
			ArrayList<MsgDirectFire> _directDmgList = (ArrayList<MsgDirectFire>)this.GetAWStateValue(_AWS_DirectDamageQueue);
			MsgDirectFire _directFireMsg = (MsgDirectFire)msg.GetValue();
			_directDmgList.add(_directFireMsg);
			
			this.UpdateAWStateValue(_AWS_DirectDamageQueue, _directDmgList);

			if(this.GetActStateValue(_AS_Action) == _ActState.Stop){
			}else if(this.GetActStateValue(_AS_Action) == _ActState.Calculate){
				this.makeContinue();
			}
			return true;
		}else if(msg.GetDstEvent() == _IE_MyInfoIn){
			MsgReport _reportMsg = (MsgReport)msg.GetValue();
			MsgLocUpdate _locMsg = (MsgLocUpdate)_reportMsg._msgValue;
			
			this.UpdateConStateValue(_CS_MyInfo, _locMsg._myInfo);
			Continue();
		}
		
		
		
		return false;
	}

	@Override
	public double TimeAdvance() {
		this._isContinuable = true;
		if(this.GetActStateValue(_AS_Action) == _ActState.Stop){
			return Double.POSITIVE_INFINITY;
		}else if(this.GetActStateValue(_AS_Action) == _ActState.Calculate){
			this._isContinuable = false;
			return 0;
		}
		
		return 0;
	}

	public void makeContinue(){
		if(this._isContinuable){
			Continue();
		}else {
			ResetContinue();
		}
	}
	
}
