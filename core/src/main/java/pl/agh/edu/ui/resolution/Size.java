package pl.agh.edu.ui.resolution;

public enum Size {
	SMALL("small"),
	MEDIUM("medium"),
	LARGE("large");

	private final String name;

	Size(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
