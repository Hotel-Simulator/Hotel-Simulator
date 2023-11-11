package pl.agh.edu.engine.bank;

import pl.agh.edu.serialization.KryoConfig;

public enum TransactionType {
	INCOME,
	EXPENSE;

	static {
		KryoConfig.kryo.register(TransactionType.class);

	}
}
