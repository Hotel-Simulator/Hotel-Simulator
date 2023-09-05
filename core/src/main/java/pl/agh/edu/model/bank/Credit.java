package pl.agh.edu.model.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Period;

import pl.agh.edu.model.time.Time;

public class Credit {
	private final BigDecimal creditValue;
	private final BigDecimal creditValueToPay;
	private final int period;
	private BigDecimal paidValue;
	private final LocalDateTime beginDate = Time.getInstance().getTime();
	private final LocalDateTime endDate;
	private final int interestRate = Bank.getInstance().getCreditInterestRate();
	private final BigDecimal monthlyPayments;
	private boolean isPaid = false;
	private LocalDateTime nextPaymentDate = beginDate.plusMonths(1);

	public Credit(BigDecimal creditValue, int period) {
		this.endDate = beginDate.plusMonths(period);
		this.period = period;
		this.creditValue = creditValue;
		this.creditValueToPay = creditValue.multiply(BigDecimal.valueOf(100 + interestRate)).divide(BigDecimal.valueOf(100), RoundingMode.CEILING);
		this.monthlyPayments = this.creditValueToPay.divideToIntegralValue(BigDecimal.valueOf(period));
	}

	public Credit(int creditValue, int period) {
		this(new BigDecimal(creditValue), period);
	}

	public void payMonth() {
		paidValue.add(monthlyPayments);
		nextPaymentDate = nextPaymentDate.plusMonths(1);
		if (paidValue.compareTo(creditValueToPay) == 0) {
			isPaid = true;
			nextPaymentDate = null;
		}
	}

	public void payAll() {
		paidValue = creditValueToPay;
		isPaid = true;
		nextPaymentDate = null;
	}

	public int getMonthsLeft() {
		LocalDateTime curr = Time.getInstance().getTime();
		Period diff = Period.between(curr.toLocalDate(), endDate.toLocalDate());
		return diff.getMonths();
	}

	public BigDecimal getCreditValue() {
		return creditValue;
	}

	public BigDecimal getMonthlyPayments() {

		return monthlyPayments;
	}

	public BigDecimal getPaidValue() {
		return paidValue;
	}

	public BigDecimal getCreditValueToPay() {
		return creditValueToPay;
	}

	public int getPeriod() {
		return period;
	}

	public LocalDateTime getBeginDate() {
		return beginDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public int getInterestRate() {
		return interestRate;
	}

	public boolean isPaid() {
		return isPaid;
	}

	public LocalDateTime getNextPaymentDate() {
		return nextPaymentDate;
	}
}
