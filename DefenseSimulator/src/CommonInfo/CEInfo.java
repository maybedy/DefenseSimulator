package CommonInfo;

import CommonMap.GridInfo;
import CommonType.CEType;
import CommonType.WTType;
import CommonType.WTTypeParam;
import MsgCommon.MsgAngleDmg;
import MsgCommon.MsgDirectFire;

public class CEInfo {
	public UUID _id;
	
	public XY _myLoc;
	public GridInfo _currentGrid;
	
	
	public CEType _unitType;
	public double _HP;
	
	public boolean _movable;
	public double _maxSpeed;
	
	public boolean _detectable;
	public double _detectRange;
	
	public boolean _engageable;
	public WTType _weaponType;
	public WTTypeParam _weaponParam; 
	
	
	public CEInfo(){
		_myLoc = new XY();
	}
	
	public CEInfo(CEInfo _info){
		_id = new UUID(_info._id);
		_myLoc = new XY(_info._myLoc);
		this._state = _info._state;
		this._detectRange = _info._detectRange;	
	}
	
	public void setInfo(CEInfo _info){
		_id.setUUID(_info._id);
		_myLoc.setXY(_info._myLoc);
		this._state = _info._state;
		this._detectRange = _info._detectRange;
	}
	
	public double applyAssessment(){
		//TODO change dmg assessment function
		double _newHP = _HP;
		
		return _newHP;
	}
	
	public double applyAssessment(MsgAngleDmg _msgAngleDmg){
		//TODO change dmg assessment function
		double _newHP = _HP;

		return _newHP;
	}

	public double applyAssessment(MsgDirectFire _msgDirectFire){
		//TODO change dmg assessment function
		double _newHP = _HP;
		
		return _newHP;
	}
	
}
