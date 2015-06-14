package CommonMap;

import CommonInfo.XY;

public class GridInfo {
	
	public static double _r = 10;
	public static int _totalNumGrid;
	
	
	public int _gridIndex;
	public XY _mainLoc;
	
	//TODO fill the information 
	
	
	public GridInfo() {
	}
	
	public void putGridInfo(int _gridIndex, XY _mainLoc){
		this._gridIndex = _gridIndex;
		this._mainLoc = new XY(_mainLoc);
	}
	
	public boolean isInThisGrid(XY _loc){
		double _dist = _loc.distance(_mainLoc);
		double _bearing = _loc.calBearing(_mainLoc);
		
		double _boundary = _r * (Math.sqrt(3) / 2) / Math.cos(Math.toRadians(_bearing));
		
		if(_dist <= _boundary){
			return true;
		}
		
		return false;
	}
	
	public double getSpeedInThisGrid(double _maxSpeed){
		//TODO make some formula for getting speed 
	
		return _maxSpeed;
	}

}
