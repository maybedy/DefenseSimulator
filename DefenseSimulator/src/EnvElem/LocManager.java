package EnvElem;

import java.util.ArrayList;
import java.util.UUID;

import CommonInfo.CEInfo;
import CommonInfo.XY;
import CommonInfo.UUID.UUIDSideType;
import MsgC2Report.MsgLocNotice;
import MsgC2Report.MsgLocUpdate;
import MsgC2Report.MsgReport;
import MsgC2Report.ReportType;
import MsgCommon.MsgMultiLocUpdate;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicEnvElement;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class LocManager extends BasicEnvElement {
	public static String _IE_LocUpdate = "LocUpdateIn";
	
	public static String _OE_LocUpdate = "LocUpdateOut";
	

	public static String _OE_LocNoticeOutB = "LocNoticeOut_B";
	public static String _OE_LocNoticeOutR = "LocNoticeOut_R";
	
	public static String _OE_dummy = "dummy";
	
	protected String _ST_Act = "ActState";
	protected enum ActState{
		TICK, SEND 
	};
	
	protected String _ST_Tick = "Tick";
	
	protected String _ST_ListOfAgents_B = "ListOfAgents_B";
	protected String _ST_ListOfAgents_R = "ListOfAgents_R";
	
	protected String _ST_ListOfMsgLocNotice = "ListOfMsgLocNotice";
	
	private boolean _isContinuable = true;
	
	public LocManager(ArrayList<CEInfo> _listOfAgents_B, ArrayList<CEInfo> _listOfAgents_R) //input parameters for constructor :: List of CEInfo  
	{
		/*
		 * Set Name and ID
		 */
		String Name = "Location Manager Element";
		SetModelName(Name);
		
		/*
		 * Add Input Port
		 */
		AddInputEvent(_IE_LocUpdate);
		
		/*
		 * Add Output Port
		 */
		AddOutputEvent(_OE_LocNoticeOutB);
		AddOutputEvent(_OE_LocNoticeOutR);
		AddOutputEvent(_OE_LocUpdate);
		
		/*
		 * Add State
		 */
		AddState(_ST_Act , ActState.TICK); // this is triggering state
		
		AddState(_ST_ListOfAgents_B, _listOfAgents_B);
		AddState(_ST_ListOfAgents_R, _listOfAgents_R);
		AddState(_ST_ListOfMsgLocNotice, null);
		
	}
	

	@Override
	public boolean Delta(Message msg) {
		if(msg.GetDstEvent() == _IE_LocUpdate) {
			System.out.println("input locupdate");
			MsgReport _reportMsg = (MsgReport)msg.GetValue();
			
			MsgLocUpdate _msg_locupdate = (MsgLocUpdate)_reportMsg._msgValue;
			CEInfo _agent_info = new CEInfo(_msg_locupdate.GetMyInfo()); //cloning
			
			this.UpdateAgentLocation(_agent_info);
			makeContinue();
			return true;
		}
		if(this.GetStateValue(_ST_Act) == ActState.TICK){
			
			/*
			 * state transition
			 */
			this._isContinuable = false;
			ResetContinue();
			System.out.println("Delta - tick");
			this.UpdateStateValue(_ST_Act, ActState.SEND);
				
			
			return true;
		}else if(this.GetStateValue(_ST_Act) == ActState.SEND){
			System.out.println("Delta - send");
			ArrayList<MsgReport> _listOfMsgLocNotice = 
					(ArrayList<MsgReport>)this.GetStateValue(_ST_ListOfMsgLocNotice);
			
			if(_listOfMsgLocNotice.isEmpty() || _listOfMsgLocNotice == null){
				//nothing to send
				System.out.println("No msg left");
				this._isContinuable = false;
				ResetContinue();
				System.out.println("Delta - update to tick");
				this.UpdateStateValue(_ST_Act, ActState.TICK);
			}else {
				this._isContinuable = false;
				ResetContinue();
				System.out.println("Delta - update to send");
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
			System.out.println("Output- tick");
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
			ArrayList<MsgReport> _listOfMsgLocNotice = LocNoticeMsgFactory();
			this.UpdateStateValue(_ST_ListOfMsgLocNotice, _listOfMsgLocNotice);
			
			//this.updateLogInfo();
			return true;
		}else if(this.GetStateValue(_ST_Act) == ActState.SEND){
			System.out.println("Output- send");
			ArrayList<MsgReport> _listOfMsgLocNotice = 
					(ArrayList<MsgReport>)this.GetStateValue(_ST_ListOfMsgLocNotice);
			
			if(_listOfMsgLocNotice.isEmpty() || _listOfMsgLocNotice == null){
				System.out.println("No msg left");
				msg.SetValue(_OE_dummy, null);
				
//				this._isContinuable = false;
//				ResetContinue();
//				System.out.println("output - update to tick");
//				this.UpdateStateValue(_ST_Act, ActState.TICK);				
			}else {
				MsgReport _sendingMsg = _listOfMsgLocNotice.remove(0);
				
				if(_sendingMsg._destUUID._side == UUIDSideType.Blue){
					msg.SetValue(_OE_LocNoticeOutB, _sendingMsg);
				}else {
					msg.SetValue(_OE_LocNoticeOutR, _sendingMsg);
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
		
		this._isContinuable = true;
		if(this.GetStateValue(_ST_Act) == ActState.TICK){
			System.out.println("TA called - tick");
			return 1;
		}else if(this.GetStateValue(_ST_Act) == ActState.SEND){
			System.out.println("TA called - send");
			return 0;
		}
		return Double.POSITIVE_INFINITY;
	}

	
	public boolean UpdateAgentLocation(CEInfo _agent_info){
		ArrayList<CEInfo> _listOfAgents ;
		String _list;
		if(_agent_info._id._side == UUIDSideType.Blue){
			_listOfAgents = (ArrayList<CEInfo>)this.GetStateValue(_ST_ListOfAgents_B);
			_list = _ST_ListOfAgents_B;
		}else if(_agent_info._id._side == UUIDSideType.Red){
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
			
			if(_other_agent._HP <= 0){
				continue;
			}
		
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
	
	public ArrayList<MsgReport> LocNoticeMsgFactory(){
		ArrayList<MsgReport> _listOfNoticeMsg = 
				new ArrayList<MsgReport>();
		ArrayList<CEInfo> _listOfAgents_B = (ArrayList<CEInfo>)this.GetStateValue(_ST_ListOfAgents_B);
		ArrayList<CEInfo> _listOfAgents_R = (ArrayList<CEInfo>)this.GetStateValue(_ST_ListOfAgents_R);
		
		for(CEInfo _agent_info_b : _listOfAgents_B){
			ArrayList<CEInfo> _listToSend;
			MsgReport _reportMsg;
			MsgLocNotice _sendingMsg;
			if(_agent_info_b._HP <= 0) continue;
			if(!_agent_info_b._detectable){
				continue;
			}
			
			_sendingMsg = new MsgLocNotice();
			_listToSend = GetNearByAgents(_agent_info_b, _listOfAgents_R);
			if(_listToSend.isEmpty() || _listToSend == null){
				continue;
			}
			_sendingMsg.SetNearByList(_listToSend);
			_reportMsg = new MsgReport(ReportType.EnemyInfo, null, _agent_info_b._id, _sendingMsg);

			_listOfNoticeMsg.add(_reportMsg);
		}
		
		for(CEInfo _agent_info_r : _listOfAgents_R){
			ArrayList<CEInfo> _listToSend;
			MsgLocNotice _sendingMsg;
			MsgReport _reportMsg;
			
			if(_agent_info_r._HP <= 0) continue;
			if(!_agent_info_r._detectable){
				continue;
			}
			
			_sendingMsg = new MsgLocNotice();
			_listToSend = GetNearByAgents(_agent_info_r, _listOfAgents_B);
			if(_listToSend.isEmpty() || _listToSend == null){
				continue;
			}
			_sendingMsg.SetNearByList(_listToSend);
			_reportMsg = new MsgReport(ReportType.EnemyInfo, null, _agent_info_r._id, _sendingMsg);

			_listOfNoticeMsg.add(_reportMsg);
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
	/*
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
	
	*/
	
	public int getAlive( ArrayList<CEInfo> _list ){ // sjkwon
		int count = 0;
		for(CEInfo _agent_info : _list){
			if( _agent_info._HP > 0) count++;
		}
		return count;
	}
	

	public void makeContinue(){
		if(this._isContinuable){
			Continue();
		}else {
			ResetContinue();
		}
	}
}
