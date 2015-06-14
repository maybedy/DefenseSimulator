package CommonType;

public enum CEType {
	Unknown(0),
	
	// Sign of index: + ==> Blud, - ==> Red
	// Absolute value of index: engagement priority
	
	
	BC2(1),
	BPlatoon(2),
	BSensor(3), 
	BShooter(4),
	
	RC2(-1),
	RPlatoon(-2);
		
	private int _index;
	
	private CEType(int _index){
		this._index = _index;
	}
	
	public int getIndex(){
		return _index;
	}
	
}
