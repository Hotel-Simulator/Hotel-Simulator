package model.bank;

import static java.math.BigDecimal.ZERO;
import static java.time.LocalDate.MIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.agh.edu.engine.bank.TransactionType.ACCOUNT_FEE_CHARGE;
import static pl.agh.edu.engine.bank.TransactionType.ROOM_RENTING_INCOME;
import static pl.agh.edu.engine.hotel.dificulty.DifficultyLevel.MEDIUM;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.engine.bank.BankAccount;
import pl.agh.edu.engine.bank.BankAccountDetails;
import pl.agh.edu.engine.bank.Credit;
import pl.agh.edu.engine.hotel.dificulty.GameDifficultyManager;

public class BankAccountTest {
	private final BigDecimal accountFee = BigDecimal.valueOf(10);
	private BankAccount bankAccount;
	private final BigDecimal initialBalance = new GameDifficultyManager(MEDIUM).getInitialBalance();

	@BeforeEach
	public void setUp() {
		bankAccount = new BankAccount(initialBalance, new BankAccountDetails(new BigDecimal("0.05"), accountFee));
	}

	@Test
	public void chargeAccountFee_ShouldDeductAccountFeeFromBalance() {
		// Given

		// When
		bankAccount.monthlyUpdate();

		// Then
		BigDecimal expectedBalance = initialBalance.subtract(accountFee);
		assertEquals(expectedBalance, bankAccount.getBalance());
	}

	@Test
	public void registerIncome_ShouldIncreaseBalance() {
		// Given
		BigDecimal income = BigDecimal.valueOf(100);

		// When
		bankAccount.registerIncome(ROOM_RENTING_INCOME, income);

		// Then
		BigDecimal expectedBalance = initialBalance.add(income);
		assertEquals(expectedBalance, bankAccount.getBalance());
	}

	@Test
	public void registerExpense_ShouldDecreaseBalance() {
		// Given

		// When
		BigDecimal expense = BigDecimal.valueOf(50);
		bankAccount.registerExpense(ROOM_RENTING_INCOME, expense);

		// Then
		BigDecimal expectedBalance = initialBalance.subtract(expense);
		assertEquals(expectedBalance, bankAccount.getBalance());
	}

	@Test
	public void registerCredit_ShouldIncreaseBalanceAndAddCredit() {
		// Given

		// When
		BigDecimal creditValue = BigDecimal.valueOf(50);
		int creditLengthInMonths = 12;
		Credit credit = new Credit(creditValue, creditLengthInMonths, ZERO, MIN);
		bankAccount.registerCredit(credit);

		// Then
		BigDecimal expectedBalance = initialBalance.add(creditValue);
		assertEquals(expectedBalance, bankAccount.getBalance());
		assertEquals(1, bankAccount.getCredits().size());
	}

	@Test
	public void getIncomeList_ShouldReturnListOfIncomeTransactions() {
		// Given
		BigDecimal income1 = BigDecimal.valueOf(100);
		BigDecimal income2 = BigDecimal.valueOf(50);
		bankAccount.registerIncome(ROOM_RENTING_INCOME, income1);
		bankAccount.registerIncome(ROOM_RENTING_INCOME, income2);

		// When
		var incomes = bankAccount.getIncomeList();

		// Then
		assertEquals(2, incomes.size());
		assertTrue(incomes.stream().allMatch(transaction -> transaction.type().isIncome));
	}

	@Test
	public void getExpenseList_ShouldReturnListOfExpenseTransactions() {
		// Given
		BigDecimal expense1 = BigDecimal.valueOf(100);
		BigDecimal expense2 = BigDecimal.valueOf(100);
		bankAccount.registerExpense(ACCOUNT_FEE_CHARGE, expense1);
		bankAccount.registerExpense(ACCOUNT_FEE_CHARGE, expense2);

		// When
		var expenses = bankAccount.getExpenseList();

		// Then
		assertEquals(2, expenses.size());
		assertTrue(expenses.stream().noneMatch(transaction -> transaction.type().isIncome));
	}
}
