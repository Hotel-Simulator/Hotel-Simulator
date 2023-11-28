package pl.agh.edu.engine.room;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.serialization.KryoConfig;

public class Room {
	private RoomRank rank;
	public final RoomSize size;
	public final RoomState roomState;
	private ClientGroup residents;

	public static void kryoRegister() {
		KryoConfig.kryo.register(Room.class, new Serializer<Room>() {
			@Override
			public void write(Kryo kryo, Output output, Room object) {
				kryo.writeObject(output, object.rank);
				kryo.writeObject(output, object.size);
				kryo.writeObject(output, object.roomState);
				kryo.writeObjectOrNull(output, object.residents, ClientGroup.class);
			}

			@Override
			public Room read(Kryo kryo, Input input, Class<? extends Room> type) {
				Room room = new Room(kryo.readObject(
						input, RoomRank.class),
						kryo.readObject(input, RoomSize.class),
						kryo.readObject(input, RoomState.class));

				room.residents = kryo.readObjectOrNull(input, ClientGroup.class);
				return room;
			}
		});
	}

	public Room(RoomRank rank, RoomSize size) {
		this.size = size;
		this.rank = rank;
		this.roomState = new RoomState();
	}

	private Room(RoomRank rank, RoomSize size, RoomState state) {
		this.size = size;
		this.rank = rank;
		this.roomState = state;
	}

	public RoomRank getRank() {
		return rank;
	}

	public void setRank(RoomRank rank) {
		this.rank = rank;
	}

	public RoomSize getSize() {
		return size;
	}

	public void changeRank(RoomRank desiredRank) {
		if (desiredRank == rank) {
			throw new IllegalArgumentException("Desired roomRank must be different from the current rank.");
		}
		this.rank = desiredRank;
	}

	public void checkIn(ClientGroup clientGroup) {
		this.residents = clientGroup;
		roomState.setOccupied(true);
	}

	public void checkOut() {
		this.residents = null;
		roomState.setOccupied(false);
		roomState.setDirty(true);
	}

	public ClientGroup getResidents() {
		return this.residents;
	}
}
