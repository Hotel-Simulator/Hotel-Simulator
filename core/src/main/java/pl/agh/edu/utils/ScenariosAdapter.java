package pl.agh.edu.utils;

import pl.agh.edu.enums.DifficultyLevel;
import pl.agh.edu.enums.HotelType;
import pl.agh.edu.screen.ScenariosScreen;

public class ScenariosAdapter {
	private HotelType hotelType;
	private DifficultyLevel difficultyLevel;

	public void extractDataFromScenariosScreen(ScenariosScreen screen) {
		hotelType = screen.selected.hotelType;
		difficultyLevel = screen.currentlySelected.getDifficulty();
	}

	public HotelType getHotelType() {
		return hotelType;
	}

	public DifficultyLevel getDifficultyLevel() {
		return difficultyLevel;
	}
}
