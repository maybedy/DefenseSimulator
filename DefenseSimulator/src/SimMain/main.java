package SimMain;

import edu.kaist.seslab.ldef.engine.SimulationEngine;
import edu.kaist.seslab.ldef.engine.SimulationEngine.LogLevel;

public class main {

	public main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		OutmostABM _outmostABM = new OutmostABM();
	
		_outmostABM.initializeScenario();
		_outmostABM.createModels();
		
		SimulationEngine se = new SimulationEngine();
		se.setSystemLog(LogLevel.None);
		se.setEndtime(100000);
		se.LoadModel(_outmostABM);
		se.SimulationStart();
	}

}
