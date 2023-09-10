package model.bank;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.agh.edu.model.bank.BankAccount;
import pl.agh.edu.model.bank.TransactionType;

public class BankAccountTest {
	private final BigDecimal accountFee = BigDecimal.valueOf(10);
	private BankAccount bankAccount;

	@BeforeEach
	public void setUp() {
		bankAccount = new BankAccount(5, accountFee);
	}

	@Test
	public void chargeAccountFee_ShouldDeductAccountFeeFromBalance() {
		// Given
		BigDecimal initialBalance = BigDecimal.valueOf(100);
		bankAccount.registerIncome(initialBalance);

		// When
		bankAccount.monthlyUpdate();

		// Then
		BigDecimal expectedBalance = initialBalance.subtract(accountFee);
		assertEquals(expectedBalance, bankAccount.getBalance());
	}

	@Test
	public void registerIncome_ShouldIncreaseBalance() {
		// Given
		BigDecimal initialBalance = BigDecimal.valueOf(100);

		// When
		bankAccount.registerIncome(initialBalance);
		bankAccount.registerIncome(initialBalance);

		// Then
		BigDecimal expectedBalance = initialBalance.add(initialBalance);
		assertEquals(expectedBalance, bankAccount.getBalance());
	}

	@Test
	public void registerExpense_ShouldDecreaseBalance() {
		// Given
		BigDecimal initialBalance = BigDecimal.valueOf(100);
		bankAccount.registerIncome(initialBalance);

		// When
		BigDecimal expense = BigDecimal.valueOf(50);
		bankAccount.registerExpense(expense);

		// Then
		BigDecimal expectedBalance = initialBalance.subtract(expense);
		assertEquals(expectedBalance, bankAccount.getBalance());
	}

	private static Stream<Arguments> hasOperationAbilityTestArgs() {
		return Stream.of(
				Arguments.of(BigDecimal.valueOf(100), BigDecimal.valueOf(100), true),
				Arguments.of(BigDecimal.valueOf(100), BigDecimal.valueOf(101), false));
	}

	@ParameterizedTest
	@MethodSource("hasOperationAbilityTestArgs")
	public void hasOperationAbilityTest(BigDecimal initialBalance, BigDecimal expense, boolean expectedResult) {
		// Given
		bankAccount.registerIncome(initialBalance);

		// When
		boolean actualResult = bankAccount.hasOperationAbility(expense);

		// Then
		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void registerCredit_ShouldIncreaseBalanceAndAddCredit() {
		// Given
		BigDecimal initialBalance = BigDecimal.valueOf(100);

		// When
		BigDecimal creditValue = BigDecimal.valueOf(50);
		int creditLengthInMonths = 12;
		bankAccount.registerIncome(initialBalance);
		bankAccount.registerCredit(creditValue, creditLengthInMonths);

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
}
