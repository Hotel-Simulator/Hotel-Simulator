package management;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.enums.HotelType;
import pl.agh.edu.management.hotel.HotelScenariosManager;

public class HotelScenariosManagerTest {
	private static HotelScenariosManager hotelScenariosManager;

	@BeforeEach
	public void setUp() {
		hotelScenariosManager = new HotelScenariosManager(HotelType.HOTEL);
	}

	@Test
	public void testGetHotelVisitPurposeProbabilities() {
		// Then
		assertNotNull(hotelScenariosManager.getHotelVisitPurposeProbabilities());
	}

	@Test
	public void testGetCurrentDayMultiplier() {
		// When
		double currentDayMultiplier = hotelScenariosManager.getCurrentDayMultiplier();

		// Then
		assertTrue(currentDayMultiplier >= 0.0 && currentDayMultiplier <= 2.0);
	}

	@Test
	public void testHotelSetUp() {
		// Then
		assertNotNull(hotelScenariosManager.getHotelVisitPurposeProbabilities());
	}

	@Test
	public void testHotelScenariosManagerInitialization() {
		// Then
		assertNotNull(hotelScenariosManager.getHotelVisitPurposeProbabilities());
	}
}
