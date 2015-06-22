package MsgC2Report;

import java.util.ArrayList;
import CommonInfo.CEInfo;
import CommonInfo.UUID;

public class MsgLocNotice {
	public ArrayList<CEInfo> _nearbyList;
	
	public MsgLocNotice() {
		this._nearbyList = new ArrayList<CEInfo>();
	}
	
	public MsgLocNotice(MsgLocNotice _msgLocNotice){
		_nearbyList = new ArrayList<CEInfo>(_msgLocNotice._nearbyList);
	}
	
	public void SetNearByList(ArrayList<CEInfo> _nearbyList){
		this._nearbyList = _nearbyList;
	}
	
	public void AddNearByAgent(CEInfo _nearbyagent) {
		this._nearbyList.add(_nearbyagent);
	}
	
	public ArrayList<CEInfo> GetNearByList(){
		return this._nearbyList;
	}
}
