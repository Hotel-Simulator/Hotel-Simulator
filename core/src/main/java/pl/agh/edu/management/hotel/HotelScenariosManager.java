package pl.agh.edu.management.hotel;

import java.util.EnumMap;
import java.util.HashMap;

import pl.agh.edu.enums.DifficultyLevel;
import pl.agh.edu.enums.HotelType;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.json.data_loader.JSONHotelScenariosDataLoader;
import pl.agh.edu.model.HotelPopularityFunction;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.utils.Pair;
import pl.agh.edu.utils.RandomUtils;

public class HotelScenariosManager {
	private EnumMap<HotelVisitPurpose, Double> hotelVisitPurposeProbabilities;
	private HashMap<Pair<Integer, Integer>, Double> seasonalMultiplier;
	private double difficultyMultiplier;
	private final Time time = Time.getInstance();

	public HotelScenariosManager() {
		hotelSetUp(HotelType.values()[RandomUtils.randomInt(HotelType.values().length)]);
		setDifficulty(DifficultyLevel.values()[RandomUtils.randomInt(DifficultyLevel.values().length)]);
	}

	public double getDifficultyMultiplier() {
		return difficultyMultiplier;
	}

	public EnumMap<HotelVisitPurpose, Double> getHotelVisitPurposeProbabilities() {
		return hotelVisitPurposeProbabilities;
	}

	private void hotelSetUp(HotelType hotelType) {

		hotelVisitPurposeProbabilities = JSONHotelScenariosDataLoader.hotelTypeVisitProbabilities.get(hotelType);
		seasonalMultiplier = HotelPopularityFunction.getSeasonalMultipliers(hotelType);
	}

	private void setDifficulty(DifficultyLevel difficulty) {
		difficultyMultiplier = JSONHotelScenariosDataLoader.difficultyMultiplier.get(difficulty);
	}

	public double getCurrentDayMultiplier() {
		int month = time.getTime().getMonthValue();
		int day = time.getTime().getDayOfMonth();
		return seasonalMultiplier.get(new Pair<>(month, day));
	}
}
