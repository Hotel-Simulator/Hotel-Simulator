package pl.agh.edu.generator.client_generator;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.RoomSize;
import pl.agh.edu.enums.Sex;
import pl.agh.edu.json.data_loader.JSONClientDataLoader;
import pl.agh.edu.management.game.GameDifficultyManager;
import pl.agh.edu.model.client.Client;
import pl.agh.edu.model.client.ClientGroup;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.utils.Pair;
import pl.agh.edu.utils.RandomUtils;

public class ClientGenerator {

	private static ClientGenerator clientGeneratorInstance;
	private final Time time = Time.getInstance();
	// Set user input here (set hotelType)
	private final GameDifficultyManager gameDifficultyManager = GameDifficultyManager.getInstance();

	private ClientGenerator() {}

	public static ClientGenerator getInstance() {
		if (clientGeneratorInstance == null)
			clientGeneratorInstance = new ClientGenerator();
		return clientGeneratorInstance;
	}

	public ClientGroup generateClientGroupForGivenHotelVisitPurpose(HotelVisitPurpose hotelVisitPurpose) {
		RoomRank desiredRoomRank = RandomUtils.randomKeyWithProbabilities(JSONClientDataLoader.desiredRankProbabilities.get(hotelVisitPurpose));
		int numberOfNights = RandomUtils.randomKeyWithProbabilities(JSONClientDataLoader.numberOfNightsProbabilities.get(hotelVisitPurpose));
		int clientGroupSize = RandomUtils.randomKeyWithProbabilities(JSONClientDataLoader.clientGroupSizeProbabilities.get(hotelVisitPurpose));
		RoomSize roomSize = RoomSize.getSmallestAvailableRoomSize(clientGroupSize).orElseThrow();

		return new ClientGroup.Builder()
				.hotelVisitPurpose(hotelVisitPurpose)
				.members(getMembers(hotelVisitPurpose, clientGroupSize))
				.desiredPricePerNight(getDesiredPricePerNight(desiredRoomRank, roomSize))
				.desiredRoomRank(desiredRoomRank)
				.maxWaitingTime(getMaxWaitingTime(JSONClientDataLoader.basicMaxWaitingTime, JSONClientDataLoader.waitingTimeVariation))
				.numberOfNights(numberOfNights)
				.build();
	}

	private BigDecimal getDesiredPricePerNight(RoomRank desiredRoomRank, RoomSize roomSize) {
		double meanPrice = JSONClientDataLoader.averagePricesPerNight
				.get(Pair.of(desiredRoomRank, roomSize)).doubleValue() / gameDifficultyManager.getDifficultyMultiplier();
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

	private Duration getMaxWaitingTime(Duration basicMaxWaitingTime, int waitingTimeVariation) {
		return basicMaxWaitingTime.plusMinutes(RandomUtils.randomInt(-waitingTimeVariation, waitingTimeVariation));
	}
}
