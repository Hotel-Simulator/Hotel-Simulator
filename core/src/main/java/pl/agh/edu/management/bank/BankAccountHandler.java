package pl.agh.edu.management.bank;

import java.math.BigDecimal;

import pl.agh.edu.json.data_loader.JSONBankDataLoader;
import pl.agh.edu.model.bank.BankAccount;

public class BankAccountHandler {
	private final BankAccount account;

	public BankAccountHandler(BankAccount account) {
		this.account = account;
	}

	public void registerExpense(BigDecimal expense) {
		if (!hasOperationAbility(expense)) {
			BigDecimal creditValue = getAutomaticCreditValue(expense.subtract(account.getBalance()));
			account.registerCredit(creditValue, JSONBankDataLoader.basicCreditLengthInMonths);
		}
		account.registerExpense(expense);
	}

	public void registerIncome(BigDecimal income) {
		account.registerIncome(income);
	}

	private BigDecimal getAutomaticCreditValue(BigDecimal moneyNeeded) {
		return JSONBankDataLoader.minCreditValue.max(moneyNeeded);
	}

	public boolean hasOperationAbility(BigDecimal expense) {
		return account.getBalance().compareTo(expense) >= 0;
	}
}
