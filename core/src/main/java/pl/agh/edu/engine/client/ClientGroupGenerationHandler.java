package pl.agh.edu.engine.client;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import pl.agh.edu.data.type.AttractivenessConstantsData;
import pl.agh.edu.engine.advertisement.AdvertisementHandler;
import pl.agh.edu.engine.bank.BankAccountHandler;
import pl.agh.edu.engine.client.report.collector.ClientGroupReportDataCollector;
import pl.agh.edu.engine.event.ClientNumberModificationEventHandler;
import pl.agh.edu.engine.generator.ClientGenerator;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;
import pl.agh.edu.engine.hotel.scenario.HotelScenariosManager;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.utils.RandomUtils;

public class ClientGroupGenerationHandler {
	private final ClientGenerator clientGenerator = ClientGenerator.getInstance();
	private final Time time = Time.getInstance();

	private final ClientNumberModificationEventHandler clientNumberModificationEventHandler = ClientNumberModificationEventHandler.getInstance();
	private final AdvertisementHandler advertisementHandler;
	private final HotelScenariosManager hotelScenariosManager;

	public ClientGroupGenerationHandler(HotelScenariosManager hotelScenariosManager,
			BankAccountHandler bankAccountHandler) {
		this.advertisementHandler = new AdvertisementHandler(bankAccountHandler);
		this.hotelScenariosManager = hotelScenariosManager;
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
		EnumMap<HotelVisitPurpose, BigDecimal> eventModifier = clientNumberModificationEventHandler.getCumulatedModifier();
		return Stream.of(HotelVisitPurpose.values()).collect(Collectors.toMap(
				hotelVisitPurpose -> hotelVisitPurpose,
				hotelVisitPurpose -> (int) Math.round(
						getBasicNumberOfClientGroups() *
								hotelScenariosManager.hotelVisitPurposeProbabilities.get(hotelVisitPurpose) *
								Math.max(0, RandomUtils.randomGaussian(1, 1. / 3)) *
								BigDecimal.ONE.add(eventModifier.get(hotelVisitPurpose)).doubleValue() *
								BigDecimal.ONE.add(advertisementHandler.getCumulatedModifier().get(hotelVisitPurpose)).doubleValue()),
				(a, b) -> b,
				() -> new EnumMap<>(HotelVisitPurpose.class)));
	}

	private double getBasicNumberOfClientGroups() {
		double popularityModifier = hotelScenariosManager.getCurrentDayMultiplier();
		AttractivenessConstantsData attractivenessConstants = hotelScenariosManager.attractivenessConstants;
		return (attractivenessConstants.localMarket() + attractivenessConstants.localAttractions()) * popularityModifier;
	}
}
