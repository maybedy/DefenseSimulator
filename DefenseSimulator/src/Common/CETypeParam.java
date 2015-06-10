package Common;

import java.util.ArrayList;

public class CETypeParam {
	
	public enum Side{ Blue, Red };
	public enum FireType{ Direct, Angle };

	public String _name;
	public CEType _type;
	public FireType _Ftype;	
	public int _priority;
	public Side _side;
	public double _detect_range;
	public double _maxinum_speed;
	public ArrayList<WTType> _wtlist;
	public boolean _bMajor;
	
	public CETypeParam(){
		_wtlist = new ArrayList<WTType>();
	}
	
	public CETypeParam(CETypeParam _param){
		
		this._name = _param._name;
		this._type = _param._type;
		this._priority = _param._priority;
		this._side = _param._side;
		this._detect_range = _param._detect_range;
		this._maxinum_speed = _param._maxinum_speed;
		this._wtlist = new ArrayList<WTType>(_param._wtlist);
		this._bMajor = _param._bMajor;
		this._Ftype = _param._Ftype;
	}
	
}
