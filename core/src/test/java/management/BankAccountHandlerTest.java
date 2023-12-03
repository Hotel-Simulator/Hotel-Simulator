package management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.agh.edu.engine.bank.TransactionType.ROOM_RENTING_INCOME;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.agh.edu.engine.bank.BankAccount;
import pl.agh.edu.engine.bank.BankAccountHandler;
import pl.agh.edu.engine.bank.Credit;
import pl.agh.edu.engine.time.Time;

public class BankAccountHandlerTest {
	private final BankAccount account = mock(BankAccount.class);
	private BankAccountHandler bankAccountHandler;
	private static final long creditLengthInMonths = 12;

	@BeforeEach
	public void setUp() {
		bankAccountHandler = new BankAccountHandler(account);
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
		bankAccountHandler.registerExpense(ROOM_RENTING_INCOME, expense);

		// Then
		verify(account, times(1)).registerExpense(any(), any());
		verify(account, times(0)).registerIncome(any(), any());
	}

	@Test
	public void testRegisterIncome() {
		// Given
		var income = BigDecimal.ONE;
		// When
		bankAccountHandler.registerIncome(ROOM_RENTING_INCOME, income);

		// Then
		verify(account, times(1)).registerIncome(ROOM_RENTING_INCOME, BigDecimal.ONE);
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
	public void getLatPaymentDateTest() {
		// Given
		BigDecimal creditValue = BigDecimal.valueOf(1000);
		when(account.getCreditInterestRate()).thenReturn(BigDecimal.ZERO);

		// When
		bankAccountHandler.registerCredit(creditValue, creditLengthInMonths);
		Credit credit = (Credit) bankAccountHandler.getCurrentCredits().keySet().toArray()[0];
		LocalDate lastPaymentDate = bankAccountHandler.getLastPaymentDate(credit);

		// Then
		LocalDate expectedNextPaymentDate = Time.getInstance().getTime().toLocalDate().plusMonths(12);
		assertEquals(expectedNextPaymentDate, lastPaymentDate);
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

	@Test
	public void getValueLeftToPayTest() {
		// Given
		when(account.getCreditInterestRate()).thenReturn(new BigDecimal("0.1"));
		BigDecimal creditValue = BigDecimal.valueOf(1000);

		// When
		bankAccountHandler.registerCredit(creditValue,creditLengthInMonths);
		Credit credit = (Credit) bankAccountHandler.getCurrentCredits().keySet().toArray()[0];

		// Then
		assertEquals(credit.valueWithInterest.stripTrailingZeros(), bankAccountHandler.getValueLeftToPay(credit).stripTrailingZeros());
	}
}
