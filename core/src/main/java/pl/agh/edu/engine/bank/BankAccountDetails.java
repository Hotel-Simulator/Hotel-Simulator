package pl.agh.edu.engine.bank;

import java.math.BigDecimal;

import pl.agh.edu.serialization.KryoConfig;

public record BankAccountDetails(
        BigDecimal creditInterestRate,
        BigDecimal accountFee
) {

    static {
        KryoConfig.kryo.register(BankAccountDetails.class);
    }
}
