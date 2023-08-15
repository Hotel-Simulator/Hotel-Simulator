package pl.agh.edu.generator.client_generator;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.Sex;
import pl.agh.edu.json.data_loader.JSONClientDataLoader;
import pl.agh.edu.json.data_loader.JSONHotelDataLoader;
import pl.agh.edu.model.Client;
import pl.agh.edu.model.ClientGroup;
import pl.agh.edu.model.advertisement.AdvertisementHandler;
import pl.agh.edu.model.advertisement.report.AdvertisementReportData;
import pl.agh.edu.model.advertisement.report.AdvertisementReportHandler;
import pl.agh.edu.model.event.temporary.ClientNumberModificationTemporaryEventHandler;
import pl.agh.edu.model.time.Time;

public class ClientGenerator {

	private static ClientGenerator clientGeneratorInstance;

    private final Random random = new Random();
    private static final Map<String, Long> attractivenessConstants = JSONHotelDataLoader.attractivenessConstants;
    private static final EnumMap<HotelVisitPurpose,Double> hotelVisitPurposeProbability = JSONClientDataLoader.hotelVisitPurposeProbabilities;
    private static final EnumMap<HotelVisitPurpose, List<Integer>> roomSizeProbabilityLists = ProbabilityListGenerator.getMapOfProbabilityLists(JSONClientDataLoader.roomSizeProbabilities,HotelVisitPurpose.class);
    private static final EnumMap<HotelVisitPurpose, List<RoomRank>> desiredRoomRankProbabilityLists = ProbabilityListGenerator.getEnumMapOfProbabilityLists(JSONClientDataLoader.desiredRankProbabilities,HotelVisitPurpose.class);
    private static final EnumMap<HotelVisitPurpose, List<Integer>> numberOfNightsProbabilityLists = ProbabilityListGenerator.getMapOfProbabilityLists(JSONClientDataLoader.numberOfNightsProbabilities,HotelVisitPurpose.class);
    private static final EnumMap<RoomRank, Map<Integer, BigDecimal>> averagePricesPerNight = JSONClientDataLoader.averagePricesPerNight;
    private final AdvertisementHandler advertisementHandler;
    private final ClientNumberModificationTemporaryEventHandler clientNumberModificationTemporaryEventHandler;
    private final Time time;

	private ClientGenerator() {

		this.time = Time.getInstance();

		advertisementHandler = AdvertisementHandler.getInstance();
		clientNumberModificationTemporaryEventHandler = ClientNumberModificationTemporaryEventHandler.getInstance();
	}

	public static ClientGenerator getInstance() {
		if (clientGeneratorInstance == null)
			clientGeneratorInstance = new ClientGenerator();
		return clientGeneratorInstance;
	}

	private <T> T getRandomValue(List<T> list) {
		return list.get(random.nextInt(0, list.size()));
	}

	private LocalTime getRandomLocalTime(LocalTime min, LocalTime max) {

		int randomTimeInMinutes = random.nextInt(min.getHour() * 60 + min.getMinute(), max.getHour() * 60 + max.getMinute()) / time.getTimeUnitInMinutes() * time
				.getTimeUnitInMinutes();
		return LocalTime.of(randomTimeInMinutes / 60, randomTimeInMinutes % 60);
	}

	private LocalDateTime getCheckOutTime(int numberOfNight, LocalTime checkOutMaxTime) {

		return LocalDateTime.of(time.getTime().toLocalDate().plusDays(numberOfNight), getRandomLocalTime(LocalTime.of(6, 0), checkOutMaxTime));
	}


    private BigDecimal getDesiredPricePerNight(RoomRank desiredRoomRank, int roomSize) {
        return BigDecimal.valueOf(averagePricesPerNight.get(desiredRoomRank).get(roomSize).intValue() +
                (int) Math.round(
                        random.nextGaussian() *
                                0.2 * averagePricesPerNight.get(desiredRoomRank).get(roomSize).intValue()
                )
        );
    }

	private List<Client> getMembers(HotelVisitPurpose hotelVisitPurpose, int roomSize) {
		return IntStream.range(0, roomSize)
				.mapToObj(it -> new Client(
						random.nextInt(1, 99),
						Sex.values()[random.nextInt(0, 3)],
						hotelVisitPurpose))
				.collect(Collectors.toList());
	}


    private ClientGroup generateClientGroupForGivenHotelVisitPurpose(LocalTime checkoutMaxTime,HotelVisitPurpose hotelVisitPurpose){
        RoomRank desiredRoomRank  = getRandomValue(desiredRoomRankProbabilityLists.get(hotelVisitPurpose));
        int numberOfNight = getRandomValue(numberOfNightsProbabilityLists.get(hotelVisitPurpose));
        int roomSize = getRandomValue(roomSizeProbabilityLists.get(hotelVisitPurpose));
        return new ClientGroup.Builder()
                .hotelVisitPurpose(hotelVisitPurpose)
                .members(getMembers(hotelVisitPurpose, roomSize))
                .checkOutTime(getCheckOutTime(numberOfNight,checkoutMaxTime))
                .desiredPricePerNight(getDesiredPricePerNight(desiredRoomRank, roomSize))
                .desiredRoomRank(desiredRoomRank)
                .maxWaitingTime(getMaxWaitingTime(JSONClientDataLoader.basicMaxWaitingTime, JSONClientDataLoader.waitingTimeVariation))
                .build();
    }

    private Duration getMaxWaitingTime(Duration basicMaxWaitingTime, int waitingTimeVariation) {
        return basicMaxWaitingTime.plusMinutes(random.nextInt(-waitingTimeVariation,waitingTimeVariation));
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
				e -> (int) Math.round(basicNumberOfClients * hotelVisitPurposeProbability.get(e) * Math.abs(1 + random.nextGaussian() / 3)
						* (clientNumberModificationTemporaryEventHandler.getClientNumberModifier().get(e) + 1)),
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
								getRandomLocalTime(checkInMinTime, LocalTime.MAX),
								generateClientGroupForGivenHotelVisitPurpose(checkOutMaxTime, e))))
				.sorted(Arrival::compareTo)
				.collect(Collectors.toList());
	}
}
