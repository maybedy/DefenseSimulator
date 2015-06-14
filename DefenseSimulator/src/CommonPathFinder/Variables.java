package CommonPathFinder;

public class Variables {
	public Variables(){}
	
	
/*Declaration part*/
	private float Gamma = 0.90f;
	private float Epsilon = 0.1f;
	
	private float[] StateReward;
	private float[][] StateFeature;
	private float[][] StateValue;
	private String[][] Pi;
	private int[] Pi_Index;
	
	private int StoppedIter;
	
	private int Number_State = 3887;
	
	
/*Initializer*/
	public void Initialization(){
		System.out.println("[log]Initialization start");
		//adjust # of iteration and states at here!
		int Iteration = this.Number_State, Number_States = this.Number_State;
		
		this.StateReward = new float[Number_States + 1];
		this.StateValue = new float[Iteration + 1][Number_States + 2 ];
		this.Pi = new String[Iteration + 1][Number_States +1 ];
		this.StateFeature = new float[Number_States +1 ][6];
		this.StoppedIter = 0;
		this.Pi_Index = new int[Number_States + 1];
		
		for(int i = 1; i <= Number_States; i++){
			Iteration = 0;
			
			this.StateReward[i] = 0;
			
			for(int j = 0; j<= 5;j++){
				
				this.StateFeature[i][j] = 0;	
				
			}
			
			this.StateValue[Iteration][i] = 0;
			this.Pi[Iteration][i] = null;
			
		}
		
		System.out.println("[log]Initialization is done");
		System.out.println();
	}
	
	
	
/*Setter*/
	public void setGamma(float Gamma){
		this.Gamma = Gamma;
	}
	public void setEpsilon(float Epsilon){
		this.Epsilon = Epsilon;
	}
	public void setStateReward(int StateIndex, float Reward){
		this.StateReward[StateIndex] = Reward; 
	}
	public void setStateValue(int Iteration, int StateIndex, float Value){
		this.StateValue[Iteration][StateIndex] = Value;
	}
	public void setPi(int Iteration, int StateIndex, String Pi){
		this.Pi[Iteration][StateIndex] = Pi;
	}
	public void setStateFeature(int StateIndex, int FeatureNumber, float FeatureValue){
		this.StateFeature[StateIndex][FeatureNumber] = FeatureValue;
	}
	public void setStoppedIter(int Iteration){
		this.StoppedIter = Iteration;
	}
	public void setPi_Index(int StateIndex, int Pi_Index){
		this.Pi_Index[StateIndex] = Pi_Index;
	}
/*Getter*/
	public float getGamma(){
		return this.Gamma;
	}
	public float getEpsilon(){
		return this.Epsilon;
	}
	public float getStateReward(int StateIndex){
		return this.StateReward[StateIndex];
	}
	public float getStateValue(int Iteration, int StateIndex){
		return this.StateValue[Iteration][StateIndex];
	}
	public String getPi(int Iteration, int StateIndex){
		return this.Pi[Iteration][StateIndex];
	}
	public float getStateFeature(int StateIndex, int FeatureNumber){
		return this.StateFeature[StateIndex][FeatureNumber];
	}
	public int getNumber_State(){
		return this.Number_State;
	}
	public int getStoppedIter(){
		return this.StoppedIter;
	}
	public int getPi_Index(int StateIndex){
		return this.Pi_Index[StateIndex];
	}
	
	
	
	
}
