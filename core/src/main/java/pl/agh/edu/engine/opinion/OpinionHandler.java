package pl.agh.edu.engine.opinion;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_EVEN;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Function;

import pl.agh.edu.data.loader.JSONOpinionDataLoader;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.TimeCommand;
import pl.agh.edu.utils.RandomUtils;

public class OpinionHandler {
	private static BigDecimal opinionModifier = new BigDecimal("0.1");
	private static final List<Opinion> opinions = new ArrayList<>();
	private static final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();
	private static final Time time = Time.getInstance();

	public static void addOpinionWithProbability(Opinion opinion, double probability) {
		if (RandomUtils.randomBooleanWithProbability(probability)) {
			opinions.add(opinion);
			timeCommandExecutor.addCommand(new TimeCommand(
					() -> opinions.remove(opinion),
					time.getTime().plus(JSONOpinionDataLoader.opinionHoldingDuration)));
		}
	}

	private static OptionalDouble getAvgRating() {
		return opinions.stream()
				.mapToDouble(opinion -> opinion.getStars().value)
				.average();
	}

	private static Optional<BigDecimal> mapRating(OptionalDouble optionalRating) {
		return optionalRating.stream()
				.map(rating -> mappingFunction().apply(rating))
				.mapToObj(BigDecimal::valueOf)
				.map(rating -> rating.setScale(4, HALF_EVEN)
						.max(new BigDecimal("0.01"))
						.min(ONE)).findAny();
	}

	private static Function<Double, Double> mappingFunction() {
		return x -> Math.pow(x, 4) / 625;
	}

	private static BigDecimal getDailyChangeValue() {
		Optional<BigDecimal> optionalTargetOpinionModifier = mapRating(getAvgRating());
		return optionalTargetOpinionModifier.map(bigDecimal -> bigDecimal.subtract(opinionModifier)
				.multiply(JSONOpinionDataLoader.opinionChangeMultiplier)
				.setScale(4, HALF_EVEN)).orElse(ZERO);
	}

	public static void dailyUpdate() {
		opinionModifier = opinionModifier.add(getDailyChangeValue());
	}

	public static BigDecimal getOpinionModifier() {
		return opinionModifier;
	}
}
