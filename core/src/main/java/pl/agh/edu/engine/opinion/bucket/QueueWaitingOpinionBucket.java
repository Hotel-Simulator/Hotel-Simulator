package pl.agh.edu.engine.opinion.bucket;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

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

	@Override
	public double getValue() {
		if (endDate == null)
			return 0.;
		double waitingRatio = (double) ChronoUnit.MINUTES.between(startDate, endDate) / maxWaitingTime.toMinutes();
		return waitingRatio < 0.5 ? 1. : 2 * Math.max(0, (1 - waitingRatio));
	}

	@Override
	public Optional<String> getComment() {
		if (getValue() < 1.) {
			return Optional.of("opinionComment.queueWaiting.longWaiting");
		}
		return Optional.empty();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		QueueWaitingOpinionBucket that = (QueueWaitingOpinionBucket) o;
		return Objects.equals(maxWaitingTime, that.maxWaitingTime) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(maxWaitingTime, startDate, endDate);
	}
}
