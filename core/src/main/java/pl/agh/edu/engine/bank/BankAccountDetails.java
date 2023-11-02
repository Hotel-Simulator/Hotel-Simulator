package pl.agh.edu.engine.bank;

import java.math.BigDecimal;

public record BankAccountDetails(
        BigDecimal creditInterestRate,
        BigDecimal accountFee
) {
}
