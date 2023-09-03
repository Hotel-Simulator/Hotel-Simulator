package pl.agh.edu.model;

import java.math.BigDecimal;
import java.util.EnumMap;

import pl.agh.edu.enums.RoomCapacity;
import pl.agh.edu.enums.RoomRank;

public class RoomPriceList {

	private final EnumMap<RoomRank, EnumMap<RoomCapacity, BigDecimal>> pricesPerNight;

	public RoomPriceList(EnumMap<RoomRank, EnumMap<RoomCapacity, BigDecimal>> pricesPerNight) {
		this.pricesPerNight = pricesPerNight;
	}

	public EnumMap<RoomRank, EnumMap<RoomCapacity, BigDecimal>> getPrices() {
		return pricesPerNight;
	}

	public BigDecimal getPrice(RoomRank roomRank, RoomCapacity roomCapacity) {
		return pricesPerNight.get(roomRank).get(roomCapacity);
	}

	public BigDecimal getPrice(Room room) {
		return getPrice(room.getRank(), room.capacity);
	}

	public void setPrice(RoomRank RoomRank, RoomCapacity roomCapacity, BigDecimal price) {
		pricesPerNight.get(RoomRank).put(roomCapacity, price);
	}

	public void setPrices(EnumMap<RoomRank, EnumMap<RoomCapacity, BigDecimal>> prices) {
		prices.keySet().forEach(
				roomRank -> prices.get(roomRank).forEach(
						(roomCapacity, price) -> pricesPerNight.get(roomRank).put(roomCapacity, price)));
	}
}
