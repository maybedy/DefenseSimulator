package CommonInfo;

import java.util.ArrayList;

import CommonMap.GridInfo;
import EnvElem.Grid;

public class Path {
	
	
	public ArrayList<GridInfo> _pathForObj;

	public Path(ArrayList<GridInfo> _path) {
		this._pathForObj = _path;
	}
	
	public boolean isEmpty(){
		if(_pathForObj == null)
			return true;
		
		return _pathForObj.isEmpty();
	}
	
	
	public GridInfo getCurrentObject() {
		GridInfo _ret = _pathForObj.get(0);
		
		return _ret;
	}
	
	public GridInfo removeCurrentObject(){
		GridInfo _ret = _pathForObj.remove(0);
		
		return _ret;
	}

}
