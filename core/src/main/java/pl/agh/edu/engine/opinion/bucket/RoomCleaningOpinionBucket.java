package pl.agh.edu.engine.opinion.bucket;

import java.util.Objects;
import java.util.Optional;

public class RoomCleaningOpinionBucket extends OpinionBucket {
	private boolean gotCleanRoom = false;
	private int cleanRoomCounter = 0;
	private final int numberOfNights;

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
