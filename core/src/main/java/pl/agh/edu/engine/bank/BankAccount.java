package pl.agh.edu.engine.bank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import pl.agh.edu.engine.time.Time;

public class BankAccount {
	private final List<Credit> credits = new ArrayList<>();
	private final List<Transaction> transactions = new ArrayList<>();
	private final Time time = Time.getInstance();
	private BigDecimal balance;
	private BankAccountDetails accountDetails;

	public BankAccount(BigDecimal initialBalance, BankAccountDetails accountDetails) {
		this.balance = initialBalance;
		this.accountDetails = accountDetails;
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
