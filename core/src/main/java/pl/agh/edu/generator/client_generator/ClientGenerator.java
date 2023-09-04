package pl.agh.edu.generator.client_generator;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import pl.agh.edu.enums.*;
import pl.agh.edu.json.data_loader.JSONClientDataLoader;
import pl.agh.edu.json.data_loader.JSONHotelDataLoader;
import pl.agh.edu.json.data_loader.JSONHotelScenariosDataLoader;
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
	private final AdvertisementHandler advertisementHandler;
	private final ClientNumberModificationTemporaryEventHandler clientNumberModificationTemporaryEventHandler;
	private final Time time;
	private EnumMap<HotelVisitPurpose, Double> hotelVisitPurposeProbabilities;
	private Map<Integer, Double> monthClientsMultiplier;
	private double difficultyMultiplier;

	private ClientGenerator() {

		this.time = Time.getInstance();

		advertisementHandler = AdvertisementHandler.getInstance();
		clientNumberModificationTemporaryEventHandler = ClientNumberModificationTemporaryEventHandler.getInstance();
	}

	public static ClientGenerator getInstance() {
		if (clientGeneratorInstance == null)
			clientGeneratorInstance = new ClientGenerator();

		// Input player choice
		clientGeneratorInstance.setHotelTypeDifficulty(HotelType.values()[RandomUtils.randomInt(HotelType.values().length)]);
		clientGeneratorInstance.setDifficulty(DifficultyLevel.values()[RandomUtils.randomInt(DifficultyLevel.values().length)]);
		return clientGeneratorInstance;
	}

	private LocalDateTime getCheckOutTime(int numberOfNight, LocalTime checkOutMaxTime) {

		return LocalDateTime.of(time.getTime().toLocalDate().plusDays(numberOfNight), RandomUtils.randomLocalTime(LocalTime.of(6, 0), checkOutMaxTime));
	}

	private BigDecimal getDesiredPricePerNight(RoomRank desiredRoomRank, RoomCapacity roomCapacity) {
		double meanPrice = JSONClientDataLoader.averagePricesPerNight.get(desiredRoomRank).get(roomCapacity).doubleValue() * difficultyMultiplier;
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
		RoomCapacity roomCapacity = RandomUtils.randomKeyWithProbabilities(JSONClientDataLoader.roomCapacityProbabilities.get(hotelVisitPurpose));
		return new ClientGroup.Builder()
				.hotelVisitPurpose(hotelVisitPurpose)
				.members(getMembers(hotelVisitPurpose, roomCapacity.value))
				.checkOutTime(getCheckOutTime(numberOfNights, checkoutMaxTime))
				.desiredPricePerNight(getDesiredPricePerNight(desiredRoomRank, roomCapacity))
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
		double popularityModifier = seasonClientsMultiplier();
		int basicNumberOfClients = (int) Math.round(((attractivenessConstants.get("local_market") + attractivenessConstants.get("local_attractions"))) * popularityModifier);
		return Stream.of(HotelVisitPurpose.values()).collect(Collectors.toMap(
				e -> e,
				e -> (int) Math.round(
						basicNumberOfClients *
								hotelVisitPurposeProbabilities.get(e) *
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

	private void setHotelTypeDifficulty(HotelType hotelType) {

		switch (hotelType) {
			case HOTEL -> {
				hotelVisitPurposeProbabilities = JSONHotelScenariosDataLoader.businessVisitsMode;
				monthClientsMultiplier = JSONHotelScenariosDataLoader.vacationPopularity.get(HotelType.HOTEL);
			}
			case RESORT -> {
				hotelVisitPurposeProbabilities = JSONHotelScenariosDataLoader.vacationVisitsMode;
				monthClientsMultiplier = JSONHotelScenariosDataLoader.vacationPopularity.get(HotelType.RESORT);
			}
			case SANATORIUM -> {
				hotelVisitPurposeProbabilities = JSONHotelScenariosDataLoader.rehabilitationVisitsMode;
				monthClientsMultiplier = JSONHotelScenariosDataLoader.vacationPopularity.get(HotelType.SANATORIUM);
			}
		}
	}

	private void setDifficulty(DifficultyLevel difficulty) {
		difficultyMultiplier = JSONHotelScenariosDataLoader.difficultyMultiplier.get(difficulty);
	}

	private double seasonClientsMultiplier() {
		double mean = monthClientsMultiplier.get(time.getTime().getMonthValue());
		double var = mean * 0.2;

		return RandomUtils.randomGaussian(mean, var);
	}
}
