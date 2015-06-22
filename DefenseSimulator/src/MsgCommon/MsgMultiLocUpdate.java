package MsgCommon;

import java.util.ArrayList;

import CommonInfo.CEInfo;


public class MsgMultiLocUpdate {
	
	ArrayList<CEInfo> _listOfAgents;

	public MsgMultiLocUpdate() {
		//Only for env model
		_listOfAgents = new ArrayList<CEInfo>();
		
	}
	
	public void SetList(ArrayList<CEInfo> _listOfAgents){
		this._listOfAgents = _listOfAgents;
	}
	
	public ArrayList<CEInfo> GetList(){
		return this._listOfAgents;
	}

}
