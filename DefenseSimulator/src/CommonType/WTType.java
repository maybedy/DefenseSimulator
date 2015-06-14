package CommonType;

public enum WTType {
	
	Unknown(0),
		
	B_DirectFire(11),
	B_AngleFire(12),
	
	R_DirectFire(-11);
	
	private int _index;
	
	private WTType(int _index){
		this._index = _index;
	}
	
	public int getIndex(){
		return _index;
	}
	
	
}
