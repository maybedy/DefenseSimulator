package Common;

import java.util.ArrayList;

import edu.kaist.seslab.ldef.parser.scenario.ParameterGroup;

public class GridInfoNetwork {
	
	private static ArrayList<GridInfo> _gridInfoList;
	

	public GridInfoNetwork() {
	}
	
	public static void gridInfoFactory(ParameterGroup _gridParameterGroup){
		
		_gridInfoList = new ArrayList<GridInfo>();
		
		//TODO make new grid and put to _gridInfoList
				// Info is coming from _gridParameterGroup
		
	}
	
	
	public static GridInfo findMyGrid(XY _loc){
		for(GridInfo eachGrid : _gridInfoList){
			if(eachGrid.isInThisGrid(_loc)){
				return eachGrid;
			}
		}
		
		System.out.println("ERROR : no gridInfo");
		return null;
	}

}
