package pl.agh.edu.engine.generator;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.javafaker.Faker;

import pl.agh.edu.data.loader.JSONClientDataLoader;
import pl.agh.edu.data.loader.JSONOpinionDataLoader;
import pl.agh.edu.engine.client.Client;
import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.client.Gender;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;
import pl.agh.edu.engine.hotel.dificulty.GameDifficultyManager;
import pl.agh.edu.engine.opinion.OpinionHandler;
import pl.agh.edu.engine.room.RoomRank;
import pl.agh.edu.engine.room.RoomSize;
import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.Pair;
import pl.agh.edu.utils.RandomUtils;

public class ClientGenerator {
	private final GameDifficultyManager gameDifficultyManager;
	private final OpinionHandler opinionHandler;
	private static final Faker faker = new Faker(new Locale("en-GB"));

	public static void kryoRegister() {
		KryoConfig.kryo.register(ClientGenerator.class, new Serializer<ClientGenerator>() {
			@Override
			public void write(Kryo kryo, Output output, ClientGenerator object) {
				kryo.writeObject(output, object.gameDifficultyManager);
				kryo.writeObject(output, object.opinionHandler);
			}

			@Override
			public ClientGenerator read(Kryo kryo, Input input, Class<? extends ClientGenerator> type) {
				return new ClientGenerator(
						kryo.readObject(input, GameDifficultyManager.class),
						kryo.readObject(input, OpinionHandler.class));
			}
		});
	}

	public ClientGenerator(GameDifficultyManager gameDifficultyManager, OpinionHandler opinionHandler) {
		this.gameDifficultyManager = gameDifficultyManager;
		this.opinionHandler = opinionHandler;
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
		double opinionModifier = (1. + JSONOpinionDataLoader.desiredPriceModifier * opinionHandler.getOpinionModifier().doubleValue());
		double meanPrice = JSONClientDataLoader.averagePricesPerNight.get(Pair.of(desiredRoomRank, roomSize)).doubleValue()
				/ gameDifficultyManager.getDifficultyMultiplier()
				* opinionModifier;
		double variation = 0.2 * meanPrice;
		return BigDecimal.valueOf(Math.round(RandomUtils.randomGaussian(meanPrice, variation)));
	}

	private List<Client> getMembers(HotelVisitPurpose hotelVisitPurpose, int clientNumber) {
		return IntStream.range(0, clientNumber)
				.mapToObj(it -> new Client(
						faker.name().firstName(),
						RandomUtils.randomInt(1, 99),
						RandomUtils.randomEnumElement(Gender.class),
						hotelVisitPurpose))
				.collect(Collectors.toList());
	}

	private Duration getMaxWaitingTime(Duration basicMaxWaitingTime, int waitingTimeVariation) {
		Duration maxWaitingTime = basicMaxWaitingTime.plusMinutes(RandomUtils.randomInt(-waitingTimeVariation, waitingTimeVariation));
		Duration opinionBonus = Duration.ofMinutes((long) (maxWaitingTime.toMinutes()
				* opinionHandler.getOpinionModifier().doubleValue() * JSONOpinionDataLoader.maxWaitingTimeModifier));
		return maxWaitingTime.plus(opinionBonus);
	}
}
