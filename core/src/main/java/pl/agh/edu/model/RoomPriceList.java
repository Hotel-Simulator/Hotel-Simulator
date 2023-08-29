package pl.agh.edu.model;

import java.math.BigDecimal;
import java.util.EnumMap;

import pl.agh.edu.enums.RoomCapacity;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.json.data_loader.JSONClientDataLoader;

public class RoomPriceList {

	private static final EnumMap<RoomRank, EnumMap<RoomCapacity, BigDecimal>> pricesPerNight = JSONClientDataLoader.averagePricesPerNight;

	private RoomPriceList() {}

	public static EnumMap<RoomRank, EnumMap<RoomCapacity, BigDecimal>> getPrices() {
		return pricesPerNight;
	}

	public static BigDecimal getPrice(RoomRank roomRank, RoomCapacity roomCapacity) {
		return pricesPerNight.get(roomRank).get(roomCapacity);
	}

	public static BigDecimal getPrice(Room room) {
		return getPrice(room.getRank(), room.getCapacity());
	}

	public static void setPrice(RoomRank RoomRank, RoomCapacity roomCapacity, BigDecimal price) {
		pricesPerNight.get(RoomRank).put(roomCapacity, price);
	}

	public static void setPrices(EnumMap<RoomRank, EnumMap<RoomCapacity, BigDecimal>> prices) {
		prices.keySet().forEach(
				roomRank -> prices.get(roomRank).forEach(
						(roomCapacity, price) -> {
							pricesPerNight.get(roomRank).put(roomCapacity, price);
						}));
	}
}
