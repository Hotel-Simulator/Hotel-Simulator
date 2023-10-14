package pl.agh.edu.management.client;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import pl.agh.edu.enums.HotelType;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.generator.client_generator.ClientGenerator;
import pl.agh.edu.json.data_loader.JSONHotelDataLoader;
import pl.agh.edu.management.advertisement.AdvertisementHandler;
import pl.agh.edu.management.bank.BankAccountHandler;
import pl.agh.edu.management.hotel.HotelScenariosManager;
import pl.agh.edu.model.advertisement.report.AdvertisementReportData;
import pl.agh.edu.model.advertisement.report.AdvertisementReportHandler;
import pl.agh.edu.model.event.temporary.ClientNumberModificationTemporaryEventHandler;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.utils.RandomUtils;

public class ClientGroupGenerationHandler {
	private final ClientGenerator clientGenerator = ClientGenerator.getInstance();
	private final Time time = Time.getInstance();

	private final AdvertisementHandler advertisementHandler;
	private final ClientNumberModificationTemporaryEventHandler clientNumberModificationTemporaryEventHandler = ClientNumberModificationTemporaryEventHandler.getInstance();
	private final HotelScenariosManager hotelScenariosManager = new HotelScenariosManager(HotelType.HOTEL);

	public ClientGroupGenerationHandler(BankAccountHandler bankAccountHandler) {
		this.advertisementHandler = new AdvertisementHandler(bankAccountHandler);
	}

	public List<Arrival> getArrivalsForDay(LocalTime checkInMinTime, LocalTime checkOutMaxTime) {
		EnumMap<HotelVisitPurpose, Integer> numberOfClientGroups = getNumberOfClientGroups();
		EnumMap<HotelVisitPurpose, Integer> numberOfClientGroupsFromAdvertisements = getNumberOfClientGroupsFromAdvertisement(numberOfClientGroups);
		AdvertisementReportHandler.collectData(
				new AdvertisementReportData(
						time.getTime().toLocalDate(),
						numberOfClientGroups,
						numberOfClientGroupsFromAdvertisements));
		return Stream.of(HotelVisitPurpose.values())
				.flatMap(e -> IntStream.range(0, numberOfClientGroups.get(e) + numberOfClientGroupsFromAdvertisements.get(e))
						.mapToObj(it -> new Arrival(
								RandomUtils.randomLocalTime(checkInMinTime, LocalTime.MAX),
								clientGenerator.generateClientGroupForGivenHotelVisitPurpose(checkOutMaxTime, e))))
				.sorted(Arrival::compareTo)
				.collect(Collectors.toList());
	}

	private EnumMap<HotelVisitPurpose, Integer> getNumberOfClientGroupsFromAdvertisement(EnumMap<HotelVisitPurpose, Integer> noClientsWithoutAdvertisements) {
		return advertisementHandler.getCumulatedModifier().entrySet()
				.stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						e -> e.getValue().multiply(BigDecimal.valueOf(noClientsWithoutAdvertisements.get(e.getKey())))
								.setScale(0, RoundingMode.HALF_EVEN)
								.intValue(),
						(a, b) -> b,
						() -> new EnumMap<>(HotelVisitPurpose.class)));
	}

	private EnumMap<HotelVisitPurpose, Integer> getNumberOfClientGroups() {
		double popularityModifier = hotelScenariosManager.getCurrentDayMultiplier();
		int basicNumberOfClients = (int) Math.round(((JSONHotelDataLoader.attractivenessConstants.get("local_market") + JSONHotelDataLoader.attractivenessConstants.get(
				"local_attractions"))) * popularityModifier);
		return Stream.of(HotelVisitPurpose.values()).collect(Collectors.toMap(
				e -> e,
				e -> (int) Math.round(
						basicNumberOfClients *
								hotelScenariosManager.getHotelVisitPurposeProbabilities().get(e) *
								Math.max(0, RandomUtils.randomGaussian(1, 1. / 3)) *
								(clientNumberModificationTemporaryEventHandler.getClientNumberModifier().get(e) + 1)),
				(a, b) -> b,
				() -> new EnumMap<>(HotelVisitPurpose.class)));
	}

}
