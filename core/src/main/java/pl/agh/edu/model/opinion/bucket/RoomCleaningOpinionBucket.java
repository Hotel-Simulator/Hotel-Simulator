package pl.agh.edu.model.opinion.bucket;

public class RoomCleaningOpinionBucket extends OpinionBucket {
	private int cleanRoomCounter = 0;
	private int dirtyRoomCounter = 0;

	public RoomCleaningOpinionBucket(int weight) {
		super(weight);
	}

	public void setRoomCleaned(boolean wasCleaned) {
		if (wasCleaned)
			cleanRoomCounter++;
		else
			dirtyRoomCounter++;
	}

	@Override
	public double getValue() {
		return dirtyRoomCounter > 0 ? 0.5 * ((double) cleanRoomCounter / (cleanRoomCounter + dirtyRoomCounter)) : 1.;
	}
}
