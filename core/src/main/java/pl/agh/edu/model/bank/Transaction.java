package pl.agh.edu.model.bank;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
	LocalDateTime transactionTime;
	BigDecimal value;

	public Transaction(LocalDateTime transactionTime, BigDecimal value) {
		this.transactionTime = transactionTime;
		this.value = value;
	}
}
