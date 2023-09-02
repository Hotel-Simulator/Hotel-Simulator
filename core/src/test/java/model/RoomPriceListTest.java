package model;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.EnumMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.enums.RoomCapacity;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.model.RoomPriceList;

public class RoomPriceListTest {
	private static EnumMap<RoomRank, EnumMap<RoomCapacity, BigDecimal>> testPrices;
	private static RoomPriceList roomPriceList;

	@BeforeAll
	static void setUpClass() throws ReflectiveOperationException {
		changeJSONPath();
	}

	@BeforeEach
	void setUp() {
		testPrices = new EnumMap<>(RoomRank.class);

		EnumMap<RoomCapacity, BigDecimal> standardPrices = new EnumMap<>(RoomCapacity.class);
		standardPrices.put(RoomCapacity.ONE, BigDecimal.valueOf(100));
		standardPrices.put(RoomCapacity.TWO, BigDecimal.valueOf(150));
		standardPrices.put(RoomCapacity.THREE, BigDecimal.valueOf(200));

		EnumMap<RoomCapacity, BigDecimal> deluxePrices = new EnumMap<>(RoomCapacity.class);
		deluxePrices.put(RoomCapacity.ONE, BigDecimal.valueOf(200));
		deluxePrices.put(RoomCapacity.TWO, BigDecimal.valueOf(300));
		deluxePrices.put(RoomCapacity.THREE, BigDecimal.valueOf(400));

		testPrices.put(RoomRank.ONE, standardPrices);
		testPrices.put(RoomRank.FIVE, deluxePrices);

		roomPriceList = new RoomPriceList(testPrices);
	}

	@Test
	public void getPrices() {
		// Given
		// When
		EnumMap<RoomRank, EnumMap<RoomCapacity, BigDecimal>> prices = roomPriceList.getPrices();

		// Then
		assertNotNull(prices);
		assertEquals(testPrices, prices);
	}

	@Test
	public void getPrice() {
		// Given
		// When
		BigDecimal price = roomPriceList.getPrice(RoomRank.ONE, RoomCapacity.ONE);

		// Then
		assertEquals(BigDecimal.valueOf(100), price);
	}

	@Test
	public void setPrice() {
		// Given

		// When
		roomPriceList.setPrice(RoomRank.FIVE, RoomCapacity.TWO, BigDecimal.valueOf(350));
		BigDecimal price = roomPriceList.getPrice(RoomRank.FIVE, RoomCapacity.TWO);

		// Then
		assertEquals(BigDecimal.valueOf(350), price);
	}

	@Test
	public void setPrices() {
		// Given
		EnumMap<RoomRank, EnumMap<RoomCapacity, BigDecimal>> newPrices = new EnumMap<>(RoomRank.class);

		EnumMap<RoomCapacity, BigDecimal> newStandardPrices = new EnumMap<>(RoomCapacity.class);
		newStandardPrices.put(RoomCapacity.ONE, BigDecimal.valueOf(120));
		newStandardPrices.put(RoomCapacity.TWO, BigDecimal.valueOf(180));
		newStandardPrices.put(RoomCapacity.THREE, BigDecimal.valueOf(240));

		EnumMap<RoomCapacity, BigDecimal> newDeluxePrices = new EnumMap<>(RoomCapacity.class);
		newDeluxePrices.put(RoomCapacity.ONE, BigDecimal.valueOf(220));
		newDeluxePrices.put(RoomCapacity.TWO, BigDecimal.valueOf(330));
		newDeluxePrices.put(RoomCapacity.THREE, BigDecimal.valueOf(440));
		// When
		newPrices.put(RoomRank.ONE, newStandardPrices);
		newPrices.put(RoomRank.FIVE, newDeluxePrices);

		roomPriceList.setPrices(newPrices);

		// Then
		assertEquals(roomPriceList.getPrices(), newPrices);
	}

	private static void changeJSONPath()
			throws ReflectiveOperationException {

		Field field = JSONFilePath.class.getDeclaredField("PATH");
		field.setAccessible(true);
		field.set(null, "../assets/jsons/%s.json");
	}
}
