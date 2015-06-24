package MsgCommon;

import CommonInfo.UUID;
import CommonType.WTTypeAngleParam;

public class MsgAngleDmg {
	public WTTypeAngleParam _angleFireParam;
	public UUID _destUUID;
	
	public MsgAngleDmg(UUID _destUUID, WTTypeAngleParam _angleFireParam){
		this._destUUID = new UUID(_destUUID);
		this._angleFireParam = _angleFireParam;
	}
}
