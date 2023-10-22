package pl.agh.edu.model.opinion.bucket;

public class RoomBreakingOpinionBucket extends OpinionBucket {

	private boolean broken = false;
	private boolean repaired = false;

	public RoomBreakingOpinionBucket(int weight) {
		super(weight);
	}

	public void roomBroke() {
		broken = true;
	}

	public void roomRepaired() {
		repaired = true;
	}

	@Override
	public double getValue() {
		return broken ? (repaired ? 0.5 : 0.) : 1.;
	}
}
