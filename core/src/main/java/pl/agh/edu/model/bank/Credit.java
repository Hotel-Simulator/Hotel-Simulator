package pl.agh.edu.model.bank;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Credit(BigDecimal value, long lengthInMonths, BigDecimal interestRate, LocalDate takeOutDate) {

}
