package pl.agh.edu.engine.bank;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.data.loader.JSONBankDataLoader;
import pl.agh.edu.engine.time.Frequency;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.NRepeatingTimeCommand;
import pl.agh.edu.serialization.KryoConfig;

public class BankAccountHandler {
	private final Time time;
	private final TimeCommandExecutor timeCommandExecutor;
	public final BankAccount account;
	private Map<Credit, NRepeatingTimeCommand> currentCredits;

	public static void kryoRegister() {
		KryoConfig.kryo.register(BankAccountHandler.class, new Serializer<BankAccountHandler>() {
			@Override
			public void write(Kryo kryo, Output output, BankAccountHandler object) {
				kryo.writeObject(output, object.time);
				kryo.writeObject(output, object.timeCommandExecutor);
				kryo.writeObject(output, object.account);
				kryo.writeObject(output, object.currentCredits, KryoConfig.mapSerializer(Credit.class, NRepeatingTimeCommand.class));

			}

			@Override
			public BankAccountHandler read(Kryo kryo, Input input, Class<? extends BankAccountHandler> type) {
				BankAccountHandler bankAccountHandler = new BankAccountHandler(
						kryo.readObject(input, Time.class),
						kryo.readObject(input, TimeCommandExecutor.class),
						kryo.readObject(input, BankAccount.class));

				kryo.reference(bankAccountHandler);

				bankAccountHandler.currentCredits = kryo.readObject(input, Map.class, KryoConfig.mapSerializer(Credit.class, NRepeatingTimeCommand.class));

				return bankAccountHandler;
			}
		});
	}

	public BankAccountHandler(BankAccount bankAccount) {
		this.time = Time.getInstance();
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
		this.account = bankAccount;
		this.currentCredits = new HashMap<>();
	}

	private BankAccountHandler(Time time,
			TimeCommandExecutor timeCommandExecutor,
			BankAccount account) {
		this.time = time;
		this.timeCommandExecutor = timeCommandExecutor;
		this.account = account;
		this.currentCredits = new HashMap<>();
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
				() -> registerExpense(monthlyPayments),
				time.getTime().plusMonths(1).truncatedTo(ChronoUnit.DAYS),
				credit.lengthInMonths,
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
