package pl.agh.edu.model.opinion.bucket;

public class RoomCleaningOpinionBucket extends OpinionBucket {
	private int cleanRoomCounter = 0;
	private final int numberOfNights;

	public RoomCleaningOpinionBucket(int weight, int numberOfNights) {
		super(weight);
		this.numberOfNights = numberOfNights;
	}

	public void setRoomCleaned() {
		cleanRoomCounter++;
	}

	@Override
	public double getValue() {
		int dirtyRoomCounter = numberOfNights - cleanRoomCounter;
		return dirtyRoomCounter > 0 ? 0.5 * ((double) cleanRoomCounter / numberOfNights) : 1.;
	}
}
