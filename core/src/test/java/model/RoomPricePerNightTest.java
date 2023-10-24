package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static pl.agh.edu.engine.room.RoomRank.DELUXE;
import static pl.agh.edu.engine.room.RoomRank.STANDARD;
import static pl.agh.edu.engine.room.RoomSize.DOUBLE;
import static pl.agh.edu.engine.room.RoomSize.FAMILY;
import static pl.agh.edu.engine.room.RoomSize.SINGLE;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.engine.room.RoomPricePerNight;
import pl.agh.edu.engine.room.RoomRank;
import pl.agh.edu.engine.room.RoomSize;
import pl.agh.edu.utils.Pair;

public class RoomPricePerNightTest {
	private static Map<Pair<RoomRank, RoomSize>, BigDecimal> testPrices;
	private static RoomPricePerNight roomPricePerNight;

	@BeforeEach
	void setUp() {
		testPrices = new HashMap<>();

		testPrices.put(Pair.of(STANDARD, SINGLE), BigDecimal.valueOf(100));
		testPrices.put(Pair.of(STANDARD, DOUBLE), BigDecimal.valueOf(150));
		testPrices.put(Pair.of(STANDARD, FAMILY), BigDecimal.valueOf(200));

		testPrices.put(Pair.of(DELUXE, SINGLE), BigDecimal.valueOf(200));
		testPrices.put(Pair.of(DELUXE, DOUBLE), BigDecimal.valueOf(300));
		testPrices.put(Pair.of(DELUXE, FAMILY), BigDecimal.valueOf(400));

		roomPricePerNight = new RoomPricePerNight(testPrices);
	}

	@Test
	public void getPrices() {
		// Given
		// When
		Map<Pair<RoomRank, RoomSize>, BigDecimal> prices = roomPricePerNight.getPrices();

		// Then
		assertNotNull(prices);
		assertEquals(testPrices, prices);
	}

	@Test
	public void getPrice() {
		// Given
		// When
		BigDecimal price = roomPricePerNight.getPrice(STANDARD, SINGLE);

		// Then
		assertEquals(BigDecimal.valueOf(100), price);
	}

	@Test
	public void setPrice() {
		// Given

		// When
		roomPricePerNight.setPrice(DELUXE, DOUBLE, BigDecimal.valueOf(350));
		BigDecimal price = roomPricePerNight.getPrice(DELUXE, DOUBLE);

		// Then
		assertEquals(BigDecimal.valueOf(350), price);
	}

	@Test
	public void setPrices() {
		// Given
		Map<Pair<RoomRank, RoomSize>, BigDecimal> newPrices = new HashMap<>();

		newPrices.put(Pair.of(STANDARD, SINGLE), BigDecimal.valueOf(100));
		newPrices.put(Pair.of(STANDARD, DOUBLE), BigDecimal.valueOf(150));
		newPrices.put(Pair.of(STANDARD, FAMILY), BigDecimal.valueOf(200));

		newPrices.put(Pair.of(DELUXE, SINGLE), BigDecimal.valueOf(200));
		newPrices.put(Pair.of(DELUXE, DOUBLE), BigDecimal.valueOf(300));
		newPrices.put(Pair.of(DELUXE, FAMILY), BigDecimal.valueOf(400));

		roomPricePerNight = new RoomPricePerNight(testPrices);
		// When
		roomPricePerNight.setPrices(newPrices);

		// Then
		assertEquals(roomPricePerNight.getPrices(), newPrices);
	}
}
