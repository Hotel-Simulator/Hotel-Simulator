package pl.agh.edu.management.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import pl.agh.edu.enums.Frequency;
import pl.agh.edu.json.data_loader.JSONBankDataLoader;
import pl.agh.edu.model.bank.BankAccount;
import pl.agh.edu.model.bank.Credit;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.time_command.NRepeatingTimeCommand;

public class BankAccountHandler {
	private final BankAccount account;

	private final Map<Credit, NRepeatingTimeCommand> currentCredits = new HashMap<>();

	private final Time time = Time.getInstance();

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

		var valueWithInterest = value.multiply(BigDecimal.ONE.add(credit.interestRate()));
		var monthlyPayment = valueWithInterest.divide(BigDecimal.valueOf(creditLengthInMonths), 2, RoundingMode.HALF_UP);

		currentCredits.put(
				credit,
				createTimeCommandForCreditMonthlyPayment(account, monthlyPayment, credit));
		account.registerCredit(credit);
		registerIncome(value);
	}

	private BigDecimal getAutomaticCreditValue(BigDecimal moneyNeeded) {
		return JSONBankDataLoader.minCreditValue.max(moneyNeeded);
	}

	private NRepeatingTimeCommand createTimeCommandForCreditMonthlyPayment(BankAccount bankAccount,
			BigDecimal monthlyPayments,
			Credit credit) {
		return new NRepeatingTimeCommand(Frequency.EVERY_MONTH,
				() -> bankAccount.registerExpense(monthlyPayments),
				time.getTime().plusMonths(1).truncatedTo(ChronoUnit.DAYS),
				credit.lengthInMonths(),
				() -> currentCredits.remove(credit));
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
		var valueWithInterest = credit.value().multiply(BigDecimal.ONE.add(credit.interestRate()));
		var monthlyPayment = valueWithInterest.divide(BigDecimal.valueOf(credit.lengthInMonths()), 2, RoundingMode.HALF_UP);

		return isPaid(credit) ? BigDecimal.ZERO : monthlyPayment.multiply(BigDecimal.valueOf(credit.lengthInMonths() - currentCredits.get(credit).getCounter()));
	}

	public LocalDate getNextPaymentDate(Credit credit) {
		return currentCredits.get(credit).getDueDateTime().toLocalDate();
	}

	public Map<Credit, NRepeatingTimeCommand> getCurrentCredits() {
		return currentCredits;
	}
}
