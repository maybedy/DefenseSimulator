package MsgC2Report;

import CommonInfo.CEInfo;

public class MsgLocUpdate {
	public CEInfo _myInfo;
	
	public MsgLocUpdate(){
	}
		
	public MsgLocUpdate(CEInfo _myInfo){
		this._myInfo = _myInfo;
	}
	
	public CEInfo GetMyInfo(){
		return this._myInfo;
	}
	
	
}
