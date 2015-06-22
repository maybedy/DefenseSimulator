package CommonPathFinder;

import java.io.IOException;
import java.util.ArrayList;

import CommonInfo.Path;
import CommonMap.GridInfo;

public class PathFinder {
	
	private static Variables key; 

	public PathFinder() throws IOException {
		key = new Variables();
		key.Initialization();
		
		key.setEpsilon(0.1f);
		key.setGamma(0.90f);
		
		Function.InputReward(key, 0.7f, 0.5f);
		
	}
	
	public static void calculatePath() throws IOException{
		Function.OffLineMDP(key, 1, 0, 0, 0, 0, 0);
	
	}
	
	public static Path getTotalPath(GridInfo _grid){
		
		// TODO calculate path 
		
		ArrayList<GridInfo> _pathList= null;
		
		
		
		Path _newPath = new Path(_pathList);
		
		
		return _newPath;
	}
	
	public static int getPath(int index){
		return Function.WhereToGo(key, index);
	}
	
	public static void addAttackHistory(int attackPlace)
	{
		key.setStateValue(0, attackPlace, -1000.0f);
	}
	
}
