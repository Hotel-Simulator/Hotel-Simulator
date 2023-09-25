package management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.json.data_loader.JSONBankDataLoader;
import pl.agh.edu.management.bank.BankAccountHandler;
import pl.agh.edu.model.bank.BankAccount;
import pl.agh.edu.model.bank.Credit;
import pl.agh.edu.model.time.Time;

public class BankAccountHandlerTest {
	private final BankAccount account = mock(BankAccount.class);
	private BankAccountHandler bankAccountHandler;
	private static final long creditLengthInMonths = 12;

	@BeforeEach
	public void setUp() {
		bankAccountHandler = new BankAccountHandler(account);
	}

	@BeforeAll
	public static void setUpClass() throws ReflectiveOperationException {
		changeJSONPath();
	}

	private static Stream<Arguments> hasOperationAbilityTestArgs() {
		return Stream.of(
				Arguments.of(BigDecimal.valueOf(100), true),
				Arguments.of(BigDecimal.valueOf(101), false));
	}

	@ParameterizedTest
    @MethodSource("hasOperationAbilityTestArgs")
    public void hasOperationAbilityTest(BigDecimal expense, boolean expectedResult) {
        // Given
        when(account.getBalance()).thenReturn(BigDecimal.valueOf(100));

        // When
        boolean actualResult = bankAccountHandler.hasOperationAbility(expense);

        // Then
        assertEquals(expectedResult, actualResult);
    }

	@Test
	public void testRegisterExpenseSufficientBalance() {
		// Given
		var expense = BigDecimal.valueOf(500);
		when(account.getBalance()).thenReturn(expense);

		// When
		bankAccountHandler.registerExpense(expense);

		// Then
		verify(account, times(1)).registerExpense(any());
		verify(account, times(0)).registerIncome(any());
	}

	@Test
	public void testRegisterExpenseInsufficientBalance() {
		// Given
		var expense = BigDecimal.valueOf(500);
		when(account.getBalance()).thenReturn(expense.subtract(BigDecimal.valueOf(1)));
		when(account.getCreditInterestRate()).thenReturn(BigDecimal.ZERO);

		// When
		bankAccountHandler.registerExpense(expense);

		// Then
		verify(account, times(1)).registerExpense(expense);
		verify(account, times(1)).registerCredit(any());
	}

	@Test
	public void testRegisterExpenseInsufficientBalance_expenseHigherThanMinCreditValue() {
		// Given
		var expense = JSONBankDataLoader.minCreditValue.add(BigDecimal.ONE);
		when(account.getBalance()).thenReturn(BigDecimal.ZERO);
		when(account.getCreditInterestRate()).thenReturn(BigDecimal.ZERO);

		// When
		bankAccountHandler.registerExpense(expense);

		// Then
		verify(account, times(1)).registerExpense(expense);
		verify(account, times(1)).registerCredit(any());
	}

	@Test
	public void testRegisterIncome() {
		// Given
		var income = BigDecimal.ONE;
		// When
		bankAccountHandler.registerIncome(income);

		// Then
		verify(account, times(1)).registerIncome(BigDecimal.ONE);
	}

	@Test
	public void creditInitialization_ShouldSetRemainingMonthsCorrectly() {
		// Given
		BigDecimal creditValue = BigDecimal.valueOf(1000);
		when(account.getCreditInterestRate()).thenReturn(BigDecimal.ZERO);

		// When
		bankAccountHandler.registerCredit(creditValue, creditLengthInMonths);
		Credit credit = (Credit) bankAccountHandler.getCurrentCredits().keySet().toArray()[0];

		// Then
		assertEquals(creditLengthInMonths, bankAccountHandler.getMonthsLeft(credit));
	}

	@Test
	public void getNextPaymentDate_ShouldReturnCorrectDate() {
		// Given
		BigDecimal creditValue = BigDecimal.valueOf(1000);
		when(account.getCreditInterestRate()).thenReturn(BigDecimal.ZERO);

		// When
		bankAccountHandler.registerCredit(creditValue, creditLengthInMonths);
		Credit credit = (Credit) bankAccountHandler.getCurrentCredits().keySet().toArray()[0];
		LocalDate nextPaymentDate = bankAccountHandler.getNextPaymentDate(credit);

		// Then
		LocalDate expectedNextPaymentDate = Time.getInstance().getTime().toLocalDate().plusMonths(1);
		assertEquals(expectedNextPaymentDate, nextPaymentDate);
	}

	@Test
	public void getPaidValue_ShouldReturnCorrectValueAfterPayments() {
		// Given
		when(account.getCreditInterestRate()).thenReturn(BigDecimal.ZERO);
		BigDecimal creditValue = BigDecimal.valueOf(1000);

		// When
		bankAccountHandler.registerCredit(creditValue,creditLengthInMonths);
		Credit credit = (Credit) bankAccountHandler.getCurrentCredits().keySet().toArray()[0];


		// Then
		assertEquals(new BigDecimal("0.00"), bankAccountHandler.getPaidValue(credit));
	}

	private static void changeJSONPath()
			throws ReflectiveOperationException {

		Field field = JSONFilePath.class.getDeclaredField("PATH");
		field.setAccessible(true);
		field.set(null, "../assets/jsons/%s.json");
	}
}
