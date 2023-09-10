package pl.agh.edu.model.bank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import pl.agh.edu.model.time.Time;

public class BankAccount {
	private BigDecimal balance = BigDecimal.ZERO;
	private final List<Credit> credits = new ArrayList<>();
	private final List<Transaction> transactions = new ArrayList<>();
	private int creditInterestRate;
	private BigDecimal accountFee;
	private final Time time = Time.getInstance();

	public BankAccount(int creditInterestRate, BigDecimal accountFee) {
		this.creditInterestRate = creditInterestRate;
		this.accountFee = accountFee;
	}

	private void chargeAccountFee() {
		registerExpense(accountFee);
	}

	public void registerIncome(BigDecimal value) {
		transactions.add(new Transaction(TransactionType.INCOME, value, time.getTime()));
		balance = balance.add(value);
	}

	public void registerExpense(BigDecimal value) {
		transactions.add(new Transaction(TransactionType.EXPENSE, value, time.getTime()));
		balance = balance.subtract(value);
	}

	public boolean hasOperationAbility(BigDecimal expense) {
		return balance.compareTo(expense) >= 0;
	}

	public void registerCredit(BigDecimal value, int creditLengthInMonths) {
		credits.add(new Credit(value, creditLengthInMonths, this));
		registerIncome(value);
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

	public int getCreditInterestRate() {
		return creditInterestRate;
	}

	public BigDecimal getAccountFee() {
		return accountFee;
	}

	public void setCreditInterestRate(int creditInterestRate) {
		this.creditInterestRate = creditInterestRate;
	}

	public void setAccountFee(BigDecimal accountFee) {
		this.accountFee = accountFee;
	}

	public void monthlyUpdate() {
		chargeAccountFee();
	}

}
