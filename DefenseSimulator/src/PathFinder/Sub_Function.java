package PathFinder;

public class Sub_Function extends IO{
	
	public Sub_Function(){}
	
	public static float Value_Coupling(Variables Key, int Iteration, int StateIndex,
			float Forward, String D01, float DiagonalRightF, String D02, float DiagonalRightB, String D03,
			float Backward, String D04, float DiagonalLeftB, String D05, float DiagonalLeftF, String D06){
		
		float Value_Action = Forward * Key.getStateValue(Iteration, FindIndex(StateIndex, D01)) + 
				DiagonalRightF * Key.getStateValue(Iteration, FindIndex(StateIndex, D02)) +
				DiagonalRightB * Key.getStateValue(Iteration, FindIndex(StateIndex, D03)) +
				Backward * Key.getStateValue(Iteration, FindIndex(StateIndex, D04)) +
				DiagonalLeftB * Key.getStateValue(Iteration, FindIndex(StateIndex, D05)) +
				DiagonalLeftF * Key.getStateValue(Iteration, FindIndex(StateIndex, D06)) ;	
		
		return Value_Action;
		
	}
		
	public static int FindIndex(int CrntIndex, String Direction){
		int ObjectIndex = 0, ObjectIndex_Odd = 0, ObjectIndex_Even = 0;
		int EmptyIndex = 3888;
		/*예외처리*/
		if(Direction == "N" || Direction == "North"){
			if(CrntIndex >= 3840){
				ObjectIndex_Odd = EmptyIndex ;
				ObjectIndex_Even = EmptyIndex ;
			}else{
				ObjectIndex_Odd = CrntIndex + 48 ;
				ObjectIndex_Even = CrntIndex + 48 ;
			}
			
		}else if(Direction == "EN"|| Direction == "EastNorth"){
			if(CrntIndex % 48 == 23 || CrntIndex >= 3864){
				ObjectIndex_Odd = EmptyIndex ;
				ObjectIndex_Even = EmptyIndex ;
			}else{
				ObjectIndex_Odd = CrntIndex + 25 ;
				ObjectIndex_Even = CrntIndex + 24 ;
			}
			
		}else if(Direction == "ES"|| Direction == "EastSouth"){
			if(CrntIndex % 48 == 23 || CrntIndex <= 23){
				ObjectIndex_Odd = EmptyIndex ;
				ObjectIndex_Even = EmptyIndex ;
			}else{
				ObjectIndex_Odd = CrntIndex + -23 ;
				ObjectIndex_Even = CrntIndex + -24 ;
			}
			
		}else if(Direction == "S"|| Direction == "South"){
			if(CrntIndex <= 47){
				ObjectIndex_Odd = EmptyIndex ;
				ObjectIndex_Even = EmptyIndex ;
			}else{
				ObjectIndex_Odd = CrntIndex - 48 ;
				ObjectIndex_Even = CrntIndex - 48 ;
			}
			
			
		}else if(Direction == "WS"|| Direction == "WestSouth"){
			if(CrntIndex % 48 == 24 || CrntIndex <= 23){
				ObjectIndex_Odd = EmptyIndex ;
				ObjectIndex_Even = EmptyIndex ;
			}else{
				ObjectIndex_Odd = CrntIndex + - 24 ;
				ObjectIndex_Even = CrntIndex + - 25 ;
			}
			
		}else if(Direction == "WN"|| Direction == "WestNorth"){
			if(CrntIndex % 48 == 24 || CrntIndex >= 3864){
				ObjectIndex_Odd = EmptyIndex ;
				ObjectIndex_Even = EmptyIndex ;
			}else{
				ObjectIndex_Odd = CrntIndex + 24 ;
				ObjectIndex_Even = CrntIndex + 23 ;
			}
			
			
		}else{
			System.err.println("[ERROR] Required direction is wrong! ");
		}
		
		
		if(CrntIndex % 48 <= 23){//홀수열이란뜻
			ObjectIndex = ObjectIndex_Odd;
		}else{
			ObjectIndex = ObjectIndex_Even;
		}
		
		return ObjectIndex;
	}
	
	
	public static String EqualCheck(float Max, float A1, float A2, float A3, float A4, float A5, float A6){
		
		if(Max == A1){
			return "North";
		}else if(Max == A2){
			return "EastNorth";
		}else if(Max == A3){
			return "EastSouth";
		}else if(Max == A4){
			return "South";
		}else if(Max == A5){
			return "WestSouth";
		}else if(Max == A6){
			return "WestNorth";
		}else{
			System.err.println("[ERROR]EqualCheck is wrong");
			return "ERROR";
		}
		
	}
	
	
	public static int Power( int a, int b ){
		int c = a;
		if(b == 0){
			a = 1;
		}else{
			for(int i = 2 ; i <= b; i++){
				a = c * a ;
			}
		}
		return a;
	}
	
	public static float Abs( float FirstNumber, float SecondNumber ){//Absolute Value
		if(FirstNumber - SecondNumber < 0) return (-1)*(FirstNumber - SecondNumber );
		else return FirstNumber - SecondNumber;
	}
	
	public static float Max( float FirstNumber, float SecondNumber ){
		if(FirstNumber >= SecondNumber){
			return FirstNumber;
		}else{
			return SecondNumber;
		}
	}
	
	
}
