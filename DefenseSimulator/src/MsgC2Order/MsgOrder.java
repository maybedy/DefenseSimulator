package MsgC2Order;

import CommonInfo.UUID;

public class MsgOrder {
	
	public OrderType _orderType;
	public Object _orderMsg;
	public UUID _srcUUID;
	public UUID _destUUID;

	public MsgOrder(OrderType _orderType, UUID _srcUUID, UUID _destUUID, Object _orderMsg) {
		this._orderType = _orderType;
		this._orderMsg = _orderMsg;
		
		this._srcUUID = _srcUUID;
		this._destUUID = _destUUID;
		
	}
	
	
	
}
