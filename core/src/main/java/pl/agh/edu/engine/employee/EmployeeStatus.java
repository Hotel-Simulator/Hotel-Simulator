package pl.agh.edu.engine.employee;

import pl.agh.edu.serialization.KryoConfig;

public enum EmployeeStatus {
	HIRED_NOT_WORKING,
	HIRED_WORKING,
	FIRED_WORKING;

	public static void kryoRegister() {
		KryoConfig.kryo.register(EmployeeStatus.class);
	}
}
