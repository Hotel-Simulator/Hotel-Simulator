package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.RoomSize;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.model.RoomPricePerNight;
import pl.agh.edu.utils.Pair;

public class RoomPricePerNightTest {
	private static Map<Pair<RoomRank, RoomSize>, BigDecimal> testPrices;
	private static RoomPricePerNight roomPricePerNight;

	@BeforeAll
	static void setUpClass() throws ReflectiveOperationException {
		changeJSONPath();
	}

	@BeforeEach
	void setUp() {
		testPrices = new HashMap<>();

		testPrices.put(new Pair<>(RoomRank.STANDARD, RoomSize.SINGLE), BigDecimal.valueOf(100));
		testPrices.put(new Pair<>(RoomRank.STANDARD, RoomSize.DOUBLE), BigDecimal.valueOf(150));
		testPrices.put(new Pair<>(RoomRank.STANDARD, RoomSize.FAMILY), BigDecimal.valueOf(200));

		testPrices.put(new Pair<>(RoomRank.DELUXE, RoomSize.SINGLE), BigDecimal.valueOf(200));
		testPrices.put(new Pair<>(RoomRank.DELUXE, RoomSize.DOUBLE), BigDecimal.valueOf(300));
		testPrices.put(new Pair<>(RoomRank.DELUXE, RoomSize.FAMILY), BigDecimal.valueOf(400));

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
		BigDecimal price = roomPricePerNight.getPrice(RoomRank.STANDARD, RoomSize.SINGLE);

		// Then
		assertEquals(BigDecimal.valueOf(100), price);
	}

	@Test
	public void setPrice() {
		// Given

		// When
		roomPricePerNight.setPrice(RoomRank.DELUXE, RoomSize.DOUBLE, BigDecimal.valueOf(350));
		BigDecimal price = roomPricePerNight.getPrice(RoomRank.DELUXE, RoomSize.DOUBLE);

		// Then
		assertEquals(BigDecimal.valueOf(350), price);
	}

	@Test
	public void setPrices() {
		// Given
		Map<Pair<RoomRank, RoomSize>, BigDecimal> newPrices = new HashMap<>();

		newPrices.put(new Pair<>(RoomRank.STANDARD, RoomSize.SINGLE), BigDecimal.valueOf(100));
		newPrices.put(new Pair<>(RoomRank.STANDARD, RoomSize.DOUBLE), BigDecimal.valueOf(150));
		newPrices.put(new Pair<>(RoomRank.STANDARD, RoomSize.FAMILY), BigDecimal.valueOf(200));

		newPrices.put(new Pair<>(RoomRank.DELUXE, RoomSize.SINGLE), BigDecimal.valueOf(200));
		newPrices.put(new Pair<>(RoomRank.DELUXE, RoomSize.DOUBLE), BigDecimal.valueOf(300));
		newPrices.put(new Pair<>(RoomRank.DELUXE, RoomSize.FAMILY), BigDecimal.valueOf(400));

		roomPricePerNight = new RoomPricePerNight(testPrices);
		// When
		roomPricePerNight.setPrices(newPrices);

		// Then
		assertEquals(roomPricePerNight.getPrices(), newPrices);
	}

	private static void changeJSONPath()
			throws ReflectiveOperationException {

		Field field = JSONFilePath.class.getDeclaredField("PATH");
		field.setAccessible(true);
		field.set(null, "../assets/jsons/%s.json");
	}
}
