package EnvElem;

import edu.kaist.seslab.ldef.engine.modelinterface.internal.BasicEnvElement;
import edu.kaist.seslab.ldef.engine.modelinterface.internal.Message;

public class DummyModel extends BasicEnvElement {
	public static String _IE_input = "IN";
	public DummyModel() {
		this.SetModelName("dummy");
		
		this.AddInputEvent(_IE_input);
	
	}

	@Override
	public boolean Delta(Message msg) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean Output(Message msg) {
		return true;
	}

	@Override
	public double TimeAdvance() {
		// TODO Auto-generated method stub
		
		return Double.POSITIVE_INFINITY;
	}

}
