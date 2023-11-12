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
import java.util.stream.Collectors;

import pl.agh.edu.data.loader.JSONOpinionDataLoader;
import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.SerializableRunnable;
import pl.agh.edu.engine.time.command.TimeCommand;
import pl.agh.edu.utils.LanguageString;
import pl.agh.edu.utils.RandomUtils;

public class OpinionHandler {
	private static BigDecimal opinionModifier = new BigDecimal("0.1");
	private static final List<OpinionData> opinions = new ArrayList<>();
	private static final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();
	private static final Time time = Time.getInstance();

	public static void addOpinionWithProbability(ClientGroup clientGroup, double probability) {
		if (RandomUtils.randomBooleanWithProbability(probability)) {
			OpinionData opinionData = new OpinionData(
					RandomUtils.randomListElement(clientGroup.getMembers()).name(),
					time.getTime().toLocalDate(),
					clientGroup.opinion.getStars(),
					clientGroup.opinion.getComment().stream().map(LanguageString::new).collect(Collectors.toSet()));
			opinions.add(opinionData);
			timeCommandExecutor.addCommand(new TimeCommand(
					(SerializableRunnable) () -> opinions.remove(opinionData),
					time.getTime().plus(JSONOpinionDataLoader.opinionHoldingDuration)));
		}
	}

	private static OptionalDouble getAvgRating() {
		return opinions.stream()
				.mapToDouble(opinion -> opinion.stars().value)
				.average();
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
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
