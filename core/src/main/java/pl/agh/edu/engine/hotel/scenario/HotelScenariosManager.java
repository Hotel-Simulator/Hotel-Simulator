package pl.agh.edu.engine.hotel.scenario;

import java.time.MonthDay;
import java.util.EnumMap;
import java.util.Map;

import pl.agh.edu.data.loader.JSONHotelScenariosDataLoader;
import pl.agh.edu.data.type.AttractivenessConstantsData;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;
import pl.agh.edu.engine.popularity.PopularityFunction;
import pl.agh.edu.engine.time.Time;

public class HotelScenariosManager {
	public final EnumMap<HotelVisitPurpose, Double> hotelVisitPurposeProbabilities;
	public final AttractivenessConstantsData attractivenessConstants;
	public final HotelType hotelType;
	private final Map<MonthDay, Double> seasonalMultiplier;

	private final Time time = Time.getInstance();

	public HotelScenariosManager(HotelType hotelType) {
		hotelVisitPurposeProbabilities = JSONHotelScenariosDataLoader.hotelTypeVisitProbabilities.get(hotelType);
		attractivenessConstants = JSONHotelScenariosDataLoader.attractivenessConstants.get(hotelType);
		this.hotelType = hotelType;
		seasonalMultiplier = PopularityFunction.getSeasonalMultipliers(hotelType);

	}

	public double getCurrentDayMultiplier() {
		return seasonalMultiplier.get(time.getMonthDay());
	}
}
