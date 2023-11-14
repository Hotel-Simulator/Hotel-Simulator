package management;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.agh.edu.engine.hotel.HotelType.CITY;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.engine.hotel.scenario.HotelScenariosManager;

public class HotelScenariosManagerTest {
	private static HotelScenariosManager hotelScenariosManager;

	@BeforeEach
	public void setUp() {
		hotelScenariosManager = new HotelScenariosManager(CITY);
	}

	@Test
	public void testGetHotelVisitPurposeProbabilities() {
		// Then
		assertNotNull(hotelScenariosManager.getHotelVisitPurposeProbabilities());
	}

	@Test
	public void testGetAttractivenessConstants() {
		// Then
		assertNotNull(hotelScenariosManager.getAttractivenessConstants());
	}

	@Test
	public void testGetCurrentDayMultiplier() {
		// When
		BigDecimal currentDayMultiplier = hotelScenariosManager.getCurrentDayMultiplier();

		// Then
		assertTrue(currentDayMultiplier.compareTo(BigDecimal.ZERO) >= 0 && currentDayMultiplier.compareTo(BigDecimal.valueOf(2)) <= 0);
	}
}
