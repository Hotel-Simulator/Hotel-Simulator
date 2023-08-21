package pl.agh.edu.enums;

public enum Resolution {
	_1366x768(1366, 768),
	_1440x900(1440, 900),
	_1600x900(1600, 900),
	_1920x1080(1920, 1080),
	_2560x1440(2560, 1440),
	_2560x1600(2560, 1600),
	_2560x1080(2560, 1080),
	_3440x1440(3440, 1440),
	_3840x2160(3840, 2160);

	private final int width;
	private final int height;

	Resolution(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public String toString() {
		return width + "x" + height;
	}

	public static Resolution fromInts(int width, int height) {
		for (Resolution resolution : Resolution.values()) {
			if (resolution.width == width && resolution.height == height) {
				return resolution;
			}
		}
		throw new IllegalArgumentException("No matching Resolution for width=" + width + " and height=" + height);
	}
}
