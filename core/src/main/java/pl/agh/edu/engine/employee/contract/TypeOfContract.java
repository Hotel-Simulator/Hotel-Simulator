package pl.agh.edu.engine.employee.contract;

import pl.agh.edu.serialization.KryoConfig;

public enum TypeOfContract {
	PERMANENT;

	public static void kryoRegister() {
		KryoConfig.kryo.register(TypeOfContract.class);
	}
}
