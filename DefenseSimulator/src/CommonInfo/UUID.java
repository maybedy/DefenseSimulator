package CommonInfo;

public class UUID {
	
	public UUIDSideType _side;
	public UnitLevel _unitLevel;
	
	public String _name;
	
	public int _battlionIndex;
	public UnitType _unitType;
	public int _unitIndex;
	
	
	public enum UnitLevel{ REGIMENT, BATTLION, COMPANY, NONE}
	public enum UnitType { 
		BattalionC2(0), CompanyC2(1), SHOOTER(2), SENSOR(3), CORPS(4), None(-1);
		
		private final int value;
		private UnitType(int value) { this.value = value;}
		private int getV() { return value;}
		
	} 
	
	public enum UUIDSideType{ 
		Blue(0), Red(1), None(-1);
		
		private final int value;
		private UUIDSideType(int value)	{ this.value = value;	}
		public int getV()				{ return value; }
	}
	
	public UUID( String name, String[] list){
		_side = UUIDSideType.None;
		
		this._name = name;
		
		switch( list[0 ]){
			case "Blue":
				_side = UUIDSideType.Blue;
				switch( list[1]){
					case "1": _battlionIndex = 1; break;
				}
				switch( list[2] ){
				case "BattalionC2" : _unitType = UnitType.BattalionC2; break;
				case "CompanyC2" : _unitType = UnitType.CompanyC2; break;
				case "Shooter" : _unitType = UnitType.SHOOTER; break;
				case "Sensor" : _unitType = UnitType.SENSOR; break;
				}
				
				switch( list[3] ){
					case "1": _unitIndex = 1; break;
					case "2": _unitIndex = 2; break;
					case "3": _unitIndex = 3; break;
					case "4": _unitIndex = 4; break;
					case "5": _unitIndex = 5; break;
					case "6": _unitIndex = 6; break;
					case "7": _unitIndex = 7; break;
					case "8": _unitIndex = 8; break;
					case "9": _unitIndex = 9; break;
					default : break;
				}
				break;
			case "Red" : 
				_side = UUIDSideType.Red;
				switch( list[1]){
				case "1": _battlionIndex = 1; break;
				case "2": _battlionIndex = 2; break;
				case "3": _battlionIndex = 3; break;
				}
				switch( list[2] ){
				case "BattalionC2" : _unitType = UnitType.BattalionC2; break;
				case "CompanyC2" : _unitType = UnitType.CompanyC2; break;
				}
				
				switch( list[3] ){
					case "1": _unitIndex = 1; break;
					case "2": _unitIndex = 2; break;
					case "3": _unitIndex = 3; break;
					case "4": _unitIndex = 4; break;
					case "5": _unitIndex = 5; break;
					case "6": _unitIndex = 6; break;
					case "7": _unitIndex = 7; break;
					case "8": _unitIndex = 8; break;
					case "9": _unitIndex = 9; break;
					default : break;
				}
				break;
		}
		
	}
	
	public UUID(){
		
	}
	
	
	public UUID( UUID _uuid ){
		this.setName(_uuid.getName());
		this.setSide(_uuid.getSide());
		this.setBattlionIndex(_uuid.getBattalionIndex());
		this.setUnitType(_uuid.getUnitType());
		this.setUnitIndex(_uuid.getUnitIndex());
	}
	
	public Boolean equals( UUID _target ){
		if(_target == null)
			return false;
		else{
			if((_side == _target.getSide())
					&& (_battlionIndex == _target.getBattalionIndex())
					&& (_unitType == _target.getUnitType())
					&& (_unitIndex == _target.getUnitIndex())
					&& (_unitLevel == _target._unitLevel))
				return true;
			else
				return false;	
		}
		
	}
	///////////////////////////////////////////////
	
	public void setName( String name ){
		_name = name;
	}
	
	public String getName(){
		return _name;
	}
	
	public void setSide(UUIDSideType side){
		
		_side = side;
	}
	
	public UUIDSideType getSide(){ // 0: Blue, 1:Red
		return _side;
	}

	public void setBattlionIndex( int batt ){
		_battlionIndex = batt;
	}
	
	public int getBattalionIndex(){
		return _battlionIndex;
	}

	public void setUnitType( UnitType type ){
		_unitType = type;
	}
	
	public UnitType getUnitType(){
		return _unitType;
	}
	
	public void setUnitIndex( int unitIndex){
		_unitIndex = unitIndex;
	}
	
	public int getUnitIndex(){
		return _unitIndex;
	}
	
	public int getUniqID_Batt(){
		return _unitType.value*10+_unitIndex;
	}
	
	public int getUniqID_MAM(){
		return this.getBattalionIndex();
	}
	
	public int getUniqID_ABM(){
		return this._side.value;
	}
	
/////////////////////////////////////////////
	
	public void setUUID(UUID _uuid){
		this.setName(_uuid.getName());
		this.setSide(_uuid.getSide());
		this.setBattlionIndex(_uuid.getBattalionIndex());
		this.setUnitType(_uuid.getUnitType());
		this.setUnitIndex(_uuid.getUnitIndex());
	}
	
	public String getString(){
		//TODO make it
		
		return this._name;
	}
	
}
