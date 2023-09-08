package pl.agh.edu.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

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

	public CustomBigDecimal subtract(CustomBigDecimal other) {
		return new CustomBigDecimal(this.value.subtract(other.value));
	}

	public CustomBigDecimal multiply(CustomBigDecimal other) {
		return new CustomBigDecimal(this.value.multiply(other.value));
	}

	public CustomBigDecimal divide(CustomBigDecimal other) {
		if (other.value.equals(BigDecimal.ZERO)) {
			throw new ArithmeticException("Division by zero");
		}
		return new CustomBigDecimal(this.value.divide(other.value, mc));
	}

	public int compareTo(CustomBigDecimal other) {
		return this.value.compareTo(other.value);
	}

	public CustomBigDecimal pow(BigDecimal n) {
		return new CustomBigDecimal(BigDecimalMath.pow(value, n, mc));
	}

	public CustomBigDecimal log10() {
		return new CustomBigDecimal(BigDecimalMath.log10(this.value, mc));
	}

	@Override
	public String toString() { // 123 456 = 123k
		int zeros = BigDecimalMath.log10(this.value, mc).intValue();
		int triZeros = zeros / 3;
		Prefix prefix = Prefix.getPrefixByValue(BigDecimalMath.pow(BigDecimal.valueOf(1000), BigDecimal.valueOf(triZeros), mc));
		String number = value.toString().split("\\.")[0];
		int tmp = prefix.getValue().toString().length();
		String beforeComa = number.substring(0, number.length() - tmp + 1);
		String result = beforeComa;
		if (prefix.getValue().compareTo(BigDecimal.ONE) > 0) {
			String afterComa = number.substring(number.length() - tmp + 1, number.length());
			result += beforeComa.length() >= 3 ? "" : ("." + afterComa.substring(0, 3 - beforeComa.length()));
			result += prefix.name();
		}
		return result;
	}

	public CustomBigDecimal roundToStringValue() { // 123 456 = 123 000 = 123 k

		int zeros = BigDecimalMath.log10(this.value, mc).intValue();
		int triZeros = zeros / 3;
		Prefix prefix = Prefix.getPrefixByValue(BigDecimalMath.pow(BigDecimal.valueOf(1000), BigDecimal.valueOf(triZeros), mc));

		int correct;
		List<Prefix> prefixes = Arrays.stream(Prefix.values()).toList();
		Prefix biggest = prefixes.get(prefixes.size() - 1);
		if (value.compareTo(BigDecimal.valueOf(1000)) < 0 || BigDecimalMath.log10(biggest.getValue(), mc).compareTo(BigDecimal.valueOf(zeros - 2)) < 0) {
			correct = 0;
		} else if (zeros % 3 == 1) { // 10000 = 10.0k
			correct = 1;
		} else if (zeros % 3 == 2) { // 100000 = 100k
			correct = 0;
		} else {
			correct = 2;
		}

		value = value.subtract(value.divideAndRemainder(prefix.getValue().divide(BigDecimalMath.pow(BigDecimal.TEN, BigDecimal.valueOf(correct), mc), mc))[1]);
		return this;
	}

	public enum Prefix {
		n(BigDecimal.valueOf(1)),
		k(BigDecimal.valueOf(1000)),
		M(BigDecimal.valueOf(1000000)),
		B(BigDecimal.valueOf(1000000000)),
		T(new BigDecimal("1000000000000"));

		private final BigDecimal value;

		public static Prefix getPrefixByValue(BigDecimal value) {
			return Arrays.stream(Prefix.values()).filter(prefix -> value.equals(prefix.getValue())).findFirst().orElseThrow();
		}

		Prefix(BigDecimal value) {
			this.value = value;
		}

		public BigDecimal getValue() {
			return value;
		}
	}

}
