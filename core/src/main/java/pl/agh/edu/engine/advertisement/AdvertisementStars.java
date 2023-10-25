package pl.agh.edu.engine.advertisement;

import static java.math.RoundingMode.HALF_EVEN;

import java.math.BigDecimal;

import pl.agh.edu.data.loader.JSONAdvertisementDataLoader;

public enum AdvertisementStars {
	ONE,
	TWO,
	THREE,
	FOUR,
	FIVE;

	private static final BigDecimal advertisementMultiplier = JSONAdvertisementDataLoader.multiplier;

	public static AdvertisementStars getStars(BigDecimal modifier) {
		int index = hasModifierMaxValue(modifier) ? 4 : getIndex(modifier);
		return AdvertisementStars.values()[index];
	}

	private static boolean hasModifierMaxValue(BigDecimal modifier) {
		return modifier.divide(advertisementMultiplier, HALF_EVEN).compareTo(BigDecimal.valueOf(100)) >= 0;
	}

	private static int getIndex(BigDecimal modifier) {
		return modifier.divide(advertisementMultiplier, HALF_EVEN)
				.divide(BigDecimal.valueOf(20), HALF_EVEN)
				.intValue();
	}
}
