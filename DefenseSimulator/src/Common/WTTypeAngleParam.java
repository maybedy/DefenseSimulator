package Common;

public class WTTypeAngleParam extends WTTypeParam {

	public double _vertical_error;
	public double _horizontal_error;
	public double _casualty_radius;
	public double _time_for_flight;
	
	public WTTypeAngleParam(){
		
		
	}
	
	public WTTypeAngleParam(WTTypeAngleParam _param){
		
		this._vertical_error = _param._vertical_error;
		this._horizontal_error = _param._horizontal_error;
		this._casualty_radius = _param._casualty_radius;
		this._time_for_flight = _param._time_for_flight;
		
	}
		
}
