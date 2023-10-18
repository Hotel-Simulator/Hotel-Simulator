package management;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.enums.HotelType;
import pl.agh.edu.management.hotel.HotelScenariosManager;

import java.math.BigDecimal;

public class HotelScenariosManagerTest {
	private static HotelScenariosManager hotelScenariosManager;

	@BeforeEach
	public void setUp() {
		hotelScenariosManager = new HotelScenariosManager(HotelType.HOTEL);
	}

	@Test
	public void testGetHotelVisitPurposeProbabilities() {
		// Then
		assertNotNull(hotelScenariosManager.hotelVisitPurposeProbabilities);
	}

	@Test
	public void testGetAttractivenessConstants() {
		// Then
		assertNotNull(hotelScenariosManager.attractivenessConstants);
	}

	@Test
	public void testGetCurrentDayMultiplier() {
		// When
		BigDecimal currentDayMultiplier = hotelScenariosManager.getCurrentDayMultiplier();

		// Then
		assertTrue(currentDayMultiplier.compareTo(BigDecimal.ZERO) >= 0 && currentDayMultiplier.compareTo(BigDecimal.valueOf(2)) <= 0);
	}
}
