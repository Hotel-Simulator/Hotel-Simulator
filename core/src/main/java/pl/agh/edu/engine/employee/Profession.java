package pl.agh.edu.engine.employee;

import pl.agh.edu.serialization.KryoConfig;

public enum Profession {
	CLEANER,
	TECHNICIAN,
	RECEPTIONIST;

	static {
		KryoConfig.kryo.register(Profession.class);
	}
}
