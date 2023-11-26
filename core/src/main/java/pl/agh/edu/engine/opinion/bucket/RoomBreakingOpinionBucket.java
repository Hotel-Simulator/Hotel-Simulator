package pl.agh.edu.engine.opinion.bucket;

import java.util.Objects;
import java.util.Optional;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.serialization.KryoConfig;

public class RoomBreakingOpinionBucket extends OpinionBucket {
	private boolean gotBrokenRoom = false;
	private boolean roomBroke = false;
	private boolean repaired = false;

	public static void kryoRegister() {
		KryoConfig.kryo.register(RoomBreakingOpinionBucket.class, new Serializer<RoomBreakingOpinionBucket>() {
			@Override
			public void write(Kryo kryo, Output output, RoomBreakingOpinionBucket object) {
				kryo.writeObject(output, object.weight);
				kryo.writeObject(output, object.gotBrokenRoom);
				kryo.writeObject(output, object.roomBroke);
				kryo.writeObject(output, object.repaired);

			}

			@Override
			public RoomBreakingOpinionBucket read(Kryo kryo, Input input, Class<? extends RoomBreakingOpinionBucket> type) {
				RoomBreakingOpinionBucket roomBreakingOpinionBucket = new RoomBreakingOpinionBucket(kryo.readObject(input, Integer.class));

				roomBreakingOpinionBucket.gotBrokenRoom = kryo.readObject(input, Boolean.class);
				roomBreakingOpinionBucket.roomBroke = kryo.readObject(input, Boolean.class);
				roomBreakingOpinionBucket.repaired = kryo.readObject(input, Boolean.class);

				return roomBreakingOpinionBucket;
			}
		});
	}

	public RoomBreakingOpinionBucket(int weight) {
		super(weight);
	}

	public void roomBroke() {
		roomBroke = true;
	}

	public void roomRepaired() {
		repaired = true;
	}

	public void setGotBrokenRoom(boolean gotBrokenRoom) {
		this.gotBrokenRoom = gotBrokenRoom;
	}

	@Override
	public double getValue() {
		return (roomBroke || gotBrokenRoom) ? (repaired ? 0.5 : 0.) : 1.;
	}

	@Override
	public Optional<String> getComment() {
		if (gotBrokenRoom && repaired) {
			return Optional.of("opinionComment.roomBreaking.gotBrokenButRepaired");
		} else if (gotBrokenRoom) {
			return Optional.of("opinionComment.roomBreaking.gotBrokenAndNotRepaired");
		}
		if (roomBroke && repaired) {
			return Optional.of("opinionComment.roomBreaking.brokeButRepaired");
		} else if (roomBroke) {
			return Optional.of("opinionComment.roomBreaking.brokeAndNotRepaired");
		}
		return Optional.empty();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		RoomBreakingOpinionBucket that = (RoomBreakingOpinionBucket) o;
		return gotBrokenRoom == that.gotBrokenRoom && roomBroke == that.roomBroke && repaired == that.repaired;
	}

	@Override
	public int hashCode() {
		return Objects.hash(gotBrokenRoom, roomBroke, repaired);
	}
}
