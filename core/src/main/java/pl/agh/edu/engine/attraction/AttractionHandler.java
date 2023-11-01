package pl.agh.edu.engine.attraction;

import static java.math.BigDecimal.ZERO;
import static java.time.temporal.ChronoUnit.DAYS;

import java.math.BigDecimal;
import java.util.EnumMap;

import pl.agh.edu.data.loader.JSONAttractionDataLoader;
import pl.agh.edu.engine.bank.BankAccountHandler;
import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.client.ClientGroupModifierSupplier;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;
import pl.agh.edu.engine.room.RoomManager;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.TimeCommand;
import pl.agh.edu.utils.Pair;
import pl.agh.edu.utils.RandomUtils;

public class AttractionHandler extends ClientGroupModifierSupplier {
	private final EnumMap<AttractionType, Attraction> attractions = new EnumMap<>(AttractionType.class);
	private final BankAccountHandler accountHandler;
	private final RoomManager roomManager;
	private final Time time = Time.getInstance();
	private final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();

	public AttractionHandler(BankAccountHandler accountHandler, RoomManager roomManager) {
		this.accountHandler = accountHandler;
		this.roomManager = roomManager;
	}

	private boolean hasAttraction(AttractionType type) {
		return attractions.containsKey(type);
	}

	public boolean canBuild(AttractionType type, AttractionSize size) {
		if (hasAttraction(type)) {
			return false;
		}
		return accountHandler.hasOperationAbility(JSONAttractionDataLoader.buildCost.get(size));
	}

	public void build(AttractionType type, AttractionSize size) {
		Attraction attraction = new Attraction(type, size);
		attractions.put(type, attraction);
		accountHandler.registerExpense(JSONAttractionDataLoader.buildCost.get(size));
		timeCommandExecutor.addCommand(new TimeCommand(
				() -> attraction.setBeingBuild(false),
				time.getTime()
						.truncatedTo(DAYS)
						.plus(JSONAttractionDataLoader.buildDuration.get(attraction.getSize()))));
	}

	public boolean canChangeSize(AttractionType type, AttractionSize size) {
		if (!hasAttraction(type) || attractions.get(type).getSize() == size) {
			return false;
		}
		BigDecimal cost = JSONAttractionDataLoader.buildCost.get(size)
				.subtract(JSONAttractionDataLoader.buildCost.get(attractions.get(type).getSize()));
		if (cost.compareTo(ZERO) < 0) {
			return true;
		}
		return accountHandler.hasOperationAbility(JSONAttractionDataLoader.buildCost.get(size));
	}

	public void changeSize(AttractionType type, AttractionSize size) {
		Attraction attraction = attractions.get(type);
		BigDecimal cost = JSONAttractionDataLoader.buildCost.get(size)
				.subtract(JSONAttractionDataLoader.buildCost.get(attraction.getSize()));
		if (cost.compareTo(ZERO) > 0) {
			accountHandler.registerExpense(cost);
		} else {
			accountHandler.registerIncome(cost.multiply(new BigDecimal("0.5")).negate());
		}
		attraction.setUnderSizeChange(true);
		timeCommandExecutor.addCommand(new TimeCommand(
				() -> attraction.setUnderSizeChange(false),
				time.getTime().truncatedTo(DAYS).plus(
						JSONAttractionDataLoader.buildDuration.get(attraction.getSize())
								.minus(JSONAttractionDataLoader.buildDuration.get(size))
								.abs())));
		attractions.get(type).setSize(size);

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
				.filter(attraction -> !attraction.isBeingBuild() && !attraction.isUnderSizeChange())
				.forEach(attraction -> {
					accountHandler.registerExpense(attraction.getDailyExpenses());
					accountHandler.registerIncome(JSONAttractionDataLoader.incomePerClient
							.multiply(BigDecimal.valueOf(getDailyClientNumber(attraction))));
				});

	}

	@Override
	public EnumMap<HotelVisitPurpose, BigDecimal> getCumulatedModifier() {
		return attractions.values().stream()
				.filter(attraction -> !attraction.isBeingBuild() && !attraction.isUnderSizeChange())
				.map(attraction -> JSONAttractionDataLoader.modifier.get(Pair.of(attraction.type, attraction.getSize())))
				.reduce(getIdentity(), getAccumulator());
	}
}
