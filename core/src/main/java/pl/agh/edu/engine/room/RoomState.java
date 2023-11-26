package pl.agh.edu.engine.room;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.serialization.KryoConfig;

public class RoomState {
	private boolean isOccupied = false;
	private boolean isDirty = false;
	private boolean isFaulty = false;
	private boolean isUnderRankChange = false;
	private boolean isBeingBuild = false;

	public static void kryoRegister() {
		KryoConfig.kryo.register(RoomState.class, new Serializer<RoomState>() {
			@Override
			public void write(Kryo kryo, Output output, RoomState object) {
				kryo.writeObject(output, object.isOccupied);
				kryo.writeObject(output, object.isDirty);
				kryo.writeObject(output, object.isFaulty);
				kryo.writeObject(output, object.isUnderRankChange);
				kryo.writeObject(output, object.isBeingBuild);
			}

			@Override
			public RoomState read(Kryo kryo, Input input, Class<? extends RoomState> type) {
				RoomState roomState = new RoomState();
				roomState.isOccupied = kryo.readObject(input, Boolean.class);
				roomState.isDirty = kryo.readObject(input, Boolean.class);
				roomState.isFaulty = kryo.readObject(input, Boolean.class);
				roomState.isUnderRankChange = kryo.readObject(input, Boolean.class);
				roomState.isBeingBuild = kryo.readObject(input, Boolean.class);

				return roomState;
			}
		});
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public void setOccupied(boolean occupied) {
		isOccupied = occupied;
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(boolean dirty) {
		isDirty = dirty;
	}

	public boolean isFaulty() {
		return isFaulty;
	}

	public void setFaulty(boolean faulty) {
		isFaulty = faulty;
	}

	public boolean isUnderRankChange() {
		return isUnderRankChange;
	}

	public void setUnderRankChange(boolean underRankChange) {
		isUnderRankChange = underRankChange;
	}

	public boolean isBeingBuild() {
		return isBeingBuild;
	}

	public void setBeingBuild(boolean beingBuild) {
		isBeingBuild = beingBuild;
	}
}
