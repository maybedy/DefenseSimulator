package MsgCommon;

import CommonInfo.CEInfo;
import CommonInfo.UUID;
import CommonType.WTType;
import CommonType.WTTypeDirectParam;

public class MsgDirectFire {
	public CEInfo _target;
	public WTTypeDirectParam _myDirectParam;
	public double _currentHP;
	
	public MsgDirectFire(MsgDirectFire _fire){
		this._target = new CEInfo(_fire._target);
		this._myDirectParam = _fire._myDirectParam;
		this._currentHP = _fire._currentHP;
	}
	
	public MsgDirectFire(CEInfo _target, WTTypeDirectParam _inputParam, double _currentHP){
		this._myDirectParam = _inputParam;
		this._target = new CEInfo(_target);
		this._currentHP = _currentHP;
	}
	
}