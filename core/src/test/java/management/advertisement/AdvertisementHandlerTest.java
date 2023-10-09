package management.advertisement;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.management.advertisement.AdvertisementHandler;
import pl.agh.edu.model.advertisement.AdvertisementType;
import pl.agh.edu.model.time.Time;

public class AdvertisementHandlerTest {

	private AdvertisementHandler advertisementHandler;
	private final Time time = Time.getInstance();

	@BeforeEach
	void setUp() {
		advertisementHandler = new AdvertisementHandler();
	}

	@ParameterizedTest
	@MethodSource("canBuyAdvertisementCampaignArgs")
	void canBuyAdvertisementCampaign(LocalDate startDate, LocalDate endDate, boolean expected) {
		// Given
		AdvertisementType type = AdvertisementType.TV_ADVERTISEMENT;

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
			assertEquals(BigDecimal.ZERO, value);
		}
		assertTrue(modifierMap.values().stream().allMatch(value -> value.compareTo(BigDecimal.ZERO) == 0));
	}

	@Test
	void getCumulatedModifier_WhenActiveCampaignsExist() {
		// Given
		LocalDate currentDate = time.getTime().toLocalDate();
		// When
		advertisementHandler.buyAdvertisementCampaign(AdvertisementType.RADIO_ADVERTISEMENT, currentDate, currentDate.plusDays(7));
		advertisementHandler.buyAdvertisementCampaign(AdvertisementType.TV_ADVERTISEMENT, currentDate, currentDate.plusDays(7));
		// should not be used in calculating cumulated modifier because of not being emitted
		advertisementHandler.buyAdvertisementCampaign(AdvertisementType.NEWSPAPER_ADVERTISEMENT, currentDate.plusDays(1), currentDate.plusDays(7));

		EnumMap<HotelVisitPurpose, BigDecimal> modifierMap = advertisementHandler.getCumulatedModifier();

		// Then
		assertEquals(new BigDecimal("0.1425"), modifierMap.get(HotelVisitPurpose.VACATION));
		assertEquals(new BigDecimal("0.0825"), modifierMap.get(HotelVisitPurpose.BUSINESS_TRIP));
		assertEquals(new BigDecimal("0.2100"), modifierMap.get(HotelVisitPurpose.REHABILITATION));
	}

	@Test
	void getFilteredAdvertisementCampaigns_ShouldReturnOnlyCurrentCampaigns() {
		// Given
		// When
		LocalDate currentDate = time.getTime().toLocalDate();
		// current
		advertisementHandler.buyAdvertisementCampaign(AdvertisementType.RADIO_ADVERTISEMENT, currentDate, currentDate.plusDays(7));
		// future
		advertisementHandler.buyAdvertisementCampaign(AdvertisementType.TV_ADVERTISEMENT, currentDate.plusDays(2), currentDate.plusDays(7));

		var currentCampaigns = advertisementHandler.getFilteredAdvertisementCampaigns(true, false, null);

		// Then
		assertEquals(1, currentCampaigns.size());
		assertEquals(AdvertisementType.RADIO_ADVERTISEMENT, currentCampaigns.get(0).advertisementData().type());
	}

	@Test
	void getFilteredAdvertisementCampaigns_ShouldReturnOnlyFutureCampaigns() {
		// Given
		// When
		LocalDate currentDate = time.getTime().toLocalDate();
		// current
		advertisementHandler.buyAdvertisementCampaign(AdvertisementType.RADIO_ADVERTISEMENT, currentDate, currentDate.plusDays(7));
		// future
		advertisementHandler.buyAdvertisementCampaign(AdvertisementType.TV_ADVERTISEMENT, currentDate.plusDays(2), currentDate.plusDays(7));

		var currentCampaigns = advertisementHandler.getFilteredAdvertisementCampaigns(false, true, null);

		// Then
		assertEquals(1, currentCampaigns.size());
		assertEquals(AdvertisementType.TV_ADVERTISEMENT, currentCampaigns.get(0).advertisementData().type());
	}

	@Test
	void getFilteredAdvertisementCampaigns_ShouldReturnCampaignsWithSpecificType() {
		// Given
		// When
		LocalDate currentDate = time.getTime().toLocalDate();
		// current
		advertisementHandler.buyAdvertisementCampaign(AdvertisementType.RADIO_ADVERTISEMENT, currentDate, currentDate.plusDays(7));
		// future
		advertisementHandler.buyAdvertisementCampaign(AdvertisementType.TV_ADVERTISEMENT, currentDate.plusDays(2), currentDate.plusDays(7));

		var currentCampaigns = advertisementHandler.getFilteredAdvertisementCampaigns(true, true, AdvertisementType.RADIO_ADVERTISEMENT);

		// Then
		assertEquals(1, currentCampaigns.size());
		assertEquals(AdvertisementType.RADIO_ADVERTISEMENT, currentCampaigns.get(0).advertisementData().type());
	}
}
