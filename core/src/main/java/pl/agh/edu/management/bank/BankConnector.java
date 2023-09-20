package pl.agh.edu.management.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;

import pl.agh.edu.json.data_loader.JSONBankDataLoader;
import pl.agh.edu.model.bank.BankAccount;

public class BankConnector {
	private final BankAccount account;

	public BankConnector(BankAccount account) {
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

	private static BigDecimal roundUpToMostSignificantDigit(BigDecimal value) {
		BigDecimal powerOfTen = BigDecimal.TEN.pow(value.precision() - 1);
		return value.divide(powerOfTen, 0, RoundingMode.CEILING).multiply(powerOfTen);
	}

	public boolean hasOperationAbility(BigDecimal expense) {
		return account.getBalance().compareTo(expense) >= 0;
	}
}
