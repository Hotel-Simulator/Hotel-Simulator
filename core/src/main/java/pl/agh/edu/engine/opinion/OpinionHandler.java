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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.data.loader.JSONOpinionDataLoader;
import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.TimeCommand;
import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.LanguageString;
import pl.agh.edu.utils.RandomUtils;

public class OpinionHandler {
	private final Time time;
	private final TimeCommandExecutor timeCommandExecutor;
	private final List<OpinionData> opinions;
	private BigDecimal opinionModifier;

	private static OpinionHandler instance;

	static {
		KryoConfig.kryo.register(OpinionHandler.class, new Serializer<OpinionHandler>() {
			@Override
			public void write(Kryo kryo, Output output, OpinionHandler object) {
				kryo.writeObject(output, object.time);
				kryo.writeObject(output, object.timeCommandExecutor);
				kryo.writeObject(output, object.opinions, KryoConfig.listSerializer(OpinionData.class));
				kryo.writeObject(output, object.opinionModifier);
			}

			@Override
			public OpinionHandler read(Kryo kryo, Input input, Class<? extends OpinionHandler> type) {
				return new OpinionHandler(
						kryo.readObject(input, Time.class),
						kryo.readObject(input, TimeCommandExecutor.class),
						kryo.readObject(input, List.class, KryoConfig.listSerializer(OpinionData.class)),
						kryo.readObject(input, BigDecimal.class));
			}
		});
	}

	private OpinionHandler() {
		this.time = Time.getInstance();
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
		this.opinions = new ArrayList<>();
		this.opinionModifier = new BigDecimal("0.1");
	}

	private OpinionHandler(Time time,
			TimeCommandExecutor timeCommandExecutor,
			List<OpinionData> opinions,
			BigDecimal opinionModifier) {
		this.time = time;
		this.timeCommandExecutor = timeCommandExecutor;
		this.opinions = opinions;
		this.opinionModifier = opinionModifier;
	}

	public static OpinionHandler getInstance() {
		if (instance == null) {
			instance = new OpinionHandler();
		}
		return instance;
	}

	public void addOpinionWithProbability(ClientGroup clientGroup, double probability) {
		if (RandomUtils.randomBooleanWithProbability(probability)) {
			OpinionData opinionData = new OpinionData(
					RandomUtils.randomListElement(clientGroup.getMembers()).name(),
					time.getTime().toLocalDate(),
					clientGroup.opinion.getStars(),
					clientGroup.opinion.getComment().stream().map(LanguageString::new).collect(Collectors.toSet()));
			opinions.add(opinionData);
			timeCommandExecutor.addCommand(new TimeCommand(
					() -> opinions.remove(opinionData),
					time.getTime().plus(JSONOpinionDataLoader.opinionHoldingDuration)));
		}
	}

	private OptionalDouble getAvgRating() {
		return opinions.stream()
				.mapToDouble(opinion -> opinion.stars().value)
				.average();
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	private Optional<BigDecimal> mapRating(OptionalDouble optionalRating) {
		return optionalRating.stream()
				.map(rating -> mappingFunction().apply(rating))
				.mapToObj(BigDecimal::valueOf)
				.map(rating -> rating.setScale(4, HALF_EVEN)
						.max(new BigDecimal("0.01"))
						.min(ONE)).findAny();
	}

	private Function<Double, Double> mappingFunction() {
		return x -> Math.pow(x, 4) / 625;
	}

	private BigDecimal getDailyChangeValue() {
		Optional<BigDecimal> optionalTargetOpinionModifier = mapRating(getAvgRating());
		return optionalTargetOpinionModifier.map(bigDecimal -> bigDecimal.subtract(opinionModifier)
				.multiply(JSONOpinionDataLoader.opinionChangeMultiplier)
				.setScale(4, HALF_EVEN)).orElse(ZERO);
	}

	public void dailyUpdate() {
		opinionModifier = opinionModifier.add(getDailyChangeValue());
	}

	public BigDecimal getOpinionModifier() {
		return opinionModifier;
	}
}
