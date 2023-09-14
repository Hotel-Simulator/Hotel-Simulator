package pl.agh.edu.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;

import ch.obermuhlner.math.big.BigDecimalMath;

public class CustomBigDecimal {
	private BigDecimal value;
	private final MathContext mc = MathContext.DECIMAL128;

	public CustomBigDecimal(String val) {
		this.value = new BigDecimal(val);
	}

	public CustomBigDecimal(BigDecimal bigDecimal) {
		this.value = bigDecimal;
	}

	public CustomBigDecimal(float val) {
		this.value = new BigDecimal(val, mc);
	}

	public CustomBigDecimal add(CustomBigDecimal other) {
		return new CustomBigDecimal(this.value.add(other.value));
	}

	@Override
	public String toString() { // 123 456 = 123k
		Prefix prefix = Prefix.getAdequatePrefixByValue(value);

		BigDecimal division = value.divide(prefix.value);
		String[] twoParts = division.toPlainString().split("\\.");
		String beforeComa = twoParts[0];
		String afterComa = "";
		if(twoParts.length>1)
			afterComa = twoParts[1];
		afterComa+="00";

		if(prefix.name().equals("n"))
			return beforeComa;
		if(beforeComa.length()>=3)
			return beforeComa + prefix;

		return beforeComa + "." + afterComa.substring(0,3 -beforeComa.length()) + prefix;

	}

	public CustomBigDecimal roundToStringValue() { // 123 456 = 123 000 = 123 k
		value = value.setScale(0, RoundingMode.HALF_UP);
		if (value.compareTo(Prefix.k.value) < 0) {
			return new CustomBigDecimal(value);
		}

		Prefix prefix = Prefix.getAdequatePrefixByValue(value);

		int correct;
		BigDecimal beforeComa = value.divideAndRemainder(prefix.value)[0].stripTrailingZeros();
		if (beforeComa.toString().length() >= 3) {
			correct = 0;
		} else if (beforeComa.toString().length() == 2) { // 10000 = 10.0k
			correct = 1;
		} else {
			correct = 2;
		}

		return new CustomBigDecimal(value.subtract(value.divideAndRemainder(prefix.value.divide(BigDecimalMath.pow(BigDecimal.TEN, BigDecimal.valueOf(correct), mc), mc))[1]));
	}

	public BigDecimal getValue() {
		return value;
	}

	public enum Prefix {
		n(new BigDecimal("1")),
		k(new BigDecimal("1000")),
		M(new BigDecimal("1000000")),
		B(new BigDecimal("1000000000")),
		T(new BigDecimal("1000000000000"));

		public final BigDecimal value;

		public static Prefix getAdequatePrefixByValue(BigDecimal value) {
			return Arrays.stream(Prefix.values()).filter(prefix -> value.compareTo(prefix.value) >= 0).max(Comparator.naturalOrder()).orElse(Prefix.values()[Prefix
					.values().length - 1]);
		}

		public static Prefix getPrefixByValue(BigDecimal value) {
			return Arrays.stream(Prefix.values()).filter(prefix -> value.equals(prefix.value)).findFirst().orElse(Prefix.values()[Prefix.values().length - 1]);
		}

		Prefix(BigDecimal value) {
			this.value = value;
		}

	}

}
