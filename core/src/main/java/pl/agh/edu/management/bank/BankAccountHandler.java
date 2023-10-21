package pl.agh.edu.management.bank;

import java.math.BigDecimal;
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
import pl.agh.edu.time_command.TimeCommandExecutor;

public class BankAccountHandler {
	private final BankAccount account;

	private final Map<Credit, NRepeatingTimeCommand> currentCredits = new HashMap<>();

	private final Time time = Time.getInstance();
	TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();

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
		NRepeatingTimeCommand timeCommandForCreditMonthlyPayment = createTimeCommandForCreditMonthlyPayment(credit.getMonthlyPayment(), credit);

        account.registerCredit(credit);
        registerIncome(credit.getValue());
		currentCredits.put(credit, timeCommandForCreditMonthlyPayment);
		timeCommandExecutor.addCommand(timeCommandForCreditMonthlyPayment);
	}

	private BigDecimal getAutomaticCreditValue(BigDecimal moneyNeeded) {
		return JSONBankDataLoader.minCreditValue.max(moneyNeeded);
	}

	private NRepeatingTimeCommand createTimeCommandForCreditMonthlyPayment(BigDecimal monthlyPayments, Credit credit) {
		return new NRepeatingTimeCommand(
				Frequency.EVERY_MONTH,
				() -> registerExpense(monthlyPayments),
				time.getTime().plusMonths(1).truncatedTo(ChronoUnit.DAYS),
				credit.getLengthInMonths(),
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
		return isPaid(credit) ? BigDecimal.ZERO : credit.getMonthlyPayment().multiply(BigDecimal.valueOf(credit.getLengthInMonths() - currentCredits.get(credit).getCounter()));
	}

	public BigDecimal getValueLeftToPay(Credit credit) {
		return credit.getValueWithInterest().subtract(getPaidValue(credit));
	}

	public LocalDate getNextPaymentDate(Credit credit) {
		return currentCredits.get(credit).getDueDateTime().toLocalDate();
	}

	public LocalDate getLastPaymentDate(Credit credit) {
		return getNextPaymentDate(credit).plusMonths(getMonthsLeft(credit) - 1);
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
