package pl.agh.edu.engine.client;

import static java.math.BigDecimal.ONE;
import static java.math.RoundingMode.HALF_EVEN;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import pl.agh.edu.data.type.AttractivenessConstantsData;
import pl.agh.edu.engine.advertisement.AdvertisementHandler;
import pl.agh.edu.engine.attraction.AttractionHandler;
import pl.agh.edu.engine.bank.BankAccountHandler;
import pl.agh.edu.engine.client.report.collector.ClientGroupReportDataCollector;
import pl.agh.edu.engine.event.temporary.ClientNumberModificationEventHandler;
import pl.agh.edu.engine.generator.ClientGenerator;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;
import pl.agh.edu.engine.hotel.dificulty.GameDifficultyManager;
import pl.agh.edu.engine.hotel.scenario.HotelScenariosManager;
import pl.agh.edu.engine.opinion.OpinionHandler;
import pl.agh.edu.utils.RandomUtils;

public class ClientGroupGenerationHandler {
	private final ClientGenerator clientGenerator = ClientGenerator.getInstance();
	private final ClientNumberModificationEventHandler clientNumberModificationEventHandler = ClientNumberModificationEventHandler.getInstance();
	private final AdvertisementHandler advertisementHandler;
	private final AttractionHandler attractionHandler;
	private final HotelScenariosManager hotelScenariosManager;

	public ClientGroupGenerationHandler(
			HotelScenariosManager hotelScenariosManager,
			BankAccountHandler bankAccountHandler,
			AttractionHandler attractionHandler) {
		this.advertisementHandler = new AdvertisementHandler(bankAccountHandler);
		this.hotelScenariosManager = hotelScenariosManager;
		this.attractionHandler = attractionHandler;
	}

	public List<Arrival> getArrivalsForDay(LocalTime checkInMinTime) {
		EnumMap<HotelVisitPurpose, Integer> numberOfClientGroups = getNumberOfClientGroups();
		ClientGroupReportDataCollector.collectData(numberOfClientGroups);
		return Stream.of(HotelVisitPurpose.values())
				.flatMap(e -> IntStream.range(0, numberOfClientGroups.get(e))
						.mapToObj(it -> new Arrival(
								RandomUtils.randomLocalTime(checkInMinTime, LocalTime.MIDNIGHT.minusHours(1)),
								clientGenerator.generateClientGroupForGivenHotelVisitPurpose(e))))
				.sorted(Arrival::compareTo)
				.collect(Collectors.toList());
	}

	private EnumMap<HotelVisitPurpose, Integer> getNumberOfClientGroups() {
		BigDecimal numberOfClientGroups = basicNumberOfClientGroups()
				.multiply(randomMultiplier())
				.multiply(OpinionHandler.getOpinionModifier());

		return Stream.of(HotelVisitPurpose.values())
				.collect(Collectors.toMap(
						hotelVisitPurpose -> hotelVisitPurpose,
						hotelVisitPurpose -> getNumberOfClientGroupsByHotelVisitPurpose(numberOfClientGroups, hotelVisitPurpose),
						(a, b) -> b,
						() -> new EnumMap<>(HotelVisitPurpose.class)));
	}

	private Integer getNumberOfClientGroupsByHotelVisitPurpose(BigDecimal numberOfClientGroups,
			HotelVisitPurpose hotelVisitPurpose) {
		return numberOfClientGroups
				.multiply(hotelVisitPurposeMultiplier(hotelVisitPurpose))
				.multiply(multiplier(clientNumberModificationEventHandler, hotelVisitPurpose))
				.multiply(multiplier(advertisementHandler, hotelVisitPurpose))
				.multiply(multiplier(attractionHandler, hotelVisitPurpose))
				.setScale(0, HALF_EVEN)
				.intValue();
	}

	private BigDecimal basicNumberOfClientGroups() {
		AttractivenessConstantsData attractivenessConstants = hotelScenariosManager.attractivenessConstants;
		return BigDecimal.valueOf(attractivenessConstants.localMarket() + attractivenessConstants.localAttractions())
				.multiply(hotelScenariosManager.getCurrentDayMultiplier());
	}

	private BigDecimal hotelVisitPurposeMultiplier(HotelVisitPurpose hotelVisitPurpose) {
		return hotelScenariosManager.hotelVisitPurposeProbabilities.get(hotelVisitPurpose);
	}

	private BigDecimal randomMultiplier() {
		return BigDecimal.valueOf(Math.max(0, RandomUtils.randomGaussian(1, 1. / 3)));
	}

	private BigDecimal multiplier(ClientGroupModifierSupplier supplier, HotelVisitPurpose hotelVisitPurpose) {
		return ONE.add(supplier.getCumulatedModifier().get(hotelVisitPurpose));
	}

}
