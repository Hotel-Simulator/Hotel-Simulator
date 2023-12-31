package pl.agh.edu.engine.bank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.engine.time.Time;
import pl.agh.edu.serialization.KryoConfig;

public class BankAccount {
	private final Time time;
	private BigDecimal balance;
	private BankAccountDetails accountDetails;
	private final List<Credit> credits;
	private final List<Transaction> transactions;

	public static void kryoRegister() {
		KryoConfig.kryo.register(BankAccount.class, new Serializer<BankAccount>() {
			@Override
			public void write(Kryo kryo, Output output, BankAccount object) {
				kryo.writeObject(output, object.time);
				kryo.writeObject(output, object.balance);
				kryo.writeObject(output, object.accountDetails);
				kryo.writeObject(output, object.credits, KryoConfig.listSerializer(Credit.class));
				kryo.writeObject(output, object.transactions, KryoConfig.listSerializer(Transaction.class));
			}

			@Override
			public BankAccount read(Kryo kryo, Input input, Class<? extends BankAccount> type) {
				return new BankAccount(
						kryo.readObject(input, Time.class),
						kryo.readObject(input, BigDecimal.class),
						kryo.readObject(input, BankAccountDetails.class),
						kryo.readObject(input, List.class, KryoConfig.listSerializer(Credit.class)),
						kryo.readObject(input, List.class, KryoConfig.listSerializer(Transaction.class)));
			}
		});
	}

	public BankAccount(BigDecimal initialBalance, BankAccountDetails accountDetails) {
		this.time = Time.getInstance();
		this.balance = initialBalance;
		this.accountDetails = accountDetails;
		this.credits = new ArrayList<>();
		this.transactions = new ArrayList<>();
	}

	private BankAccount(Time time,
			BigDecimal balance,
			BankAccountDetails accountDetails,
			List<Credit> credits,
			List<Transaction> transactions) {
		this.time = time;
		this.balance = balance;
		this.accountDetails = accountDetails;
		this.credits = credits;
		this.transactions = transactions;
	}

	private void chargeAccountFee() {
		registerExpense(accountDetails.accountFee());
	}

	public void registerIncome(BigDecimal value) {
		transactions.add(new Transaction(TransactionType.INCOME, value, time.getTime()));
		balance = balance.add(value);
	}

	public void registerExpense(BigDecimal value) {
		transactions.add(new Transaction(TransactionType.EXPENSE, value, time.getTime()));
		balance = balance.subtract(value);
	}

	public void registerCredit(Credit credit) {
		credits.add(credit);
		registerIncome(credit.value);
	}

	public List<Credit> getCredits() {
		return credits;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public List<Transaction> getIncomeList() {
		return transactions.stream()
				.filter(transaction -> transaction.type() == TransactionType.INCOME)
				.toList();
	}

	public List<Transaction> getExpenseList() {
		return transactions.stream()
				.filter(transaction -> transaction.type() == TransactionType.EXPENSE)
				.toList();
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public BigDecimal getCreditInterestRate() {
		return accountDetails.creditInterestRate();
	}

	public BigDecimal getAccountFee() {
		return accountDetails.accountFee();
	}

	public void setAccountDetails(BankAccountDetails accountDetails) {
		this.accountDetails = accountDetails;
	}

	public void monthlyUpdate() {
		chargeAccountFee();
	}

}
