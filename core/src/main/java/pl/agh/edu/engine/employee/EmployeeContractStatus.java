package pl.agh.edu.engine.employee;

import pl.agh.edu.serialization.KryoConfig;

public enum EmployeeContractStatus {
	PENDING,
	ACTIVE,
	TERMINATED;

	public static void kryoRegister() {
		KryoConfig.kryo.register(EmployeeContractStatus.class);
	}
}
