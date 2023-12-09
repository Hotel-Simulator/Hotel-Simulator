package pl.agh.edu.engine.bank;

import java.math.BigDecimal;

public interface BalanceListener {
	void onBalanceChange(BigDecimal balance);
}
