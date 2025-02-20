package CommonMap;

import CommonInfo.XY;

public class GridInfo {
	
	public static double _r = 125/2/Math.sqrt(3);
	public static int _totalNumGrid=0;
	
	
	public int _gridIndex;
	public XY _mainLoc;
	public int obstacle;
	
	//TODO fill the information 
	
	
	public GridInfo(int _gridIndex, XY _mainLoc, int obstacle) {
		this._gridIndex = _gridIndex;
		this._mainLoc = new XY(_mainLoc);
		this.obstacle = obstacle;
	}
	
	public void putGridInfo(int _gridIndex, XY _mainLoc, int obstacle){
		this._gridIndex = _gridIndex;
		this._mainLoc = new XY(_mainLoc);
		this.obstacle = obstacle;
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
