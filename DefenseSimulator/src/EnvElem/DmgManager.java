package EnvElem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import CommonInfo.CEInfo;
import CommonInfo.XY;
import MsgCommon.MsgAngleDmg;
import MsgCommon.MsgAngleFire;
import MsgCommon.MsgMultiLocUpdate;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicEnvElement;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class DmgManager extends BasicEnvElement {

	public String _IE_AngFire = "AngleFireIn";
	public String _IE_LocUpdate = "LocUpdate";
	
	public String _OE_AngDmg = "AngleDmgOut";
	
	protected String _ST_Act = "ActState";
	
	
	protected String _ST_ListOfAgents = "ListOfAgents";
	//protected String _ST_ListOfDmg = "ListOfDmgAgents";
	
	protected String _ST_UnsortedMsgList = "UnsortedListOfAngleFire";
	protected String _ST_MsgList = "ListOfAngleFire";
	protected String _ST_DmgList = "ListOfAngleDmg";
	protected String _ST_SimTime = "SimulationTime";
	protected String _FLAG_IsContinuable = "IsContinuable";
	
	
	protected enum ActState{
		WAIT, SEND
	};
	
	public DmgManager() //input parameters for constructor :: List of Agents, XYs,  
	{
		/*
		 * Set Name and ID
		 */
		String Name = "Damage Manager Element";
		SetModelName(Name);
		
		/*
		 * Add Input Port
		 */
		AddInputEvent(_IE_AngFire);
		
		AddInputEvent(_IE_LocUpdate);
		
		/*
		 * Add Output Port
		 */
		AddOutputEvent(_OE_AngDmg);
				
		/*
		 * Add State
		 */
		AddState(_ST_Act , ActState.WAIT); // this is triggering state
		
		Map<MsgAngleFire, Double> _unsortedMsgList = new HashMap<MsgAngleFire, Double>();
		ValueComparator vc = new ValueComparator(_unsortedMsgList);
		TreeMap<MsgAngleFire, Double> _msgList = new TreeMap<MsgAngleFire, Double>(vc);
		
		AddState(_ST_UnsortedMsgList, _unsortedMsgList);
		
		AddState(_ST_MsgList, _msgList);
		AddState(_ST_DmgList, null); // only for sending moment
		
		AddState(_ST_SimTime, (double)0);
		

		AddState(_ST_ListOfAgents, null); // TODO modify if there is input list
		
		AddState(_FLAG_IsContinuable, true);
	}
	
	@Override
	public boolean Delta(Message msg) {
		if(msg.GetDstEvent() == _IE_AngFire) {
			double _currentTime = msg.GetTime();
			double _lastTime = (double)this.GetStateValue(_ST_SimTime);
			
			this.UpdateTimeRemain(_currentTime - _lastTime);  
			MsgAngleFire _angleFireMsg = (MsgAngleFire)msg.GetValue();
			this.AddFireMsg(_angleFireMsg);
			
			
			if((boolean)this.GetStateValue(_FLAG_IsContinuable) == true){
				Continue();	
			}else {
				ResetContinue();
			}
			
			this.UpdateStateValue(_ST_SimTime, _currentTime);
			return true;
		}
		else if(msg.GetDstEvent() == _IE_LocUpdate){
			if(this.GetStateValue(_ST_Act) == ActState.SEND){
				//originally, can't happened
			}
			
			double _currentTime = msg.GetTime();
			double _lastTime = (double)this.GetStateValue(_ST_SimTime);
			
			this.UpdateTimeRemain(_currentTime - _lastTime);
			
			MsgMultiLocUpdate _msgMultiLoc = (MsgMultiLocUpdate)msg.GetValue();
			ArrayList<CEInfo> _listOfAgents = _msgMultiLoc.GetList();
			
			this.UpdateStateValue(_ST_ListOfAgents, _listOfAgents);
			
			double _minimumRemainTa = this.GetTa();
			
			while(_minimumRemainTa <= 0){
				/*
				 * 2cases : remained flight time == 0 or not
				 */
				
				/*
				 * for this case, make dmg msgs
				 */
				
				if(_minimumRemainTa < 0){
					//originally, flight time is Discrete value
				//	System.out.println("Strange case : minimum remaining time is negative value");
				}
				
				MsgAngleFire _angleFireMsg = this.RemoveFireMsg();
				ArrayList<MsgAngleDmg> _listOfDmg = MakeDmgList(_angleFireMsg, _listOfAgents);
				
				if(_listOfDmg.isEmpty()){
					//If list of dmg is empty, search next fire msg
					_minimumRemainTa = this.GetTa();
					continue;
				}else {
					//if list of dmg is not empty, go to send and send dmg msgs
					this.UpdateDmgList(_listOfDmg);
					
					ResetContinue();
					this.UpdateStateValue(_FLAG_IsContinuable, false);
					this.UpdateStateValue(_ST_Act, ActState.SEND);
					
					return true;
				}				
			}
			/*
			 * For this case, update to wait
			 * In delta, nothing to do
			 */
			this.UpdateDmgList(null);
			
			ResetContinue();
			this.UpdateStateValue(_FLAG_IsContinuable, false);
			this.UpdateStateValue(_ST_Act, ActState.WAIT);
			return true;
			
		}
		
		else if(this.GetStateValue(_ST_Act) == ActState.SEND){
			ArrayList<MsgAngleDmg> _listOfDmg  = this.GetDmgMsgList();

			ResetContinue();
			this.UpdateStateValue(_FLAG_IsContinuable, false);
			
			
			if(_listOfDmg.isEmpty()){
				ArrayList<CEInfo> _listOfAgents = (ArrayList<CEInfo>) this.GetStateValue(_ST_ListOfAgents);
				while(true){
					/*
					 * 2cases : remained flight time == 0 or not
					 */
					double _minimumRemainTa = this.GetTa();
					if(_minimumRemainTa <= 0) {
						/*
						 * for this case, make dmg msgs
						 */
						
						if(_minimumRemainTa < 0){
							//originally, flight time is Discrete value
						//	System.out.println("Strange case : minimum remaining time is negative value");
						}
						
						MsgAngleFire _angleFireMsg = this.RemoveFireMsg();
						_listOfDmg = MakeDmgList(_angleFireMsg, _listOfAgents);
						
						if(_listOfDmg.isEmpty()){
							//If list of dmg is empty, search next fire msg
							continue;
						}else {
							//if list of dmg is not empty, go to send and send dmg msgs
							this.UpdateDmgList(_listOfDmg);
							
							ResetContinue();
							this.UpdateStateValue(_FLAG_IsContinuable, false);
							this.UpdateStateValue(_ST_Act, ActState.SEND);
							
							return true;
						}				
					}else {
						/*
						 * For this case, update to wait
						 * In delta, nothing to do
						 */
						
						this.UpdateDmgList(null);
						ResetContinue();
						this.UpdateStateValue(_FLAG_IsContinuable, false);
						this.UpdateStateValue(_ST_Act, ActState.WAIT);
						return true;
					}
				}
			}else {
				
				//dmg msg remained case 
				
				this.UpdateStateValue(_ST_Act, ActState.SEND);
			}
			
			return true;
		}
//		else if(msg.GetDstEvent() == ){
//			// almost same as upper 'if' block -> merge it??
//			return true;
//		}
//		
		return false;
	}

	@Override
	public boolean Output(Message msg) {
		// TODO call message factory for all agents who will get the updated location
		// msg need dst, src model to notify its destination(for dynamic structure)
		
		if(this.GetStateValue(_ST_Act) == ActState.SEND){
			ArrayList<MsgAngleDmg> _listOfDmg = this.GetDmgMsgList();
			if(_listOfDmg.isEmpty()){
				//System.out.println("list of dmg empty in output function can't be happened");
			}
			
			//System.out.println("Remained msg : " + _listOfDmg.size());
			if( _listOfDmg.size() == 0 ) return false;
			
			MsgAngleDmg _msgToSend = this.RemoveDmgMsg();
			msg.SetValue(_OE_AngDmg, _msgToSend);
			
			if(_listOfDmg.isEmpty()){
			//	System.out.println("All sended and in delta function -it will search new fire msg(to send or wait) or go to wait");
			}
			return true;
		}else if(this.GetStateValue(_ST_Act) == ActState.WAIT) {
			return true;
		}
		return false;
	}

	@Override
	public double TimeAdvance() {
		this.UpdateStateValue(_FLAG_IsContinuable, true);
		if(this.GetStateValue(_ST_Act) == ActState.SEND){
			return 0;
		}else if(this.GetStateValue(_ST_Act) == ActState.WAIT){
			return Double.POSITIVE_INFINITY;
		}
		return 0;
	}
	
	/*
	 * belowings are related to MsgAngleDmg
	 */
	
	public ArrayList<MsgAngleDmg> MakeDmgList(MsgAngleFire _msgAngleFire, ArrayList<CEInfo> _listOfAgents) 
	{
		ArrayList<MsgAngleDmg> _listOfDmg = new ArrayList<MsgAngleDmg>();
		//TODO find the list of agent in fire range
		
		double _fireRange = _msgAngleFire._angleFireParam._casualty_radius;
		XY _impactLoc = _msgAngleFire._impactLoc;

		for(CEInfo _eachAgent : _listOfAgents){
			
			if( _eachAgent._HP <= 0) continue;
			
			double _dist = _eachAgent._myLoc.distance(_impactLoc);
			
			if(_dist <= _fireRange) {
				MsgAngleDmg _newDmg = new MsgAngleDmg(_eachAgent._id);
				_listOfDmg.add(_newDmg);
			}
		}
		
		return _listOfDmg;
	}
	
	public void UpdateDmgList(ArrayList<MsgAngleDmg> _listOfDmg){
		this.UpdateStateValue(_ST_DmgList, _listOfDmg);
	}
	
	public ArrayList<MsgAngleDmg> GetDmgMsgList(){
		ArrayList<MsgAngleDmg> _listOfDmg = (ArrayList<MsgAngleDmg>)this.GetStateValue(_ST_DmgList);
		
		return _listOfDmg;
	}
	
	public MsgAngleDmg RemoveDmgMsg(){
		ArrayList<MsgAngleDmg> _listOfDmg = this.GetDmgMsgList();
		MsgAngleDmg _dmgMsg = _listOfDmg.remove(0);
		
		this.UpdateDmgList(_listOfDmg);
		
		return _dmgMsg;
	}
	
	
	
	
	/*
	 * belowing functions are related to MsgAngleFire
	 */
	
	public TreeMap<MsgAngleFire, Double> GetMsgList()	{
		TreeMap<MsgAngleFire, Double> _msgList = (TreeMap<MsgAngleFire, Double>)this.GetStateValue(_ST_MsgList);
		
		return _msgList;
	}
	
	public Map<MsgAngleFire, Double> GetUnsortedMsgList()	{
		Map<MsgAngleFire, Double> _msgList = (Map<MsgAngleFire, Double>)this.GetStateValue(_ST_UnsortedMsgList);
		
		return _msgList;
	}
	
	public void UpdateUnsortedMsgList(Map<MsgAngleFire, Double> _msgList) {
		this.UpdateStateValue(_ST_UnsortedMsgList, _msgList);
	}
	
	public void UpdateMsgList(TreeMap<MsgAngleFire, Double> _msgList){
		this.UpdateStateValue(_ST_MsgList, _msgList);
	}
	

	public void UpdateTimeRemain(double _timePassed)
	{
		Map<MsgAngleFire, Double> _unsortedMsgList = this.GetUnsortedMsgList();
		
		Set<MsgAngleFire> _msgSet = _unsortedMsgList.keySet();
		
		for(MsgAngleFire _eachMsg : _msgSet){
			double _originalTimeRemain = _unsortedMsgList.get(_eachMsg);
			
			_unsortedMsgList.put(_eachMsg, (_originalTimeRemain - _timePassed));
		}
		
		TreeMap<MsgAngleFire, Double> _msgList = new TreeMap<MsgAngleFire, Double>(new ValueComparator(_unsortedMsgList));
		_msgList.putAll(_unsortedMsgList);
		
		this.UpdateUnsortedMsgList(_unsortedMsgList);
		this.UpdateMsgList(_msgList);
		// TODO 비효율적인듯
	}
	
	public void AddFireMsg(MsgAngleFire _angleFireMsg)
	{
		Map<MsgAngleFire, Double> _unsortedMsgList = this.GetUnsortedMsgList();
		TreeMap<MsgAngleFire, Double> _msgList = this.GetMsgList();
		
		MsgAngleFire _msg = new MsgAngleFire(_angleFireMsg);
		Double _value = _angleFireMsg._angleFireParam._time_for_flight;
		
		_unsortedMsgList.put(_msg, _value);
		_msgList.put(_msg, _value);
		
		this.UpdateUnsortedMsgList(_unsortedMsgList);
		this.UpdateMsgList(_msgList);
	}
	
	public MsgAngleFire RemoveFireMsg()
	{
		
		TreeMap<MsgAngleFire, Double> _msgList = this.GetMsgList();
		Map<MsgAngleFire, Double> _unsortedMsgList = this.GetUnsortedMsgList();
		
		MsgAngleFire _firstMsg = _msgList.firstKey();
		
		
		_msgList.remove(_firstMsg);
		_unsortedMsgList.remove(_firstMsg);
		
		this.UpdateMsgList(_msgList);
		this.UpdateUnsortedMsgList(_unsortedMsgList);
		return _firstMsg;
	}
	
	public double GetTa()
	{
		TreeMap<MsgAngleFire, Double> _msgList = this.GetMsgList();
		
		if(_msgList.isEmpty()){
			return Double.POSITIVE_INFINITY;
		}
		MsgAngleFire _firstMsg = _msgList.firstKey();
		return _msgList.get(_firstMsg);			
 
	}

}


class ValueComparator implements Comparator<MsgAngleFire> {
	
	Map<MsgAngleFire, Double> map;
	
	public ValueComparator(Map<MsgAngleFire, Double> base) {
		this.map = base;
	}


	@Override
	public int compare(MsgAngleFire o1, MsgAngleFire o2) {
		// TODO Auto-generated method stub
		Double v1 = map.get(o1);
		Double v2 = map.get(o2);
		
		return v2.compareTo(v1);
	}
	
}