package pl.agh.edu.management.hotel;

import java.time.MonthDay;
import java.util.EnumMap;
import java.util.Map;

import pl.agh.edu.enums.HotelType;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.json.data_loader.JSONHotelScenariosDataLoader;
import pl.agh.edu.model.HotelPopularityFunction;
import pl.agh.edu.model.time.Time;

public class HotelScenariosManager {
	private EnumMap<HotelVisitPurpose, Double> hotelVisitPurposeProbabilities;
	private Map<MonthDay, Double> seasonalMultiplier;
	private final Time time = Time.getInstance();

	public HotelScenariosManager(HotelType hotelType) {
		this.hotelSetUp(hotelType);
	}

	public EnumMap<HotelVisitPurpose, Double> getHotelVisitPurposeProbabilities() {
		return hotelVisitPurposeProbabilities;
	}

	private void hotelSetUp(HotelType hotelType) {

		hotelVisitPurposeProbabilities = JSONHotelScenariosDataLoader.hotelTypeVisitProbabilities.get(hotelType);
		seasonalMultiplier = HotelPopularityFunction.getSeasonalMultipliers(hotelType);
	}

	public double getCurrentDayMultiplier() {
		return seasonalMultiplier.get(time.getMonthDay());
	}

	public static void main(String[] args) {}
}
