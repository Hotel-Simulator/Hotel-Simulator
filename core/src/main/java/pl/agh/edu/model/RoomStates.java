package pl.agh.edu.model;

public class RoomStates {
	private boolean isOccupied;
	private boolean isDirty;
	private boolean isFaulty;
	private boolean isBeingUpgraded;

	public RoomStates() {
		isOccupied = false;
		isDirty = false;
		isFaulty = false;
		isBeingUpgraded = false;
	}

	public RoomStates(boolean isOccupied, boolean isDirty, boolean isFaulty, boolean isBeingUpgraded) {
		this.isOccupied = isOccupied;
		this.isDirty = isDirty;
		this.isFaulty = isFaulty;
		this.isBeingUpgraded = isBeingUpgraded;
	}

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

	public boolean isBeingUpgraded() {
		return isBeingUpgraded;
	}

	public void setBeingUpgraded(boolean beingUpgraded) {
		isBeingUpgraded = beingUpgraded;
	}
}
