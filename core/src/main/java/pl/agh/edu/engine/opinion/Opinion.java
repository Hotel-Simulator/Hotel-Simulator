package pl.agh.edu.engine.opinion;

import static pl.agh.edu.engine.opinion.OpinionStars.ONE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.opinion.bucket.EmployeesSatisfactionOpinionBucket;
import pl.agh.edu.engine.opinion.bucket.OpinionBucket;
import pl.agh.edu.engine.opinion.bucket.QueueWaitingOpinionBucket;
import pl.agh.edu.engine.opinion.bucket.RoomBreakingOpinionBucket;
import pl.agh.edu.engine.opinion.bucket.RoomCleaningOpinionBucket;
import pl.agh.edu.engine.opinion.bucket.RoomPriceOpinionBucket;

public class Opinion {

	private boolean clientGroupGotRoom = false;
	private boolean clientSteppedOutOfQueue = false;
	public final RoomCleaningOpinionBucket roomCleaning;
	public final RoomBreakingOpinionBucket roomBreaking;
	public final RoomPriceOpinionBucket roomPrice;
	public final QueueWaitingOpinionBucket queueWaiting;
	public final EmployeesSatisfactionOpinionBucket employeesSatisfaction;

	private final List<OpinionBucket> opinionBuckets = new ArrayList<>();

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
