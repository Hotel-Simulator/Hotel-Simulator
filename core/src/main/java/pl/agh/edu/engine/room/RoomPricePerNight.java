package pl.agh.edu.engine.room;

import java.math.BigDecimal;
import java.util.Map;

import pl.agh.edu.utils.Pair;

public class RoomPricePerNight {

	private final Map<Pair<RoomRank, RoomSize>, BigDecimal> pricesPerNightMap;

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
