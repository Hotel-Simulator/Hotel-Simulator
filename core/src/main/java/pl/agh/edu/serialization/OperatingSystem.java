package pl.agh.edu.serialization;

public enum OperatingSystem {
	WINDOWS("\\"),
	MAC_OS("/"),
	LINUX("/");

	public final String pathSeparator;

	OperatingSystem(String pathSeparator) {
		this.pathSeparator = pathSeparator;
	}

	public static OperatingSystem detect() {
		String osName = System.getProperty("os.name").toLowerCase();

		if (osName.contains("win")) {
			return WINDOWS;
		} else if (osName.contains("mac")) {
			return MAC_OS;
		} else if (osName.contains("nix") || osName.contains("nux") || osName.contains("bsd")) {
			return LINUX;
		} else {
			throw new RuntimeException("Unsupported operating system");
		}
	}

	public String getHomeDirectoryPath() {
		return switch (this) {
			case WINDOWS -> "\\AppData\\Local";
			case MAC_OS -> "/Library/Application Support";
			case LINUX -> "/.local/share";
		};
	}

}
