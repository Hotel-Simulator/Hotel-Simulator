package pl.agh.edu.model.bank;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import pl.agh.edu.model.time.Time;

public class Deposit {
	private final int period;

	private final LocalDateTime endDate;
	private final BigDecimal endValue;

	public Deposit(BigDecimal placedValue, int period) {
		this.period = period;

		int interestRate = Bank.getInstance().getDepositInterestRate();
		LocalDateTime beginDate = Time.getInstance().getTime();
		this.endDate = beginDate.plusMonths(period);
		BigDecimal profitPerMonth = placedValue.multiply(BigDecimal.valueOf(interestRate)).divide(BigDecimal.valueOf(12));
		this.endValue = profitPerMonth.multiply(BigDecimal.valueOf(period)).add(placedValue);
	}

}
