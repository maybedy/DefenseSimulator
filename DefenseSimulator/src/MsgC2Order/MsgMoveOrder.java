package MsgC2Order;

import java.util.ArrayList;

import CommonInfo.XY;

public class MsgMoveOrder {
	
	private ArrayList<XY> _pathForObj;

	public MsgMoveOrder(ArrayList<XY> _path) {
		this._pathForObj = _path;
	}
	
	public void setPath(ArrayList<XY> _path){
		this._pathForObj = _path;
	}
	
	public ArrayList<XY> getPath() {
		return _pathForObj;
	}

}
