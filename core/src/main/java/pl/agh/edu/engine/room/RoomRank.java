package pl.agh.edu.engine.room;

import pl.agh.edu.serialization.KryoConfig;

public enum RoomRank {
	ECONOMIC,
	STANDARD,
	DELUXE;

	public static void kryoRegister() {
		KryoConfig.kryo.register(RoomRank.class);
	}
}
