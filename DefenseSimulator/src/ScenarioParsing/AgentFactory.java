package ScenarioParsing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicAgentModel;
import edu.kaist.seslab.ldef.parser.scenario.ParameterGroup;
import edu.kaist.seslab.ldef.parser.scenario.Scenario;
import CommonInfo.CEInfo;
import CommonInfo.UUID;
import CommonInfo.XY;
import CommonMap.GridInfo;
import CommonMap.GridInfoNetwork;
import CommonType.WTType;
import CommonType.WTTypeAngleParam;
import CommonType.WTTypeDirectParam;
import ModelAgent_BlueC2.BlueBattalionC2;
import ModelAgent_BlueC2.BlueCompany;
import ModelAgent_BlueC2.Sensor;
import ModelAgent_BlueC2.Shooter;
import ModelAgent_RedC2.RedBattalionC2;
import ModelAgent_RedC2.RedCompany;
import ModelMultiAgent_Blue.BlueBattalion;
import ModelMultiAgent_Blue.BlueMA;
import ModelMultiAgent_Red.RedBattalion;
import ModelMultiAgent_Red.RedMA;

public class AgentFactory {
	
	public ArrayList<CEInfo> _listOfAgent_B;
	public ArrayList<CEInfo> _listOfAgent_R;
	public RedMA _redMAM;
	public BlueMA _blueMAM;

	public AgentFactory(Scenario sce) {
		ParameterGroup rootGroup = sce.getParams();
		ParameterGroup generalInfo = rootGroup.getParameterGroup("General Information");
		ParameterGroup simParam = generalInfo.getParameterGroup("Simulation Params");
		String _AgentLocFileName = (String)simParam.getParameterValueOfFirstHit("AgentLocFile");
		
		ParameterGroup CEInfoGroup = rootGroup.getParameterGroup("CEInfo");
		ParameterGroup WTTypeParamGroup = rootGroup.getParameterGroup("WTTypeParam");
		
		BufferedReader parBr = null;
		
		////////////GridInfo///////////////
		
		String _gridFileName = (String)simParam.getParameterValueOfFirstHit("GridInfoFile");
		try {
			GridInfoNetwork.gridInfoFactory(_gridFileName);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/////////////WTTypeRead/////////////
		WTTypeDirectParam _blueDirect;
		WTTypeDirectParam _redDirect;
		WTTypeAngleParam _blueAngle;
		
		ParameterGroup BlueAngleGroup = WTTypeParamGroup.getParameterGroupOfFirstHit("BlueAngle");
		double power = Double.parseDouble((String)BlueAngleGroup.getParameterValueOfFirstHit("power"));
		double accuracy = Double.parseDouble((String)BlueAngleGroup.getParameterValueOfFirstHit("accuracyRate"));
		double casualty_rad = Double.parseDouble((String)BlueAngleGroup.getParameterValueOfFirstHit("casualty_radius"));
		double time_for_flight = Double.parseDouble((String)BlueAngleGroup.getParameterValueOfFirstHit("time_for_flight"));
		_blueAngle = new WTTypeAngleParam(WTType.B_AngleFire, power, accuracy, casualty_rad, time_for_flight);
		
		ParameterGroup BlueDirectGroup = WTTypeParamGroup.getParameterGroupOfFirstHit("BlueDirect");
		power = Double.parseDouble((String)BlueDirectGroup.getParameterValueOfFirstHit("power"));
		accuracy = Double.parseDouble((String)BlueDirectGroup.getParameterValueOfFirstHit("accuracyRate"));
		_blueDirect = new WTTypeDirectParam(WTType.B_DirectFire, power, accuracy);
		
		ParameterGroup RedDirectGroup = WTTypeParamGroup.getParameterGroupOfFirstHit("RedDirect");
		power = Double.parseDouble((String)RedDirectGroup.getParameterValueOfFirstHit("power"));
		accuracy = Double.parseDouble((String)RedDirectGroup.getParameterValueOfFirstHit("accuracyRate"));
		_redDirect = new WTTypeDirectParam(WTType.R_DirectFire, power, accuracy);
		
		///////////CEInfoRead///////////////
		try {
			parBr = IOUtil.getBufferedReaderInJar(_AgentLocFileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String line;
		String[] list;
		String[] list2 = new String[5];
		String name;
		ArrayList<BlueCompany> _blueCompanyList = new ArrayList<BlueCompany>();
		ArrayList<Sensor> _blueSensorList = new ArrayList<Sensor>();
		ArrayList<Shooter> _blueShooterList = new ArrayList<Shooter>();
		ArrayList<RedCompany> _redCompanyList = new ArrayList<RedCompany>();
		ArrayList<RedBattalion> _redBattalionlist= new ArrayList<RedBattalion>();
		
		try{
			parBr.readLine();
			while((line = parBr.readLine())!= null){
				list = line.split(",");
				name= list[0];
				for(int i = 1;i< 5;i++){
					list2[i-1] = list[i];
				}
				
				UUID _newID = new UUID(name, list2);
				
				double x, y;
				int index; 
				
				x = Double.parseDouble(list[5]);
				y = Double.parseDouble(list[6]);
					
				index = Integer.parseInt(list[7]);
				
				//TODO find grid
				
				XY _newLoc = new XY(x, y);
				GridInfo _newGrid = GridInfoNetwork.findMyGrid(index);
				
				CEInfo _newAgentInfo = new CEInfo(_newID,_newLoc,_newGrid);
				
				ParameterGroup CEInfoParam = CEInfoGroup.getParameterGroup(name);
				
				double _HP = Double.parseDouble((String)CEInfoParam.getParameterValueOfFirstHit("HP"));
				_newAgentInfo._HP = _HP;
				
				String Movable = (String)CEInfoParam.getParameterValueOfFirstHit("Movable");
				if(Movable.equalsIgnoreCase("TRUE")){
					_newAgentInfo._movable = true;
				}else {
					_newAgentInfo._movable = false;
				}
				
				double maxSpeed = Double.parseDouble((String)CEInfoParam.getParameterValueOfFirstHit("maxSpeed"));
				_newAgentInfo._maxSpeed = maxSpeed;
				
				String detectable = (String)CEInfoParam.getParameterValueOfFirstHit("detectable");
				if(detectable.equalsIgnoreCase("TRUE")){
					_newAgentInfo._detectable = true;
				}else {
					_newAgentInfo._detectable = false;
				}
				
				double detectRange = Double.parseDouble((String)CEInfoParam.getParameterValueOfFirstHit("detectRange"));
				_newAgentInfo._detectRange = detectRange;
				
				String engageable = (String)CEInfoParam.getParameterValueOfFirstHit("engageable");
				if(engageable.equalsIgnoreCase("TRUE")){
					_newAgentInfo._engageable = true;
				}else {
					_newAgentInfo._engageable = false;
				}
				
				String weaponType = (String)CEInfoParam.getParameterValueOfFirstHit("weaponType");
				if(weaponType.equalsIgnoreCase("BlueAngle")){
					_newAgentInfo._weaponType = WTType.B_AngleFire;
					_newAgentInfo._weaponParam = _blueAngle;
				}else if(weaponType.equalsIgnoreCase("RedAngle")){
					
				}else if(weaponType.equalsIgnoreCase("BlueDirect")) {
					_newAgentInfo._weaponType = WTType.B_DirectFire;
					_newAgentInfo._weaponParam = _blueDirect;
				}else if(weaponType.equalsIgnoreCase("RedDirect")){
					_newAgentInfo._weaponType = WTType.R_DirectFire;
					_newAgentInfo._weaponParam = _redDirect;
				}else if(weaponType.equalsIgnoreCase("NONE")){
					
				}else {
					//error
				}
				
				
				
				BasicAgentModel _newAgent = null;
				
				if(name.equalsIgnoreCase("BlueBattalionC2")){
					_newAgent = new BlueBattalionC2(_newAgentInfo,_blueCompanyList, _blueShooterList);
					BlueBattalion _battalion = new BlueBattalion(, (BlueBattalionC2)_newAgent, _blueCompanyList, _blueSensorList, _blueShooterList);
					
					_blueMAM = new BlueMA(_myInfo, _battalion);
				}else if(name.equalsIgnoreCase("BlueCompanyC2")){
					_newAgent = new BlueCompany(_newAgentInfo);
					_blueCompanyList.add((BlueCompany)_newAgent);
				}else if(name.equalsIgnoreCase("BlueShooter")){
					_newAgent = new Shooter(_newAgentInfo); 
					_blueShooterList.add((Shooter)_newAgent);
				}else if(name.equalsIgnoreCase("BlueSensor")){
					_newAgent = new Sensor(_newAgentInfo);
					_blueSensorList.add((Sensor)_newAgent);
				}else if(name.equalsIgnoreCase("RedBattalionC2")){
					
					_newAgent = new RedBattalionC2(_newAgentInfo, _redCompanyList);
					RedBattalion _battalion = new RedBattalion(, (RedBattalionC2)_newAgent, _redCompanyList);
					_redBattalionlist.add(_battalion);
					
					_redCompanyList = new ArrayList<RedCompany>();
				}else if(name.equalsIgnoreCase("RedCompanyC2")){
					_newAgent = new RedCompany(_newAgentInfo);
					_redCompanyList.add((RedCompany)_newAgent);
					
				}else {
					
				}
				
				
			}
		} catch (Exception e){
			
		}
		
		_redMAM = new RedMA(_myInfo, _redBattalionlist);
	}
	
	
	public void generatingAgent(){
		
	}
	
	

}
