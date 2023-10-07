package pl.agh.edu.model.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;


public class Credit{
            private final BigDecimal value;
            private final long lengthInMonths;
            private final BigDecimal interestRate;
            private final LocalDate takeOutDate;
            private final BigDecimal valueWithInterest;
            private final BigDecimal monthlyPayment;

         public Credit(BigDecimal value, long lengthInMonths, BigDecimal interestRate, LocalDate takeOutDate) {
            this.value = value;
            this.lengthInMonths = lengthInMonths;
            this.interestRate = interestRate;
            this.takeOutDate = takeOutDate;
            this.valueWithInterest = value.multiply(BigDecimal.ONE.add(interestRate));
            this.monthlyPayment = valueWithInterest.divide(BigDecimal.valueOf(lengthInMonths), 2, RoundingMode.HALF_UP);
         }

    public BigDecimal getValue() {
        return value;
    }

    public long getLengthInMonths() {
        return lengthInMonths;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public LocalDate getTakeOutDate() {
        return takeOutDate;
    }

    public BigDecimal getValueWithInterest() {
        return valueWithInterest;
    }

    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }
}
