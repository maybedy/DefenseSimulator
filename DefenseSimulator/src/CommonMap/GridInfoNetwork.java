package CommonMap;

import java.io.IOException;
import java.util.ArrayList;

import CommonInfo.XY;
import CommonPathFinder.PathFinder;
import edu.kaist.seslab.ldef.parser.scenario.ParameterGroup;

public class GridInfoNetwork {
	
	private static ArrayList<GridInfo> gridInfoList;
	
	private static PathFinder pathFinder;
	

	public GridInfoNetwork() {
	}
	
	public static void gridInfoFactory(ParameterGroup _gridParameterGroup) throws IOException{
		
		gridInfoList = new ArrayList<GridInfo>();
		
		//TODO make new grid and put to _gridInfoList
				// Info is coming from _gridParameterGroup
		
		

		////////////////////////////////////////////////////////
		pathFinder = new PathFinder();
		
		PathFinder.calculatePath();
		
	}
	
	public static void addAttack(int index){
		PathFinder.addAttackHistory(index);
	}
	
	public static GridInfo getNextPlace(int index){
		GridInfo nextGrid;
		
		nextGrid = findMyGrid(PathFinder.getPath(index));
		
		return nextGrid;
	}
	
	public static GridInfo getNextPlace(GridInfo grid){
		return getNextPlace(grid._gridIndex);
	}
	
	
	public static GridInfo findMyGrid(XY _loc){
		for(GridInfo eachGrid : gridInfoList){
			if(eachGrid.isInThisGrid(_loc)){
				return eachGrid;
			}
		}
		
		System.out.println("ERROR : no gridInfo");
		return null;
	}
	
	public static GridInfo findMyGrid(int index){
		for(GridInfo eachGrid : gridInfoList){
			if(eachGrid._gridIndex == index)
				return eachGrid;
		}
		
		return null;
	}

}
