package CommonType;

public class WTTypeAngleParam extends WTTypeParam {
	public WTType _type;
	public double _power;
	public double _accuracyRate;
	public double _casualty_radius;
	public double _time_for_flight;
	
	public WTTypeAngleParam(WTType _type, double _power, double _accuracyRate, double _casualty_radius, double _time_for_flight){
		
		this._type =_type;
		this._power = _power;
		this._accuracyRate = _accuracyRate;
		this._casualty_radius = _casualty_radius;
		this._time_for_flight = _time_for_flight;
		
	}
	
	public WTTypeAngleParam(WTTypeAngleParam _param){
		
		this._type =_param._type;
		
		this._power = _param._power;
		this._accuracyRate = _param._accuracyRate;
		this._casualty_radius = _param._casualty_radius;
		this._time_for_flight = _param._time_for_flight;
		
	}
		
}
