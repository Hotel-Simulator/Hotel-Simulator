package pl.agh.edu.engine.opinion.bucket;

import java.util.Optional;

public class RoomBreakingOpinionBucket extends OpinionBucket {
	private boolean gotBrokenRoom = false;
	private boolean roomBroke = false;
	private boolean repaired = false;

	public RoomBreakingOpinionBucket(int weight) {
		super(weight);
	}

	public void roomBroke() {
		roomBroke = true;
	}

	public void roomRepaired() {
		repaired = true;
	}

	public void setGotBrokenRoom(boolean gotBrokenRoom) {
		this.gotBrokenRoom = gotBrokenRoom;
	}

	@Override
	public double getValue() {
		return (roomBroke || gotBrokenRoom) ? (repaired ? 0.5 : 0.) : 1.;
	}

	@Override
	public Optional<String> getComment() {
		if (gotBrokenRoom && repaired) {
			return Optional.of("opinionComment.roomBreaking.gotBrokenButRepaired");
		} else if (gotBrokenRoom) {
			return Optional.of("opinionComment.roomBreaking.gotBrokenAndNotRepaired");
		}
		if (roomBroke && repaired) {
			return Optional.of("opinionComment.roomBreaking.brokeButRepaired");
		} else if (roomBroke) {
			return Optional.of("opinionComment.roomBreaking.brokeAndNotRepaired");
		}
		return Optional.empty();
	}
}
