package CommonType;


public class WTTypeDirectParam extends WTTypeParam {
	// TODO Fill 
	public WTType _type;
	public double _power;
	public double _accuracyRate;
	
	public WTTypeDirectParam(WTType _type, double power, double accuracyRate){
		this._type = _type;
		this._power = power;
		this._accuracyRate = accuracyRate;
		
	}
	
	public WTTypeDirectParam(WTTypeDirectParam _param){
		this._type = _param._type;
		this._power = _param._power;
		this._accuracyRate = _param._accuracyRate;
	}
		
}
