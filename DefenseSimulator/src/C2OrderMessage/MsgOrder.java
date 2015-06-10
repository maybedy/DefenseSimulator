package C2OrderMessage;

import Common.OrderType;

public class MsgOrder {
	
	public OrderType _orderType;
	public Object _orderMsg;

	public MsgOrder(OrderType _orderType, Object _orderMsg) {
		this._orderType = _orderType;
		this._orderMsg = _orderMsg;
	}
	
	
	
}
