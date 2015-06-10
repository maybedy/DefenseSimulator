package PathFinder;

import java.io.IOException;

public class Function extends Sub_Function{
	
	public Function(){}
	
	
/*Input Reward into state*/
	
	
	public static void InputReward(Variables Key, float w_expo, float w_terr) throws IOException{
		ReadReward(Key, 0);//S_mission
			Key.setStateFeature(437, 0, 1000);//extra addition / object point of ENY
			Key.setStateFeature(396, 0, 1000);
			Key.setStateFeature(329, 0, 1000);
		ReadReward(Key, 1);//S_feature_01
		ReadReward(Key, 2);
		ReadReward(Key, 3);
		ReadReward(Key, 4);
		ReadReward(Key, 5);
		
		int Number_States = Key.getNumber_State();
		
		float w_f_01 = 1, w_f_02 = 1, w_f_03 = 1, w_f_04 = 1, w_f_05 = 1;
		
		float /*w_expo = 0.7f,*/ w_stam = 1 - w_expo;//나중에 실시간 조절 가능토록
		float /*w_terr = 0.5f,*/ w_miss = 1 - w_terr;//나중에 실시간 조절 가능토록
		
		Key.setStateValue( 0, 3888, 0 );//empty-value space
		
		for (int StateIndex = 0; StateIndex <= Number_States ; ++StateIndex ) {
			
			int a, b, c, d, e = 0;
			
			if(Key.getStateFeature(StateIndex, 1)==1){ a = -1; }else{ a =  1; }
			if(Key.getStateFeature(StateIndex, 2)==1){ b =  1; }else{ b = -1; }
			if(Key.getStateFeature(StateIndex, 3)==1){ c =  1; }else{ c = -1; }
			if(Key.getStateFeature(StateIndex, 4)==1){ d = -1; }else{ d =  1; }
			if(Key.getStateFeature(StateIndex, 5)==1){ e =  1; }else{ e = -1; }
			
			
			float S_exposure = + w_f_01 * a 
							   + w_f_02 * b 
							   + w_f_03 * c * 0 
							   + w_f_04 * d 
							   + w_f_05 * e ;
			float S_stamina =  - w_f_01 * a 
							   - w_f_02 * b 
							   - w_f_03 * c 
							   - w_f_04 * d 
							   - w_f_05 * e ;
			float S_terrain = w_expo * S_exposure + w_stam * S_stamina ;
			
			float S_mission = Key.getStateFeature(StateIndex, 0);
			
			float Reward = 100 * ( w_terr * S_terrain + w_miss * S_mission ) ;
			
			Key.setStateReward( StateIndex, Reward );
			Key.setStateValue(0, StateIndex, Key.getStateReward(StateIndex));
			
//			System.out.println(Reward);
			
		}
		
	}
	
	
	public static void OffLineMDP(Variables Key, float Prob_F, float Prob_D_R_F, float Prob_D_R_B, 
			float Prob_B, float Prob_D_L_B, float Prob_D_L_F) throws IOException{
		
		int Number_State = Key.getNumber_State();
		
		for(int Iteration = 1; Iteration <= Number_State; Iteration++){
			for(int StateIndex = 0; StateIndex <= Number_State; StateIndex++){
				
//Action		
				float[] Action = new float[6];
				int North = 0, EastNorth = 1, EastSouth = 2, South = 3, WestSouth = 4, WestNorth = 5;
				
//Probability of Direction which Agent want to go
				
				float Forward = Prob_F, DiagonalRightF = Prob_D_R_F, DiagonalRightB = Prob_D_R_B, 
						Backward =Prob_B, DiagonalLeftB = Prob_D_L_B, DiagonalLeftF = Prob_D_L_F;  
				
				int BeforeIter = Iteration - 1;
				int CrntIndex = StateIndex;
				
				//log
//				System.out.println("iter = "+Iteration +" StateNumber = " +StateIndex);
//				System.out.println("value = "+Key.getStateValue(Iteration-1, 1));
				
				
				Action[North] = Value_Coupling(Key, BeforeIter, CrntIndex, 
						Forward, "N", DiagonalRightF, "EN", DiagonalRightB, "ES", Backward, "S", DiagonalLeftB, "WS", DiagonalLeftF, "WN");

				Action[EastNorth] = Value_Coupling(Key, BeforeIter, CrntIndex, 
						Forward, "EN", DiagonalRightF, "ES", DiagonalRightB, "S", Backward, "WS", DiagonalLeftB, "WN", DiagonalLeftF, "N");
				
				Action[EastSouth] = Value_Coupling(Key, BeforeIter, CrntIndex, 
						Forward, "ES", DiagonalRightF, "S", DiagonalRightB, "WS", Backward, "WN", DiagonalLeftB, "N", DiagonalLeftF, "EN");
				
				Action[South] = Value_Coupling(Key, BeforeIter, CrntIndex, 
						Forward, "S", DiagonalRightF, "WS", DiagonalRightB, "WN", Backward, "N", DiagonalLeftB, "EN", DiagonalLeftF, "ES");
				
				Action[WestSouth] = Value_Coupling(Key, BeforeIter, CrntIndex, 
						Forward, "WS", DiagonalRightF, "WN", DiagonalRightB, "N", Backward, "EN", DiagonalLeftB, "ES", DiagonalLeftF, "S");
				
				Action[WestNorth] = Value_Coupling(Key, BeforeIter, CrntIndex, 
						Forward, "WN", DiagonalRightF, "N", DiagonalRightB, "EN", Backward, "ES", DiagonalLeftB, "S", DiagonalLeftF, "WS");
				
				float Max = Max(Action[North], Max(Action[EastNorth], Max(Action[EastSouth], Max(Action[South], Max(Action[WestSouth], Action[WestNorth])))));
				Key.setStateValue(Iteration, CrntIndex, Key.getStateReward(CrntIndex) + Key.getGamma() * Max);
								
				String Pi = EqualCheck(Max, Action[North], Action[EastNorth], Action[EastSouth], Action[South], Action[WestSouth], Action[WestNorth]);				
				Key.setPi(Iteration, CrntIndex, Pi);
				Key.setPi_Index(CrntIndex, FindIndex(CrntIndex, Key.getPi(Iteration, CrntIndex)));
			}
			

//break condition			
			if(Abs(Key.getStateValue(Iteration, Number_State), Key.getStateValue(Iteration - 1, Number_State)) != 0 && 
					Abs(Key.getStateValue(Iteration, Number_State), Key.getStateValue(Iteration - 1, Number_State)) <= Key.getEpsilon()){
				
				System.out.println("[log] in this Iteration["+Iteration+"], loop is stopped.");
				Key.setStoppedIter(Iteration);//save stoppedIteration into this space
				System.out.println("[log] at this time, Value of state 10   : "+Key.getStateValue(Iteration, 10));
				System.out.println("[log] at this time, Value of state 3887 : "+Key.getStateValue(Iteration, 3887));
//Output
				for(int StateIndex = 1; StateIndex <= Number_State; StateIndex++){
					Write("result.txt", "policy of state["+StateIndex+"] : "+Key.getPi(Iteration, StateIndex));
				}
				
				break;
			}
		}
		
	}
	
	public static void PrintConsole(Variables Key){//콘솔 출력부분
		for(int i = 161; i >= 0; --i){
			if(i % 2 == 0){
				System.out.print("  ");
			}
			for(int StateIndex =  i * 24;StateIndex <= i * 24 + 23; ++StateIndex ){
				
				switch(Key.getPi(Key.getStoppedIter(), StateIndex)){
				case "North" : System.out.print("[↑]");  break;
				case "EastNorth" : System.out.print("[↗]");break;
				case "EastSouth" : System.out.print("[↘]");break;
				case "South" : System.out.print("[↓]");break;
				case "WestSouth" : System.out.print("[↙]");break;
				case "WestNorth" :System.out.print("[↘]");break;
				}
				if(StateIndex % 24 == 23){
					System.out.println();
				}
			}
		}
	}
	
	public static int WhereToGo(Variables Key, int CrntIndex){
		
		return Key.getPi_Index(CrntIndex);
		
	}
	
	
}
