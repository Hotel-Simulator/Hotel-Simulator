package model.bank;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.model.bank.BankAccount;
import pl.agh.edu.model.bank.Credit;
import pl.agh.edu.model.time.Time;

public class CreditTest {
	private BankAccount bankAccount;
	private Credit credit;

	@BeforeEach
	public void setUp() {
		bankAccount = mock(BankAccount.class);
		when(bankAccount.getCreditInterestRate()).thenReturn(new BigDecimal("0.05"));
	}

	@Test
	public void creditInitialization_ShouldCalculateMonthlyPaymentsCorrectly() {
		// Given
		BigDecimal creditValue = BigDecimal.valueOf(1000);
		int creditLengthInMonths = 12;

		// When
		credit = new Credit(creditValue, creditLengthInMonths, bankAccount);

		// Then
		BigDecimal expectedMonthlyPayments = creditValue
				.multiply(BigDecimal.ONE.add(credit.getInterestRate()))
				.divide(BigDecimal.valueOf(creditLengthInMonths), 2, RoundingMode.HALF_UP);
		assertEquals(expectedMonthlyPayments, credit.monthlyPayments);
	}

	@Test
	public void creditInitialization_ShouldSetRemainingMonthsCorrectly() {
		// Given
		BigDecimal creditValue = BigDecimal.valueOf(1000);
		int creditLengthInMonths = 12;

		// When
		credit = new Credit(creditValue, creditLengthInMonths, bankAccount);

		// Then
		assertEquals(12, credit.getMonthsLeft());
	}

	@Test
	public void getNextPaymentDate_ShouldReturnCorrectDate() {
		// Given
		BigDecimal creditValue = BigDecimal.valueOf(1000);
		int creditLengthInMonths = 12;
		credit = new Credit(creditValue, creditLengthInMonths, bankAccount);

		// When
		LocalDate nextPaymentDate = credit.getNextPaymentDate();

		// Then
		LocalDate expectedNextPaymentDate = Time.getInstance().getTime().toLocalDate().plusMonths(1);
		assertEquals(expectedNextPaymentDate, nextPaymentDate);
	}

	@Test
	public void getPaidValue_ShouldReturnCorrectValueAfterPayments() {
		// Given
		BigDecimal creditValue = BigDecimal.valueOf(1000);
		int creditLengthInMonths = 12;
		credit = new Credit(creditValue, creditLengthInMonths, bankAccount);

		// Then
		assertEquals(new BigDecimal("0.00"), credit.getPaidValue());
	}
}
