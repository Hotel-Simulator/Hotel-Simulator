package pl.agh.edu.engine.bank;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import pl.agh.edu.data.loader.JSONBankDataLoader;
import pl.agh.edu.engine.time.Frequency;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.NRepeatingTimeCommand;
import pl.agh.edu.engine.time.command.SerializableRunnable;

public class BankAccountHandler {
	public final BankAccount account;

	private final Map<Credit, NRepeatingTimeCommand> currentCredits = new HashMap<>();

	private final Time time = Time.getInstance();
	private final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();

	public BankAccountHandler(BankAccount account) {
		this.account = account;
	}

	public void registerExpense(BigDecimal expense) {
		if (!hasOperationAbility(expense)) {
			BigDecimal creditValue = getAutomaticCreditValue(expense.subtract(account.getBalance()));
			registerCredit(creditValue, JSONBankDataLoader.basicCreditLengthInMonths);
		}
		account.registerExpense(expense);
	}

	public void registerIncome(BigDecimal income) {
		account.registerIncome(income);
	}

	public void registerCredit(BigDecimal value, long creditLengthInMonths) {
		var credit = new Credit(value, creditLengthInMonths, account.getCreditInterestRate(), time.getTime().toLocalDate());
		NRepeatingTimeCommand timeCommandForCreditMonthlyPayment = createTimeCommandForCreditMonthlyPayment(credit.monthlyPayment, credit);

		account.registerCredit(credit);
		registerIncome(credit.value);
		currentCredits.put(credit, timeCommandForCreditMonthlyPayment);
		timeCommandExecutor.addCommand(timeCommandForCreditMonthlyPayment);
	}

	private BigDecimal getAutomaticCreditValue(BigDecimal moneyNeeded) {
		return JSONBankDataLoader.minCreditValue.max(moneyNeeded);
	}

	private NRepeatingTimeCommand createTimeCommandForCreditMonthlyPayment(BigDecimal monthlyPayments, Credit credit) {
		return new NRepeatingTimeCommand(
				Frequency.EVERY_MONTH,
				(SerializableRunnable)() -> registerExpense(monthlyPayments),
				time.getTime().plusMonths(1).truncatedTo(ChronoUnit.DAYS),
				credit.lengthInMonths,
				(SerializableRunnable)() -> currentCredits.remove(credit));
	}

	public boolean hasOperationAbility(BigDecimal expense) {
		return account.getBalance().compareTo(expense) >= 0;
	}

	public boolean isPaid(Credit credit) {
		return !currentCredits.containsKey(credit);
	}

	public long getMonthsLeft(Credit credit) {
		return isPaid(credit) ? 0 : currentCredits.get(credit).getCounter();
	}

	public BigDecimal getPaidValue(Credit credit) {
		return isPaid(credit) ? BigDecimal.ZERO : credit.monthlyPayment.multiply(BigDecimal.valueOf(credit.lengthInMonths - currentCredits.get(credit).getCounter()));
	}

	public BigDecimal getValueLeftToPay(Credit credit) {
		return credit.valueWithInterest.subtract(getPaidValue(credit));
	}

	public LocalDate getNextPaymentDate(Credit credit) {
		return currentCredits.get(credit).getDueDateTime().toLocalDate();
	}

	public LocalDate getLastPaymentDate(Credit credit) {
		return credit.takeOutDate.plusMonths(credit.lengthInMonths);
	}

	public void payEntireCredit(Credit credit) {
		currentCredits.get(credit).stop();
		currentCredits.remove(credit);
		registerExpense(getValueLeftToPay(credit));
	}

	public Map<Credit, NRepeatingTimeCommand> getCurrentCredits() {
		return currentCredits;
	}
}
