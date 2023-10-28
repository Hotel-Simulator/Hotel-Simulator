package pl.agh.edu.ui.frame;

import java.math.BigDecimal;

import com.badlogic.gdx.utils.Align;

import pl.agh.edu.engine.bank.BankAccount;
import pl.agh.edu.engine.bank.BankAccountHandler;
import pl.agh.edu.ui.component.table.CreditTable;

public class TestFrame extends BaseFrame {
	public TestFrame(String languagePath) {
		super(languagePath);

		BankAccount bankAccount = new BankAccount(BigDecimal.ONE, BigDecimal.TEN, BigDecimal.TEN);
		BankAccountHandler bankAccountHandler = new BankAccountHandler(bankAccount);
		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
		CreditTable creditTable = new CreditTable(bankAccountHandler);

		creditTable.align(Align.left);
		mainTable.add(creditTable).grow().align(Align.topLeft);

	}
}
