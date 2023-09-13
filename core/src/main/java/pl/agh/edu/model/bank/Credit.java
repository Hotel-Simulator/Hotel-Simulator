package pl.agh.edu.model.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import pl.agh.edu.enums.Frequency;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.time_command.NRepeatingTimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;

public class Credit {
	public final BigDecimal creditValue;
	public final BigDecimal creditValueWithInterest;
	public final long creditLengthInMonths;
	private final Time time = Time.getInstance();
	private final LocalDate beginDate = time.getTime().toLocalDate();
	private final BankAccount bankAccount;
	public final BigDecimal monthlyPayments;
	private boolean isPaid = false;
	private final NRepeatingTimeCommand paymentTimeCommand;

	public Credit(BigDecimal creditValue, long creditLengthInMonths, BankAccount bankAccount) {
		this.creditValue = creditValue;
		this.creditLengthInMonths = creditLengthInMonths;
		this.bankAccount = bankAccount;

		this.creditValueWithInterest = creditValue.multiply(BigDecimal.ONE.add(bankAccount.getCreditInterestRate()));
		this.monthlyPayments = this.creditValueWithInterest.divide(BigDecimal.valueOf(creditLengthInMonths), 2, RoundingMode.HALF_UP);
		this.paymentTimeCommand = createTimeCommandForCreditMonthlyPayment(bankAccount);
		TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();
		timeCommandExecutor.addCommand(paymentTimeCommand);
	}

	private NRepeatingTimeCommand createTimeCommandForCreditMonthlyPayment(BankAccount bankAccount) {
		return new NRepeatingTimeCommand(Frequency.EVERY_MONTH,
				() -> {
					bankAccount.registerExpense(monthlyPayments);
					if (getMonthsLeft() == 1) {
						isPaid = true;
					}
				}, time.getTime().plusMonths(1), creditLengthInMonths);
	}

	public long getMonthsLeft() {
		return paymentTimeCommand.getCounter();
	}

	public BigDecimal getPaidValue() {
		return monthlyPayments.multiply(BigDecimal.valueOf(creditLengthInMonths - paymentTimeCommand.getCounter()));
	}

	public LocalDate getBeginDate() {
		return beginDate;
	}

	public LocalDate getEndDate() {
		return beginDate.plusMonths(creditLengthInMonths);
	}

	public BigDecimal getInterestRate() {
		return bankAccount.getCreditInterestRate();
	}

	public boolean isPaid() {
		return isPaid;
	}

	public LocalDate getNextPaymentDate() {
		return paymentTimeCommand.getDueDateTime().toLocalDate();
	}
}
