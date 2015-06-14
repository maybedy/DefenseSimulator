package MsgCommon;

import CommonInfo.XY;
import CommonType.WTTypeAngleParam;

public class MsgAngleFire {
	public WTTypeAngleParam _angleFireParam;
	public XY _impactLoc;
	
	public double _casualty_radius;
	public double _time_for_flight;
	
	public MsgAngleFire(){
		_impactLoc = new XY();
	}
	
	public MsgAngleFire(MsgAngleFire _fire){
		this._impactLoc = new XY(_fire._impactLoc);
		this._casualty_radius = _fire._casualty_radius;
		this._time_for_flight = _fire._time_for_flight;
	}
	
	public MsgAngleFire(XY _impactLoc, double _casualty_radius, double _time_for_flight){
		this._impactLoc = new XY(_impactLoc);
		this._casualty_radius = _casualty_radius;
		this._time_for_flight = _time_for_flight;
	}
	
}
