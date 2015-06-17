package EnvElem;

import java.util.ArrayList;

import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicEnvElement;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class LocManager extends BasicEnvElement {
	protected String _IE_LocUpdate_B = "LocUpdateIn_B";
	protected String _IE_LocUpdate_R = "LocUpdateIn_R";
	
	protected String _OE_LocNotice_B = "LocNotice_B";
	protected String _OE_LocNotice_R = "LocNotice_R";
	
	protected String _OE_LocUpdate = "LocUpdate";
	
	protected String _ST_Act = "ActState";
	protected enum ActState{
		TICK, SEND 
	};
	
	protected String _ST_Tick = "Tick";
	
	protected String _ST_ListOfAgents_B = "ListOfAgents_B";
	protected String _ST_ListOfAgents_R = "ListOfAgents_R";
	
	protected String _ST_ListOfMsgLocNotice = "ListOfMsgLocNotice";
	
	public LocManager() //input parameters for constructor :: List of CEInfo  
	{
		/*
		 * Set Name and ID
		 */
		String Name = "Location Manager Element";
		SetModelName(Name);
		
		/*
		 * Add Input Port
		 */
		AddInputEvent(_IE_LocUpdate_B);
		AddInputEvent(_IE_LocUpdate_R);
		
		/*
		 * Add Output Port
		 */
		AddOutputEvent(_OE_LocNotice_B);
		AddOutputEvent(_OE_LocNotice_R);
		
		AddOutputEvent(_OE_LocUpdate);
		
		/*
		 * Add State
		 */
		AddState(_ST_Act , ActState.TICK); // this is triggering state
		
		ArrayList<CEInfo> ListOfAgents_B = new ArrayList<CEInfo>();
		ArrayList<CEInfo> ListOfAgents_R = new ArrayList<CEInfo>();
		AddState(_ST_ListOfAgents_B, ListOfAgents_B);
		AddState(_ST_ListOfAgents_R, ListOfAgents_R);
		AddState(_ST_ListOfMsgLocNotice, null);
		
		double tick = Constants._update_clock_time;
		AddState(_ST_Tick , tick);
		
		
		
	}
	

	@Override
	public boolean Delta(Message msg) {
		if(msg.GetDstEvent() == _IE_LocUpdate_B || msg.GetDstEvent() == _IE_LocUpdate_R) {
			MsgLocUpdate _msg_locupdate = (MsgLocUpdate)msg.GetValue();
			CEInfo _agent_info = new CEInfo(_msg_locupdate.GetMyInfo()); //cloning
			
			if( _agent_info._state == DmgState.Destroyed )
				Logger.getLogger().LogOut(this, msg.GetTime(), _agent_info._id.getFullString() + " is Destroyed " );
			
			UpdateAgentLocation(_agent_info);
			Continue();
			return true;
		}
		if(this.GetStateValue(_ST_Act) == ActState.TICK){
			
			/*
			 * state transition
			 */
			this.UpdateStateValue(_ST_Act, ActState.SEND);
				
			
			return true;
		}else if(this.GetStateValue(_ST_Act) == ActState.SEND){
			ArrayList<MsgLocNotice> _listOfMsgLocNotice = 
					(ArrayList<MsgLocNotice>)this.GetStateValue(_ST_ListOfMsgLocNotice);
			
			if(_listOfMsgLocNotice.isEmpty()){
				//nothing to send
				//System.out.println("No msg left");
				this.UpdateStateValue(_ST_Act, ActState.TICK);
			}else {
				this.UpdateStateValue(_ST_Act, ActState.SEND);	
			}
			
			return true;
		}
		
		
//		else if(msg.GetDstEvent() == _IE_LocUpdate_R){
//			// almost same as upper 'if' block -> merge it??
//			MsgLocUpdate _msg_locupdate = (MsgLocUpdate)msg.GetValue();
//			CEInfo _agent_info = new CEInfo(_msg_locupdate.GetMyInfo());
//			UpdateAgentLocation(_agent_info);
//			
//			
//			return true;
//		}
		
		return false;
	}

	@Override
	public boolean Output(Message msg) {
		// msg need dst, src model to notify its destination(for dynamic structure)
		if(this.GetStateValue(_ST_Act) == ActState.TICK){
			//nothing to send, but makes LocNotice messages for all agents			
			
			/*
			 * nothing to send
			 */
			
			/*
			 * for making msg to dmgelement
			 * updating locations
			 */
			ArrayList<CEInfo> _listOfAgents;
			_listOfAgents = MergeLocList();
			MsgMultiLocUpdate _listOfAllLoc = new MsgMultiLocUpdate();
			_listOfAllLoc.SetList(_listOfAgents);
			msg.SetValue(_OE_LocUpdate, _listOfAllLoc);
			
			/*
			 * for locNotice
			 */
			ArrayList<MsgLocNotice> _listOfMsgLocNotice = LocNoticeMsgFactory();
			this.UpdateStateValue(_ST_ListOfMsgLocNotice, _listOfMsgLocNotice);
			
			if( ((int)msg.GetTime()) % 100 == 0 ){
				Logger.getLogger().LogOut(this, msg.GetTime(), "Detected Blue Entity : " + ((ArrayList<CEInfo>)this.GetStateValue(_ST_ListOfAgents_B)).size() + "  Alive : " + getAlive( (ArrayList<CEInfo>)this.GetStateValue(_ST_ListOfAgents_B) ) );
				Logger.getLogger().LogOut(this, msg.GetTime(), "Detected Red  Entity : " + ((ArrayList<CEInfo>)this.GetStateValue(_ST_ListOfAgents_R)).size() + "  Alive : " + getAlive( (ArrayList<CEInfo>)this.GetStateValue(_ST_ListOfAgents_R) ) );
			
			}
			
			//this.updateLogInfo();
			return true;
		}else if(this.GetStateValue(_ST_Act) == ActState.SEND){
			ArrayList<MsgLocNotice> _listOfMsgLocNotice = 
					(ArrayList<MsgLocNotice>)this.GetStateValue(_ST_ListOfMsgLocNotice);
			
			if(_listOfMsgLocNotice.isEmpty()){
				System.out.println("No msg left");
			}else {
				MsgLocNotice _sendingMsg = _listOfMsgLocNotice.remove(0);
	
				if(_sendingMsg._destUUID.getSide() == UUID.UUIDSideType.Blue){ // blue
					msg.SetValue(_OE_LocNotice_B, _sendingMsg);
				}else if(_sendingMsg._destUUID.getSide() == UUID.UUIDSideType.Red) { // red
					msg.SetValue(_OE_LocNotice_R, _sendingMsg);
				}
				
				this.UpdateStateValue(_ST_ListOfMsgLocNotice, _listOfMsgLocNotice);
			}
			
			return true;
		}
		
		return false;
	}

	@Override
	public double TimeAdvance() {
		// TODO Auto-generated method stub
		if(this.GetStateValue(_ST_Act) == ActState.TICK){
			double ta = (double)this.GetStateValue(_ST_Tick);
			return ta;
		}else if(this.GetStateValue(_ST_Act) == ActState.SEND){
			return 0;
		}
		return Double.POSITIVE_INFINITY;
	}

	
	public boolean UpdateAgentLocation(CEInfo _agent_info){
		ArrayList<CEInfo> _listOfAgents ;
		String _list;
		if(_agent_info._id._side == UUID.UUIDSideType.Blue){
			_listOfAgents = (ArrayList<CEInfo>)this.GetStateValue(_ST_ListOfAgents_B);
			_list = _ST_ListOfAgents_B;
		}else if(_agent_info._id._side == UUID.UUIDSideType.Red){
			_listOfAgents = (ArrayList<CEInfo>)this.GetStateValue(_ST_ListOfAgents_R);
			_list = _ST_ListOfAgents_R;
		}else {
			// TODO makes it error
			return false;
		}
		
		if(_listOfAgents.isEmpty()){
			_listOfAgents.add(_agent_info);
		}else {
			boolean _bExist = false;
			for(CEInfo _original_info : _listOfAgents) {
				if(_original_info._id.equals(_agent_info._id)){
					_listOfAgents.remove(_original_info);
					_listOfAgents.add(_agent_info);
					_bExist = true;
					break;
				}else {
					continue;
				}
			}
			if(!_bExist){
				_listOfAgents.add(_agent_info);
			}
		}
		
		this.UpdateStateValue(_list, _listOfAgents);
		
		return true;
		
	}
	
	public ArrayList<CEInfo> GetNearByAgents(CEInfo _agent_info, ArrayList<CEInfo> _listOfAgent){
		ArrayList<CEInfo> _nearByAgents = new ArrayList<CEInfo>();
			
		XY _originalPoint = _agent_info._myLoc;
		double _detectRange = _agent_info._detectRange;
		
		for(CEInfo _other_agent : _listOfAgent){
			if(_agent_info._id.equals(_other_agent._id)){
				continue;
			}
			//if( _other_agent._state == DmgState.Destroyed ){
		//		continue; // sjkwon
		//	}
		
			XY _nearPoint = _other_agent._myLoc;
			double _dist = _originalPoint.distance(_nearPoint);
			if(_dist <= _detectRange){
				_nearByAgents.add(_other_agent);
			}else {
				continue;
			}
		}
		return _nearByAgents;
	}
	
	public ArrayList<MsgLocNotice> LocNoticeMsgFactory(){
		ArrayList<MsgLocNotice> _listOfNoticeMsg = 
				new ArrayList<MsgLocNotice>();
		ArrayList<CEInfo> _listOfAgents_B = (ArrayList<CEInfo>)this.GetStateValue(_ST_ListOfAgents_B);
		ArrayList<CEInfo> _listOfAgents_R = (ArrayList<CEInfo>)this.GetStateValue(_ST_ListOfAgents_R);
		
		for(CEInfo _agent_info_b : _listOfAgents_B){
			ArrayList<CEInfo> _listToSend;
			MsgLocNotice _sendingMsg;
			if(_agent_info_b._state == DmgState.Destroyed) continue;
			if(_agent_info_b._FireType == CETypeParam.FireType.Angle){
				continue;
			}
			
			_sendingMsg = new MsgLocNotice(new UUID(_agent_info_b._id));
			_listToSend = GetNearByAgents(_agent_info_b, _listOfAgents_R);
			_sendingMsg.SetNearByList(_listToSend);
			
			_listOfNoticeMsg.add(_sendingMsg);
		}
		
		for(CEInfo _agent_info_r : _listOfAgents_R){
			ArrayList<CEInfo> _listToSend;
			MsgLocNotice _sendingMsg;
			if(_agent_info_r._state == DmgState.Destroyed) continue;
			if(_agent_info_r._FireType == CETypeParam.FireType.Angle){
				continue;
			}
			
			_sendingMsg = new MsgLocNotice(new UUID(_agent_info_r._id));
			_listToSend = GetNearByAgents(_agent_info_r, _listOfAgents_B);
			_sendingMsg.SetNearByList(_listToSend);
			
			_listOfNoticeMsg.add(_sendingMsg);
		}
		
		return _listOfNoticeMsg;
		
	}
	
	public ArrayList<CEInfo> MergeLocList(){
		ArrayList<CEInfo> _listOfAgents_B = (ArrayList<CEInfo>)this.GetStateValue(_ST_ListOfAgents_B);
		ArrayList<CEInfo> _listOfAgents_R = (ArrayList<CEInfo>)this.GetStateValue(_ST_ListOfAgents_R);
		
		ArrayList<CEInfo> _listOfAgents = new ArrayList<CEInfo>();
		
		for(CEInfo _agent_info_b : _listOfAgents_B){
			_listOfAgents.add(new CEInfo(_agent_info_b));
		}
		
		for(CEInfo _agent_info_r : _listOfAgents_R){
			_listOfAgents.add(new CEInfo(_agent_info_r));
		}
		
		return _listOfAgents;
	}
	
	
	
	/*
	 * For Visualizer movement,
	 * call updateLogInfo() for every tick.
	 */
	
	public void updateLogInfo()
	{
		String type = "A";
		String log;
		
		ArrayList<CEInfo> _listOfAgents_B = (ArrayList<CEInfo>)this.GetStateValue(_ST_ListOfAgents_B);
		ArrayList<CEInfo> _listOfAgents_R = (ArrayList<CEInfo>)this.GetStateValue(_ST_ListOfAgents_R);
		
		
		for(CEInfo _agent_info_b : _listOfAgents_B){
			if(_agent_info_b._state == DmgState.Destroyed){
				continue;
			}
			XY _agent_loc = _agent_info_b._myLoc;
			
			double curLat = (Double) _agent_loc.x;
			double curLon = (Double) _agent_loc.y;
			
			String side = "Blue";
			DmgState state = _agent_info_b._state;
			log = type + ", " + _agent_info_b._id.getFullStringForVisualizer() + ", " + curLat + ", " + curLon;
			
			setLogInfo(log);
		}
		
		for(CEInfo _agent_info_r : _listOfAgents_R){
			if(_agent_info_r._state == DmgState.Destroyed){
				continue;
			}
			XY _agent_loc = _agent_info_r._myLoc;
			
			double curLat = (Double) _agent_loc.x;
			double curLon = (Double) _agent_loc.y;
			
			String side = "Red";
			DmgState state = _agent_info_r._state;
			log = type + ", " + _agent_info_r._id.getFullStringForVisualizer() + ", " + curLat + ", " + curLon;
			
			setLogInfo(log);
		}
		
	}
	
	public int getAlive( ArrayList<CEInfo> _list ){ // sjkwon
		int count = 0;
		for(CEInfo _agent_info : _list){
			if( _agent_info._state != DmgState.Destroyed) count++;
		}
		return count;
	}
}
