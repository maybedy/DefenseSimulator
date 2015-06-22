package MsgCommon;

import CommonInfo.XY;
import CommonType.WTTypeAngleParam;

public class MsgAngleFire {
	public WTTypeAngleParam _angleFireParam;
	public XY _impactLoc;
	
	public MsgAngleFire(MsgAngleFire _fire){
		this._impactLoc = new XY(_fire._impactLoc);
		this._angleFireParam = _fire._angleFireParam;
	}
	
	public MsgAngleFire(XY _impactLoc, WTTypeAngleParam _inputParam){
		this._impactLoc = new XY(_impactLoc);
		_angleFireParam = _inputParam;
	}
	
}
