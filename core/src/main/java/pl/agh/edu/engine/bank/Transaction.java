package pl.agh.edu.engine.bank;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import pl.agh.edu.serialization.KryoConfig;

public record Transaction(TransactionType type, BigDecimal value, LocalDateTime transactionTime) {

    static {
        KryoConfig.kryo.register(Transaction.class);
    }
}
