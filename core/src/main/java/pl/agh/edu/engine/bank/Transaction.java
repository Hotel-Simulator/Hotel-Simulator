package pl.agh.edu.engine.bank;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transaction(TransactionType type, BigDecimal value, LocalDateTime transactionTime) {
}