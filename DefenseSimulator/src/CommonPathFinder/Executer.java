//package CommonPathFinder;
//
//import java.io.IOException;
//
//public class Executer extends Function {
//	/*
//	public static void main(String[] ar) throws IOException{
//		
//		
///*making accessor*/ 
//		Variables Key = new Variables();
//		Key.Initialization();
//		
//
//		
///*parameter configuration*/
//		Key.setEpsilon(0.1f);//Epsilon
//		Key.setGamma(0.90f);//Gamma
//		
//		
///*Reward Input*/
//		System.out.println("[log]Input Reward into State space.");
//		
//		InputReward(Key, 0.7f, 0.5f);//�տ� ���ڴ� weight of exposure, �ڿ��� weight of terrain
//		
//		System.out.println("[log]Input work is done.");
//		System.out.println();
//		
//		
//		//for On-line MDP
//		
//		Key.setStateValue(0, ShotPoint_01, -1000.0f);
//		Key.setStateValue(0, ShotPoint_02, -1000.0f);
//		Key.setStateValue(0, ShotPoint_03, -1000.0f);
//		Key.setStateValue(0, ShotPoint_04, -1000.0f);
//		Key.setStateValue(0, ShotPoint_05, -1000.0f);
//		
//		
///*OffLineMDP*/
//		System.out.println("[log]Value iteration is starting.");
//		
//		OffLineMDP(Key, 1, 0, 0, 0, 0, 0);		//������� ������ �� Ȯ��, ��-������ �� Ȯ��, ��-��, ��, ��-��, ��-��.
//		
//		System.out.println("[log]Value iteration is done.");
//		System.out.println();
//
//		
///*Where to go?*/
//		System.out.println("[log]PathFinding is operating");
//		
//		System.out.println( WhereToGo(Key, 0) );//CrntIndex ���� ��ġ �Է�
//		
//		System.out.println("[log]PathFinding is done.");
//		System.out.println();
//		
///*print outcome on console*/
//		System.out.println("[log]outcome is printing");
//		
//		PrintConsole(Key);
//		
//		System.out.println("[log]outcome printing is done.");
//		System.out.println();
//		
///*OnLineMDP*/
//		
//		//in extra file
//		
//		
//		
//	}
//
//}
