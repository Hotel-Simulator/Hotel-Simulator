package pl.agh.edu.engine.attraction;

import static java.math.BigDecimal.ZERO;
import static java.time.temporal.ChronoUnit.DAYS;
import static pl.agh.edu.engine.attraction.AttractionState.ACTIVE;
import static pl.agh.edu.engine.attraction.AttractionState.CHANGING_SIZE;
import static pl.agh.edu.engine.attraction.AttractionState.INACTIVE;
import static pl.agh.edu.engine.attraction.AttractionState.OPENING;
import static pl.agh.edu.engine.attraction.AttractionState.SHUTTING_DOWN;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Optional;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.EnumMapSerializer;

import pl.agh.edu.data.loader.JSONAttractionDataLoader;
import pl.agh.edu.engine.bank.BankAccountHandler;
import pl.agh.edu.engine.building_cost.BuildingCostSupplier;
import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.client.ClientGroupModifierSupplier;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;
import pl.agh.edu.engine.room.RoomManager;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.TimeCommand;
import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.Pair;
import pl.agh.edu.utils.RandomUtils;

public class AttractionHandler extends ClientGroupModifierSupplier {
	private final Time time;
	private final TimeCommandExecutor timeCommandExecutor;
	private final BankAccountHandler accountHandler;
	private final RoomManager roomManager;
	private final BuildingCostSupplier buildingCostSupplier;
	private final EnumMap<AttractionType, Attraction> attractions;
	private final EnumMap<AttractionType, Pair<AttractionSize, LocalDateTime>> attractionBuildingTimes;
	private final EnumMap<AttractionType, Pair<AttractionSize, LocalDateTime>> attractionChangingSizeTimes;

	public static void kryoRegister() {
		KryoConfig.kryo.register(AttractionHandler.class, new Serializer<AttractionHandler>() {
			@Override
			public void write(Kryo kryo, Output output, AttractionHandler object) {
				kryo.writeObject(output, object.time);
				kryo.writeObject(output, object.timeCommandExecutor);
				kryo.writeObject(output, object.accountHandler);
				kryo.writeObject(output, object.roomManager);
				kryo.writeObject(output, object.buildingCostSupplier);
				kryo.writeObject(output, object.attractions, new EnumMapSerializer(AttractionType.class));
				kryo.writeObject(output, object.attractionBuildingTimes, new EnumMapSerializer(AttractionType.class));
				kryo.writeObject(output, object.attractionChangingSizeTimes, new EnumMapSerializer(AttractionType.class));
			}

			@Override
			public AttractionHandler read(Kryo kryo, Input input, Class<? extends AttractionHandler> type) {
				return new AttractionHandler(
						kryo.readObject(input, Time.class),
						kryo.readObject(input, TimeCommandExecutor.class),
						kryo.readObject(input, BankAccountHandler.class),
						kryo.readObject(input, RoomManager.class),
						kryo.readObject(input, BuildingCostSupplier.class),
						kryo.readObject(input, EnumMap.class, new EnumMapSerializer(AttractionType.class)),
						kryo.readObject(input, EnumMap.class, new EnumMapSerializer(AttractionType.class)),
						kryo.readObject(input, EnumMap.class, new EnumMapSerializer(AttractionType.class)));
			}
		});
	}

	public AttractionHandler(BankAccountHandler accountHandler,
			RoomManager roomManager,
			BuildingCostSupplier buildingCostSupplier) {
		this.time = Time.getInstance();
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
		this.accountHandler = accountHandler;
		this.roomManager = roomManager;
		this.buildingCostSupplier = buildingCostSupplier;
		this.attractions = new EnumMap<>(AttractionType.class);
		this.attractionBuildingTimes = new EnumMap<>(AttractionType.class);
		this.attractionChangingSizeTimes = new EnumMap<>(AttractionType.class);
	}

	private AttractionHandler(Time time,
			TimeCommandExecutor timeCommandExecutor,
			BankAccountHandler accountHandler,
			RoomManager roomManager,
			BuildingCostSupplier buildingCostSupplier,
			EnumMap<AttractionType, Attraction> attractions,
			EnumMap<AttractionType, Pair<AttractionSize, LocalDateTime>> attractionBuildingTimes,
			EnumMap<AttractionType, Pair<AttractionSize, LocalDateTime>> attractionChangingSizeTimes) {
		this.time = time;
		this.timeCommandExecutor = timeCommandExecutor;
		this.accountHandler = accountHandler;
		this.roomManager = roomManager;
		this.buildingCostSupplier = buildingCostSupplier;
		this.attractions = attractions;
		this.attractionBuildingTimes = attractionBuildingTimes;
		this.attractionChangingSizeTimes = attractionChangingSizeTimes;
	}

	private boolean hasAttraction(AttractionType type) {
		return attractions.containsKey(type);
	}

	public boolean canBuild(AttractionType type, AttractionSize size) {
		if (hasAttraction(type)) {
			return false;
		}
		return accountHandler.hasOperationAbility(buildingCostSupplier.attractionBuildingCost(size));
	}

	public void build(AttractionType type, AttractionSize size) {
		Attraction attraction = new Attraction(type, size);

		attractions.put(type, attraction);
		accountHandler.registerExpense(buildingCostSupplier.attractionBuildingCost(size));

		LocalDateTime buildTime = time.getTime().truncatedTo(DAYS)
				.plus(JSONAttractionDataLoader.buildDuration.get(attraction.getSize()));
		attractionBuildingTimes.put(type, Pair.of(size, buildTime));

		timeCommandExecutor.addCommand(new TimeCommand(() -> {
			attraction.setState(INACTIVE);
			attractionBuildingTimes.remove(type);
		}, buildTime));
	}

	public boolean canChangeSize(AttractionType type, AttractionSize size) {
		if (!hasAttraction(type) || attractions.get(type).getSize() == size || attractions.get(type).getState() != INACTIVE) {
			return false;
		}
		BigDecimal cost = buildingCostSupplier.attractionBuildingCost(size)
				.subtract(buildingCostSupplier.attractionBuildingCost(attractions.get(type).getSize()));
		if (cost.compareTo(ZERO) < 0) {
			return true;
		}
		return accountHandler.hasOperationAbility(buildingCostSupplier.attractionBuildingCost(size));
	}

	public void changeSize(AttractionType type, AttractionSize size) {
		Attraction attraction = attractions.get(type);

		BigDecimal cost = buildingCostSupplier.attractionBuildingCost(size)
				.subtract(buildingCostSupplier.attractionBuildingCost(attraction.getSize()));
		if (cost.compareTo(ZERO) > 0) {
			accountHandler.registerExpense(cost);
		} else {
			accountHandler.registerIncome(cost.multiply(new BigDecimal("0.5")).negate());
		}

		attraction.setState(CHANGING_SIZE);

		LocalDateTime changeSizeTime = time.getTime().truncatedTo(DAYS).plus(
				JSONAttractionDataLoader.buildDuration.get(attraction.getSize())
						.minus(JSONAttractionDataLoader.buildDuration.get(size))
						.abs());
		attractionChangingSizeTimes.put(type, Pair.of(size, changeSizeTime));

		timeCommandExecutor.addCommand(new TimeCommand(
				() -> {
					attraction.setSize(size);
					attraction.setState(INACTIVE);
					attractionChangingSizeTimes.remove(type);
				}, changeSizeTime));
	}

	private boolean clientGroupVisitAttraction(ClientGroup clientGroup, Attraction attraction) {
		Pair<AttractionType, HotelVisitPurpose> pair = Pair.of(attraction.type, clientGroup.getHotelVisitPurpose());
		return RandomUtils.randomBooleanWithProbability(JSONAttractionDataLoader.chancesOfVisit.get(pair));
	}

	private int getPossibleDailyClientNumber(Attraction attraction) {
		return roomManager.getResidents().stream()
				.filter(clientGroup -> clientGroupVisitAttraction(clientGroup, attraction))
				.mapToInt(clientGroup -> clientGroup.getMembers().size())
				.sum();
	}

	private int getDailyClientNumber(Attraction attraction) {
		return Math.min(getPossibleDailyClientNumber(attraction), attraction.getDailyCapacity());
	}

	public void dailyUpdate() {
		attractions.values().stream()
				.filter(Attraction::isWorking)
				.forEach(attraction -> {
					accountHandler.registerExpense(attraction.getDailyExpenses());
					accountHandler.registerIncome(JSONAttractionDataLoader.incomePerClient
							.multiply(BigDecimal.valueOf(getDailyClientNumber(attraction))));
				});

	}

	@Override
	public EnumMap<HotelVisitPurpose, BigDecimal> getCumulatedModifier() {
		return attractions.values().stream()
				.filter(Attraction::isWorking)
				.map(attraction -> JSONAttractionDataLoader.modifier.get(Pair.of(attraction.type, attraction.getSize())))
				.reduce(getIdentity(), getAccumulator());
	}

	public void activateAttraction(Attraction attraction) {
		switch (attraction.getState()) {
			case SHUTTING_DOWN -> attraction.setState(ACTIVE);
			case INACTIVE -> {
				attraction.setState(OPENING);
				timeCommandExecutor.addCommand(new TimeCommand(() -> attraction.setState(ACTIVE), time.getTime().truncatedTo(DAYS).plusDays(1)));
			}
		}
	}

	public void deactivateAttraction(Attraction attraction) {
		switch (attraction.getState()) {
			case OPENING -> attraction.setState(INACTIVE);
			case ACTIVE -> {
				attraction.setState(SHUTTING_DOWN);
				timeCommandExecutor.addCommand(new TimeCommand(() -> attraction.setState(INACTIVE), time.getTime().truncatedTo(DAYS).plusDays(1)));
			}
		}
	}

	public Optional<Pair<AttractionSize, Duration>> findBuildingTime(AttractionType type) {
		return Optional.ofNullable(attractionBuildingTimes.get(type)).map(pair -> Pair.of(pair.first(), Duration.between(time.getTime(), pair.second())));
	}

	public Optional<Pair<AttractionSize, Duration>> findChangingSizeTime(AttractionType type) {
		return Optional.ofNullable(attractionChangingSizeTimes.get(type)).map(pair -> Pair.of(pair.first(), Duration.between(time.getTime(), pair.second())));
	}
}
