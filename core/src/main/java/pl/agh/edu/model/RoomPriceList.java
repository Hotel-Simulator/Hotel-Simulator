package pl.agh.edu.model;

import java.math.BigDecimal;
import java.util.EnumMap;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.RoomSize;

public class RoomPriceList {

	private final EnumMap<RoomRank, EnumMap<RoomSize, BigDecimal>> pricesPerNight;

	public RoomPriceList(EnumMap<RoomRank, EnumMap<RoomSize, BigDecimal>> pricesPerNight) {
		this.pricesPerNight = pricesPerNight;
	}

	public EnumMap<RoomRank, EnumMap<RoomSize, BigDecimal>> getPrices() {
		return pricesPerNight;
	}

	public BigDecimal getPrice(RoomRank roomRank, RoomSize roomSize) {
		return pricesPerNight.get(roomRank).get(roomSize);
	}

	public BigDecimal getPrice(Room room) {
		return getPrice(room.getRank(), room.size);
	}

	public void setPrice(RoomRank RoomRank, RoomSize roomSize, BigDecimal price) {
		pricesPerNight.get(RoomRank).put(roomSize, price);
	}

	public void setPrices(EnumMap<RoomRank, EnumMap<RoomSize, BigDecimal>> prices) {
		prices.keySet().forEach(
				roomRank -> prices.get(roomRank).forEach(
						(roomSize, price) -> pricesPerNight.get(roomRank).put(roomSize, price)));
	}
}
