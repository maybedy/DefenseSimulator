package Message;

import Common.*;

public class MsgDirectFire {
	public UUID _destUUID;
	public UUID _srcUUID;
	public WTType _type;
		
	public MsgDirectFire(){
		_destUUID = new UUID();
		_srcUUID = new UUID();
	}
	
	public MsgDirectFire(UUID _destUUID, UUID _srcUUID, WTType _type){
		this._destUUID = new UUID(_destUUID);
		this._srcUUID = new UUID(_srcUUID);
		this._type = _type;
	}
	
	public MsgDirectFire(MsgDirectFire _fire){
		this._destUUID = new UUID(_fire._destUUID);
		this._srcUUID = new UUID(_fire._srcUUID);
		this._type = _fire._type;
	}
	
}