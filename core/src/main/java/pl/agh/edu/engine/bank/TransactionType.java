package pl.agh.edu.engine.bank;

import pl.agh.edu.serialization.KryoConfig;

public enum TransactionType {
	INCOME,
	EXPENSE;

	public static void kryoRegister() {
		KryoConfig.kryo.register(TransactionType.class);
	}
}
