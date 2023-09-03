package pl.agh.edu.enums;

public enum RoomCapacity {
	ONE(1),
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5);

	public final int value;

	RoomCapacity(int value) {
		this.value = value;
	}

}
