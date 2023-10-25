package pl.agh.edu.engine.opinion.bucket;

import static java.time.temporal.ChronoUnit.MINUTES;

import java.time.Duration;
import java.time.LocalDateTime;

public class QueueWaitingOpinionBucket extends OpinionBucket {
	private final Duration maxWaitingTime;
	private LocalDateTime startDate;
	private LocalDateTime endDate;

	public QueueWaitingOpinionBucket(int weight, Duration maxWaitingTime) {
		super(weight);
		this.maxWaitingTime = maxWaitingTime;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public QueueWaitingOpinionBucket(int weight, Duration maxWaitingTime) {
		super(weight);
		this.maxWaitingTime = maxWaitingTime;
	}

	@Override
	public double getValue() {
		if (endDate == null)
			return 0.;
		double waitingRatio = (double) ChronoUnit.MINUTES.between(startDate, endDate) / maxWaitingTime.toMinutes();
		return waitingRatio < 0.5 ? 1. : 2 * (1 - waitingRatio);
	}
}
