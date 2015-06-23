package CommonInfo;

import CommonMap.GridInfo;
import CommonMap.GridInfoNetwork;
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
	public double _detectRate;
	
	public boolean _engageable;
	public WTType _weaponType;
	public WTTypeParam _weaponParam; 
	
	public CEInfo(UUID _id){
		_id = new UUID(_id);
		_myLoc = new XY(0,0);
		this._currentGrid = GridInfoNetwork.findMyGrid(-1);
		
	}
	
	
	public CEInfo(UUID _id, XY _loc, GridInfo _grid){
		_id =new UUID(_id);
		_myLoc = new XY(_loc);
		this._currentGrid = _grid;
		
	}
	
	public CEInfo(CEInfo _info){
		_id = new UUID(_info._id);
		_myLoc = new XY(_info._myLoc);
		
		this._currentGrid = _info._currentGrid;
		this._unitType = _info._unitType;
		this._HP = _info._HP;
		this._detectRange = _info._detectRange;
		
		this._movable = _info._movable;
		this._maxSpeed = _info._maxSpeed;
		this._detectable = _info._detectable;
		this._detectRange = _info._detectRange;
		this._detectRate = _info._detectRate;
		
		this._engageable = _info._engageable;
		this._weaponType = _info._weaponType;
		this._weaponParam = _info._weaponParam;
	}
	
	public void setInfo(CEInfo _info){
		_id.setUUID(_info._id);
		_myLoc.setXY(_info._myLoc);
		
		this._currentGrid = _info._currentGrid;
		this._unitType = _info._unitType;
		this._HP = _info._HP;
		this._detectRange = _info._detectRange;
		
		this._movable = _info._movable;
		this._maxSpeed = _info._maxSpeed;
		this._detectable = _info._detectable;
		this._detectRange = _info._detectRange;
		this._detectRate = _info._detectRate;
		
		this._engageable = _info._engageable;
		this._weaponType = _info._weaponType;
		this._weaponParam = _info._weaponParam;
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
