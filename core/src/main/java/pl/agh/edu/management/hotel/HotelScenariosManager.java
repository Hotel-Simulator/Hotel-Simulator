package pl.agh.edu.management.hotel;

import java.time.MonthDay;
import java.util.EnumMap;
import java.util.HashMap;

import pl.agh.edu.enums.HotelType;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.json.data_loader.JSONHotelScenariosDataLoader;
import pl.agh.edu.model.HotelPopularityFunction;
import pl.agh.edu.model.time.Time;

public class HotelScenariosManager {
	private EnumMap<HotelVisitPurpose, Double> hotelVisitPurposeProbabilities;
	private HashMap<MonthDay, Double> seasonalMultiplier;
	private final Time time = Time.getInstance();

	public HotelScenariosManager() {
		// Set user input here
		hotelSetUp(HotelType.HOTEL);
	}

	public EnumMap<HotelVisitPurpose, Double> getHotelVisitPurposeProbabilities() {
		return hotelVisitPurposeProbabilities;
	}

	private void hotelSetUp(HotelType hotelType) {

		hotelVisitPurposeProbabilities = JSONHotelScenariosDataLoader.hotelTypeVisitProbabilities.get(hotelType);
		seasonalMultiplier = HotelPopularityFunction.getSeasonalMultipliers(hotelType);
	}

	public double getCurrentDayMultiplier() {
		int month = time.getTime().getMonthValue();
		int day = time.getTime().getDayOfMonth();
		return seasonalMultiplier.get(MonthDay.of(month, day));
	}
}
