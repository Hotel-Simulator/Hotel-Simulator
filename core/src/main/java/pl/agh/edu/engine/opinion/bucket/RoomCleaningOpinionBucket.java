package pl.agh.edu.engine.opinion.bucket;

import java.util.Objects;
import java.util.Optional;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.serialization.KryoConfig;

public class RoomCleaningOpinionBucket extends OpinionBucket {
	private boolean gotCleanRoom = false;
	private int cleanRoomCounter = 0;
	private final int numberOfNights;

	public static void kryoRegister() {
		KryoConfig.kryo.register(RoomCleaningOpinionBucket.class, new Serializer<RoomCleaningOpinionBucket>() {
			@Override
			public void write(Kryo kryo, Output output, RoomCleaningOpinionBucket object) {
				kryo.writeObject(output, object.weight);
				kryo.writeObject(output, object.numberOfNights);
				kryo.writeObject(output, object.gotCleanRoom);
				kryo.writeObject(output, object.cleanRoomCounter);

			}

			@Override
			public RoomCleaningOpinionBucket read(Kryo kryo, Input input, Class<? extends RoomCleaningOpinionBucket> type) {
				RoomCleaningOpinionBucket roomCleaningOpinionBucket = new RoomCleaningOpinionBucket(
						kryo.readObject(input, Integer.class),
						kryo.readObject(input, Integer.class));
				roomCleaningOpinionBucket.gotCleanRoom = kryo.readObject(input, Boolean.class);
				roomCleaningOpinionBucket.cleanRoomCounter = kryo.readObject(input, Integer.class);

				return roomCleaningOpinionBucket;
			}
		});
	}

	public RoomCleaningOpinionBucket(int weight, int numberOfNights) {
		super(weight);
		this.numberOfNights = numberOfNights;
	}

	public void setRoomCleaned() {
		cleanRoomCounter++;
	}

	public void setGotCleanRoom(boolean gotCleanRoom) {
		this.gotCleanRoom = gotCleanRoom;
		cleanRoomCounter++;
	}

	@Override
	public double getValue() {
		int dirtyRoomCounter = numberOfNights - cleanRoomCounter;
		return dirtyRoomCounter > 0 ? 0.5 * ((double) cleanRoomCounter / numberOfNights) : 1.;
	}

	@Override
	public Optional<String> getComment() {
		double value = getValue();
		if (value == 1.) {
			return Optional.of("opinionComment.roomCleaning.alwaysCleanedAndGotCleanRoom");
		}
		if (gotCleanRoom) {
			return Optional.of("opinionComment.roomCleaning.sometimesCleanedAndGotCleanRoom");
		} else if (cleanRoomCounter == numberOfNights - 1) {
			return Optional.of("opinionComment.roomCleaning.alwaysCleanedAndGotDirtyRoom");
		}
		if (value == 0.) {
			return Optional.of("opinionComment.roomCleaning.alwaysDirty");
		}
		return Optional.of("opinionComment.roomCleaning.sometimesCleanedAndGotDirtyRoom");

	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		RoomCleaningOpinionBucket that = (RoomCleaningOpinionBucket) o;
		return gotCleanRoom == that.gotCleanRoom && cleanRoomCounter == that.cleanRoomCounter && numberOfNights == that.numberOfNights;
	}

	@Override
	public int hashCode() {
		return Objects.hash(gotCleanRoom, cleanRoomCounter, numberOfNights);
	}
}
