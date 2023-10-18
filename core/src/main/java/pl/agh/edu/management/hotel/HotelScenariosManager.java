package pl.agh.edu.management.hotel;

import java.math.BigDecimal;
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
	public final EnumMap<HotelVisitPurpose, BigDecimal> hotelVisitPurposeProbabilities;
	public final AttractivenessConstantsData attractivenessConstants;
	public final HotelType hotelType;
	private final Map<MonthDay, BigDecimal> seasonalMultiplier;

	private final Time time = Time.getInstance();

	public HotelScenariosManager(HotelType hotelType) {
		hotelVisitPurposeProbabilities = JSONHotelScenariosDataLoader.hotelTypeVisitProbabilities.get(hotelType);
		attractivenessConstants = JSONHotelScenariosDataLoader.attractivenessConstants.get(hotelType);
		this.hotelType = hotelType;
		seasonalMultiplier = HotelPopularityFunction.getSeasonalMultipliers(hotelType);

	}

	public BigDecimal getCurrentDayMultiplier() {
		return seasonalMultiplier.get(time.getMonthDay());
	}
}
