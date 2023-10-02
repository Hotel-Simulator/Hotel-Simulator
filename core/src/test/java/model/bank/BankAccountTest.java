package model.bank;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.management.game.GameDifficultyManager;
import pl.agh.edu.model.bank.BankAccount;
import pl.agh.edu.model.bank.Credit;
import pl.agh.edu.model.bank.TransactionType;

public class BankAccountTest {
	private final BigDecimal accountFee = BigDecimal.valueOf(10);
	private BankAccount bankAccount;
	private final BigDecimal initialBalance = GameDifficultyManager.getInstance().getInitialBalance();;

	static {
		try {
			changeJSONPath();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	@BeforeEach
	public void setUp() throws ReflectiveOperationException {
		bankAccount = new BankAccount(initialBalance, new BigDecimal("0.05"), accountFee);
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
		bankAccount.registerIncome(income);

		// Then
		BigDecimal expectedBalance = initialBalance.add(income);
		assertEquals(expectedBalance, bankAccount.getBalance());
	}

	@Test
	public void registerExpense_ShouldDecreaseBalance() {
		// Given

		// When
		BigDecimal expense = BigDecimal.valueOf(50);
		bankAccount.registerExpense(expense);

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
		Credit credit = new Credit(creditValue, creditLengthInMonths, BigDecimal.ZERO, LocalDate.MIN);
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
		bankAccount.registerIncome(income1);
		bankAccount.registerIncome(income2);

		// When
		var incomes = bankAccount.getIncomeList();

		// Then
		assertEquals(2, incomes.size());
		assertTrue(incomes.stream().allMatch(transaction -> transaction.type() == TransactionType.INCOME));
	}

	@Test
	public void getExpenseList_ShouldReturnListOfExpenseTransactions() {
		// Given
		BigDecimal expense1 = BigDecimal.valueOf(100);
		BigDecimal expense2 = BigDecimal.valueOf(100);
		bankAccount.registerExpense(expense1);
		bankAccount.registerExpense(expense2);

		// When
		var expenses = bankAccount.getExpenseList();

		// Then
		assertEquals(2, expenses.size());
		assertTrue(expenses.stream().allMatch(transaction -> transaction.type() == TransactionType.EXPENSE));
	}

	private static void changeJSONPath()
			throws ReflectiveOperationException {

		Field field = JSONFilePath.class.getDeclaredField("PATH");
		field.setAccessible(true);
		field.set(null, "../assets/jsons/%s.json");
	}
}
