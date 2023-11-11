package pl.agh.edu.engine.opinion;

import static pl.agh.edu.engine.opinion.OpinionStars.ONE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.opinion.bucket.EmployeesSatisfactionOpinionBucket;
import pl.agh.edu.engine.opinion.bucket.OpinionBucket;
import pl.agh.edu.engine.opinion.bucket.QueueWaitingOpinionBucket;
import pl.agh.edu.engine.opinion.bucket.RoomBreakingOpinionBucket;
import pl.agh.edu.engine.opinion.bucket.RoomCleaningOpinionBucket;
import pl.agh.edu.engine.opinion.bucket.RoomPriceOpinionBucket;
import pl.agh.edu.serialization.KryoConfig;

public class Opinion {

	private boolean clientGroupGotRoom = false;
	private boolean clientSteppedOutOfQueue = false;
	public final RoomCleaningOpinionBucket roomCleaning;
	public final RoomBreakingOpinionBucket roomBreaking;
	public final RoomPriceOpinionBucket roomPrice;
	public final QueueWaitingOpinionBucket queueWaiting;
	public final EmployeesSatisfactionOpinionBucket employeesSatisfaction;

	private final List<OpinionBucket> opinionBuckets = new ArrayList<>();

	static {
		KryoConfig.kryo.register(Opinion.class, new Serializer<Opinion>() {
			@Override
			public void write(Kryo kryo, Output output, Opinion object) {
				kryo.writeObject(output, object.roomCleaning);
				kryo.writeObject(output, object.roomBreaking);
				kryo.writeObject(output, object.roomPrice);
				kryo.writeObject(output, object.queueWaiting);
				kryo.writeObject(output, object.employeesSatisfaction);
				kryo.writeObject(output, object.clientGroupGotRoom);
				kryo.writeObject(output, object.clientSteppedOutOfQueue);

			}

			@Override
			public Opinion read(Kryo kryo, Input input, Class type) {
				return new Opinion(
						kryo.readObject(input, RoomCleaningOpinionBucket.class),
						kryo.readObject(input, RoomBreakingOpinionBucket.class),
						kryo.readObject(input, RoomPriceOpinionBucket.class),
						kryo.readObject(input, QueueWaitingOpinionBucket.class),
						kryo.readObject(input, EmployeesSatisfactionOpinionBucket.class),
						kryo.readObject(input, Boolean.class),
						kryo.readObject(input, Boolean.class));
			}
		});
	}

	public Opinion(ClientGroup clientGroup) {
		roomCleaning = new RoomCleaningOpinionBucket(2, clientGroup.getNumberOfNights());
		roomBreaking = new RoomBreakingOpinionBucket(1);
		roomPrice = new RoomPriceOpinionBucket(1, clientGroup.getDesiredPricePerNight());
		queueWaiting = new QueueWaitingOpinionBucket(1, clientGroup.getMaxWaitingTime());
		employeesSatisfaction = new EmployeesSatisfactionOpinionBucket(1);

		Collections.addAll(opinionBuckets, roomCleaning, roomBreaking, roomPrice, queueWaiting, employeesSatisfaction);
	}

	public Opinion(RoomCleaningOpinionBucket roomCleaning,
			RoomBreakingOpinionBucket roomBreaking,
			RoomPriceOpinionBucket roomPrice,
			QueueWaitingOpinionBucket queueWaiting,
			EmployeesSatisfactionOpinionBucket employeesSatisfaction,
			boolean clientGotRoom,
			boolean clientSteppedOutOfQueue) {
		this.roomCleaning = roomCleaning;
		this.roomBreaking = roomBreaking;
		this.roomPrice = roomPrice;
		this.queueWaiting = queueWaiting;
		this.employeesSatisfaction = employeesSatisfaction;
		this.clientGroupGotRoom = clientGotRoom;
		this.clientSteppedOutOfQueue = clientSteppedOutOfQueue;

		Collections.addAll(opinionBuckets, roomCleaning, roomBreaking, roomPrice, queueWaiting, employeesSatisfaction);
	}

	public void setClientGroupGotRoom() {
		this.clientGroupGotRoom = true;
	}

	public void setClientSteppedOutOfQueue() {
		this.clientSteppedOutOfQueue = true;
	}

	private double getValue() {
		return opinionBuckets.stream()
				.flatMapToDouble(opinionBucket -> IntStream.range(0, opinionBucket.weight).mapToDouble(i -> opinionBucket.getValue()))
				.average()
				.orElse(0.0);
	}

	public OpinionStars getStars() {
		return clientGroupGotRoom ? OpinionStars.get(getValue()) : ONE;
	}

	public List<String> getComment() {
		if (clientSteppedOutOfQueue) {
			return List.of("opinionComment.result.steppedOutOfQueue");
		}
		if (!clientGroupGotRoom) {
			return List.of("opinionComment.result.didNotGetRoom");
		}
		return opinionBuckets.stream().map(OpinionBucket::getComment).filter(Optional::isPresent).map(Optional::get).toList();
	}

}
