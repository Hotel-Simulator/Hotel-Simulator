package pl.agh.edu.generator.client_generator;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.RoomSize;
import pl.agh.edu.enums.Sex;
import pl.agh.edu.json.data_loader.JSONClientDataLoader;
import pl.agh.edu.json.data_loader.JSONHotelDataLoader;
import pl.agh.edu.model.advertisement.AdvertisementHandler;
import pl.agh.edu.model.advertisement.report.AdvertisementReportData;
import pl.agh.edu.model.advertisement.report.AdvertisementReportHandler;
import pl.agh.edu.model.client.Client;
import pl.agh.edu.model.client.ClientGroup;
import pl.agh.edu.model.event.temporary.ClientNumberModificationTemporaryEventHandler;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.utils.RandomUtils;

public class ClientGenerator {

	private static ClientGenerator clientGeneratorInstance;

	private static final Map<String, Long> attractivenessConstants = JSONHotelDataLoader.attractivenessConstants;
	private final AdvertisementHandler advertisementHandler = AdvertisementHandler.getInstance();
	private final ClientNumberModificationTemporaryEventHandler clientNumberModificationTemporaryEventHandler = ClientNumberModificationTemporaryEventHandler.getInstance();
	private final Time time = Time.getInstance();

	private ClientGenerator() {}

	public static ClientGenerator getInstance() {
		if (clientGeneratorInstance == null)
			clientGeneratorInstance = new ClientGenerator();
		return clientGeneratorInstance;
	}

	private LocalDateTime getCheckOutTime(int numberOfNight, LocalTime checkOutMaxTime) {

		return LocalDateTime.of(time.getTime().toLocalDate().plusDays(numberOfNight), RandomUtils.randomLocalTime(LocalTime.of(6, 0), checkOutMaxTime));
	}

	private BigDecimal getDesiredPricePerNight(RoomRank desiredRoomRank, RoomSize roomSize) {
		double meanPrice = JSONClientDataLoader.averagePricesPerNight.get(desiredRoomRank).get(roomSize).doubleValue();
		double variation = 0.2 * meanPrice;

		return BigDecimal.valueOf(Math.round(RandomUtils.randomGaussian(meanPrice, variation)));
	}

	private List<Client> getMembers(HotelVisitPurpose hotelVisitPurpose, int clientNumber) {
		return IntStream.range(0, clientNumber)
				.mapToObj(it -> new Client(
						RandomUtils.randomInt(1, 99),
						RandomUtils.randomEnumElement(Sex.class),
						hotelVisitPurpose))
				.collect(Collectors.toList());
	}

	private ClientGroup generateClientGroupForGivenHotelVisitPurpose(LocalTime checkoutMaxTime, HotelVisitPurpose hotelVisitPurpose) {
		RoomRank desiredRoomRank = RandomUtils.randomKeyWithProbabilities(JSONClientDataLoader.desiredRankProbabilities.get(hotelVisitPurpose));
		int numberOfNights = RandomUtils.randomKeyWithProbabilities(JSONClientDataLoader.numberOfNightsProbabilities.get(hotelVisitPurpose));
		int clientGroupSize = RandomUtils.randomKeyWithProbabilities(JSONClientDataLoader.clientGroupSizeProbabilities.get(hotelVisitPurpose));
		return new ClientGroup.Builder()
				.hotelVisitPurpose(hotelVisitPurpose)
				.members(getMembers(hotelVisitPurpose, clientGroupSize))
				.checkOutTime(getCheckOutTime(numberOfNights, checkoutMaxTime))
				.desiredPricePerNight(getDesiredPricePerNight(desiredRoomRank, RoomSize.getSmallestAvailableRoomSize(clientGroupSize)))
				.desiredRoomRank(desiredRoomRank)
				.maxWaitingTime(getMaxWaitingTime(JSONClientDataLoader.basicMaxWaitingTime, JSONClientDataLoader.waitingTimeVariation))
				.build();
	}

	private Duration getMaxWaitingTime(Duration basicMaxWaitingTime, int waitingTimeVariation) {
		return basicMaxWaitingTime.plusMinutes(RandomUtils.randomInt(-waitingTimeVariation, waitingTimeVariation));
	}

	private EnumMap<HotelVisitPurpose, Integer> getNumberOfClientGroupsFromAdvertisement(EnumMap<HotelVisitPurpose, Integer> noClientsWithoutAdvertisements) {
		return advertisementHandler.getCumulatedModifier().entrySet()
				.stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						e -> (int) Math.round(e.getValue() * noClientsWithoutAdvertisements.get(e.getKey())),
						(a, b) -> b,
						() -> new EnumMap<>(HotelVisitPurpose.class)));
	}

	private EnumMap<HotelVisitPurpose, Integer> getNumberOfClientGroups() {
		double popularityModifier = 0.1;
		int basicNumberOfClients = (int) Math.round(((attractivenessConstants.get("local_market") + attractivenessConstants.get("local_attractions"))) * popularityModifier);
		return Stream.of(HotelVisitPurpose.values()).collect(Collectors.toMap(
				e -> e,
				e -> (int) Math.round(
						basicNumberOfClients *
								JSONClientDataLoader.hotelVisitPurposeProbabilities.get(e) *
								Math.max(0, RandomUtils.randomGaussian(1, 1. / 3)) *
								(clientNumberModificationTemporaryEventHandler.getClientNumberModifier().get(e) + 1)),
				(a, b) -> b,
				() -> new EnumMap<>(HotelVisitPurpose.class)));
	}

	public List<Arrival> generateArrivalsForDay(LocalTime checkInMinTime, LocalTime checkOutMaxTime) {
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
								generateClientGroupForGivenHotelVisitPurpose(checkOutMaxTime, e))))
				.sorted(Arrival::compareTo)
				.collect(Collectors.toList());
	}
}
