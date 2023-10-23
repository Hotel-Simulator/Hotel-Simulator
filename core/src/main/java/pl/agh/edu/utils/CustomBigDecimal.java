package pl.agh.edu.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;

import ch.obermuhlner.math.big.BigDecimalMath;

public class CustomBigDecimal {
	private static final MathContext MC = MathContext.DECIMAL128;
	private final BigDecimal value;
	private final Prefix prefix;

	public CustomBigDecimal(String value) {
		this(new BigDecimal(value));
	}

	public CustomBigDecimal(BigDecimal value) {
		this.prefix = Prefix.getAdequatePrefixByValue(value);
		this.value = roundValue(value, prefix);
	}

	public static BigDecimal getMaxValue(BigDecimal value) {
		return value.min(Prefix.EXCESS.value.subtract(BigDecimal.ONE));
	}

	public static BigDecimal getMinValue(BigDecimal value) {
		return value.max(BigDecimal.ZERO);
	}

	@Override
	public String toString() {
		return value.toPlainString() + prefix.code;
	}

	private BigDecimal roundValue(BigDecimal value, Prefix prefix) {
		if (prefix == Prefix.n) {
			return roundToWholeNumber(value);
		}

		BigDecimal valueWithOffsetPrecision = adjustPrecision(value, prefix);
		int scale = determineScale(valueWithOffsetPrecision);

		return scaleValueWithRounding(valueWithOffsetPrecision, scale);
	}

	private BigDecimal roundToWholeNumber(BigDecimal value) {
		return value.setScale(0, RoundingMode.FLOOR);
	}

	private BigDecimal adjustPrecision(BigDecimal value, Prefix prefix) {
		BigDecimal precisionAdjustment = BigDecimalMath.log10(prefix.value, MC).subtract(new BigDecimal(3));
		return value.movePointLeft(precisionAdjustment.intValue()).setScale(0, RoundingMode.FLOOR);
	}

	private int determineScale(BigDecimal value) {
		int scale = 5 - BigDecimalMath.log10(value, MC).intValue();
		return Math.max(scale, 0);
	}

	private BigDecimal scaleValueWithRounding(BigDecimal value, int scale) {
		return value.movePointLeft(3).setScale(scale, RoundingMode.FLOOR);
	}

	public BigDecimal getValue() {
		return value.multiply(prefix.value);
	}

	public enum Prefix {
		n(new BigDecimal("1"), ""),
		k(new BigDecimal("1E3"), "k"),
		M(new BigDecimal("1E6"), "M"),
		B(new BigDecimal("1E9"), "B"),
		T(new BigDecimal("1E12"), "T"),
		Q(new BigDecimal("1E15"), "Q"),
		EXCESS(new BigDecimal("1E18"), "ERROR");

		public final BigDecimal value;
		public final String code;

		Prefix(BigDecimal value, String code) {
			this.code = code;
			this.value = value;
		}

		public static Prefix getAdequatePrefixByValue(BigDecimal value) {
			return Arrays.stream(Prefix.values())
					.sorted()
					.takeWhile(prefix -> value.compareTo(prefix.value) >= 0)
					.max(Comparator.naturalOrder())
					.orElse(Prefix.n);
		}
	}
}
