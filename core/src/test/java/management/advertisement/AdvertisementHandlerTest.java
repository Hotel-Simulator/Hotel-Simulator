package management.advertisement;

import static java.math.BigDecimal.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static pl.agh.edu.engine.advertisement.AdvertisementType.NEWSPAPER_ADVERTISEMENT;
import static pl.agh.edu.engine.advertisement.AdvertisementType.RADIO_ADVERTISEMENT;
import static pl.agh.edu.engine.advertisement.AdvertisementType.TV_ADVERTISEMENT;
import static pl.agh.edu.engine.advertisement.AdvertisementType.WEB_PAGE;
import static pl.agh.edu.engine.hotel.HotelVisitPurpose.BUSINESS_TRIP;
import static pl.agh.edu.engine.hotel.HotelVisitPurpose.REHABILITATION;
import static pl.agh.edu.engine.hotel.HotelVisitPurpose.VACATION;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.agh.edu.data.loader.JSONAdvertisementDataLoader;
import pl.agh.edu.engine.advertisement.AdvertisementHandler;
import pl.agh.edu.engine.advertisement.AdvertisementType;
import pl.agh.edu.engine.bank.BankAccountHandler;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;
import pl.agh.edu.engine.time.Time;

public class AdvertisementHandlerTest {

	private AdvertisementHandler advertisementHandler;
	private final Time time = Time.getInstance();

	@BeforeEach
	void setUp() {
		advertisementHandler = new AdvertisementHandler(mock(BankAccountHandler.class));
	}

	@ParameterizedTest
	@MethodSource("canBuyAdvertisementCampaignArgs")
	void canBuyAdvertisementCampaign(LocalDate startDate, LocalDate endDate, boolean expected) {
		// Given
		AdvertisementType type = TV_ADVERTISEMENT;

		// When
		advertisementHandler.buyAdvertisementCampaign(type, LocalDate.now(), LocalDate.now().plusDays(7));
		boolean actual = advertisementHandler.canBuyAdvertisementCampaign(type, startDate, endDate);

		// Then
		assertEquals(expected, actual);
	}

	static Stream<Arguments> canBuyAdvertisementCampaignArgs() {
		return Stream.of(
				// Arguments: startDate, endDate, expected result
				Arguments.of(LocalDate.now().minusDays(7), LocalDate.now(), true),
				Arguments.of(LocalDate.now().minusDays(3), LocalDate.now().plusDays(3), false),
				Arguments.of(LocalDate.now().plusDays(5), LocalDate.now().plusDays(8), false),
				Arguments.of(LocalDate.now().plusDays(7), LocalDate.now().plusDays(14), true));
	}

	@Test
	void getCumulatedModifier_WhenNoActiveCampaigns() {
		// Given
		// When
		EnumMap<HotelVisitPurpose, BigDecimal> modifierMap = advertisementHandler.getCumulatedModifier();

		// Then
		for (BigDecimal value : modifierMap.values()) {
			assertEquals(ZERO, value);
		}
		assertTrue(modifierMap.values().stream().allMatch(value -> value.compareTo(ZERO) == 0));
	}

	@Test
	void getCumulatedModifier_WhenActiveCampaignsExist() {
		// Given
		LocalDate currentDate = time.getTime().toLocalDate();
		// When
		advertisementHandler.buyAdvertisementCampaign(RADIO_ADVERTISEMENT, currentDate, currentDate.plusDays(7));
		advertisementHandler.buyAdvertisementCampaign(TV_ADVERTISEMENT, currentDate, currentDate.plusDays(7));
		// should not be used in calculating cumulated modifier because of not being emitted
		advertisementHandler.buyAdvertisementCampaign(NEWSPAPER_ADVERTISEMENT, currentDate.plusDays(1), currentDate.plusDays(7));

		EnumMap<HotelVisitPurpose, BigDecimal> modifierMap = advertisementHandler.getCumulatedModifier();

		// Then
		assertEquals(new BigDecimal("0.1425"), modifierMap.get(VACATION));
		assertEquals(new BigDecimal("0.0825"), modifierMap.get(BUSINESS_TRIP));
		assertEquals(new BigDecimal("0.2100"), modifierMap.get(REHABILITATION));
	}

	@Test
	void getFilteredAdvertisementCampaigns_ShouldReturnOnlyCurrentCampaigns() {
		// Given
		// When
		LocalDate currentDate = time.getTime().toLocalDate();
		// current
		advertisementHandler.buyAdvertisementCampaign(RADIO_ADVERTISEMENT, currentDate, currentDate.plusDays(7));
		// future
		advertisementHandler.buyAdvertisementCampaign(TV_ADVERTISEMENT, currentDate.plusDays(2), currentDate.plusDays(7));

		var currentCampaigns = advertisementHandler.getFilteredAdvertisementCampaigns(true, false, null);

		// Then
		assertEquals(1, currentCampaigns.size());
		assertEquals(RADIO_ADVERTISEMENT, currentCampaigns.get(0).advertisementData().type());
	}

	@Test
	void getFilteredAdvertisementCampaigns_ShouldReturnOnlyFutureCampaigns() {
		// Given
		// When
		LocalDate currentDate = time.getTime().toLocalDate();
		// current
		advertisementHandler.buyAdvertisementCampaign(RADIO_ADVERTISEMENT, currentDate, currentDate.plusDays(7));
		// future
		advertisementHandler.buyAdvertisementCampaign(TV_ADVERTISEMENT, currentDate.plusDays(2), currentDate.plusDays(7));

		var currentCampaigns = advertisementHandler.getFilteredAdvertisementCampaigns(false, true, null);

		// Then
		assertEquals(1, currentCampaigns.size());
		assertEquals(TV_ADVERTISEMENT, currentCampaigns.get(0).advertisementData().type());
	}

	@Test
	void getFilteredAdvertisementCampaigns_ShouldReturnCampaignsWithSpecificType() {
		// Given
		// When
		LocalDate currentDate = time.getTime().toLocalDate();
		// current
		advertisementHandler.buyAdvertisementCampaign(RADIO_ADVERTISEMENT, currentDate, currentDate.plusDays(7));
		// future
		advertisementHandler.buyAdvertisementCampaign(TV_ADVERTISEMENT, currentDate.plusDays(2), currentDate.plusDays(7));

		var currentCampaigns = advertisementHandler.getFilteredAdvertisementCampaigns(true, true, RADIO_ADVERTISEMENT);

		// Then
		assertEquals(1, currentCampaigns.size());
		assertEquals(RADIO_ADVERTISEMENT, currentCampaigns.get(0).advertisementData().type());
	}

	static Stream<Arguments> getCampaignFullCostArgs() {
		return Stream.of(
				Arguments.of(13, BigDecimal.ONE),
				Arguments.of(14, new BigDecimal("0.9")),
				Arguments.of(27, new BigDecimal("0.9")),
				Arguments.of(28, new BigDecimal("0.8"))

		);
	}

	@ParameterizedTest
	@MethodSource("getCampaignFullCostArgs")
	void getCampaignFullCost_ShouldCalculateCostWithout(long noDays, BigDecimal multiplier) {
		// Given
		BigDecimal expectedCost = JSONAdvertisementDataLoader.advertisementData.get(WEB_PAGE).costPerDay()
				.multiply(BigDecimal.valueOf(noDays))
				.multiply(multiplier);
		// When
		BigDecimal actualCost = AdvertisementHandler.getCampaignFullCost(WEB_PAGE, noDays);

		// Then
		assertEquals(expectedCost, actualCost);
	}

}
