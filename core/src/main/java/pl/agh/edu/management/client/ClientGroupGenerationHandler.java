package pl.agh.edu.management.client;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.generator.client_generator.ClientGenerator;
import pl.agh.edu.json.data.AttractivenessConstantsData;
import pl.agh.edu.management.advertisement.AdvertisementHandler;
import pl.agh.edu.management.bank.BankAccountHandler;
import pl.agh.edu.management.client.report.ClientGroupReportHandler;
import pl.agh.edu.management.event.ClientNumberModificationEventHandler;
import pl.agh.edu.management.hotel.HotelScenariosManager;
import pl.agh.edu.model.time.Time;
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

	public List<Arrival> getArrivalsForDay(LocalTime checkInMinTime, LocalTime checkOutMaxTime) {
		EnumMap<HotelVisitPurpose, Integer> numberOfClientGroups = getNumberOfClientGroups();
		ClientGroupReportHandler.collectData(numberOfClientGroups);
		return Stream.of(HotelVisitPurpose.values())
				.flatMap(e -> IntStream.range(0, numberOfClientGroups.get(e))
						.mapToObj(it -> new Arrival(
								RandomUtils.randomLocalTime(checkInMinTime, LocalTime.MAX),
								clientGenerator.generateClientGroupForGivenHotelVisitPurpose(checkOutMaxTime, e))))
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
