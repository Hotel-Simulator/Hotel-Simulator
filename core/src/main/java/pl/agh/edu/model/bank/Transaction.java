package pl.agh.edu.model.bank;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transaction(TransactionType type, BigDecimal value, LocalDateTime transactionTime) {
}
