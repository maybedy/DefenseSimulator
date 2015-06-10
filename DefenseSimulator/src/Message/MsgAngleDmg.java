package Message;

import Common.*;

public class MsgAngleDmg {
	public UUID _destUUID;
		
	public MsgAngleDmg(){
	}
	
	public MsgAngleDmg(UUID _destUUID){
		this._destUUID = new UUID(_destUUID);
	}
}
