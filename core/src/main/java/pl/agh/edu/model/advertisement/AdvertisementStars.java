package pl.agh.edu.model.advertisement;

import java.math.BigDecimal;
import java.math.RoundingMode;

import pl.agh.edu.json.data_loader.JSONAdvertisementDataLoader;

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
		return modifier.divide(advertisementMultiplier, RoundingMode.HALF_EVEN).compareTo(BigDecimal.valueOf(100)) >= 0;
	}

	private static int getIndex(BigDecimal modifier) {
		return modifier.divide(advertisementMultiplier, RoundingMode.HALF_EVEN)
				.divide(BigDecimal.valueOf(20), RoundingMode.HALF_EVEN)
				.intValue();
	}
}
