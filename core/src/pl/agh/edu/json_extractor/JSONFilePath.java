package pl.agh.edu.json_extractor;

public enum JSONFilePath {

    ADVERTISEMENT_CONFIG,
    BANK_CONFIG,
    CLIENT_CONFIG,
    EMPLOYEE_CONFIG,
    EVENT_CONFIG,
    GAME_CONFIG,
    HOTEL_CONFIG,
    ROOM_CONFIG
    ;


    public String get() {
        return "assets/jsons/%s.json".formatted(this.toString().toLowerCase());
    }
}
