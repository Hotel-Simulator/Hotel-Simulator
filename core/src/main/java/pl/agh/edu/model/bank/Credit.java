package pl.agh.edu.model.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class Credit {
	public final BigDecimal value;
	public final long lengthInMonths;
	public final BigDecimal interestRate;
	public final LocalDate takeOutDate;
	public final BigDecimal valueWithInterest;
	public final BigDecimal monthlyPayment;

	public Credit(BigDecimal value, long lengthInMonths, BigDecimal interestRate, LocalDate takeOutDate) {
		this.value = value;
		this.lengthInMonths = lengthInMonths;
		this.interestRate = interestRate;
		this.takeOutDate = takeOutDate;
		this.valueWithInterest = value.multiply(BigDecimal.ONE.add(interestRate));
		this.monthlyPayment = valueWithInterest.divide(BigDecimal.valueOf(lengthInMonths), 2, RoundingMode.HALF_UP);
	}
}
