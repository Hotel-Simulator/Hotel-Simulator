package model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.EnumMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.RoomSize;
import pl.agh.edu.model.RoomPriceList;

public class RoomPriceListTest {
	private static EnumMap<RoomRank, EnumMap<RoomSize, BigDecimal>> testPrices;
	private static RoomPriceList roomPriceList;

	@BeforeEach
	void setUp() {
		testPrices = new EnumMap<>(RoomRank.class);

		EnumMap<RoomSize, BigDecimal> standardPrices = new EnumMap<>(RoomSize.class);
		standardPrices.put(RoomSize.SINGLE, BigDecimal.valueOf(100));
		standardPrices.put(RoomSize.DOUBLE, BigDecimal.valueOf(150));
		standardPrices.put(RoomSize.FAMILY, BigDecimal.valueOf(200));

		EnumMap<RoomSize, BigDecimal> deluxePrices = new EnumMap<>(RoomSize.class);
		deluxePrices.put(RoomSize.SINGLE, BigDecimal.valueOf(200));
		deluxePrices.put(RoomSize.DOUBLE, BigDecimal.valueOf(300));
		deluxePrices.put(RoomSize.FAMILY, BigDecimal.valueOf(400));

		testPrices.put(RoomRank.STANDARD, standardPrices);
		testPrices.put(RoomRank.DELUXE, deluxePrices);

		roomPriceList = new RoomPriceList(testPrices);
	}

	@Test
	public void getPrices() {
		// Given
		// When
		EnumMap<RoomRank, EnumMap<RoomSize, BigDecimal>> prices = roomPriceList.getPrices();

		// Then
		assertNotNull(prices);
		assertEquals(testPrices, prices);
	}

	@Test
	public void getPrice() {
		// Given
		// When
		BigDecimal price = roomPriceList.getPrice(RoomRank.STANDARD, RoomSize.SINGLE);

		// Then
		assertEquals(BigDecimal.valueOf(100), price);
	}

	@Test
	public void setPrice() {
		// Given

		// When
		roomPriceList.setPrice(RoomRank.DELUXE, RoomSize.DOUBLE, BigDecimal.valueOf(350));
		BigDecimal price = roomPriceList.getPrice(RoomRank.DELUXE, RoomSize.DOUBLE);

		// Then
		assertEquals(BigDecimal.valueOf(350), price);
	}

	@Test
	public void setPrices() {
		// Given
		EnumMap<RoomRank, EnumMap<RoomSize, BigDecimal>> newPrices = new EnumMap<>(RoomRank.class);

		EnumMap<RoomSize, BigDecimal> newStandardPrices = new EnumMap<>(RoomSize.class);
		newStandardPrices.put(RoomSize.SINGLE, BigDecimal.valueOf(120));
		newStandardPrices.put(RoomSize.DOUBLE, BigDecimal.valueOf(180));
		newStandardPrices.put(RoomSize.FAMILY, BigDecimal.valueOf(240));

		EnumMap<RoomSize, BigDecimal> newDeluxePrices = new EnumMap<>(RoomSize.class);
		newDeluxePrices.put(RoomSize.SINGLE, BigDecimal.valueOf(220));
		newDeluxePrices.put(RoomSize.DOUBLE, BigDecimal.valueOf(330));
		newDeluxePrices.put(RoomSize.FAMILY, BigDecimal.valueOf(440));
		// When
		newPrices.put(RoomRank.STANDARD, newStandardPrices);
		newPrices.put(RoomRank.DELUXE, newDeluxePrices);

		roomPriceList.setPrices(newPrices);

		// Then
		assertEquals(roomPriceList.getPrices(), newPrices);
	}
}
