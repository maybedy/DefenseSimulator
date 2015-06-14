package MsgC2Report;

import CommonInfo.UUID;


public class MsgReport {
	public UUID _destUUID;
	public UUID _srcUUID;
	
	public ReportType _reportType;
	
	public Object _msgValue;

	public MsgReport(ReportType _reportType, UUID _srcUUID, UUID _destUUID, Object _msgValue) {
		this._reportType = _reportType;
		this._destUUID = _destUUID;
		this._srcUUID = _srcUUID;
		this._msgValue = _msgValue;
	}
	

}
