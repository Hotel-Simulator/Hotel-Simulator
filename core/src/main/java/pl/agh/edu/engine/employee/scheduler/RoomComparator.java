package pl.agh.edu.engine.employee.scheduler;

import java.util.Comparator;

import pl.agh.edu.engine.room.Room;
import pl.agh.edu.serialization.KryoConfig;

public class RoomComparator implements Comparator<Room> {

	public static void kryoRegister() {
		KryoConfig.kryo.register(RoomComparator.class);
	}

	@Override
	public int compare(Room o1, Room o2) {
		return Boolean.compare(o1.roomState.isOccupied(), o2.roomState.isOccupied());
	}
}
