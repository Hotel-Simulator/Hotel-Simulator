package pl.agh.edu.engine.opinion;

import java.util.Arrays;

import pl.agh.edu.serialization.KryoConfig;

public enum OpinionStars {

	ONE(1.),
	TWO(2.),
	THREE(3.),
	FOUR(4.),
	FIVE(5.);

	static {
		KryoConfig.kryo.register(OpinionStars.class);
	}

	public final double value;

	OpinionStars(double value) {
		this.value = value;
	}

	public static OpinionStars get(double value) {
		double roundedValue = Math.ceil(5.0 * value);
		return Arrays.stream(OpinionStars.values())
				.filter(opinionStars -> opinionStars.value == roundedValue)
				.findAny()
				.orElse(ONE);
	}
}
