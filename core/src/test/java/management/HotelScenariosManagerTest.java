package management;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.agh.edu.engine.hotel.HotelType.HOTEL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.engine.hotel.scenario.HotelScenariosManager;

public class HotelScenariosManagerTest {
	private static HotelScenariosManager hotelScenariosManager;

	@BeforeEach
	public void setUp() {
		hotelScenariosManager = new HotelScenariosManager(HOTEL);
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
		double currentDayMultiplier = hotelScenariosManager.getCurrentDayMultiplier();

		// Then
		assertTrue(currentDayMultiplier >= 0.0 && currentDayMultiplier <= 2.0);
	}
}
