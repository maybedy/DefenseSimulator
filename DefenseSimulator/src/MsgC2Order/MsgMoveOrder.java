package MsgC2Order;

import CommonInfo.Path;

public class MsgMoveOrder {
	
	private Path _pathForObj;

	public MsgMoveOrder(Path _path) {
		this._pathForObj = _path;
	}
	
	public void setPath(Path _path){
		this._pathForObj = _path;
	}
	
	public Path getPath() {
		return _pathForObj;
	}
	
}
