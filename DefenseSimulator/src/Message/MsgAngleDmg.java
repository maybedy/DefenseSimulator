package Message;

import Common.*;

public class MsgAngleDmg {
	public UUID _destUUID;
		
	public MsgAngleDmg(){
		_destUUID = new UUID();
	}
	public MsgAngleDmg(UUID _destUUID){
		
		this._destUUID = new UUID(_destUUID);
	}
}
