package pl.agh.edu.engine.opinion;

import java.util.Arrays;

public enum OpinionStars {
	ZERO(0.),
	HALF(0.5),
	ONE(1.),
	ONE_AND_HALF(1.5),
	TWO(2.),
	TWO_AND_HALF(2.5),
	THREE(3.),
	THREE_AND_HALF(3.5),
	FOUR(4.),
	FOUR_AND_HALF(4.5),
	FIVE(5.);

	public final double value;

	OpinionStars(double value) {
		this.value = value;
	}

	public static OpinionStars get(double value) {
		double scaledValue = value * 5;
		double roundedValue = Math.round(scaledValue * 2.0) / 2.0;
		return Arrays.stream(OpinionStars.values())
				.filter(opinionStars -> opinionStars.value == roundedValue)
				.findAny()
				.orElse(ZERO);
	}
}
