package CommonMap;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import ScenarioParsing.IOUtil;
import CommonInfo.XY;
import CommonPathFinder.PathFinder;
import edu.kaist.seslab.ldef.parser.scenario.ParameterGroup;

public class GridInfoNetwork {
	
	private static ArrayList<GridInfo> gridInfoList;
	
	private static PathFinder pathFinder;
	

	public GridInfoNetwork() {
	}
	
	public static void gridInfoFactory(String name) throws IOException{
		
		gridInfoList = new ArrayList<GridInfo>();
		GridInfo _dummyGrid = new GridInfo(-1, new XY(0,0), 0);
		gridInfoList.add(_dummyGrid);
		GridInfo._totalNumGrid++;
		
		///iteration 
		
		
		BufferedReader parBr= null;
		
		try {
			parBr = IOUtil.getBufferedReaderInJar(name);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		parBr.readLine();
		
		String line;
		String[] list;
		
		while((line = parBr.readLine())!= null){
			list = line.split(",");
			
			int index= Integer.parseInt(list[0]);
			double x= Double.parseDouble(list[1]);
			double y= Double.parseDouble(list[2]);
			int obstacle = Integer.parseInt(list[3]);
			
			GridInfo _newGrid = new GridInfo(index, new XY(x, y), obstacle);
			gridInfoList.add(_newGrid);
			GridInfo._totalNumGrid++;
			
		}
		
		
		
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
