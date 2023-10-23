package pl.agh.edu.json.data_extractor;

public enum JSONFilePath {

	ADVERTISEMENT_CONFIG,
	BANK_CONFIG,
	CLIENT_CONFIG,
	EMPLOYEE_CONFIG,
	EVENT_CONFIG,
	GAME_INIT_DATA,
	HOTEL_CONFIG,
	ROOM_CONFIG,
	HOTEL_SCENARIOS_CONFIG,
	OPINION_CONFIG;

	private final static String PATH = "assets/jsons/%s.json";

	public String get() {
		return PATH.formatted(this.toString().toLowerCase());
	}
}
