package pl.agh.edu.model;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.json.data_loader.JSONClientDataLoader;
import pl.agh.edu.json.data_loader.JSONRoomDataLoader;

public class RoomPriceList {

	private static final EnumMap<RoomRank, Map<Integer, BigDecimal>> pricesPerNight = JSONClientDataLoader.averagePricesPerNight;

	private RoomPriceList() {}

	public static EnumMap<RoomRank, Map<Integer, BigDecimal>> getPrices() {
		return pricesPerNight;
	}

	public static BigDecimal getPrice(RoomRank roomRank, int roomCapacity) {
		if (roomCapacity < 1 || roomCapacity > JSONRoomDataLoader.maxCapacity) {
			throw new IllegalArgumentException("Room capacity must be between 1 and " + JSONRoomDataLoader.maxCapacity);
		}
		return pricesPerNight.get(roomRank).get(roomCapacity);
	}

	public static BigDecimal getPrice(Room room) {
		return getPrice(room.getRank(), room.getCapacity());
	}

	public static void setPrice(RoomRank RoomRank, int roomCapacity, BigDecimal price) {
		if (roomCapacity < 1 || roomCapacity > JSONRoomDataLoader.maxCapacity) {
			throw new IllegalArgumentException("Room capacity must be between 1 and " + JSONRoomDataLoader.maxCapacity);
		}
		pricesPerNight.get(RoomRank).put(roomCapacity, price);
	}

	public static void setPrices(EnumMap<RoomRank, Map<Integer, BigDecimal>> prices) {
		prices.keySet().forEach(
				roomRank -> prices.get(roomRank).forEach(
						(roomCapacity, price) -> {
							if (roomCapacity < 1 || roomCapacity > JSONRoomDataLoader.maxCapacity) {
								throw new IllegalArgumentException("Room capacity must be between 1 and " + JSONRoomDataLoader.maxCapacity);
							}
							pricesPerNight.get(roomRank).put(roomCapacity, price);
						}));
	}
}
