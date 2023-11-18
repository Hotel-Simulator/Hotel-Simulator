package pl.agh.edu.engine.room;

import java.math.BigDecimal;
import java.util.Map;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.Pair;

public class RoomPricePerNight {

	private final Map<Pair<RoomRank, RoomSize>, BigDecimal> pricesPerNightMap;

	static {
		KryoConfig.kryo.register(RoomPricePerNight.class, new Serializer<RoomPricePerNight>() {
			@Override
			public void write(Kryo kryo, Output output, RoomPricePerNight object) {
				kryo.writeObject(output, object.pricesPerNightMap, KryoConfig.mapSerializer(Pair.class, BigDecimal.class));
			}

			@Override
			public RoomPricePerNight read(Kryo kryo, Input input, Class<? extends RoomPricePerNight> type) {
				return new RoomPricePerNight(
						kryo.readObject(input, Map.class, KryoConfig.mapSerializer(Pair.class, BigDecimal.class)));
			}
		});
	}

	public RoomPricePerNight(Map<Pair<RoomRank, RoomSize>, BigDecimal> pricesPerNightMap) {
		this.pricesPerNightMap = pricesPerNightMap;
	}

	public Map<Pair<RoomRank, RoomSize>, BigDecimal> getPrices() {
		return pricesPerNightMap;
	}

	public void setPrices(Map<Pair<RoomRank, RoomSize>, BigDecimal> prices) {
		pricesPerNightMap.putAll(prices);
	}

	public BigDecimal getPrice(RoomRank roomRank, RoomSize roomSize) {
		return pricesPerNightMap.get(Pair.of(roomRank, roomSize));
	}

	public BigDecimal getPrice(Room room) {
		return getPrice(room.getRank(), room.size);
	}

	public void setPrice(RoomRank roomRank, RoomSize roomSize, BigDecimal price) {
		pricesPerNightMap.put(Pair.of(roomRank, roomSize), price);
	}
}
