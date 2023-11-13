package pl.agh.edu.engine.room;

import pl.agh.edu.serialization.KryoConfig;

public enum RoomRank {
	ECONOMIC,
	STANDARD,
	DELUXE;

	static {
		KryoConfig.kryo.register(RoomRank.class);
	}
}
