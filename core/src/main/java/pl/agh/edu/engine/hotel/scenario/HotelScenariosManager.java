package pl.agh.edu.engine.hotel.scenario;

import java.math.BigDecimal;
import java.time.MonthDay;
import java.util.EnumMap;
import java.util.Map;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.data.loader.JSONHotelScenariosDataLoader;
import pl.agh.edu.data.type.AttractivenessConstantsData;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;
import pl.agh.edu.engine.popularity.PopularityFunction;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.serialization.KryoConfig;

public class HotelScenariosManager {
	private final Time time;
	public final HotelType hotelType;
	private final Map<MonthDay, BigDecimal> seasonalMultiplier;

	public static void kryoRegister() {
		KryoConfig.kryo.register(HotelScenariosManager.class, new Serializer<HotelScenariosManager>() {
			@Override
			public void write(Kryo kryo, Output output, HotelScenariosManager object) {
				kryo.writeObject(output, object.time);
				kryo.writeObject(output, object.hotelType);
				kryo.writeObject(output, object.seasonalMultiplier, KryoConfig.mapSerializer(MonthDay.class, BigDecimal.class));
			}

			@Override
			public HotelScenariosManager read(Kryo kryo, Input input, Class<? extends HotelScenariosManager> type) {
				return new HotelScenariosManager(
						kryo.readObject(input, Time.class),
						kryo.readObject(input, HotelType.class),
						kryo.readObject(input, Map.class, KryoConfig.mapSerializer(MonthDay.class, BigDecimal.class)));
			}
		});
	}

	public HotelScenariosManager(HotelType hotelType) {
		this.time = Time.getInstance();
		this.hotelType = hotelType;
		seasonalMultiplier = PopularityFunction.getSeasonalMultipliers(hotelType);
	}

	public HotelScenariosManager(Time time, HotelType hotelType, Map<MonthDay, BigDecimal> seasonalMultiplier) {
		this.time = time;
		this.hotelType = hotelType;
		this.seasonalMultiplier = seasonalMultiplier;
	}

	public BigDecimal getCurrentDayMultiplier() {
		return seasonalMultiplier.get(time.getMonthDay());
	}

	public EnumMap<HotelVisitPurpose, BigDecimal> getHotelVisitPurposeProbabilities() {
		return JSONHotelScenariosDataLoader.hotelTypeVisitProbabilities.get(hotelType);
	}

	public AttractivenessConstantsData getAttractivenessConstants() {
		return JSONHotelScenariosDataLoader.attractivenessConstants.get(hotelType);
	}
}
