package pl.agh.edu.engine.opinion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.opinion.bucket.EmployeesSatisfactionOpinionBucket;
import pl.agh.edu.engine.opinion.bucket.OpinionBucket;
import pl.agh.edu.engine.opinion.bucket.QueueWaitingOpinionBucket;
import pl.agh.edu.engine.opinion.bucket.RoomBreakingOpinionBucket;
import pl.agh.edu.engine.opinion.bucket.RoomCleaningOpinionBucket;
import pl.agh.edu.engine.opinion.bucket.RoomPriceOpinionBucket;

public class Opinion {
	public final RoomCleaningOpinionBucket roomCleaning;
	public final RoomBreakingOpinionBucket roomBreaking;
	public final RoomPriceOpinionBucket roomPrice;
	public final QueueWaitingOpinionBucket queueWaiting;
	public final EmployeesSatisfactionOpinionBucket employeesSatisfaction;

	private final List<OpinionBucket> opinionBuckets = new ArrayList<>();

	public Opinion(ClientGroup clientGroup) {
		roomCleaning = new RoomCleaningOpinionBucket(2);
		roomBreaking = new RoomBreakingOpinionBucket(1);
		roomPrice = new RoomPriceOpinionBucket(1);
		queueWaiting = new QueueWaitingOpinionBucket(1, clientGroup.getMaxWaitingTime());
		employeesSatisfaction = new EmployeesSatisfactionOpinionBucket(1);

		Collections.addAll(opinionBuckets, roomCleaning, roomBreaking, roomPrice, queueWaiting, employeesSatisfaction);
	}

	private double getValue() {
		return opinionBuckets.stream()
				.flatMapToDouble(opinionBucket -> IntStream.range(0, opinionBucket.weight).mapToDouble(i -> opinionBucket.getValue()))
				.average()
				.orElse(0.0);
	}

	public OpinionStars getStars() {
		return OpinionStars.get(getValue());
	}

}
