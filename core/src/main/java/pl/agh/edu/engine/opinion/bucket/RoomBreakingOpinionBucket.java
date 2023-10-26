package pl.agh.edu.engine.opinion.bucket;

import java.util.Optional;

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

	@Override
	public Optional<String> getComment() {
		if (broken && repaired) {
			return Optional.of("opinionComment.roomBreaking.brokeButRepaired");
		} else if (broken) {
			return Optional.of("opinionComment.roomBreaking.brokeAndNotRepaired");
		}
		return Optional.empty();
	}
}
