package pl.agh.edu.data.type;

import java.math.BigDecimal;

public record BankData(
        String name,
        int creditInterestRate,
        int depositInterestRate,
        BigDecimal bankAccountFee
) {
}
