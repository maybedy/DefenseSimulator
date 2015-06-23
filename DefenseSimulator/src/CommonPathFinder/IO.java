package CommonPathFinder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import ScenarioParsing.IOUtil;


public class IO {

	protected static String getCurrentTime() {
		String time = "";
		Calendar cal = Calendar.getInstance();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("[dd HH mm ss]");
		time = sdf.format(cal.getTime());

		return time;
	}

	public static void ReadReward( Variables Key, int FeatureNumber) throws IOException {
		BufferedReader br = null;
		br = IOUtil.getBufferedReaderInJar("feature_"+FeatureNumber+".txt");
		
		
		int Number_States = Key.getNumber_State();
		
		for(int StateIndex = 0; StateIndex <= Number_States ; ++StateIndex ) {
			float FeatureValue = Float.parseFloat(br.readLine());
			Key.setStateFeature(StateIndex, FeatureNumber, FeatureValue);
		}
		br.close();
		
	}

//	public static void ReadReward_Suc( Parameter Key  ) throws IOException {
//		File f = new File("C:\\Users\\Jung Chi-jung\\workspace\\PROJECT_01_15\\setting\\RewardSucSetting.txt");
//		FileReader fr = new FileReader(f);
//		BufferedReader br = new BufferedReader(fr);
//
//		for (int StateNumber = 1; StateNumber <= 10; ++StateNumber ) {
//			
//				float Reward = Float.parseFloat(br.readLine());
//				Key.setReward_Suc_State(StateNumber, Reward);
//			
//		}
//		br.close();
//	}
	
	//Usage : Print("result.txt", "ilikepizza");
	public static void Write( String Name, String A ) throws IOException {
//		Parameter P = new Parameter();
		// File f = new File("result ver. " +getCurrentTime() +".txt");
		File f = new File(Name);
		FileWriter fw = new FileWriter(f, true);
		BufferedWriter bw = new BufferedWriter(fw, 1024);
		PrintWriter pw = new PrintWriter(bw);
		pw.println(A);
		pw.close();
	}
	
	
	
	
}
