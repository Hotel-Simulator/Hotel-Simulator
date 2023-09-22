package management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.json.data_loader.JSONBankDataLoader;
import pl.agh.edu.management.bank.BankConnector;
import pl.agh.edu.model.bank.BankAccount;

public class BankConnectorTest {
	private final BankAccount account = mock(BankAccount.class);
	private BankConnector connector;

	@BeforeEach
	public void setUp() {
		connector = new BankConnector(account);
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
        boolean actualResult = connector.hasOperationAbility(expense);

        // Then
        assertEquals(expectedResult, actualResult);
    }

	@Test
	public void testRegisterExpenseSufficientBalance() {
		// Given
		var expense = BigDecimal.valueOf(500);
		when(account.getBalance()).thenReturn(expense);

		// When
		connector.registerExpense(expense);

		// Then
		verify(account, times(1)).registerExpense(any());
		verify(account, times(0)).registerIncome(any());
	}

	@Test
	public void testRegisterExpenseInsufficientBalance() {
		// Given
		var expense = BigDecimal.valueOf(500);
		when(account.getBalance()).thenReturn(expense.subtract(BigDecimal.valueOf(1)));

		// When
		connector.registerExpense(expense);

		// Then
		verify(account, times(1)).registerExpense(expense);
		verify(account, times(1)).registerCredit(JSONBankDataLoader.minCreditValue, JSONBankDataLoader.basicCreditLengthInMonths);
	}

	@Test
	public void testRegisterExpenseInsufficientBalance_expenseHigherThanMinCreditValue() {
		// Given
		var expense = JSONBankDataLoader.minCreditValue.add(BigDecimal.ONE);
		when(account.getBalance()).thenReturn(BigDecimal.ZERO);

		// When
		connector.registerExpense(expense);

		// Then
		verify(account, times(1)).registerExpense(expense);
		verify(account, times(1)).registerCredit(expense, JSONBankDataLoader.basicCreditLengthInMonths);
	}

	@Test
	public void testRegisterIncome() {
		// Given
		var income = BigDecimal.ONE;
		// When
		connector.registerIncome(income);

		// Then
		verify(account, times(1)).registerIncome(BigDecimal.ONE);
	}

	private static void changeJSONPath()
			throws ReflectiveOperationException {

		Field field = JSONFilePath.class.getDeclaredField("PATH");
		field.setAccessible(true);
		field.set(null, "../assets/jsons/%s.json");
	}
}
