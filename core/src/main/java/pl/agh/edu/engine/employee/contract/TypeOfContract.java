package pl.agh.edu.engine.employee.contract;

import pl.agh.edu.serialization.KryoConfig;

public enum TypeOfContract {
	PERMANENT;

	static {
		KryoConfig.kryo.register(TypeOfContract.class);
	}
}
