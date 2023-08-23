package pl.agh.edu.model.console;

public enum LogLevel {

	DEFAULT(""),
	ERROR("Error: "),
	SUCCESS("Success! "),
	WARNING("Warning: ");

	private final String identifier;

	LogLevel(String identity) {
		identifier = identity;
	}

	String getIdentifier() {
		return identifier;
	}
}
