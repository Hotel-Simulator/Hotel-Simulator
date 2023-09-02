package pl.agh.edu.model;

public class RoomState {
	private boolean isOccupied = false;
	private boolean isDirty = false;
	private boolean isFaulty = false;
	private boolean isUnderRankChange = false;

	public boolean isOccupied() {
		return isOccupied;
	}

	public void setOccupied(boolean occupied) {
		isOccupied = occupied;
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(boolean dirty) {
		isDirty = dirty;
	}

	public boolean isFaulty() {
		return isFaulty;
	}

	public void setFaulty(boolean faulty) {
		isFaulty = faulty;
	}

	public boolean isUnderRankChange() {
		return isUnderRankChange;
	}

	public void setUnderRankChange(boolean underRankChange) {
		isUnderRankChange = underRankChange;
	}
}
