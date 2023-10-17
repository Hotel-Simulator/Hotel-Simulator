package pl.agh.edu.management.hotel;

import java.time.MonthDay;
import java.util.EnumMap;
import java.util.Map;

import pl.agh.edu.enums.HotelType;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.json.data.AttractivenessConstantsData;
import pl.agh.edu.json.data_loader.JSONHotelScenariosDataLoader;
import pl.agh.edu.model.HotelPopularityFunction;
import pl.agh.edu.model.time.Time;

public class HotelScenariosManager {
	public final EnumMap<HotelVisitPurpose, Double> hotelVisitPurposeProbabilities;
	public final AttractivenessConstantsData attractivenessConstants;
	public final HotelType type;
	private final Map<MonthDay, Double> seasonalMultiplier;

	private final Time time = Time.getInstance();

	public HotelScenariosManager(HotelType hotelType) {
		hotelVisitPurposeProbabilities = JSONHotelScenariosDataLoader.hotelTypeVisitProbabilities.get(hotelType);
		attractivenessConstants = JSONHotelScenariosDataLoader.attractivenessConstants.get(hotelType);
		this.type = hotelType;
		seasonalMultiplier = HotelPopularityFunction.getSeasonalMultipliers(hotelType);

	}

	public double getCurrentDayMultiplier() {
		return seasonalMultiplier.get(time.getMonthDay());
	}
}
